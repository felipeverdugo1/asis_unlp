import { Component, ElementRef, inject, ViewChild } from "@angular/core";
import { CommonModule, formatDate } from "@angular/common";
import { AsyncPipe } from "@angular/common";
import { OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { ReporteService } from "../../services/reporte.service";
import { ReporteResultadoComponent } from "../../components/reporte/reporte-resultado";
import { PdfService } from "../../services/pdf.service";
import { AuthService } from "../../services/auth.service";

/*Hay que aplicarle estilos a esto.*/
@Component({
  standalone: true,
  imports: [CommonModule, ReporteResultadoComponent, AsyncPipe],
  template: `
    <div class="page-container">
      <div class="page-header">
        <h1>Reporte Generado</h1>
        <ng-container *ngIf="generandoPDF">
          <p>Generando PDF, por favor espere...</p>
        </ng-container>
        <ng-container *ngIf="!generandoPDF">
          <button class="btn btn-create" (click)="generarYGuardarPDF()">Guardar Reporte</button>
        </ng-container>
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


  ngOnInit() {
    const filtro = this.reporteService.getFiltroActual();
    
    if (!filtro) {
      this.router.navigate(['/reporte/filtro']);
      return;
    }

    //this.reporteService.generarReporte(filtro).subscribe();
  }

  async generarYGuardarPDF() {
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
          console.log('PDF guardado en servidor', response);
        },
        error: err => console.error('Error guardando PDF', err)
      });

      this.generandoPDF = false;
    
    } catch (error) {
      console.error('Error generando PDF:', error);
      // Aquí podrías agregar notificación al usuario
    }
  }
}