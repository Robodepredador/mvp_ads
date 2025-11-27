// login.component.ts

import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './services/auth.service'; // Asegúrate que la ruta sea correcta
import { RouterModule } from '@angular/router';

// Importaciones de Angular Material
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCheckboxModule } from '@angular/material/checkbox'; // <-- Nuevo
import { MatIconModule } from '@angular/material/icon'; // <-- Nuevo

@Component({
  selector: 'app-login',
  standalone: true,
  // Actualizamos las importaciones
  imports: [
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCheckboxModule, // <-- Nuevo
    MatIconModule,      // <-- Nuevo
    RouterModule
  ],
  // Cambiamos a archivos externos
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class LoginComponent {
  username = 'programacion';
  password = 'mvp2024';
  hidePassword = true; // Para controlar la visibilidad de la contraseña

  constructor(private auth: AuthService, private router: Router) {}

  login() {
    this.auth.login(this.username, this.password).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: () => alert('Credenciales incorrectas') // Puedes reemplazar esto con un MatSnackBar para una mejor UX
    });
  }
}