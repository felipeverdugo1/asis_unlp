import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Rol} from '../models/rol.model';
import { environment } from '../../environments/environment';
import { log } from 'console';



@Injectable({ providedIn: 'root' })
export class RolesService {
  private apiUrl = environment.apiUrl+"rol";

  constructor(private http: HttpClient) {}

  getRoles(): Observable<Rol[]> {
    return this.http.get<Rol[]>(this.apiUrl);
  }

  deleteRol(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  crearRol(rol: Rol): Observable<Rol>{
    return this.http.post<Rol>(this.apiUrl, rol);
  }
}