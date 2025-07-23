import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ReporteService } from '../../services/reporte.service';
import { FiltroReporteComponent } from '../../components/filtro/form-filtro';

@Component({
  standalone: true,
  imports: [FiltroReporteComponent],
  template: `
      <app-filtro-reporte 
        (generarReporte)="onGenerarReporte($event)">
      </app-filtro-reporte>
  `
})
export class FiltroPage {
  constructor(
    private router: Router,
    private reporteService: ReporteService
  ) {}

  onGenerarReporte(filtro: any) {
    this.reporteService.setFiltroActual(filtro);
    this.router.navigate(['/filtro/resultado']);
  }
}