import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Usuario } from '../../models/usuario.model';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'form-usuario',
  imports: [CommonModule, FormsModule],
  templateUrl: './form-usuario.html',
  styleUrls: ['../component-style.css']
})
export class FormUsuario {
  @Input() usuario: Usuario = { nombreUsuario: '', email: '', password: '', habilitado: true, especialidad: "" };
  @Output() onSubmit = new EventEmitter<Usuario>();
  
  goBack() {
    window.history.back();
  }
}
