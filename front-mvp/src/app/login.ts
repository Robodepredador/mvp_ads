import { Component } from '@angular/core';
import { AuthService } from './services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card'; // <--- CRÍTICO

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, MatInputModule, MatButtonModule, MatCardModule],
  template: `
    <div class="login-wrapper">
      <mat-card class="login-card">
        <mat-card-header>
          <mat-card-title>Acceso al Sistema</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <p class="hint">Usuario inicial: programacion / mvp2024</p>
          <div class="input-group">
            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Usuario</mat-label>
              <input matInput [(ngModel)]="username">
            </mat-form-field>
          </div>
          <div class="input-group">
            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Contraseña</mat-label>
              <input matInput type="password" [(ngModel)]="password">
            </mat-form-field>
          </div>
        </mat-card-content>
        <mat-card-actions align="end">
          <button mat-raised-button color="primary" (click)="login()">Ingresar</button>
        </mat-card-actions>
      </mat-card>
    </div>
  `,
  styles: [`
    .login-wrapper { height: 80vh; display: flex; justify-content: center; align-items: center; }
    .login-card { width: 350px; padding: 20px; }
    .full-width { width: 100%; }
    .input-group { margin-bottom: 10px; margin-top: 10px; }
    .hint { margin: 0; color: #666; font-size: 12px; }
  `]
})
export class LoginComponent {
  username = 'programacion';
  password = 'mvp2024';

  constructor(private auth: AuthService, private router: Router) {}

  login() {
    this.auth.login(this.username, this.password).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: () => alert('Credenciales incorrectas')
    });
  }
}