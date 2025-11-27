import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { NgIf, NgFor, NgClass } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

interface Producto {
  id: number;
  nombre: string;
}

interface DetalleRequerimiento {
  id: number;
  producto?: Producto | null;
  cantidad: number;
}

interface Requerimiento {
  id: number;
  estado: string;
  detalles: DetalleRequerimiento[];
}

interface LoteProducto {
  id: number;
  numeroLote: string;
  cantidad: number;
}

interface FormDecision {
  accion: 'DISTRIBUIR' | 'COMPRAR';
  distCantidad?: number;
  loteId?: number;
  compraCantidad?: number;
}

interface DecisionRegistro {
  tipo: string;
  productoId: number;
  producto: string;
  cantidad: number;
  motivo: string;
  fecha: string;
}

@Component({
  selector: 'app-programacion',
  standalone: true,
  imports: [NgIf, NgFor, NgClass, FormsModule, MatButtonModule, MatIconModule],
  templateUrl: './programacion.html',
  styleUrls: ['./programacion.scss']
})
export class ProgramacionComponent implements OnInit {
  private readonly api = 'http://localhost:8080/api/programacion';

  requerimientos: Requerimiento[] = [];
  requerimiento: Requerimiento | null = null;
  lotes: Record<number, LoteProducto[]> = {};
  formularios: Record<number, FormDecision> = {};
  historial: Record<number, number> = {}; // productoId -> cantidad programada
  mensaje = '';
  cargando = false;
  finalizando = false;

  constructor(private http: HttpClient, private auth: AuthService) { }

  ngOnInit(): void {
    this.cargarRequerimientos();
  }

  private headers(): HttpHeaders {
    return this.auth.getAuthHeader();
  }

  cargarRequerimientos(): void {
    this.cargando = true;
    this.http.get<Requerimiento[]>(`${this.api}/requerimientos`, { headers: this.headers() })
      .subscribe({
        next: (reqs) => {
          this.requerimientos = reqs;
          this.cargando = false;
        },
        error: () => {
          this.mensaje = 'No se pudieron cargar los requerimientos.';
          this.cargando = false;
        }
      });
  }

  verDetalle(id: number): void {
    this.http.get<Requerimiento>(`${this.api}/requerimientos/${id}`, { headers: this.headers() })
      .subscribe({
        next: (req) => {
          this.requerimiento = req;
          this.mensaje = '';
          this.formularios = {};
          this.historial = {};
          (req.detalles || []).forEach(det => {
            const stock = this.getStockTotal(det.producto?.id);
            this.formularios[det.id] = {
              accion: stock > 0 ? 'DISTRIBUIR' : 'COMPRAR'
            };
            this.asegurarLotes(det.producto?.id);
          });
          this.cargarHistorial(id);
        },
        error: () => this.mensaje = 'No se pudo obtener el detalle del requerimiento.'
      });
  }

  getStockTotal(productoId?: number): number {
    if (!productoId || !this.lotes[productoId]) return 0;
    return this.lotes[productoId].reduce((acc, lote) => acc + lote.cantidad, 0);
  }

  cargarHistorial(id: number): void {
    this.http.get<DecisionRegistro[]>(`${this.api}/requerimientos/${id}/historial`, { headers: this.headers() })
      .subscribe({
        next: (registros) => {
          const mapa: Record<number, number> = {};
          registros.forEach(r => {
            const actual = mapa[r.productoId] || 0;
            mapa[r.productoId] = actual + r.cantidad;
          });
          this.historial = mapa;
        }
      });
  }

  getProgramado(productoId?: number): number {
    return productoId ? (this.historial[productoId] || 0) : 0;
  }

  getPendiente(det: DetalleRequerimiento): number {
    if (!det.producto) return 0;
    const programado = this.getProgramado(det.producto.id);
    return Math.max(0, det.cantidad - programado);
  }

  isCompletado(det: DetalleRequerimiento): boolean {
    return this.getPendiente(det) === 0;
  }

  asegurarLotes(productoId?: number | null): void {
    if (!productoId || this.lotes[productoId]) {
      return;
    }
    this.http.get<LoteProducto[]>(`${this.api}/lotes`, {
      headers: this.headers(),
      params: { productoId }
    }).subscribe({
      next: (lotes) => this.lotes[productoId] = lotes,
      error: () => this.mensaje = 'No se pudieron cargar los lotes del producto.'
    });
  }

  registrarDistribucion(det: DetalleRequerimiento): void {
    const form = this.formularios[det.id];
    const productoId = det.producto?.id;
    if (!form?.distCantidad || !form?.loteId || !this.requerimiento || !productoId) {
      return;
    }
    this.http.post(`${this.api}/decidir/distribucion`, null, {
      headers: this.headers(),
      params: {
        requerimientoId: this.requerimiento.id,
        productoId,
        cantidad: form.distCantidad,
        loteId: form.loteId
      }
    }).subscribe({
      next: (msg: any) => {
        this.mensaje = typeof msg === 'string' ? msg : 'Distribución registrada.';
        form.distCantidad = undefined;
        form.loteId = undefined;
        this.verDetalle(this.requerimiento!.id);
      },
      error: (err) => {
        this.mensaje = err?.error || 'No se pudo registrar la distribución.';
      }
    });
  }

  registrarCompra(det: DetalleRequerimiento): void {
    const form = this.formularios[det.id];
    const productoId = det.producto?.id;
    if (!form?.compraCantidad || !this.requerimiento || !productoId) {
      return;
    }
    this.http.post(`${this.api}/decidir/compra`, null, {
      headers: this.headers(),
      params: {
        requerimientoId: this.requerimiento.id,
        productoId,
        cantidad: form.compraCantidad
      }
    }).subscribe({
      next: (msg: any) => {
        this.mensaje = typeof msg === 'string' ? msg : 'Solicitud de compra registrada.';
        form.compraCantidad = undefined;
        this.verDetalle(this.requerimiento!.id);
      },
      error: (err) => {
        this.mensaje = err?.error || 'No se pudo registrar la compra.';
      }
    });
  }

  finalizarRequerimiento(): void {
    if (!this.requerimiento) {
      return;
    }
    this.finalizando = true;
    this.http.post(`${this.api}/requerimientos/${this.requerimiento.id}/finalizar`, null, {
      headers: this.headers()
    }).subscribe({
      next: (msg: any) => {
        this.mensaje = typeof msg === 'string' ? msg : 'Requerimiento finalizado.';
        this.finalizando = false;
        this.verDetalle(this.requerimiento!.id);
        this.cargarRequerimientos();
      },
      error: (err) => {
        this.mensaje = err?.error || 'No se pudo finalizar el requerimiento.';
        this.finalizando = false;
      }
    });
  }

  lotesDe(productoId?: number | null): LoteProducto[] {
    if (!productoId) {
      return [];
    }
    return this.lotes[productoId] || [];
  }

  get pendientes(): number {
    return this.requerimientos.filter(req => req.estado === 'PENDIENTE').length;
  }

  get parciales(): number {
    return this.requerimientos.filter(req => req.estado === 'PARCIAL').length;
  }

  get programados(): number {
    return this.requerimientos.filter(req => req.estado === 'PROGRAMADO').length;
  }
}
