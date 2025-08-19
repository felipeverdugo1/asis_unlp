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
      <ng-container *ngIf="reporteData$ | async as data">
        <reporte-resultado [data]="data"></reporte-resultado>       
      </ng-container>
      <grafico-torta
          [title]="'Ventas por categoría'"
          [labels]="['Electrónica', 'Ropa', 'Hogar', 'Otros']"
          [values]="[120, 90, 60, 30]"
          [doughnut]="true">
        </grafico-torta>
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