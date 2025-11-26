import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [MatButtonModule],
  template: `
    <h1>Panel Principal</h1>

    <button mat-raised-button color="primary" (click)="go('/programacion')">Programación</button>
    <button mat-raised-button color="accent" (click)="go('/compras')">Compras</button>
    <button mat-raised-button color="warn" (click)="go('/almacen')">Almacén</button>
    <button mat-raised-button color="primary" (click)="go('/distribucion')">Distribución</button>
  `,
  styles: [`
    h1 { text-align: center; margin-top: 40px; }
    button { margin: 10px; }
  `]
})
export class DashboardComponent {
  constructor(private router: Router) {}
  go(route: string) { this.router.navigate([route]); }
}
