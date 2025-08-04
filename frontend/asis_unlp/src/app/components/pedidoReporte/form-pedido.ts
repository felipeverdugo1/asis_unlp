import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgModel, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PedidoReporte  } from '../../models/pedido.model';

@Component({
  standalone: true,
  selector: 'form-pedido',
  imports: [CommonModule, FormsModule],
  templateUrl: './form-pedido.html',
  styleUrls: ['../../../styles.css']
})
export class FormPedido {
  @Input() pedido: PedidoReporte = {id: 0, nombre: '', camposPedidos: '', creadoPor_id: 0, estado: ''};
  @Output() onSubmit = new EventEmitter<PedidoReporte>();
}