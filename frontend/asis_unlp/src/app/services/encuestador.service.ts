import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Encuestador } from '../models/encuestador.model';
import { environment } from '../../environments/environment';



@Injectable({ providedIn: 'root' })
export class EncuestadorService {
  private apiUrl = environment.apiUrl+"encuestador";

  constructor(private http: HttpClient) {}

  getEncuestadores(): Observable<Encuestador[]> {
    return this.http.get<Encuestador[]>(this.apiUrl);
  }

  getEncuestador(id: number): Observable<Encuestador> {
    return this.http.get<Encuestador>(`${this.apiUrl}/${id}` ) ;
  }

  deleteEncuestador(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }



  updateEncuestador(encuestador: Encuestador): Observable<Encuestador> {
    return this.http.put<Encuestador>(`${this.apiUrl}/${encuestador.id}`,encuestador);
  }
  

  createEncuestador(encuestador: Encuestador): Observable<Encuestador> {
    return this.http.post<Encuestador>(this.apiUrl, encuestador);
  }



}