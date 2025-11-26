import { Routes } from '@angular/router';
import { LoginComponent } from './login';
import { DashboardComponent } from './dashboard';
import { ProgramacionComponent } from './modules/programacion';
import { ComprasComponent } from './modules/compras';
import { AlmacenComponent } from './modules/almacen';
import { DistribucionComponent } from './modules/distribucion';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent },

  { path: 'programacion', component: ProgramacionComponent },
  { path: 'compras', component: ComprasComponent },
  { path: 'almacen', component: AlmacenComponent },
  { path: 'distribucion', component: DistribucionComponent },
];
