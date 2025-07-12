import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Barrio, BarrioForm } from '../../models/barrio.model';
import { NgModel, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'form-barrio',
  imports: [CommonModule, FormsModule],
  templateUrl: './form-barrio.html',
  styleUrls: ['../../../styles.css']
})
export class FormBarrio {
  @Input() barrio: BarrioForm = { nombre: '', informacion: '', geolocalizacion: '' };
  @Output() onSubmit = new EventEmitter<BarrioForm>();
}

@Component({
  standalone: true,
  selector: 'form-barrio',
  imports: [CommonModule, FormsModule],
  templateUrl: './form-barrio.html',
  styleUrls: ['../../../styles.css']
})
export class FormActualizarBarrio {
  @Input() barrio: Barrio = { id: 0, nombre: '', informacion: '', geolocalizacion: '' };
  @Output() onSubmit = new EventEmitter<Barrio>();
}
