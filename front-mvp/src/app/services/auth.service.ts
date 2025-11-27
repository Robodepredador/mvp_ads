import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

export interface MenuItem {
  label: string;
  path: string;
  icon: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {

  private api = 'http://localhost:8080';

  private isAuth$ = new BehaviorSubject<boolean>(false);
  private user$ = new BehaviorSubject<string | null>(null);

  constructor(private http: HttpClient) {
    const saved = sessionStorage.getItem('auth');
    const user = sessionStorage.getItem('user');
    if (saved && user) {
      this.isAuth$.next(true);
      this.user$.next(user);
    }
  }

  login(username: string, password: string): Observable<any> {
    const headers = new HttpHeaders({
      Authorization: 'Basic ' + btoa(`${username}:${password}`)
    });

    // endpoint sencillo que exista y acepte GET (puedes crear /api/public/ping)
    return this.http.get(`${this.api}/api/public/ping`, { headers })
      .pipe(
        tap(() => {
          this.isAuth$.next(true);
          this.user$.next(username);
          sessionStorage.setItem('auth', btoa(`${username}:${password}`));
          sessionStorage.setItem('user', username);
        })
      );
  }

  getAuthHeader() {
    const token = sessionStorage.getItem('auth');
    return new HttpHeaders({
      Authorization: `Basic ${token}`
    });
  }

  isLogged() {
    return this.isAuth$.asObservable();
  }

  getCurrentUserName(): string {
    return this.user$.value ?? '';
  }

  /** Menú según usuario (simple mapeo por username) */
  getMenu(): MenuItem[] {
    const user = this.user$.value;

    if (user === 'programacion') {
      return [
        { label: 'Dashboard', path: '/dashboard', icon: 'dashboard' },
        { label: 'Programación', path: '/programacion', icon: 'assignment' },
        { label: 'Distribución', path: '/distribucion', icon: 'local_shipping' }
      ];
    }

    if (user === 'compras') {
      return [
        { label: 'Dashboard', path: '/dashboard', icon: 'dashboard' },
        { label: 'Compras', path: '/compras', icon: 'shopping_cart' }
      ];
    }

    if (user === 'almacen') {
      return [
        { label: 'Dashboard', path: '/dashboard', icon: 'dashboard' },
        { label: 'Almacén', path: '/almacen', icon: 'inventory_2' }
      ];
    }

    if (user === 'distribucion') {
      return [
        { label: 'Dashboard', path: '/dashboard', icon: 'dashboard' },
        { label: 'Distribución', path: '/distribucion', icon: 'local_shipping' }
      ];
    }

    // por defecto, todo (para pruebas)
    return [
      { label: 'Dashboard', path: '/dashboard', icon: 'dashboard' },
      { label: 'Programación', path: '/programacion', icon: 'assignment' },
      { label: 'Compras', path: '/compras', icon: 'shopping_cart' },
      { label: 'Almacén', path: '/almacen', icon: 'inventory_2' },
      { label: 'Distribución', path: '/distribucion', icon: 'local_shipping' }
    ];
  }

  logout() {
    sessionStorage.clear();
    this.isAuth$.next(false);
    this.user$.next(null);
  }
}
