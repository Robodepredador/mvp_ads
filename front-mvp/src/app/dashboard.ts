import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { NgFor, NgIf } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [MatButtonModule, MatIconModule, NgFor, NgIf],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.scss']
})
export class DashboardComponent {
  stats = [
    { label: 'Requerimientos pendientes', value: 12, meta: '+3 hoy' },
    { label: 'Compras en revisión', value: 5, meta: '2 requieren firma' },
    { label: 'Órdenes en tránsito', value: 8, meta: 'sin incidencias' }
  ];

  modules = [
    {
      title: 'Programación',
      description: 'Analiza requerimientos, decide distribución o compra y cierra casos.',
      route: '/programacion',
      icon: 'assignment'
    },
    {
      title: 'Compras',
      description: 'Centraliza solicitudes de compra y da seguimiento a proveedores.',
      route: '/compras',
      icon: 'shopping_cart'
    },
    {
      title: 'Almacén',
      description: 'Controla inventario, lotes críticos y movimientos entre sedes.',
      route: '/almacen',
      icon: 'inventory_2'
    },
    {
      title: 'Distribución',
      description: 'Gestiona órdenes de despacho y estado de entregas en ruta.',
      route: '/distribucion',
      icon: 'local_shipping'
    }
  ];

  constructor(private router: Router) {}
  go(route: string) { this.router.navigate([route]); }
}
