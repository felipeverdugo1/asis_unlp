import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { PedidoReporteCompletado } from '../models/pedido.model';

@Injectable({ providedIn: 'root' })
export class PedidoService {
  private apiUrl = environment.apiUrl+"pedidoReporte";
  constructor(private http: HttpClient) {}
  pedido: PedidoReporteCompletado = {
    asignado_a_id: 0,
    reporte_id: 0,
    comentario: ''
  };

  getPedidos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}`);
  }

  tomarPedido(pedidoId: number, usuarioId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${pedidoId}/tomar/${usuarioId}`, {});
  }

  soltarPedido(pedidoId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${pedidoId}/soltar`, {});
  }

  crearPedido(pedido: any): Observable<any> {
    return this.http.post(`${this.apiUrl}`, pedido);
  }

  completarPedido(pedidoId: number, reporteId: number, comentario?: string): Observable<any> {
    this.pedido.reporte_id = reporteId;
    this.pedido.asignado_a_id = pedidoId;
    this.pedido.comentario = comentario || '';
    return this.http.put(`${this.apiUrl}/${pedidoId}/completar`, { ...this.pedido });
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

  getPedidosTomadosPorUsuario(usuarioId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/tomados/${usuarioId}`);
  }

  eliminarPedido(pedidoId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${pedidoId}`);
  }
}