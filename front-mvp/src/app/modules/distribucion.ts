import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { NgFor, NgIf, JsonPipe, DatePipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';

interface Vehiculo {
  id: number;
  placa: string;
  marca: string;
  modelo: string;
  tipo: string;
  capacidad: number;
  estado: string;
}

interface DetalleOrden {
  id: number;
  producto: { nombre: string };
  cantidad: number;
}

interface OrdenDistribucion {
  id: number;
  destino: string;
  fechaSalida: string;
  estado: string;
  detalles: DetalleOrden[];
}

@Component({
  selector: 'app-distribucion',
  standalone: true,
  imports: [NgFor, NgIf, JsonPipe, DatePipe, MatButtonModule, MatCardModule, MatIconModule, FormsModule],
  templateUrl: './distribucion.html',
  styleUrls: ['./distribucion.scss']
})
export class DistribucionComponent implements OnInit {
  private readonly api = 'http://localhost:8080/api/distribucion';

  ordenes: OrdenDistribucion[] = [];
  vehiculos: Vehiculo[] = [];
  seleccionada: OrdenDistribucion | null = null;

  // Formulario de asignación
  vehiculoSeleccionadoId: number | null = null;
  observacion: string = '';

  mensaje = '';
  cargando = false;
  procesando = false;

  constructor(private http: HttpClient, private auth: AuthService) { }

  ngOnInit() {
    this.cargarPendientes();
    this.cargarVehiculos();
  }

  get headers() {
    return this.auth.getAuthHeader();
  }

  cargarPendientes() {
    this.cargando = true;
    this.http.get<OrdenDistribucion[]>(`${this.api}/pendientes`, { headers: this.headers })
      .subscribe({
        next: (data) => {
          this.ordenes = data;
          this.cargando = false;
          if (this.seleccionada && !this.ordenes.find(o => o.id === this.seleccionada?.id)) {
            this.seleccionada = null;
          }
        },
        error: () => this.cargando = false
      });
  }

  cargarVehiculos() {
    this.http.get<Vehiculo[]>(`${this.api}/vehiculos`, { headers: this.headers })
      .subscribe(data => this.vehiculos = data);
  }

  seleccionar(orden: OrdenDistribucion) {
    this.seleccionada = orden;
    this.mensaje = '';
    this.vehiculoSeleccionadoId = null;
    this.observacion = '';
  }

  asignarTransporte() {
    if (!this.seleccionada || !this.vehiculoSeleccionadoId) return;

    this.procesando = true;

    // Usamos el endpoint simple 'asignar' que toma ordenId y vehiculoId
    // El endpoint complejo 'asignaciones' es para asignar por detalle, pero para este MVP
    // simplificaremos asignando toda la orden a un vehículo.

    const params = `?ordenId=${this.seleccionada.id}&vehiculoId=${this.vehiculoSeleccionadoId}`;

    this.http.post(`${this.api}/asignar${params}`, {}, { headers: this.headers })
      .subscribe({
        next: (res) => {
          this.mensaje = 'Vehículo asignado y orden en ruta.';
          this.procesando = false;
          this.cargarPendientes();
          this.seleccionada = null;
        },
        error: () => {
          this.mensaje = 'Error al asignar vehículo.';
          this.procesando = false;
        }
      });
  }

  getVehiculoSeleccionado() {
    return this.vehiculos.find(v => v.id == this.vehiculoSeleccionadoId);
  }
}