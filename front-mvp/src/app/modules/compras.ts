import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { NgFor, NgIf, JsonPipe } from '@angular/common'; // Agregado JsonPipe
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card'; // <--- AGREGADO

@Component({
  selector: 'app-compras',
  standalone: true,
  imports: [NgFor, NgIf, JsonPipe, MatButtonModule, MatCardModule],
  template: `
    <h2>Departamento de Compras</h2>
    
    <mat-card>
      <button mat-raised-button color="primary" (click)="cargarPendientes()">
        🔄 Cargar Solicitudes Pendientes
      </button>
    </mat-card>

    <div class="section">
      <div *ngIf="solicitudes.length === 0" style="padding:10px">
        <small>No hay solicitudes pendientes.</small>
      </div>

      <mat-card *ngFor="let s of solicitudes">
        <h3>Solicitud #{{ s.id }}</h3>
        <p>Servicio: {{ s.servicioSolicitante }} | Estado: {{ s.estado }}</p>
        <button mat-raised-button color="accent" (click)="generarOrden(s.id)">
          Generar Orden de Compra
        </button>
      </mat-card>
    </div>

    <div *ngIf="ordenGenerada" class="section">
      <mat-card style="background-color: #e8f5e9;">
        <h3>✅ Orden de Compra Generada</h3>
        <pre>{{ ordenGenerada | json }}</pre>
      </mat-card>
    </div>
  `
})
export class ComprasComponent implements OnInit {
  api = 'http://localhost:8080/api/compras';
  solicitudes: any[] = [];
  ordenGenerada: any;

  constructor(private http: HttpClient, private auth: AuthService) {}

  ngOnInit() { this.cargarPendientes(); }

  cargarPendientes() {
    this.http.get(`${this.api}/pendientes`, { headers: this.auth.getAuthHeader() })
      .subscribe((r: any) => this.solicitudes = r);
  }

  generarOrden(id: number) {
    this.http.post(`${this.api}/generar-orden/${id}`, {}, { headers: this.auth.getAuthHeader() })
      .subscribe((r: any) => this.ordenGenerada = r);
  }
}