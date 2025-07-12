import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Encuestador } from '../../models/encuestador.model';
@Component({
  standalone: true,
  selector: 'form-encuestador',
  imports: [CommonModule, FormsModule],
  templateUrl: './form-encuestador.html',
  styleUrls: ['../../../styles.css']
})
export class FormEncuestador {
  @Input() encuestador: Encuestador ={ id: 0, nombre : '',dni: '', edad : 0 , ocupacion : '' ,genero : ''}
  @Output() onSubmit = new EventEmitter<Encuestador>();
}

@Component({
  standalone: true,
  selector: 'form-encuestador',
  imports: [CommonModule, FormsModule],
  templateUrl: './form-encuestador.html',
  styleUrls: ['../../../styles.css']
})
export class FormActualizarEncuestador {
  @Input() encuestador: Encuestador ={ id: 0, nombre : '',dni: '', edad : 0 , ocupacion : '' ,genero : ''}
  @Output() onSubmit = new EventEmitter<Encuestador>();
}
