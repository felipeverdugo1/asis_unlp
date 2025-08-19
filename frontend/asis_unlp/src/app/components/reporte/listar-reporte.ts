import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Reporte } from '../../models/reporte.model';

@Component({
  standalone:true,
  selector: 'listar-reportes',
  imports: [CommonModule,RouterModule],
  templateUrl: './reporte.html',
})
export class ListarReportes {
  @Input() reportes: Reporte[] = [];
  @Output() onEdit = new EventEmitter<number>();
  @Output() onDelete = new EventEmitter<number>();
}
