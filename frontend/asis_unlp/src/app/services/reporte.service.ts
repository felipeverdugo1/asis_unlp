import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { BehaviorSubject, Observable } from "rxjs";
import { tap } from "rxjs/operators";

@Injectable({ providedIn: 'root' })
export class ReporteService {
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
    //////////////// 
    //////////////
    //  TODO    ENDPOINT PARA OBTENER LOS DATOS PARA EL REPORTE
    ///////////////
    ////////////////
    console.log('Generando reporte con filtro:', filtro);
    return this.http.post('/api/reportes', filtro).pipe(
      tap(data => this.reporteData.next(data))
    );
  }

  getReporteData() {
    return this.reporteData.asObservable();
  }
}