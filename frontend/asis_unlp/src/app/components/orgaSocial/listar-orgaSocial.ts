import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { OrgaSocial } from '../../models/orgaSocial.model';
import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  standalone:true,
  selector: 'listar-orgaSociales',
  imports: [CommonModule,RouterModule],
  templateUrl: './orgaSocial.html',
})
export class ListarOrgaSocial {
  @Input() orgaSociales: OrgaSocial[] = [];
  @Output() onEdit = new EventEmitter<number>();
  @Output() onDelete = new EventEmitter<number>();
}
