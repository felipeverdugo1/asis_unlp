import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Zona } from '../models/zona.model';
import { environment } from '../../environments/environment';
import { BarriosService } from './barrios.service';



@Injectable({ providedIn: 'root' })
export class ZonaService {
  private apiUrl = environment.apiUrl+"zona";

  constructor(private http: HttpClient) {}

  getZonas(): Observable<Zona[]> {
    return this.http.get<Zona[]>(this.apiUrl);
  }

  getZona(id: number): Observable<Zona> {
    return this.http.get<Zona>(`${this.apiUrl}/${id}` ) ;
  }

  deleteZona(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }



  updateZona(zona: Zona): Observable<Zona> {
    return this.http.put<Zona>(`${this.apiUrl}/${zona.id}`,zona);
  }
  

  createZona(zona: Zona): Observable<Zona> {
    return this.http.post<Zona>(this.apiUrl, zona);
  }



}