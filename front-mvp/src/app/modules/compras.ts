import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { NgFor, NgIf, JsonPipe, DatePipe, CurrencyPipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';

interface Producto {
  id: number;
  nombre: string;
}

interface Proveedor {
  id: number;
  nombre: string;
}

interface ProductoProveedor {
  id: number;
  proveedor: Proveedor;
  precio: number;
  moneda: string;
}

interface DetalleSolicitud {
  id: number;
  producto: Producto;
  cantidad: number;
  proveedorSeleccionado?: Proveedor;
  precioNegociado?: number;
}

interface SolicitudCompra {
  id: number;
  fechaSolicitud: string;
  solicitante: string;
  servicioSolicitante: string;
  estado: string;
  detalles: DetalleSolicitud[];
}

@Component({
  selector: 'app-compras',
  standalone: true,
  imports: [NgFor, NgIf, JsonPipe, DatePipe, CurrencyPipe, MatButtonModule, MatCardModule, MatIconModule, FormsModule],
  templateUrl: './compras.html',
  styleUrls: ['./compras.scss']
})
export class ComprasComponent implements OnInit {
  private readonly api = 'http://localhost:8080/api/compras';

  solicitudes: SolicitudCompra[] = [];
  seleccionada: SolicitudCompra | null = null;

  // Cache de proveedores por producto: productoId -> lista
  proveedoresCache: Record<number, ProductoProveedor[]> = {};

  // Formularios temporales: detalleId -> { proveedorId, precio }
  formularios: Record<number, { proveedorId?: number; precio?: number }> = {};

  ordenGenerada: any;
  mensaje = '';
  cargando = false;

  constructor(private http: HttpClient, private auth: AuthService) { }

  ngOnInit() {
    this.cargarPendientes();
  }

  get headers() {
    return this.auth.getAuthHeader();
  }

  cargarPendientes() {
    this.cargando = true;
    this.http.get<SolicitudCompra[]>(`${this.api}/pendientes`, { headers: this.headers })
      .subscribe({
        next: (data) => {
          this.solicitudes = data;
          this.cargando = false;
        },
        error: () => this.cargando = false
      });
  }

  seleccionar(solicitud: SolicitudCompra) {
    this.seleccionada = solicitud;
    this.ordenGenerada = null;
    this.mensaje = '';
    this.formularios = {};

    // Pre-cargar proveedores para cada producto
    solicitud.detalles.forEach(det => {
      this.formularios[det.id] = {
        proveedorId: det.proveedorSeleccionado?.id,
        precio: det.precioNegociado
      };
      this.cargarProveedores(det.producto.id);
    });
  }

  cargarProveedores(productoId: number) {
    if (this.proveedoresCache[productoId]) return;

    this.http.get<ProductoProveedor[]>(`${this.api}/proveedores/${productoId}`, { headers: this.headers })
      .subscribe(data => this.proveedoresCache[productoId] = data);
  }

  getProveedores(productoId: number): ProductoProveedor[] {
    return this.proveedoresCache[productoId] || [];
  }

  onProveedorChange(detalleId: number, productoId: number) {
    const form = this.formularios[detalleId];
    if (!form?.proveedorId) return;

    const prov = this.getProveedores(productoId).find(p => p.proveedor.id === form.proveedorId);
    if (prov) {
      form.precio = prov.precio; // Sugerir precio referencial
    }
  }

  asignarProveedor(detalle: DetalleSolicitud) {
    const form = this.formularios[detalle.id];
    if (!form?.proveedorId || !form?.precio) return;

    this.http.post(`${this.api}/detalles/${detalle.id}/proveedor`, {
      proveedorId: form.proveedorId,
      precioNegociado: form.precio
    }, { headers: this.headers }).subscribe({
      next: (detActualizado: any) => {
        // Actualizar localmente
        detalle.proveedorSeleccionado = detActualizado.proveedorSeleccionado;
        detalle.precioNegociado = detActualizado.precioNegociado;
        this.mensaje = 'Proveedor asignado correctamente.';
        setTimeout(() => this.mensaje = '', 3000);
      },
      error: () => this.mensaje = 'Error al asignar proveedor.'
    });
  }

  todosAsignados(): boolean {
    if (!this.seleccionada) return false;
    return this.seleccionada.detalles.every(d => d.proveedorSeleccionado);
  }

  generarOrden() {
    if (!this.seleccionada || !this.todosAsignados()) return;

    this.http.post(`${this.api}/generar-orden/${this.seleccionada.id}`, {}, { headers: this.headers })
      .subscribe({
        next: (res) => {
          this.ordenGenerada = res;
          this.mensaje = 'Orden de compra generada exitosamente.';
          this.cargarPendientes(); // Refrescar lista
          this.seleccionada = null; // Limpiar selección o mantenerla para ver resultado
        },
        error: () => this.mensaje = 'Error al generar la orden.'
      });
  }
}