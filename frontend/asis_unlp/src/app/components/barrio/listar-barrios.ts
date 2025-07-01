import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Barrio } from '../../models/barrio.model';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  standalone : true,
  selector: 'listar-barrios',
  imports : [CommonModule,RouterModule],
  templateUrl: './listar-barrios.html',
  styleUrls: ['../../../styles.css']
})
export class ListarBarrios {
  @Input() barrios: Barrio[] = [];
  @Output() onEdit = new EventEmitter<number>();
  @Output() onDelete = new EventEmitter<number>();
}