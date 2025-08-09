import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable, tap } from 'rxjs';
import { Encuesta } from '../models/encuesta.model'

@Injectable({ providedIn: 'root' })
export class EncuestaService {
  private apiUrl = environment.apiUrl+"encuesta";

  constructor(private http: HttpClient) { }

  enviarDatos(formData: FormData) {
    return this.http.post(`${this.apiUrl}/importar-encuestas`, formData, {
      headers: { 'enctype': 'multipart/form-data' }
    }).pipe(
        tap({
            next: response => console.log('Datos enviados correctamente', response),
            error: err => {
                console.error('Error al enviar los datos', err);
                console.error('Detalles del error:', err.error);
                console.error('Estado del error:', err.status);
            }
        })
    );
  }

  deleteEncuesta(id: number): Observable<void> {
      return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getEncuestas(): Observable<Encuesta[]> {
      return this.http.get<Encuesta[]>(this.apiUrl);
  }
}