import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Jornada } from '../../models/jornada.model';
import { NgModel, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Zona } from '../../models/zona.model';


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


@Component({
  standalone: true,
  selector: 'form-administrar-zonas',
  imports: [CommonModule, FormsModule],
  templateUrl: './form-administrar-zonas.html',
  styleUrls: ['../../../styles.css']
})

export class FormAdministrarZonas {
  @Input() zonas: (Zona & { seleccionada?: boolean })[] = [];
  @Output() onSubmit = new EventEmitter<(Zona & { seleccionada?: boolean })[]>();

  guardar() {
    this.onSubmit.emit(this.zonas);
  }

  zonasSeleccionadas() {
    return this.zonas.filter(z => z.seleccionada);
  }
}