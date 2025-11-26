import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-topbar',
  standalone: true,
  imports: [MatToolbarModule, MatButtonModule],
  template: `
    <mat-toolbar color="primary">
      <span>Farmacéutica MVP</span>
      <span class="spacer"></span>
      <button mat-button (click)="logout()">Salir</button>
    </mat-toolbar>
  `,
  styles: [`
    .spacer { flex: 1 1 auto; }
    mat-toolbar {
      position: sticky;
      top: 0;
      z-index: 100;
    }
  `]
})
export class TopbarComponent {
  constructor(private router: Router) {}
  logout() {
    sessionStorage.clear();
    this.router.navigate(['/login']);
  }
}
