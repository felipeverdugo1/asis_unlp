import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Barrio, BarrioForm } from '../../models/barrio.model';
import { NgModel, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Zona,  } from '../../models/zona.model';
@Component({
  standalone: true,
  selector: 'form-zona',
  imports: [CommonModule, FormsModule],
  templateUrl: './form-zona.html',
  styleUrls: ['../../../styles.css']
})
export class FormZona {
  @Input() barrios: Barrio[] = [];
  @Input() zona: Zona = {id: 0, nombre: '', geolocalizacion: '' , barrio_id : 0};
  @Output() onSubmit = new EventEmitter<Zona>();
}

@Component({
  standalone: true,
  selector: 'form-zona',
  imports: [CommonModule, FormsModule],
  templateUrl: './form-zona.html',
  styleUrls: ['../../../styles.css']
})
export class FormActualizarZona {
  @Input() barrios: Barrio[] = [];
  @Input() zona: Zona = { id: 0 , nombre: '', geolocalizacion: '' , barrio_id : 0};
  @Output() onSubmit = new EventEmitter<Zona>();
}
