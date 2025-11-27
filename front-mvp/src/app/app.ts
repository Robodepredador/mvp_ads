import { Component, inject } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { NgIf } from '@angular/common';
import { TopbarComponent } from './layout/topbar';
import { LoaderComponent } from './shared/loader.component';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.html',
  styleUrls: ['./app.scss'],
  imports: [
    RouterOutlet,
    NgIf,
    TopbarComponent,
    LoaderComponent
  ]
})
export class App {
  private router = inject(Router);

  get showLogin() {
    const url = this.router.url;
    return url === '/' ||
      url.startsWith('/login') ||
      url.startsWith('/registro') ||
      url.startsWith('/olvide-mi-contrasena');
  }

  get showTopbar() {
    return !this.showLogin;
  }
}
