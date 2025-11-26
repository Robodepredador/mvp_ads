import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { NgFor, NgIf } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card'; // <--- AGREGADO

@Component({
  selector: 'app-almacen',
  standalone: true,
  imports: [NgFor, NgIf, MatButtonModule, MatCardModule],
  template: `
    <h2>Almacén - Recepción</h2>
    <mat-card>
      <button mat-raised-button color="primary" (click)="cargarPendientes()">
        📦 Cargar Órdenes Pendientes
      </button>
    </mat-card>

    <div class="section">
      <div *ngIf="ordenes.length === 0" style="padding:10px">No hay órdenes pendientes.</div>

      <mat-card *ngFor="let oc of ordenes">
        <h3>Orden de Compra #{{ oc.id }}</h3>
        <p>Proveedor: Farmacia Central</p>
        <button mat-raised-button color="warn" (click)="recibir(oc.id)">
          📥 Recibir Productos e Ingresar a Stock
        </button>
      </mat-card>
    </div>

    <div *ngIf="mensaje" class="section">
      <mat-card style="background-color: #e3f2fd;">
        <h3>Resultado:</h3>
        <p>{{ mensaje }}</p>
      </mat-card>
    </div>
  `
})
export class AlmacenComponent implements OnInit {
  api = 'http://localhost:8080/api/almacen';
  ordenes: any[] = [];
  mensaje: string = '';

  constructor(private http: HttpClient, private auth: AuthService) {}

  ngOnInit() { this.cargarPendientes(); }

  cargarPendientes() {
    this.http.get(`${this.api}/pendientes`, { headers: this.auth.getAuthHeader() })
      .subscribe((r: any) => this.ordenes = r);
  }

  recibir(id: number) {
    this.http.post(`${this.api}/recibir/${id}`, {}, { 
      headers: this.auth.getAuthHeader(),
      responseType: 'text' // Importante si el backend devuelve String plano
    }).subscribe((r: any) => this.mensaje = r);
  }
}