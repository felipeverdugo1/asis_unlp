import { Component, Input, Output, EventEmitter } from '@angular/core';
import { PedidoReporte } from '../../models/pedido.model';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'reporte-input-dialog-pedido',
  imports: [CommonModule, FormsModule],
  templateUrl: './reporte-input-pedido.component.html',
  styleUrls: ['../../../styles.css']
})
export class ReporteInputPedidoComponent {
  @Input() reportes: any[] = [];
  @Output() saved = new EventEmitter<number>();
  @Output() closed = new EventEmitter<void>();

  reporteId: number | null = null;

  save() {
    if (this.reporteId) {
      this.saved.emit(this.reporteId);
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
  imports: [CommonModule, RouterModule, ReporteInputPedidoComponent],
  templateUrl: './listar-pedidos.html',
  styleUrls: ['../../../styles.css']
})
export class ListarPedidosComponent {
  @Input() pedidos: any[] = [];
  @Input() showCompleteDialog: boolean = false;
  @Input() selectedPedidoId: number | null = null;
  
  @Output() onTake = new EventEmitter<number>();
  @Output() onComplete = new EventEmitter<number>();
  @Output() onDownload = new EventEmitter<number>();
  @Output() onCloseDialog = new EventEmitter<void>();
  @Output() onCompleteWithReporte = new EventEmitter<number>();

  constructor(public auth: AuthService) {}

  openCompleteDialog(pedidoId: number) {
    this.selectedPedidoId = pedidoId;
    this.onComplete.emit(pedidoId);
  }
}