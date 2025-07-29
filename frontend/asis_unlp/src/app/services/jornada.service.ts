import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Jornada } from '../models/jornada.model';
import { environment } from '../../environments/environment';
import { BarriosService } from './barrios.service';



@Injectable({ providedIn: 'root' })
export class JornadaService {
  private apiUrl = environment.apiUrl+"jornada";

  constructor(private http: HttpClient) {}

  getJornadas(): Observable<Jornada[]> {
    return this.http.get<Jornada[]>(this.apiUrl);
  }

  getJornada(id: number): Observable<Jornada> {
    return this.http.get<Jornada>(`${this.apiUrl}/${id}` ) ;
  }

  deleteJornada(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updateJornada(jornada: Jornada): Observable<Jornada> {
    return this.http.put<Jornada>(`${this.apiUrl}/${jornada.id}`,jornada);
  }

  createJornada(jornada: Jornada): Observable<Jornada> {
    return this.http.post<Jornada>(`${this.apiUrl}`, jornada);
  }

  getJornadasPorCampania(idCampania: number): Observable<Jornada[]> {
    return this.http.get<Jornada[]>(`${this.apiUrl}/campania/${idCampania}`);
  }


  agregarZona(jornadaId: number, zonaId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/agregarZona/${jornadaId}/${zonaId}`, {});
  }

  quitarZona(jornadaId: number, zonaId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/quitarZona/${jornadaId}/${zonaId}`,{});
  }


}