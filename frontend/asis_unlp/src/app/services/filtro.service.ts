 import { Injectable } from '@angular/core';
 import { HttpClient } from '@angular/common/http';
 import { Observable } from 'rxjs';
 import { Filtro } from '../models/filtro.model';
 import { environment } from '../../environments/environment';



@Injectable({ providedIn: 'root' })
export class FiltroService {
    private apiUrl = environment.apiUrl+"filtro";

    constructor(private http: HttpClient) {}

    getFiltros(): Observable<Filtro[]> {
        return this.http.get<Filtro[]>(this.apiUrl);
    }

    getFiltro(id: number): Observable<Filtro> {
        return this.http.get<Filtro>(`${this.apiUrl}/${id}` ) ;
    }

    deleteFiltro(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    updateFiltro(filtro: Filtro): Observable<Filtro> {
        return this.http.put<Filtro>(`${this.apiUrl}/${filtro.id}`,filtro);
    }
  
    createFiltro(filtro: Filtro): Observable<Filtro> {
        return this.http.post<Filtro>(this.apiUrl, filtro);
    }

    getFiltroById(id: number): Observable<Filtro> {
      return this.http.get<Filtro>(`${this.apiUrl}/${id}`);
    }

    getFiltrosByUsuario(usuarioId: number): Observable<Filtro[]> {
        return this.http.get<Filtro[]>(`${this.apiUrl}/usuario/${usuarioId}`);
    }

}