import { Component, OnInit } from '@angular/core';
import { RolesService } from '../../services/roles.service';
import { Rol } from '../../models/rol.model';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
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

    <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>

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
  errorMensaje: string | null = null;

  constructor(
    private rolesService: RolesService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.roles$ = this.rolesService.getRoles();
    this.errorMensaje = null;
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
        error: (err) => this.errorMensaje = err.error?.error || 'Error inesperado al borrar el rol.'
      });
    }
  }
}



@Component({
  standalone: true, 
  imports: [FormsModule, CommonModule, FormRol], 
  template: `
    <h2>{{ esEdicion ? 'Editar Rol' : 'Nuevo Rol' }}</h2>

    <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>
    
    <ng-container *ngIf="!loading; else cargando">
      <form-rol [rol]="rol" (onSubmit)="guardarRol($event)"></form-rol>
    </ng-container>
    <ng-template #cargando>
      <p>Cargando rol...</p>
    </ng-template>
  `,
})
export class FormRolPage {
  rol: Rol = { nombre: '' };
  esEdicion = false;
  loading = false;
  errorMensaje: string | null = null;

  constructor(
    private rolesService: RolesService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.errorMensaje = null;
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.esEdicion = true;
      this.loading = true;
      this.rolesService.getRol(+id).subscribe({
        next: (data) => {
          this.rol = data;
          this.loading = false;
        },
        error: (err) => {
          this.errorMensaje = err.error?.error || 'Error inesperado al cargar el rol.';
          this.loading = false;
        }
      });
    }
  }

  guardarRol(rol: Rol) {
    const req = this.esEdicion
      ? this.rolesService.updateRol(rol)
      : this.rolesService.crearRol(rol);

    req.subscribe({
      next: () => this.router.navigate(['/rol']),
      error: (err) => this.errorMensaje = err.error?.error || 'Error inesperado al guardar el rol.'
    });
  }

}