import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Campania } from '../../models/campania.model';
import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  standalone:true,
  selector: 'listar-campanias',
  imports: [CommonModule,RouterModule],
  templateUrl: './campania.html',
})
export class ListarCampanias {
  @Input() campanias: Campania[] = [];
  @Output() onEdit = new EventEmitter<number>();
  @Output() onDelete = new EventEmitter<number>();
}
