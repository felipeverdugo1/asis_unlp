import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Reporte } from '../../models/reporte.model';

@Component({
  standalone:true,
  selector: 'listar-reportes',
  imports: [CommonModule,RouterModule],
  templateUrl: './reporte.html',
  styleUrls: ['../../../styles.css']
})
export class ListarReportes {
  @Input() reportes: Reporte[] = [];
  @Input() downloading: boolean = false;
  @Output() onEdit = new EventEmitter<number>();
  @Output() onDelete = new EventEmitter<number>();
  @Output() onDownload = new EventEmitter<number>();
}
