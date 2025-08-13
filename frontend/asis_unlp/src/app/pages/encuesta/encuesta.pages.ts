import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { CommonModule, formatDate } from '@angular/common';
import { CargaCsvComponent } from '../../components/encuesta/encuesta.component';
import { Router, RouterModule } from '@angular/router';
import { EncuestaService } from '../../services/encuesta.service';
import { ListarEncuestas } from '../../components/encuesta/list-encuestas.component';
import { Observable } from 'rxjs';
import { Encuesta } from '../../models/encuesta.model';
import { PdfService } from '../../services/pdf.service';
import { ReporteService } from '../../services/reporte.service';
import { AuthService } from '../../services/auth.service';

@Component({
  standalone: true,
  imports: [CommonModule, CargaCsvComponent, RouterModule],
  template: `
    <div class="page-container">
      <div class="page-header">
        <h1 class="page-title">Carga de Datos CSV</h1>
      </div>

      <div *ngIf="errorMensaje" class="error-box">
        {{ errorMensaje }}
      </div>

      <app-carga-csv
        (datosCargados)="onDatosCargados($event)">
      </app-carga-csv>
    </div>
  `,
  styleUrls: ['../../../styles.css']
})
export class CargaCsvPage { 
    errorMensaje: string | null = null;

    constructor(private router: Router, private cargaCsvService: EncuestaService) {
        this.errorMensaje = null;
    }

    onDatosCargados(datos: any) {
        this.cargaCsvService.enviarDatos(datos).subscribe({
            next: (response: any) => {
                alert('Datos cargados exitosamente');
                this.router.navigate(['/encuestas']);
            },
            error: (err) => this.errorMensaje = err.error?.error || 'Error inesperado al cargar los archivos.'            
        });
    }
}


@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, ListarEncuestas],
  template: `
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">Encuestas</h1>
      <button (click)="importarEncuestas()" class="btn btn-create">Cargar Encuestas</button>
    </div>

    <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>

    <div class="content-container">
    <listar-encuestas 
      [encuestas]="(encuestas$ | async) ?? []"
      (onDelete)="borrarEncuesta($event)">
    </listar-encuestas >
    </div>
  </div>
  `
})
export class ListarEncuestaPage implements OnInit {
  encuestas$!: Observable<Encuesta[]>;
  errorMensaje: string | null = null;

  constructor(
    private encuestaService: EncuestaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarEncuesta();
    this.errorMensaje = null;
  }

  cargarEncuesta() {
    this.encuestas$ = this.encuestaService.getEncuestas();
    console.log(this.encuestas$);
  }

  importarEncuestas() {
    this.router.navigate(['/encuestas/cargar-csv']);
  }

  borrarEncuesta(id: number) {
    if (confirm('¿Borrar encuesta?')) {
      this.encuestaService.deleteEncuesta(id).subscribe({
        next: () => this.cargarEncuesta(),
        error: (err) => this.errorMensaje = err.error?.error || 'Error inesperado al borrar la encuesta.'
      });
    }
  }

}
