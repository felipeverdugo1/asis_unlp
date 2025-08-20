import { Injectable, inject } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { BehaviorSubject, Observable } from "rxjs";
import { tap } from "rxjs/operators";
import { Reporte } from "../models/reporte.model";
import { environment } from "../../environments/environment";
import { EncuestaService } from "./encuesta.service";
import { log } from "console";

@Injectable({ providedIn: 'root' })
export class ReporteService {
  private apiUrl = environment.apiUrl+"reporte";

  private encuestaService = inject(EncuestaService);

  private filtroActual = new BehaviorSubject<any>(null);
  private reporteData = new BehaviorSubject<any>(null);
  private encuestasFiltradasData = new BehaviorSubject<any>(null);
  private cantidadEncuestadasData = new BehaviorSubject<any>(null);
  private totalEdadesData = new BehaviorSubject<any>(null);
  private totalPersonasData = new BehaviorSubject<any>(null);
  private totalGenerosData = new BehaviorSubject<any>(null);
  private totalMaterialesData = new BehaviorSubject<any>(null);

  constructor(private http: HttpClient) {}

  setFiltroActual(filtro: any) {
    console.log('Filtro actual establecido:', filtro);
    this.filtroActual.next(filtro);
  }

  getFiltroActual() {
    return this.filtroActual.value;
  }
  
  generarReporte(filtro: any): Observable<any> {
    console.log('Generando reporte con filtro:', filtro);

    return this.encuestaService.obtenerDatos(filtro).pipe(
      tap((resp) => {
        console.log('Datos obtenidos para reporte:', resp);
        this.encuestasFiltradasData.next(resp.encuestasFiltradas)
        this.cantidadEncuestadasData.next(resp.cantEncuestadas)
        this.totalPersonasData.next(resp.total_personas)
        this.totalEdadesData.next(resp.total_edades)
        this.totalGenerosData.next(resp.total_generos)
        this.totalMaterialesData.next(resp.total_materiales)
        this.reporteData.next(resp); 
      })
    );
  }

  getReporteData() {
    return this.reporteData.asObservable();
  }

  getEncuestasFiltradasData(){
    return this.encuestasFiltradasData.asObservable();
  }

  getCantidadEncuestadasData(){
    return this.cantidadEncuestadasData.asObservable();
  }

  getTotalEdadesData(){
    return this.totalEdadesData.asObservable();
  }

  getTotalPersonasData(){
    return this.totalPersonasData.asObservable();
  }

  getTotalGenerosData(){
    return this.totalGenerosData.asObservable();
  }

  getTotalMaterialesData(){
    return this.totalMaterialesData.asObservable();
  }

  getReportes(): Observable<Reporte[]> {
    return this.http.get<Reporte[]>(`${this.apiUrl}`);
  }

  getReportesByUserId(userId: number): Observable<Reporte[]> {
    return this.http.get<Reporte[]>(`${this.apiUrl}/creadoPor/${userId}`);
  }

  getReportesCompartidosById(userId: number): Observable<Reporte[]> {
    return this.http.get<Reporte[]>(`${this.apiUrl}/compartidoCon/${userId}`);
  }

  compartir(id: number, usuarioId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/agregarUsuarioCompartido/${id}/${usuarioId}`, {});
  }

  persistirPDF(formData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}/guardarPDFenDisco`, formData, {
      reportProgress: true,
      responseType: 'json'
    });
  }

  deleteReporte(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  descargarPDF(reporteId: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/descargarPDF/${reporteId}`, {
      responseType: 'blob'
    });
  }
}