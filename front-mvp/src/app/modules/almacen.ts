import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { NgFor, NgIf, JsonPipe, DatePipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';

interface Producto {
  id: number;
  nombre: string;
}

interface DetalleOrden {
  id: number;
  producto: Producto;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
}

interface OrdenCompra {
  id: number;
  proveedor: { nombre: string };
  fecha: string;
  total: number;
  estado: string;
  detalles: DetalleOrden[];
}

// Formulario de recepción por detalle
interface RecepcionForm {
  detalleOrdenCompraId: number;
  cantidadRecibida: number;
  numeroLote: string;
  fechaVencimiento: string;
  ubicacion: string;
  incidencia?: string;
}

@Component({
  selector: 'app-almacen',
  standalone: true,
  imports: [NgFor, NgIf, JsonPipe, DatePipe, MatButtonModule, MatCardModule, MatIconModule, FormsModule],
  templateUrl: './almacen.html',
  styleUrls: ['./almacen.scss']
})
export class AlmacenComponent implements OnInit {
  private readonly api = 'http://localhost:8080/api/almacen';

  ordenes: OrdenCompra[] = [];
  seleccionada: OrdenCompra | null = null;

  // Map: detalleId -> Formulario
  formularios: Record<number, RecepcionForm> = {};

  mensaje = '';
  cargando = false;
  procesando = false;

  constructor(private http: HttpClient, private auth: AuthService) { }

  ngOnInit() {
    this.cargarPendientes();
  }

  get headers() {
    return this.auth.getAuthHeader();
  }

  cargarPendientes() {
    this.cargando = true;
    this.http.get<OrdenCompra[]>(`${this.api}/pendientes`, { headers: this.headers })
      .subscribe({
        next: (data) => {
          this.ordenes = data;
          this.cargando = false;
          // Si la seleccionada ya no está en pendientes, deseleccionar
          if (this.seleccionada && !this.ordenes.find(o => o.id === this.seleccionada?.id)) {
            this.seleccionada = null;
          }
        },
        error: () => this.cargando = false
      });
  }

  seleccionar(orden: OrdenCompra) {
    this.seleccionada = orden;
    this.mensaje = '';
    this.formularios = {};

    // Inicializar formularios con valores por defecto
    orden.detalles.forEach(det => {
      this.formularios[det.id] = {
        detalleOrdenCompraId: det.id,
        cantidadRecibida: det.cantidad, // Por defecto la cantidad pedida
        numeroLote: '',
        fechaVencimiento: '',
        ubicacion: 'ALMACEN-PRINCIPAL',
        incidencia: ''
      };
    });
  }

  esFormularioValido(): boolean {
    if (!this.seleccionada) return false;
    return this.seleccionada.detalles.every(det => {
      const form = this.formularios[det.id];
      return form &&
        form.cantidadRecibida > 0 &&
        form.numeroLote &&
        form.fechaVencimiento &&
        form.ubicacion;
    });
  }

  registrarRecepcion() {
    if (!this.seleccionada || !this.esFormularioValido()) return;

    this.procesando = true;
    const payload = {
      ordenCompraId: this.seleccionada.id,
      detalles: Object.values(this.formularios)
    };

    this.http.post(`${this.api}/recepciones`, payload, {
      headers: this.headers,
      responseType: 'text'
    }).subscribe({
      next: (res) => {
        this.mensaje = 'Recepción registrada correctamente. Inventario actualizado.';
        this.procesando = false;
        this.cargarPendientes();
        this.seleccionada = null;
      },
      error: (err) => {
        console.error(err);
        this.mensaje = 'Error al registrar la recepción.';
        this.procesando = false;
      }
    });
  }
}