import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { LoginDTO } from '../models/loginDTO';
import { Token } from '../models/token';
import { Observable } from 'rxjs';

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
}