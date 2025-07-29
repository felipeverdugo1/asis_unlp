import { Component, inject } from "@angular/core";
import { CommonModule } from "@angular/common";
import { Router } from "@angular/router";
import { Input } from "@angular/core";
import { ChangeDetectionStrategy } from "@angular/core";


@Component({
  standalone: true,
  imports: [CommonModule],
  selector: 'reporte-resultado',
  inputs: ['data'],
  template: `
    <div *ngIf="data">
      <!-- Aca van los graficos o texto que consuman los datos del backend -->
      <pre>{{ data | json }}</pre>
      
      <button (click)="volverAFiltro()">Ajustar Filtros</button>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ReporteResultadoComponent {
  @Input() data: any;
  
  private router = inject(Router);

  volverAFiltro() {
    this.router.navigate(['/filtro']);
  }
}