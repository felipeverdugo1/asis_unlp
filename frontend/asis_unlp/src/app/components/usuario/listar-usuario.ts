import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Usuario } from '../../models/usuario.model';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  standalone : true,
  selector: 'listar-usuarios',
  imports : [CommonModule,RouterModule],
  templateUrl: './usuario.html',
  styleUrls: ['./usuario.css']
})
export class ListarUsuarios {
  @Input() usuarios: Usuario[] = [];
  @Output() onEdit = new EventEmitter<number>();
  @Output() onDelete = new EventEmitter<number>();

}