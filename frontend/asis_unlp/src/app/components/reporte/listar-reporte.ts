import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Reporte } from '../../models/reporte.model';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';



@Component({
  standalone: true,
  selector: 'compartir-input-dialog-reporte',
  imports: [CommonModule, FormsModule],
  templateUrl: './compartir-input-reporte.component.html',
  styleUrls: ['../../../styles.css']
})
export class CompartirConInputReporteComponent {
  @Input() usuarios_compartidos: any[] = [];
  @Output() saved = new EventEmitter<{compartidoConId : number}>();
  @Output() closed = new EventEmitter<void>();

  compartidoConId: number | null = null;

  save() {
    if (this.compartidoConId) {
      this.saved.emit({compartidoConId : this.compartidoConId})
    }
  }

  close() {
    this.closed.emit();
  }

  isValid(): boolean {
    return this.compartidoConId !== null;
  }
}




@Component({
  standalone:true,
  selector: 'listar-reportes',
  imports: [CommonModule,RouterModule],
  templateUrl: './reporte.html',
  styleUrls: ['../../../styles.css']
})
export class ListarReportes {
  @Input() reportes: Reporte[] = [];
  @Input() usuarios_compartidos: any[] = [];
  @Input() downloading: boolean = false;
  @Output() onDelete = new EventEmitter<number>();
  @Output() closeDialog = new EventEmitter<void>();
  @Output() share = new EventEmitter<number>(); 
  @Output() onDownload = new EventEmitter<number>();
  @Input() showCompleteDialog = false;

    constructor(public auth: AuthService) {}
}
