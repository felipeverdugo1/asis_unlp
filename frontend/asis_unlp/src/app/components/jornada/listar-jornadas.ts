import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Jornada } from '../../models/jornada.model';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  standalone : true,
  selector: 'listar-jornadas',
  imports : [CommonModule,RouterModule],
  templateUrl: './listar-jornadas.html',
  styleUrls: ['../../../styles.css']
})
export class ListarJornadas {
  @Input() jornadas: Jornada[] = [];
  @Output() onEdit = new EventEmitter<number>();
  @Output() onDelete = new EventEmitter<number>();
}