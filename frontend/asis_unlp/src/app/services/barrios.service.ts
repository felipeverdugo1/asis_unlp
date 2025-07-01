import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Barrio, BarrioForm } from '../models/barrio.model';
import { environment } from '../../environments/environment';
import { log } from 'console';



@Injectable({ providedIn: 'root' })
export class BarriosService {
  private apiUrl = environment.apiUrl+"barrio";

  constructor(private http: HttpClient) {}

  getBarrios(): Observable<Barrio[]> {
    return this.http.get<Barrio[]>(this.apiUrl);
  }

  deleteBarrio(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  createBarrio(barrio: BarrioForm): Observable<Barrio> {
    return this.http.post<Barrio>(this.apiUrl, barrio);
  }

  updateBarrio(barrio: Barrio): Observable<Barrio> {
    console.log(barrio)
    return this.http.put<Barrio>(`${this.apiUrl}/${barrio.id}`, barrio);
  }

  getBarrio(id: number): Observable<Barrio> {
    return this.http.get<Barrio>(`${this.apiUrl}/${id}`);
  }
}