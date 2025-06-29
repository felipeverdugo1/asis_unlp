import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';

@Component({
  standalone: true, 
  imports: [FormsModule, CommonModule], 
  template: `
    <form>
      <input 
        [(ngModel)]="usuario.nombreUsuario" 
        name="nombreUsuario" 
        placeholder="Nombre"
      >
      <input
        [(ngModel)]="usuario.email"
        name="email"
        placeholder="Email"
      >
    </form>
  `,
})
export class FormUsuarioPage {
  usuario = {
    nombreUsuario: '',
    email: ''
  };
}