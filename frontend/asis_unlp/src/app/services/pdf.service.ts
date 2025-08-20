import { Injectable } from '@angular/core';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';

@Injectable({
  providedIn: 'root'
})
export class PdfService {
    async generarPDF(
      contenido: HTMLElement, 
      nombreArchivo: string = 'reporte.pdf'
    ): Promise<{ pdfBlob: Blob, fileName: string }> {
        await this.esperarMapaEstable();
        // Opciones específicas para capturar mapas Leaflet
        const options = {
          scale: 2,
          useCORS: true,
          allowTaint: true,
          logging: true,
          onclone: (clonedDoc: Document) => {
            // Forzar estilos para la captura
            const mapContainer = clonedDoc.getElementById('map');
            if (mapContainer) {
              mapContainer.style.visibility = 'visible';
              mapContainer.style.opacity = '1';
              mapContainer.style.height = mapContainer.offsetHeight + 'px'; // Fijar altura
              mapContainer.style.width = mapContainer.offsetWidth + 'px'; // Fijar ancho
            }

            // Congelar animaciones y transiciones
            const allElements = clonedDoc.querySelectorAll('*');
            allElements.forEach(el => {
              (el as HTMLElement).style.transition = 'none';
              (el as HTMLElement).style.animation = 'none';
            });
          },
          async: true,
          proxy: undefined,
          ignoreElements: (element: Element) => {
            // Ignorar elementos que puedan causar problemas
            return element.tagName === 'SCRIPT' || element.tagName === 'LINK';
          }
          
        };

      const canvas = await Promise.race([
        html2canvas(contenido, options),
        new Promise((_, reject) => 
          setTimeout(() => reject(new Error('Timeout en captura HTML2Canvas')), 30000)
        )
      ]) as HTMLCanvasElement;
      const imgData = canvas.toDataURL('image/png');
      const pdf = new jsPDF('p', 'mm', 'a4');
    
      const imgWidth = 190;
      const pageHeight = 295;
      const imgHeight = canvas.height * imgWidth / canvas.width;
    
      let heightLeft = imgHeight;
      let position = 0;
    
      pdf.addImage(imgData, 'PNG', 10, position, imgWidth, imgHeight);
      heightLeft -= pageHeight;
    
      while (heightLeft > 0) {
        position = heightLeft - imgHeight;
        pdf.addPage();
        pdf.addImage(imgData, 'PNG', 10, position, imgWidth, imgHeight);
        heightLeft -= pageHeight;
      }
  
      const pdfBlob = pdf.output('blob');
      
      return { 
        pdfBlob, 
        fileName: nombreArchivo.endsWith('.pdf') ? nombreArchivo : `${nombreArchivo}.pdf`
      };
    }

    private async esperarMapaEstable(): Promise<void> {
      return new Promise((resolve) => {
        // Esperar a que se completen las operaciones del mapa
        setTimeout(resolve, 1000); // 1 segundo de espera
      });
    }
}