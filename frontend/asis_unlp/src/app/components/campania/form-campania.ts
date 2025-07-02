import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Barrio, BarrioForm } from '../../models/barrio.model';
import { NgModel, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Campania } from '../../models/campania.model';
@Component({
  standalone: true,
  selector: 'form-campania',
  imports: [CommonModule, FormsModule],
  templateUrl: './form-campania.html',
  styleUrls: ['../../../styles.css']
})
export class FormCampania {
  @Input() barrios: Barrio[] = [];
  @Input() campania: Campania = {id: 0, nombre: '', fechaFin: '' , fechaInicio: '', barrio_id : 0};
  @Output() onSubmit = new EventEmitter<Campania>();
}

@Component({
  standalone: true,
  selector: 'form-campania',
  imports: [CommonModule, FormsModule],
  templateUrl: './form-campania.html',
  styleUrls: ['../../../styles.css']
})
export class FormActualizarCampania {
  @Input() barrios: Barrio[] = [];
  @Input() campania: Campania = { id: 0 , nombre: '', fechaFin: '' , fechaInicio: '', barrio_id : 0};
  @Output() onSubmit = new EventEmitter<Campania>();
}
