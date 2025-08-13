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
      const canvas = await html2canvas(contenido, { scale: 2 });
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
}