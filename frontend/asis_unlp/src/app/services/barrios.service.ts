import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Barrio, BarrioForm } from '../models/barrio.model';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';
import { log } from 'console';



@Injectable({ providedIn: 'root' })
export class BarriosService {
  private apiUrl = environment.apiUrl+"barrio";

  constructor(private http: HttpClient,  private authService: AuthService) {}

  getBarrios(): Observable<Barrio[]> {
    const token = this.authService.obtenerToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.get<Barrio[]>(this.apiUrl, {headers});
  }

  deleteBarrio(id: number): Observable<void> {
    const token = this.authService.obtenerToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.delete<void>(`${this.apiUrl}/${id}`, {headers});
  }


  createBarrio(barrio: BarrioForm): Observable<Barrio> {
    const token = this.authService.obtenerToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.post<Barrio>(this.apiUrl, barrio, {headers});
  }

  updateBarrio(barrio: Barrio): Observable<Barrio> {
    const token = this.authService.obtenerToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.put<Barrio>(`${this.apiUrl}/${barrio.id}`, barrio, {headers});
  }

  getBarrio(id: number): Observable<Barrio> {
    const token = this.authService.obtenerToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.get<Barrio>(`${this.apiUrl}/${id}`, {headers});
  }

}