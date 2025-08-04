import { Component, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { ReporteService } from '../../services/reporte.service';
import { FiltroReporteComponent } from '../../components/filtro/form-filtro';
import { FiltroService } from '../../services/filtro.service';
import { AuthService } from '../../services/auth.service';
import { Filtro } from '../../models/filtro.model';
import { FilenameInputDialogComponent } from '../../components/filtro/filename-input-dialog.component';
import { CommonModule } from '@angular/common';
import { FiltroCardComponent } from '../../components/filtro/filtro-card';
import { OnInit } from '@angular/core';

@Component({
  standalone: true,
  imports: [FiltroReporteComponent],
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

@Component({
  standalone: true,
  imports: [CommonModule, FiltroCardComponent],
  template:`
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">Mis Filtros Guardados</h1>
    </div>

    <div *ngIf="cargando" class="loading-message">
      Cargando filtros...
    </div>

    <div *ngIf="error" class="error-message">
      {{ error }}
    </div>

    <ng-container *ngIf="!cargando && !error">
      <div *ngIf="filtros.length === 0" class="no-filters">
        No tienes filtros guardados
      </div>

      <div class="menu-grid">
        <app-filtro-card 
          *ngFor="let filtro of filtros"
          [filtro]="filtro"
          (eliminar)="eliminarFiltro($event)">
        </app-filtro-card>
      </div>
    </ng-container>
  </div>
  `,
  styleUrls: ['../../../styles.css']
})
export class FiltrosListPage implements OnInit {
  filtros: Filtro[] = [];
  cargando = true;
  error: string | null = null;

  constructor(
    private filtroService: FiltroService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.cargarFiltros();
  }

  cargarFiltros() {
    this.cargando = true;
    this.error = null;
    
    const usuarioId = this.authService.getUsuarioId();
    if (!usuarioId) {
      this.error = 'Usuario no identificado';
      this.cargando = false;
      return;
    }

    this.filtroService.getFiltrosByUsuario(usuarioId).subscribe({
      next: (filtros) => {
        this.filtros = filtros;
        this.cargando = false;
      },
      error: (err) => {
        this.error = 'Error al cargar los filtros';
        this.cargando = false;
        console.error(err);
      }
    });
  }

  eliminarFiltro(id: number) {
    if (confirm('¿Estás seguro de eliminar este filtro?')) {
      this.filtroService.deleteFiltro(id).subscribe({
        next: () => {
          this.filtros = this.filtros.filter(f => f.id !== id);
        },
        error: (err) => {
          console.error('Error eliminando filtro:', err);
          alert('No se pudo eliminar el filtro');
        }
      });
    }
  }
}