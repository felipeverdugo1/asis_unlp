import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Rol } from '../../models/rol.model';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'form-rol',
  imports: [CommonModule, FormsModule],
  templateUrl: './form-rol.html',
  styleUrls: ['../component-style.css']
})
export class FormRol {
  @Input() rol: Rol = { nombre: '' };
  @Output() onSubmit = new EventEmitter<Rol>();
  
  goBack() {
    window.history.back();
  }
}
