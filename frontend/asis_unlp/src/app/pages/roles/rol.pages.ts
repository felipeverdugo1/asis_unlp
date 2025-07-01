import { Component, OnInit } from '@angular/core';
import { RolesService } from '../../services/roles.service';
import { Rol } from '../../models/rol.model';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';
import { ListarRoles } from '../../components/rol/listar-rol';
import { FormRol } from '../../components/rol/form-rol';
import { Observable } from 'rxjs';

@Component({
  standalone :true,
  imports : [CommonModule,RouterModule,ListarRoles],
  template: `
    <h2>Roles</h2>
    <listar-roles 
    [roles]="(roles$ | async) ?? []"
      (onEdit)="editarRol($event)"
      (onDelete)="borrarRol($event)">
    </listar-roles>
    <button (click)="nuevoRol()" class="btn-add"> Agregar Rol</button>

  `,
  styles: [`
    .btn-add {
      margin-bottom: 20px;
      padding: 8px 16px;
      background: #4CAF50;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
  `]
})
export class ListarRolesPage implements OnInit {
  roles$!: Observable<Rol[]>;

  constructor(
    private rolesService: RolesService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.roles$ = this.rolesService.getRoles();
  }
  
  cargarRol() {
    this.roles$ = this.rolesService.getRoles();
  }

  nuevoRol() {
    this.router.navigate(['/rol/nuevo']);
  }

  editarRol(id: number) {
    this.router.navigate(['/rol/editar', id]);
  }

  borrarRol(id: number) {
    if (confirm('¿Borrar rol?')) {
      this.rolesService.deleteRol(id).subscribe({
        next: () => this.cargarRol(),
        error: (err) => console.error('Error al borrar:', err)
      });
    }
  }
}



@Component({
  standalone: true, 
  imports: [FormsModule, CommonModule, FormRol], 
  template: `
    <h2>Nuevo rol</h2>
    <form-rol [rol]="rol" (onSubmit)="guardarRol($event)"></form-rol>
  `,
})
export class FormRolPage {
  rol: Rol = { nombre: '' };

  constructor(
    private rolesService: RolesService,
    private router: Router
  ) {}

  guardarRol(rol: Rol) {
    this.rolesService.crearRol(rol).subscribe({
      next: () => this.router.navigate(['/rol']),
      error: (err) => console.error('Error al guardar rol:', err)
    });
  }

}