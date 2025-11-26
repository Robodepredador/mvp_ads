import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { NgFor, NgIf, JsonPipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card'; // <--- AGREGADO

@Component({
  selector: 'app-distribucion',
  standalone: true,
  imports: [NgFor, NgIf, JsonPipe, MatButtonModule, MatCardModule],
  template: `
    <h2>Distribución y Flota</h2>
    <mat-card>
      <button mat-raised-button color="primary" (click)="cargarPendientes()">
        🚚 Cargar Envíos Pendientes
      </button>
    </mat-card>

    <div class="section">
      <div *ngIf="ordenes.length === 0" style="padding:10px">No hay envíos pendientes.</div>

      <mat-card *ngFor="let od of ordenes">
        <h3>Orden de Distribución #{{ od.id }}</h3>
        <button mat-raised-button color="accent" (click)="asignar(od.id)">
          🔑 Asignar Vehículo (V-001)
        </button>
      </mat-card>
    </div>

    <div *ngIf="seguimiento" class="section">
      <mat-card style="background-color: #fff3e0;">
        <h3>Vehículo Asignado</h3>
        <pre>{{ seguimiento | json }}</pre>
        <button mat-raised-button color="warn" (click)="entregar(seguimiento.id)">
          🏁 Marcar como Entregado
        </button>
      </mat-card>
    </div>
  `
})
export class DistribucionComponent implements OnInit {
  api = 'http://localhost:8080/api/distribucion';
  ordenes: any[] = [];
  seguimiento: any;

  constructor(private http: HttpClient, private auth: AuthService) {}

  ngOnInit() { this.cargarPendientes(); }

  cargarPendientes() {
    this.http.get(`${this.api}/pendientes`, { headers: this.auth.getAuthHeader() })
      .subscribe((r: any) => this.ordenes = r);
  }

  asignar(id: number) {
    this.http.post(`${this.api}/asignar?ordenId=${id}&vehiculoId=1`, {}, { headers: this.auth.getAuthHeader() })
      .subscribe(r => this.seguimiento = r);
  }

  entregar(id: number) {
    this.http.post(`${this.api}/entregar/${id}`, {}, { headers: this.auth.getAuthHeader() })
      .subscribe(() => {
        alert("Entregado con éxito");
        this.seguimiento = null;
        this.cargarPendientes();
      });
  }
}