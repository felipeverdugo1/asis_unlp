import { Component, ElementRef, inject, ViewChild, ChangeDetectorRef } from "@angular/core";
import { CommonModule, formatDate } from "@angular/common";
import { AsyncPipe } from "@angular/common";
import { OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { ReporteService } from "../../services/reporte.service";
import { ReporteResultadoComponent } from "../../components/reporte/reporte-resultado";
import { PdfService } from "../../services/pdf.service";
import { AuthService } from "../../services/auth.service";
import { EncuestaService } from "../../services/encuesta.service";

/*Hay que aplicarle estilos a esto.*/
@Component({
  standalone: true,
  imports: [CommonModule, ReporteResultadoComponent, AsyncPipe],
  template: `
    <div class="page-container">
      <div class="page-header">
        <h1>Reporte Generado</h1>
        <button class="btn btn-create" (click)="generarYGuardarPDF()" [disabled]="generandoPDF">
            {{ generandoPDF ? 'Generando...' : 'Guardar Reporte' }}
        </button>
      </div>
    </div>

    <div class="content-container" #contenidoParaPDF>
      <h2>MAPAS EXQUISITOS y GRAFICOS COPADOS</h2>
      <ng-container *ngIf="reporteData$ | async as data">
        <reporte-resultado [data]="data"></reporte-resultado>
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
  reporteData$ = this.reporteService.getReporteData();

  constructor(private cdRef: ChangeDetectorRef) {}


  ngOnInit() {
    const filtro = this.reporteService.getFiltroActual();
    
    if (!filtro) {
      this.router.navigate(['/filtro']);
      return;
    }

    this.reporteService.generarReporte(filtro).subscribe();
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
}