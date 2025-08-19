
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { ReporteService } from '../../services/reporte.service';
import { Filtro } from '../../models/filtro.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-filtro-card',
  standalone: true,
  imports: [ CommonModule],
  templateUrl: './filtro-card.html',
  styleUrls: ['../../../styles.css']
})
export class FiltroCardComponent {
  @Input() filtro!: Filtro;
  @Input() mostrarBotones: boolean = true;
  @Output() eliminar = new EventEmitter<number>();

  constructor(
    private router: Router, 
    private reporteService: ReporteService
  ) {}

  get criterios(): any {
    try {
      return JSON.parse(this.filtro.criterios);
    } catch {
      return {};
    }
  }

  aplicarFiltro() {
    this.reporteService.setFiltroActual(this.criterios);
    this.router.navigate(['/filtro/resultado']);
  }

  eliminarFiltro() {
    this.eliminar.emit(this.filtro.id);
  }
}