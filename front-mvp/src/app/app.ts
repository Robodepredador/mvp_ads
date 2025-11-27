import { Component, inject } from '@angular/core';
import { RouterOutlet, Router, RouterLink } from '@angular/router';
import { NgIf, NgFor } from '@angular/common';
import { TopbarComponent } from './layout/topbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from './services/auth.service';
import { LoaderComponent } from './shared/loader.component';

@Component({
  selector: 'app-root',
  standalone: true,
  template: `
    <app-loader></app-loader>

    <!-- LOGIN -->
    <ng-container *ngIf="showLogin">
      <router-outlet></router-outlet>
    </ng-container>

    <!-- APP LAYOUT -->
    <ng-container *ngIf="!showLogin">
      <mat-sidenav-container class="app-container">
        <mat-sidenav mode="side" opened>
          <div class="sidebar-header">
            <span class="logo">MVP</span>
            <small>{{ currentUser() }}</small>
          </div>

          <mat-nav-list>
            <a mat-list-item *ngFor="let item of menu()" [routerLink]="item.path">
              <mat-icon>{{ item.icon }}</mat-icon>
              <span>{{ item.label }}</span>
            </a>
          </mat-nav-list>
        </mat-sidenav>

        <mat-sidenav-content>
          <app-topbar></app-topbar>
          <div class="page-container">
            <router-outlet></router-outlet>
          </div>
        </mat-sidenav-content>
      </mat-sidenav-container>
    </ng-container>
  `,
  imports: [
    RouterOutlet,
    RouterLink,
    NgIf,
    NgFor,
    TopbarComponent,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    LoaderComponent
  ]
})
export class App {
  router = inject(Router);
  auth = inject(AuthService);

  get showLogin() {
    const url = this.router.url;
    return url === '/' ||
      url.startsWith('/login') ||
      url.startsWith('/registro') ||
      url.startsWith('/olvide-mi-contrasena');
  }

  menu() {
    return this.auth.getMenu();
  }

  currentUser() {
    return this.auth.getCurrentUserName();
  }
}
