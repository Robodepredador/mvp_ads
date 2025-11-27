import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { NgFor, NgIf } from '@angular/common';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [MatButtonModule, MatIconModule, NgFor, NgIf],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.scss']
})
export class DashboardComponent implements OnInit {
  stats: any[] = [];
  modules: any[] = [];
  userRole: string = '';

  private allModules = [
    {
      title: 'Programación',
      description: 'Analiza requerimientos, decide distribución o compra y cierra casos.',
      route: '/programacion',
      icon: 'assignment',
      role: 'PROGRAMACION'
    },
    {
      title: 'Compras',
      description: 'Centraliza solicitudes de compra y da seguimiento a proveedores.',
      route: '/compras',
      icon: 'shopping_cart',
      role: 'COMPRAS'
    },
    {
      title: 'Almacén',
      description: 'Controla inventario, lotes críticos y movimientos entre sedes.',
      route: '/almacen',
      icon: 'inventory_2',
      role: 'ALMACEN'
    },
    {
      title: 'Distribución',
      description: 'Gestiona órdenes de despacho y estado de entregas en ruta.',
      route: '/distribucion',
      icon: 'local_shipping',
      role: 'DISTRIBUCION'
    }
  ];

  constructor(private router: Router, private auth: AuthService, private http: HttpClient) { }

  ngOnInit() {
    this.userRole = this.auth.getCurrentRole().replace('ROLE_', '');
    this.filterModules();
    this.loadStats();
  }

  filterModules() {
    if (this.userRole === 'ADMIN') {
      this.modules = this.allModules;
    } else {
      this.modules = this.allModules.filter(m => m.role === this.userRole);
    }
  }

  loadStats() {
    const headers = this.auth.getAuthHeader();

    if (this.userRole === 'PROGRAMACION' || this.userRole === 'ADMIN') {
      this.http.get<any>('http://localhost:8080/api/programacion/resumen', { headers }).subscribe(res => {
        this.stats.push({ label: 'Pendientes de Programar', value: res.pendientes, meta: 'Requerimientos' });
        this.stats.push({ label: 'En Proceso', value: res.parciales, meta: 'Parcialmente atendidos' });
      });
    }

    if (this.userRole === 'COMPRAS' || this.userRole === 'ADMIN') {
      this.http.get<any[]>('http://localhost:8080/api/compras/pendientes', { headers }).subscribe(res => {
        this.stats.push({ label: 'Solicitudes de Compra', value: res.length, meta: 'Pendientes de orden' });
      });
    }

    if (this.userRole === 'ALMACEN' || this.userRole === 'ADMIN') {
      this.http.get<any[]>('http://localhost:8080/api/almacen/pendientes', { headers }).subscribe(res => {
        this.stats.push({ label: 'Recepciones Pendientes', value: res.length, meta: 'Órdenes de Compra' });
      });
    }

    if (this.userRole === 'DISTRIBUCION' || this.userRole === 'ADMIN') {
      this.http.get<any[]>('http://localhost:8080/api/distribucion/pendientes', { headers }).subscribe(res => {
        this.stats.push({ label: 'Despachos Pendientes', value: res.length, meta: 'Órdenes de Distribución' });
      });
    }
  }

  go(route: string) { this.router.navigate([route]); }
}
