// filtro-view.component.ts
import { Component, Input } from '@angular/core';
import { Filtro } from '../../models/filtro.model';
import { Router } from '@angular/router';
import { ReporteService } from '../../services/reporte.service';
/*
@Component({
  selector: 'filtro-card-text',
  standalone: true,
  templateUrl: './filtro-card.html',
  styleUrls: ['../../../styles.css']
})
export class FiltroCardTextComponent {
  @Input() filtro!: Filtro;

  constructor(private router: Router, private reporteService: ReporteService) {}

  get criterios(): any {
    return JSON.parse(this.filtro.criterios);
  }

  aplicarFiltro(filtro_id: number) {
    this.reporteService.setFiltroActual(this.criterios);
    this.router.navigate(['/filtro/resultado']);
  }
}*/