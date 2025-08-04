import { Component, Input, Output, EventEmitter } from '@angular/core';
import { PedidoReporte } from '../../models/pedido.model';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';
import { ReporteService } from '../../services/reporte.service';
import { PedidoService } from '../../services/pedido.service';

@Component({
  standalone: true,
  selector: 'reporte-input-dialog-pedido',
  imports: [CommonModule, FormsModule],
  templateUrl: './reporte-input-pedido.component.html',
  styleUrls: ['../../../styles.css']
})
export class ReporteInputPedidoComponent {
  @Input() reportes: any[] = [];
  @Output() saved = new EventEmitter<{reporteId: number, comentario?: string}>();
  @Output() closed = new EventEmitter<void>();

  reporteId: number | null = null;
  comentario: string = '';

  save() {
    if (this.reporteId) {
      this.saved.emit({
        reporteId: this.reporteId,
        comentario: this.comentario.trim() || undefined
      });
    }
  }

  close() {
    this.closed.emit();
  }

  isValid(): boolean {
    return this.reporteId !== null;
  }
}

@Component({
  standalone: true,
  selector: 'listar-pedidos',
  imports: [CommonModule, RouterModule],
  templateUrl: './listar-pedidos.html',
  styleUrls: ['../../../styles.css']
})
export class ListarPedidosComponent {
  @Input() pedidos: any[] = [];
  @Input() showCompleteDialog = false;
  @Input() reportes: any[] = [];
  
  @Output() take = new EventEmitter<number>();
  @Output() complete = new EventEmitter<number>();
  @Output() download = new EventEmitter<number>();
  @Output() closeDialog = new EventEmitter<void>();
  @Output() completeWithReporte = new EventEmitter<{ reporteId: number, comentario?: string }>();
  @Output() release = new EventEmitter<number>();
  @Output() delete = new EventEmitter<number>();

  constructor(public auth: AuthService) {}
}