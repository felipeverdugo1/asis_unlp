import { Component, Input, Output, EventEmitter, OnInit  } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Usuario } from '../../models/usuario.model';
import { Rol } from '../../models/rol.model';
import { FormsModule } from '@angular/forms';
import { RolesService } from "../../services/roles.service"

@Component({
  standalone: true,
  selector: 'form-usuario',
  imports: [CommonModule, FormsModule],
  templateUrl: './form-usuario.html',
  styleUrls: ['../component-style.css']
})
export class FormUsuario implements OnInit {
  @Input() usuario: Usuario = { 
    nombreUsuario: '', 
    email: '', 
    password: '', 
    habilitado: true, 
    especialidad: "", 
    roles_id: [] 
  };
  @Output() onSubmit = new EventEmitter<Usuario>();
  
  roles: Rol[] = [];

  constructor(private rolesService: RolesService) {}

  ngOnInit(): void {
    this.rolesService.getRoles().subscribe({
      next: (roles) => (this.roles = roles),
      error: (err) => console.error('Error al cargar roles', err)
    });
  }

  goBack() {
    window.history.back();
  }
}
