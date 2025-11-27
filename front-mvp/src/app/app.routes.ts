import { Routes } from '@angular/router';
import { LoginComponent } from './login';
import { DashboardComponent } from './dashboard';
import { ProgramacionComponent } from './modules/programacion';
import { ComprasComponent } from './modules/compras';
import { AlmacenComponent } from './modules/almacen';
import { DistribucionComponent } from './modules/distribucion';
import { RegisterComponent } from './register';
import { ForgotPasswordComponent } from './forgot-password';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent },

  { path: 'programacion', component: ProgramacionComponent },
  { path: 'compras', component: ComprasComponent },
  { path: 'almacen', component: AlmacenComponent },
  { path: 'distribucion', component: DistribucionComponent },
  { path: 'registro', component: RegisterComponent },
  { path: 'olvide-mi-contrasena', component: ForgotPasswordComponent }
];
