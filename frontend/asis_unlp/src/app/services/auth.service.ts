import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { LoginDTO } from '../models/loginDTO';
import { Token } from '../models/token';
import { Observable } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

@Injectable({ providedIn: 'root' })
export class AuthService {
  
  private apiUrl = environment.apiUrl+"usuario";

  constructor(private http: HttpClient) {}

  login(credentials: LoginDTO): Observable<Token> {
    return this.http.post<Token>(this.apiUrl+"/login", credentials);
  }

  guardarToken(token: string) {
    localStorage.setItem('authToken', token);
  }

  obtenerToken(): string | null {
    return localStorage.getItem('authToken');
  }

  cerrarSesion() {
    localStorage.removeItem('authToken');
  }

  getRoles(): string[] {
    const token = this.obtenerToken();
    if (!token) return [];
    
    try {
      const decoded: any = jwtDecode(token);
      return decoded.roles || []; // Extrae los roles del token
    } catch {
      return [];
    }
  }

  tieneRol(role: string): boolean {
    return this.getRoles().includes(role);
  }

  loggedIn(): boolean {
    return !!this.obtenerToken();
  }

  rolAdmin(): boolean {
    return this.tieneRol('admin');
  }

  rolSalud(): boolean {
    return this.tieneRol('personal_salud') || this.rolAdmin();
  }

  rolReferente(): boolean { 
    return this.tieneRol('referente') || this.rolAdmin();
  }

  getUsuarioId(): number | null {
    const token = this.obtenerToken();
    if (!token) return null;

    try {
      const decoded: any = jwtDecode(token);
      return decoded.sub || null;
    } catch {
      return null;
    }
  }
}