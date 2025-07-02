import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Usuario } from '../../models/usuario.model';
import { NgModel, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { OrgaSocial } from '../../models/orgaSocial.model';
import { Barrio } from '../../models/barrio.model';
@Component({
  standalone: true,
  selector: 'form-orgaSocial',
  imports: [CommonModule, FormsModule],
  templateUrl: './form-orgaSocial.html',
  styleUrls: ['../../../styles.css']
})
export class FormOrgaSocial {
  @Input() usuarios: Usuario[] = [];
  @Input() barrios: Barrio[] = [];
  @Input() orgaSocial: OrgaSocial = {id: 0, nombre: '', domicilio: '',actividadPrincipal : '' , barrio_id : 0,referente_id : 0};
  @Output() onSubmit = new EventEmitter<OrgaSocial>();
}

@Component({
  standalone: true,
  selector: 'form-orgaSocial',
  imports: [CommonModule, FormsModule],
  templateUrl: './form-orgaSocial.html',
  styleUrls: ['../../../styles.css']
})
export class FormActualizarOrgaSocial {
  @Input() usuarios: Usuario[] = [];
  @Input() barrios: Barrio[] = [];
  @Input() orgaSocial: OrgaSocial = { id: 0 , nombre: '',domicilio : '' ,actividadPrincipal: '' , barrio_id : 0 , referente_id : 0};
  @Output() onSubmit = new EventEmitter<OrgaSocial>();
}
