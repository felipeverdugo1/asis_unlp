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
import { Observable } from 'rxjs';
import { CompartirConInputReporteComponent, ListarReportes } from "../../components/reporte/listar-reporte";
import { UsuariosService } from "../../services/usuarios.service";



@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, ListarReportes,CompartirConInputReporteComponent],
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
      [reportes]="(reportes$ | async) ?? []"
      [usuarios_compartidos]="usuarios_compartidos"
      [showCompleteDialog]="showCompleteDialog"
      (onDelete)="borrarReporte($event)"
      (share)="openCompleteDialog($event)" 
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
      (onDelete)="borrarReporte($event)"
      (onDownload)="descargarReporte($event)">
    </listar-reportes>
    </div>
    </ng-container>



    @if (showCompleteDialog) {
      <compartir-input-dialog-reporte
        [usuarios_compartidos]="usuarios_compartidos"
        (saved)="onShare($event)" 
        (closed)="closeDialog()">
      </compartir-input-dialog-reporte>
    }

  </div>
  `
})
export class ListarReportePage implements OnInit {
  reportes$: Observable<any[]> = new Observable();
  usuarios_compartidos : any [] = [];
  id: number | null = null;

  errorMensaje: string | null = null;
  downloading = false;

  // Estado para controlar el modal
  showCompleteDialog = false;
  selectedReporteId: number | null = null;

  constructor(
    private reporteService: ReporteService,
    private usuarioService : UsuariosService,
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
      this.reportes$ = this.reporteService.getReportesByUserId(this.id);
      
    }else if (this.auth.rolReferente()) {
      this.reportes$ = this.reporteService.getReportesCompartidosById(this.id);
    }
    else if (this.authService.rolAdmin()) {
      this.reportes$ = this.reporteService.getReportes();
    }


  }


  loadCompartidos() {
    if (!this.id) {
      this.router.navigate(['/login']);
      return;
    }
    this.usuarioService.getUsuarios().subscribe({
      next: (usuario) => this.usuarios_compartidos = usuario,
      error: (err) => console.error('Error cargando usuarios compartidos', err)
    });
  }


  openCompleteDialog(reporteId: number) {
    this.selectedReporteId = reporteId;
  
    this.usuarioService.getUsuarios().subscribe({
      next: (usuarios) => {
        this.usuarios_compartidos = usuarios;
        this.showCompleteDialog = true; 
      },
      error: (err) => console.error('Error cargando usuarios', err)
    });
  }

  onShare(data: { compartidoConId: number }) {
    if (!this.selectedReporteId) return;
  
    this.reporteService.compartir(this.selectedReporteId, data.compartidoConId)
      .subscribe({
        next: () => {
          this.closeDialog();
          this.cargarReportes(); 
        },
        error: (err) => console.error('Error compartiendo reporte', err)
      });
  }


  closeDialog() {
    this.showCompleteDialog = false;
    this.selectedReporteId = null;
  }


  borrarReporte(id: number) {
    if (confirm('¿Borrar reporte?')) {
      this.reporteService.deleteReporte(id).subscribe({
        next: () => this.cargarReportes(),
        error: (err) => {
          this.errorMensaje = err.error?.error || 'Error inesperado al borrar el encuestador.';
          alert(this.errorMensaje);
        }
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
        this.errorMensaje = err.error?.error || 'Error inesperado al descargar el PDF. El archivo podria no existir.';
        alert(this.errorMensaje);
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

      <div class="pdf-container" #contenidoParaPDF>

        <!-- Totales generales -->
        <div class="pdf-header-content">
          <h2>ASIS UNLP - {{ fechaHora }} </h2>
          <p><strong>Total personas filtradas:</strong> {{ totalPersonasData$ | async }}</p>
          <p><strong>Total personas encuestadas:</strong> {{ totalEncuestadosData$ | async }}</p>
        </div>   

        <div class="reporte-grid-container">

          <div class="reporte-grid-item">
            <app-filtro-card 
              [filtro]="filtroActual" 
              [mostrarBotones]="false">
            </app-filtro-card>
          </div>  

          <ng-container *ngIf="reporteData$ | async as data">
            <div class="reporte-grid-item">
              <reporte-resultado [data]="data"></reporte-resultado>       
            </div>
          </ng-container>

          <!-- Gráfico Edades -->
          <ng-container *ngIf="totalEdadesData$ | async as edades">
            <div class="reporte-grid-item">
              <grafico-torta
                *ngIf="objectToChartData(edades).labels.length > 0"
                [title]="'Distribución por edades'"
                [labels]="objectToChartData(edades).labels"
                [values]="objectToChartData(edades).values"
                [doughnut]="true">
              </grafico-torta>
            </div>
          </ng-container>

          <!-- Gráfico Géneros -->
          <ng-container *ngIf="totalGenerosData$ | async as generos">
            <div class="reporte-grid-item">
              <grafico-torta
                *ngIf="objectToChartData(generos).labels.length > 0"
                [title]="'Distribución por género'"
                [labels]="objectToChartData(generos).labels"
                [values]="objectToChartData(generos).values"
                [doughnut]="true">
              </grafico-torta>
            </div>
          </ng-container>

          <!-- Gráfico Materiales -->
          <ng-container *ngIf="totalMaterialesData$ | async as materiales">
            <div class="reporte-grid-item">
              <grafico-torta
                *ngIf="objectToChartData(materiales).labels.length > 0"
                [title]="'Distribución por materiales'"
                [labels]="objectToChartData(materiales).labels"
                [values]="objectToChartData(materiales).values"
                [doughnut]="true">
              </grafico-torta>
            </div>
          </ng-container>
        </div>
      </div>
    </div>
  `,
  styleUrls: ['../../../styles.css'],
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
  fechaHora: string | undefined;

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
    
    const now = new Date();
    this.fechaHora = formatDate(now, 'yyyy-MM-dd_HH-mm-ss', 'en-US');

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

      const fileName = `reporte_${user_id}_${this.fechaHora}.pdf`;
      
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