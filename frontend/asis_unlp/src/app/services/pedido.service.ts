import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class PedidoService {
  private apiUrl = environment.apiUrl+"pedidoReporte";
  constructor(private http: HttpClient) {}

  getPedidos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}`);
  }

  tomarPedido(pedidoId: number, usuarioId: number): Observable<any> {
    return this.http.patch(`${this.apiUrl}/${pedidoId}/tomar`, { usuarioId });
  }

  completarPedido(pedidoId: number, reporteId: number): Observable<any> {
    // CAMBIAR
    return this.http.patch(`${this.apiUrl}/${pedidoId}/completar`, { reporteId });
  }

  descargarReporte(pedidoId: number): void {
    window.open(`${this.apiUrl}/${pedidoId}/descargar`, '_blank');
  }

  getPedidosPendientes(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/estado/pendiente`);
  }

  getPedidosReferente(usuarioId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/referente/${usuarioId}`);
  }
}