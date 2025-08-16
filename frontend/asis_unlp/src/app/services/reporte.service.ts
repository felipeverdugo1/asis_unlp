import { Injectable, inject } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { BehaviorSubject, Observable } from "rxjs";
import { tap } from "rxjs/operators";
import { Reporte } from "../models/reporte.model";
import { environment } from "../../environments/environment";
import { EncuestaService } from "./encuesta.service";

@Injectable({ providedIn: 'root' })
export class ReporteService {
  private apiUrl = environment.apiUrl+"reporte";

  private encuestaService = inject(EncuestaService);

  private filtroActual = new BehaviorSubject<any>(null);
  private reporteData = new BehaviorSubject<any>(null);

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
        this.reporteData.next(resp);
      })
    );
  }

  getReporteData() {
    return this.reporteData.asObservable();
  }

  getReportes(): Observable<Reporte[]> {
    return this.http.get<Reporte[]>(`${this.apiUrl}`);
  }

  getReportesByUserId(userId: number): Observable<Reporte[]> {
    return this.http.get<Reporte[]>(`${this.apiUrl}/creadoPor/${userId}`);
  }

  persistirPDF(formData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}/guardarPDFenDisco`, formData, {
      reportProgress: true,
      responseType: 'json'
    });
  }
}