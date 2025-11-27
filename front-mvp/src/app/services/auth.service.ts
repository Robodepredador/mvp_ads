import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

export interface MenuItem {
  label: string;
  path: string;
  icon: string;
}

export interface AuthPingResponse {
  status: string;
  username: string;
  roles: string[];
}

@Injectable({ providedIn: 'root' })
export class AuthService {

  private api = 'http://localhost:8080';

  private isAuth$ = new BehaviorSubject<boolean>(false);
  private user$ = new BehaviorSubject<string | null>(null);
  private role$ = new BehaviorSubject<string | null>(null);

  constructor(private http: HttpClient) {
    const saved = sessionStorage.getItem('auth');
    const user = sessionStorage.getItem('user');
    const role = sessionStorage.getItem('role');
    if (saved && user) {
      this.isAuth$.next(true);
      this.user$.next(user);
      if (role) {
        this.role$.next(role);
      }
    }
  }

  login(username: string, password: string): Observable<AuthPingResponse> {
    const headers = new HttpHeaders({
      Authorization: 'Basic ' + btoa(`${username}:${password}`)
    });

    // endpoint sencillo que exista y acepte GET (puedes crear /api/public/ping)
    return this.http.get<AuthPingResponse>(`${this.api}/api/public/ping`, { headers })
      .pipe(
        tap((resp) => {
          const currentUser = resp?.username || username;
          const role = resp?.roles?.[0] || null;
          this.isAuth$.next(true);
          this.user$.next(currentUser);
          this.role$.next(role);
          sessionStorage.setItem('auth', btoa(`${username}:${password}`));
          sessionStorage.setItem('user', currentUser);
          if (role) {
            sessionStorage.setItem('role', role);
          } else {
            sessionStorage.removeItem('role');
          }
        })
      );
  }

  getAuthHeader() {
    const token = sessionStorage.getItem('auth');
    if (!token) {
      return new HttpHeaders();
    }
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

  getCurrentRole(): string {
    return this.role$.value ?? '';
  }


  /** Menú según usuario (simple mapeo por username) */
  getMenu(): MenuItem[] {
    const role = this.role$.value?.replace('ROLE_', '').toLowerCase();
    const user = this.user$.value;

    if (role === 'programacion' || user === 'programacion') {
      return [
        { label: 'Dashboard', path: '/dashboard', icon: 'dashboard' },
        { label: 'Programación', path: '/programacion', icon: 'assignment' }
      ];
    }

    if (role === 'compras' || user === 'compras') {
      return [
        { label: 'Dashboard', path: '/dashboard', icon: 'dashboard' },
        { label: 'Compras', path: '/compras', icon: 'shopping_cart' }
      ];
    }

    if (role === 'almacen' || user === 'almacen') {
      return [
        { label: 'Dashboard', path: '/dashboard', icon: 'dashboard' },
        { label: 'Almacén', path: '/almacen', icon: 'inventory_2' }
      ];
    }

    if (role === 'distribucion' || user === 'distribucion') {
      return [
        { label: 'Dashboard', path: '/dashboard', icon: 'dashboard' },
        { label: 'Distribución', path: '/distribucion', icon: 'local_shipping' }
      ];
    }

    // Default: Dashboard only
    return [
      { label: 'Dashboard', path: '/dashboard', icon: 'dashboard' }
    ];
  }

  logout() {
    sessionStorage.clear();
    this.isAuth$.next(false);
    this.user$.next(null);
    this.role$.next(null);
  }
}
