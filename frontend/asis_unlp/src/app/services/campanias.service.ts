import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Campania } from '../models/campania.model';
import { environment } from '../../environments/environment';
import { BarriosService } from './barrios.service';



@Injectable({ providedIn: 'root' })
export class CampaniaService {
  private apiUrl = environment.apiUrl+"campania";

  constructor(private http: HttpClient) {}

  getCampanias(): Observable<Campania[]> {
    return this.http.get<Campania[]>(this.apiUrl);
  }

  getCampania(id: number): Observable<Campania> {
    return this.http.get<Campania>(`${this.apiUrl}/${id}` ) ;
  }

  deleteCampania(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updateCampania(campaña: Campania): Observable<Campania> {
    return this.http.put<Campania>(`${this.apiUrl}/${campaña.id}`,campaña);
  }
  
  createCampania(campaña: Campania): Observable<Campania> {
    return this.http.post<Campania>(this.apiUrl, campaña);
  }

  getCampaniasByBarrio(barrioId: number): Observable<Campania[]> {
    return this.http.get<Campania[]>(`${this.apiUrl}/barrio/${barrioId}`);
  }

}