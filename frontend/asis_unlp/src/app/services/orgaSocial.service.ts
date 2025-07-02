import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrgaSocial } from '../models/orgaSocial.model';
import { environment } from '../../environments/environment';



@Injectable({ providedIn: 'root' })
export class OrgaSocialService {
  private apiUrl = environment.apiUrl+"organizacionSocial";

  constructor(private http: HttpClient) {}

  getOrgaSociales(): Observable<OrgaSocial[]> {
    return this.http.get<OrgaSocial[]>(this.apiUrl);
  }

  getOrgaSocial(id: number): Observable<OrgaSocial> {
    return this.http.get<OrgaSocial>(`${this.apiUrl}/${id}` ) ;
  }

  deleteOrgaSocial(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }



  updateOrgaSocial(orgaSocial: OrgaSocial): Observable<OrgaSocial> {
    return this.http.put<OrgaSocial>(`${this.apiUrl}/${orgaSocial.id}`,orgaSocial);
  }
  

  createOrgaSocial(orgaSocial: OrgaSocial): Observable<OrgaSocial> {
    return this.http.post<OrgaSocial>(this.apiUrl, orgaSocial);
  }



}