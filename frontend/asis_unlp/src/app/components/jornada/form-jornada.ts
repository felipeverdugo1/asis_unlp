import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Jornada } from '../../models/jornada.model';
import { NgModel, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'form-jornada',
  imports: [CommonModule, FormsModule],
  templateUrl: './form-jornada.html',
  styleUrls: ['../../../styles.css']
})
export class FormJornada {
  @Input() jornada: Jornada = { id: 0, fechaInicio: '', fechaFin: '', campaña_id: 0 };
  @Output() onSubmit = new EventEmitter<Jornada>();
}

@Component({
  standalone: true,
  selector: 'form-jornada',
  imports: [CommonModule, FormsModule],
  templateUrl: './form-jornada.html',
  styleUrls: ['../../../styles.css']
})
export class FormActualizarJornada {
  @Input() jornada: Jornada = { id: 0, fechaInicio: '', fechaFin: '', campaña_id: 0 };
  @Output() onSubmit = new EventEmitter<Jornada>();
}
