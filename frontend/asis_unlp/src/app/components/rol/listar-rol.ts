import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Rol } from '../../models/rol.model';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  standalone : true,
  selector: 'listar-roles',
  imports : [CommonModule,RouterModule],
  templateUrl: './listar-rol.html',
  styleUrls: ['../../../styles.css']
})
export class ListarRoles {
  @Input() roles: Rol[] = [];
  @Output() onEdit = new EventEmitter<number>();
  @Output() onDelete = new EventEmitter<number>();

}