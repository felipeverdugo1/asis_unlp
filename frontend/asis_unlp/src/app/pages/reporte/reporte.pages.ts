import { Component, inject } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AsyncPipe } from "@angular/common";
import { OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { ReporteService } from "../../services/reporte.service";
import { ReporteResultadoComponent } from "../../components/reporte/reporte-resultado";

@Component({
  standalone: true,
  imports: [CommonModule, ReporteResultadoComponent, AsyncPipe],
  template: `
    <h1>Reporte Generado</h1>
    
    <ng-container *ngIf="reporteData$ | async as data">
      <reporte-resultado [data]="data"></reporte-resultado>
    </ng-container>
  `
})
export class ReportePage implements OnInit {
  private reporteService = inject(ReporteService);
  private router = inject(Router);

  reporteData$ = this.reporteService.getReporteData();

  ngOnInit() {
    const filtro = this.reporteService.getFiltroActual();
    
    if (!filtro) {
      this.router.navigate(['/reporte/filtro']);
      return;
    }

    this.reporteService.generarReporte(filtro).subscribe();
  }
}