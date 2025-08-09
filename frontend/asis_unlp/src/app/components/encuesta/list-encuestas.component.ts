import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Encuesta } from '../../models/encuesta.model';

@Component({
  standalone:true,
  selector: 'listar-encuestas',
  imports: [CommonModule,RouterModule],
  templateUrl: './list-encuestas.html',
})
export class ListarEncuestas {
  @Input() encuestas: Encuesta[] = [];
  @Output() onDelete = new EventEmitter<number>();
}
