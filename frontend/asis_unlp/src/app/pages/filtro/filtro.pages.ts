import { Component, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { ReporteService } from '../../services/reporte.service';
import { FiltroReporteComponent } from '../../components/filtro/form-filtro';
import { FiltroService } from '../../services/filtro.service';
import { AuthService } from '../../services/auth.service';
import { Filtro } from '../../models/filtro.model';
import { FilenameInputDialogComponent } from '../../components/filtro/filename-input-dialog.component';

@Component({
  standalone: true,
  imports: [FiltroReporteComponent, FilenameInputDialogComponent],
  template: `
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">Generación de Reporte</h1>
      <button class="btn btn-nav" (click)="onLimpiarFiltro()">Limpiar Filtro</button>
    </div>
    
    <app-filtro-reporte
      (generarReporte)="onGenerarReporte($event)"
      #filtroRef
    ></app-filtro-reporte>
  </div>
  `
})
export class FiltroPage {
  constructor(
    private router: Router,
    private reporteService: ReporteService
  ) {}

  @ViewChild('filtroRef') filtroComp!: FiltroReporteComponent;

  onGenerarReporte(filtro: any) {
    this.reporteService.setFiltroActual(filtro);
    this.router.navigate(['/filtro/resultado']);
  }

  onLimpiarFiltro() {
    this.reporteService.setFiltroActual(null);
    this.filtroComp.resetForm();  
  }
}