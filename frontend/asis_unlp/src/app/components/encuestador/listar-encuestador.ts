import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Encuestador } from '../../models/encuestador.model';

@Component({
  standalone:true,
  selector: 'listar-encuestadores',
  imports: [CommonModule,RouterModule],
  templateUrl: './encuestador.html',
})
export class ListarEncuestadores {
  @Input() encuestadores: Encuestador[] = [];
  @Output() onEdit = new EventEmitter<number>();
  @Output() onDelete = new EventEmitter<number>();
}
