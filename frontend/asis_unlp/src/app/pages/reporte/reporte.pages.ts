import { Component, ElementRef, inject, ViewChild, ChangeDetectorRef } from "@angular/core";
import { CommonModule, formatDate } from "@angular/common";
import { AsyncPipe } from "@angular/common";
import { OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { ReporteService } from "../../services/reporte.service";
import { ReporteResultadoComponent } from "../../components/reporte/reporte-resultado";
import { GraficoTortaComponent } from "../../components/reporte/grafico-torta";
import { PdfService } from "../../services/pdf.service";
import { AuthService } from "../../services/auth.service";
import { FiltroCardComponent } from "../../components/filtro/filtro-card";
import { Filtro } from '../../models/filtro.model';

import { RouterModule } from "@angular/router";
import { Reporte } from "../../models/reporte.model";
import { Observable } from 'rxjs';
import { ListarReportes } from "../../components/reporte/listar-reporte";
import { log } from "console";
/*Hay que aplicarle estilos a esto.*/


@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, ListarReportes],
  template: `
  <div class="page-container">
    <div class="page-header">
        <ng-container *ngIf="auth.rolReferente()">
          <h1 class="page-title">Mis Reporte</h1>
        </ng-container>

        <ng-container *ngIf="auth.rolAdmin()">
          <h1 class="page-title">Todos los Reportes</h1>
        </ng-container>

        <ng-container *ngIf="auth.rolSalud()">
          <h1 class="page-title">Mis Reportes Generados</h1>
        </ng-container>
      </div>

      <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>

   

  <ng-container *ngIf="auth.rolSalud()">
    <div class="content-container">
    <listar-reportes 
      [reportes]="(reportesTomados$ | async) ?? []"
      [downloading]="downloading"
      (onEdit)="editarReporte($event)"
      (onDelete)="borrarReporte($event)"
      (onDownload)="descargarReporte($event)">
    </listar-reportes>
    </div>
    </ng-container>


      <ng-container *ngIf="auth.rolReferente()">
    <div class="content-container">
    <listar-reportes 
      [reportes]="(reportes$ | async) ?? []"
      [downloading]="downloading"
      (onDelete)="borrarReporte($event)"
      (onDownload)="descargarReporte($event)">
    </listar-reportes>
    </div>
    </ng-container>





      <ng-container *ngIf="auth.rolAdmin()">
    <div class="content-container">
    <listar-reportes 
      [reportes]="(reportes$ | async) ?? []"
      [downloading]="downloading"
      (onEdit)="editarReporte($event)"
      (onDelete)="borrarReporte($event)"
      (onDownload)="descargarReporte($event)">

    </listar-reportes>
    </div>
    </ng-container>




  </div>
  `
})
export class ListarReportePage implements OnInit {
  reportes$: Observable<any[]> = new Observable();
  reportesTomados$ : Observable<any[]> = new Observable();
  usuarios_compartidos : any [] = [];
  id: number | null = null;

  errorMensaje: string | null = null;
  downloading = false;

  constructor(
    private reporteService: ReporteService,
    private router: Router,
    private authService: AuthService,
    private cdRef: ChangeDetectorRef

  ) {}

  auth = inject(AuthService);

  ngOnInit(): void {
    this.cargarReportes();
    this.errorMensaje = null;
  }

  cargarReportes() {
    console.log(this.authService.rolSalud());
    console.log(this.authService.rolAdmin());
    console.log(this.authService.rolReferente());
    
    if (!this.authService.loggedIn()) {
      this.router.navigate(['/login']);
      return;
    }
    this.id = this.authService.getUsuarioId();
    if (!this.id) {
      this.errorMensaje = 'Usuario no encontrado';
      return;
    }

    if (this.authService.rolSalud()) {
      this.reportesTomados$ = this.reporteService.getReportesByUserId(this.id);
      
    }
    else if (this.authService.rolAdmin()) {
      this.reportes$ = this.reporteService.getReportes();
    }


  }

  editarReporte(id: number) {
  }

  borrarReporte(id: number) {
    if (confirm('¿Borrar reporte?')) {
      this.reporteService.deleteReporte(id).subscribe({
        next: () => this.cargarReportes(),
        error: (err) => this.errorMensaje = err.error?.error || 'Error inesperado al borrar el encuestador.'
      });
    }
  }

  descargarReporte(reporteId: number) {
    this.downloading = true;
    this.cdRef.detectChanges(); 
    this.reporteService.descargarPDF(reporteId).subscribe({
      next: (blob: Blob) => {
        // Crear nombre de archivo con la fecha actual
        const fecha = new Date().toISOString().slice(0, 10);
        const nombreArchivo = `reporte_${reporteId}_${fecha}.pdf`;

        // Descargar el archivo
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = nombreArchivo;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
        this.downloading = false;
        this.cdRef.detectChanges(); 
      },
      error: (err: any) => {
        console.error('Error descargando PDF:', err);
        this.downloading = false;
        this.cdRef.detectChanges(); 
      }
    });
  }
}





@Component({
  standalone: true,
  imports: [CommonModule, ReporteResultadoComponent, AsyncPipe, FiltroCardComponent, GraficoTortaComponent],
  template: `
    <div class="page-container">
      <div class="page-header">
        <h1>Reporte Generado</h1>
        <button class="btn btn-create" (click)="generarYGuardarPDF()" [disabled]="generandoPDF">
            {{ generandoPDF ? 'Generando...' : 'Guardar Reporte' }}
        </button>

        <button class="btn btn-func" (click)="volverAFiltro()">Ajustar Filtros</button>
      </div>
    </div>

    <div class="content-container" #contenidoParaPDF>
      <hr/>
      <app-filtro-card 
        [filtro]="filtroActual" 
        [mostrarBotones]="false">
      </app-filtro-card>
      
       <!-- Totales generales -->
      <div class="totales-container">
        <p><strong>Total personas:</strong> {{ totalPersonasData$ | async }}</p>
        <p><strong>Total encuestados:</strong> {{ totalEncuestadosData$ | async }}</p>
      </div>     

      <ng-container *ngIf="reporteData$ | async as data">
        <reporte-resultado [data]="data"></reporte-resultado>       
      </ng-container>

      <!-- Gráfico Edades -->
      <ng-container *ngIf="totalEdadesData$ | async as edades">
        <grafico-torta
          *ngIf="objectToChartData(edades).labels.length > 0"
          [title]="'Distribución por edades'"
          [labels]="objectToChartData(edades).labels"
          [values]="objectToChartData(edades).values"
          [doughnut]="true">
        </grafico-torta>
      </ng-container>

      <!-- Gráfico Géneros -->
      <ng-container *ngIf="totalGenerosData$ | async as generos">
        <grafico-torta
          *ngIf="objectToChartData(generos).labels.length > 0"
          [title]="'Distribución por género'"
          [labels]="objectToChartData(generos).labels"
          [values]="objectToChartData(generos).values"
          [doughnut]="true">
        </grafico-torta>
      </ng-container>

      <!-- Gráfico Materiales -->
      <ng-container *ngIf="totalMaterialesData$ | async as materiales">
        <grafico-torta
          *ngIf="objectToChartData(materiales).labels.length > 0"
          [title]="'Distribución por materiales'"
          [labels]="objectToChartData(materiales).labels"
          [values]="objectToChartData(materiales).values"
          [doughnut]="true">
        </grafico-torta>
      </ng-container>

    </div>
  `
})
export class ReportePage implements OnInit {
  @ViewChild('contenidoParaPDF', { static: false }) contenidoParaPDF!: ElementRef;

  private reporteService = inject(ReporteService);
  private router = inject(Router);
  private pdfService = inject(PdfService);
  private authService = inject(AuthService);
  generandoPDF: boolean = false;
  reporteData$ = this.reporteService.getEncuestasFiltradasData();
  totalPersonasData$ = this.reporteService.getTotalPersonasData();
  totalEncuestadosData$ = this.reporteService.getCantidadEncuestadasData();
  totalEdadesData$ = this.reporteService.getTotalEdadesData();
  totalGenerosData$ = this.reporteService.getTotalGenerosData();
  totalMaterialesData$ = this.reporteService.getTotalMaterialesData();
  filtroActual!: Filtro;

  constructor(private cdRef: ChangeDetectorRef) {}


  ngOnInit() {
    const filtro = this.reporteService.getFiltroActual();

    if (!filtro) {
      this.router.navigate(['/filtro']);
      return;
    }

    this.filtroActual = {
      id: 0,
      nombre: 'Reporte con los siguientes filtros:',
      criterios: JSON.stringify(filtro),
      propietario_id: this.authService.getUsuarioId() || 0
    };

    this.reporteService.generarReporte(filtro).subscribe();
  }

  objectToChartData(obj: Record<string, number>): { labels: string[], values: number[] } {
    if (!obj || Object.keys(obj).length === 0) {
      return { labels: [], values: [] };
    }
    const labels = Object.keys(obj);
    const values = Object.values(obj);
    return { labels, values };
  }

  async generarYGuardarPDF() {
    this.cdRef.detectChanges(); 
    this.generandoPDF = true;
    try {
      const user_id = this.authService.getUsuarioId();
      if (!user_id) {
        throw new Error('Usuario no autenticado');
      }

      const now = new Date();
      const fechaHora = formatDate(now, 'yyyy-MM-dd_HH-mm-ss', 'en-US');
      const fileName = `reporte_${user_id}_${fechaHora}.pdf`;
      
      // Generar el PDF (devuelve directamente el Blob)
      const { pdfBlob, fileName: safeFileName } = await this.pdfService.generarPDF(
        this.contenidoParaPDF.nativeElement,
        fileName
      );
    
      // 1. Descargar automáticamente en el cliente
      const url = window.URL.createObjectURL(pdfBlob);
      const a = document.createElement('a');
      a.href = url;
      a.download = safeFileName;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);
    
      // 2. Enviar al servidor
      const formData = new FormData();
      formData.append('file', pdfBlob);
      formData.append('fileName', safeFileName);
      formData.append('usuario_id', user_id.toString());
      formData.append('campania_id', "1");
      formData.append('descripcion', "Descripción del reporte");

      this.reporteService.persistirPDF(formData).subscribe({
        next: (response) => {
          this.generandoPDF = false;
          this.cdRef.detectChanges(); 
          console.log('PDF guardado en servidor', response);
        },
        error: err => {
          this.generandoPDF = false;
          this.cdRef.detectChanges(); 
          console.error('Error guardando PDF', err);
        },
        complete: () => {
          this.generandoPDF = false;
          this.cdRef.detectChanges(); 
        }
      });
    
    } catch (error) {
      console.error('Error generando PDF:', error);
      this.generandoPDF = false;
      // Aquí podrías agregar notificación al usuario
    }
  }

  volverAFiltro() {
    this.router.navigate(['/filtro/nuevo'])
  }
}