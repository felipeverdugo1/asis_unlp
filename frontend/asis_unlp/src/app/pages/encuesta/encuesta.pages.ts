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
      <!----PDF PRUEBA----->
      <!----PDF PRUEBA----->
      <button (click)="generarYGuardarPDF()">Generar PDF</button>
      <!----PDF PRUEBA----->
      <!----PDF PRUEBA----->
    </div>

    <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>

    <!----PDF PRUEBA ---------------------VVVVVVVV------------>
    <div class="content-container" #contenidoParaPDF>
    <listar-encuestas 
      [encuestas]="(encuestas$ | async) ?? []"
      (onDelete)="borrarEncuesta($event)">
    </listar-encuestas >
    </div>
  </div>
  `
})
export class ListarEncuestaPage implements OnInit {
  //////////////////////
  ////////// PDF prueba
  @ViewChild('contenidoParaPDF', { static: false }) contenidoParaPDF!: ElementRef;
  /////////////////////
  /////////// PDF prueba
  encuestas$!: Observable<Encuesta[]>;
  errorMensaje: string | null = null;

  constructor(
    private encuestaService: EncuestaService,
    private router: Router,
    /////////////////////
    /////// PDF prueba
    private pdfService: PdfService,
    private reporteService: ReporteService,
    private authService: AuthService
    //////// PDF prueba
    /////////////////////
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


  //////////////////////
  /////////////// pDF PRUEBA
  async generarYGuardarPDF() {
    try {
      const user_id = this.authService.getUsuarioId();
      const now = new Date();
      const fechaHora = formatDate(now, 'yyyy-MM-dd_HH-mm-ss', 'en-US');
      const fileName = `reporte_${user_id}_${fechaHora}.pdf`;
      const { pdfBlob, pdfBase64 } = await this.pdfService.generarPDF(
        this.contenidoParaPDF.nativeElement,
        fileName
      );

      // Descargar automáticamente
      const url = window.URL.createObjectURL(pdfBlob);
      const a = document.createElement('a');
      a.href = url;
      a.download = fileName;
      a.click();
      window.URL.revokeObjectURL(url);

      const formData = new FormData();
      formData.append('file', pdfBlob, fileName);

      // Guardar en el servidor (si es necesario)
      this.reporteService.persistirPDF(fileName, pdfBase64).subscribe({
        next: (response) => console.log('PDF guardado en servidor', response),
        error: err => console.error('Error guardando PDF', err)
      });

    } catch (error) {
      console.error('Error generando PDF:', error);
    }
  }
  //////////////////////
}
