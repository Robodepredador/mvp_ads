import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { NgIf, NgFor, NgClass } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';

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
  distCantidad?: number;
  loteId?: number;
  compraCantidad?: number;
}

@Component({
  selector: 'app-programacion',
  standalone: true,
  imports: [NgIf, NgFor, NgClass, FormsModule, MatButtonModule],
  template: `
    <section class="programacion">
      <div class="panel">
        <div class="panel-header">
          <h2>Requerimientos pendientes</h2>
          <button mat-stroked-button color="primary" (click)="cargarRequerimientos()" [disabled]="cargando">
            Refrescar
          </button>
        </div>

        <div *ngIf="!requerimientos.length && !cargando" class="empty">
          No se encontraron requerimientos.
        </div>

        <ul class="req-list">
          <li *ngFor="let req of requerimientos" [class.activo]="req.id === requerimiento?.id">
            <div>
              <strong>#{{ req.id }}</strong> · Estado: {{ req.estado }}
            </div>
            <button mat-button color="primary" (click)="verDetalle(req.id)">
              Atender
            </button>
          </li>
        </ul>
      </div>

      <div class="panel detalle" *ngIf="requerimiento">
        <div class="panel-header">
          <h2>Requerimiento #{{ requerimiento.id }}</h2>
          <span class="estado" [ngClass]="{ completo: requerimiento.estado === 'PROGRAMADO' }">
            {{ requerimiento.estado }}
          </span>
        </div>

        <table class="tabla-detalles">
          <thead>
            <tr>
              <th>Producto</th>
              <th>Cantidad solicitada</th>
              <th>Distribución</th>
              <th>Compra</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let det of requerimiento.detalles">
              <td>
                {{ det.producto?.nombre || ('Producto #' + det.producto?.id) }}
              </td>
              <td>{{ det.cantidad }}</td>
              <td>
                <div class="campo">
                  <input type="number"
                         placeholder="Cantidad"
                         [(ngModel)]="formularios[det.id].distCantidad">
                </div>
                <div class="campo">
                  <select [(ngModel)]="formularios[det.id].loteId" (click)="asegurarLotes(det.producto?.id)">
                    <option [ngValue]="undefined">Seleccionar lote</option>
                    <option *ngFor="let lote of lotesDe(det.producto?.id)" [ngValue]="lote.id">
                      {{ lote.numeroLote }} · {{ lote.cantidad }} uds
                    </option>
                  </select>
                </div>
                <button mat-stroked-button color="primary"
                        (click)="registrarDistribucion(det)"
                        [disabled]="!formularios[det.id].distCantidad || !formularios[det.id].loteId">
                  Distribuir
                </button>
              </td>
              <td>
                <div class="campo">
                  <input type="number"
                         placeholder="Cantidad"
                         [(ngModel)]="formularios[det.id].compraCantidad">
                </div>
                <button mat-stroked-button color="accent"
                        (click)="registrarCompra(det)"
                        [disabled]="!formularios[det.id].compraCantidad">
                  Comprar
                </button>
              </td>
            </tr>
          </tbody>
        </table>

        <div class="acciones-final">
          <button mat-raised-button color="primary"
                  (click)="finalizarRequerimiento()"
                  [disabled]="finalizando">
            Finalizar programación
          </button>
        </div>
      </div>
    </section>

    <div class="mensaje" *ngIf="mensaje">{{ mensaje }}</div>
  `,
  styles: [`
    .programacion { display: flex; gap: 24px; flex-wrap: wrap; }
    .panel { flex: 1; min-width: 320px; border: 1px solid #ddd; border-radius: 8px; padding: 16px; background: #fff; }
    .panel-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
    .req-list { list-style: none; padding: 0; margin: 0; }
    .req-list li { display: flex; justify-content: space-between; align-items: center; padding: 8px 0; border-bottom: 1px solid #eee; }
    .req-list li.activo { background: #f3f6ff; }
    .tabla-detalles { width: 100%; border-collapse: collapse; }
    .tabla-detalles th, .tabla-detalles td { border-bottom: 1px solid #eee; padding: 8px; vertical-align: top; }
    .campo { margin-bottom: 8px; }
    .campo input, .campo select { width: 100%; padding: 6px; }
    .acciones-final { margin-top: 16px; display: flex; justify-content: flex-end; }
    .mensaje { margin-top: 16px; padding: 12px; border-radius: 4px; background: #e8f5e9; color: #1b5e20; }
    .estado { font-weight: 600; }
    .estado.completo { color: #1b5e20; }
    .empty { padding: 12px; color: #666; }
  `]
})
export class ProgramacionComponent implements OnInit {
  private readonly api = 'http://localhost:8080/api/programacion';

  requerimientos: Requerimiento[] = [];
  requerimiento: Requerimiento | null = null;
  lotes: Record<number, LoteProducto[]> = {};
  formularios: Record<number, FormDecision> = {};
  mensaje = '';
  cargando = false;
  finalizando = false;

  constructor(private http: HttpClient, private auth: AuthService) {}

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
          (req.detalles || []).forEach(det => this.formularios[det.id] = {});
        },
        error: () => this.mensaje = 'No se pudo obtener el detalle del requerimiento.'
      });
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
}
