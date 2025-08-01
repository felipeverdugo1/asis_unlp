import { Component, OnInit, ChangeDetectorRef  } from '@angular/core';
import { UsuariosService } from '../../services/usuarios.service';
import { Usuario } from '../../models/usuario.model';
import { Rol } from '../../models/rol.model';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';
import { ListarUsuarios } from '../../components/usuario/listar-usuario';
import { FormUsuario } from '../../components/usuario/form-usuario';
import { Observable } from 'rxjs';
import { error, log } from 'console';
import { RolesService } from '../../services/roles.service';


@Component({
  standalone :true,
  imports : [CommonModule,RouterModule,ListarUsuarios],
  template: `
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">Usuarios</h1>
      <button (click)="nuevoUsuario()" class="btn btn-create"> Agregar Usuario</button>
    </div>

    <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>

    <div class="content-container">
      <listar-usuarios 
      [usuarios]="(usuarios$ | async) ?? []"
        (onEdit)="editarUsuario($event)"
        (onDelete)="borrarUsuario($event)"
        (toggleHabilitado)="toggleEstado($event)"
        (editarRoles)="editarRolesUsuario($event)">
      </listar-usuarios>
    </div>
  </div>
  `
})
export class ListaUsuariosPage implements OnInit {
  usuarios$!: Observable<Usuario[]>;
  errorMensaje: string | null = null;

  constructor(
    private usuariosService: UsuariosService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.usuarios$ = this.usuariosService.getUsuarios();
    this.errorMensaje = null;
  }
  
  cargarUsuarios() {
    this.usuarios$ = this.usuariosService.getUsuarios();
  }

  nuevoUsuario() {
    this.router.navigate(['/usuario/nuevo']);
  }

  editarUsuario(id: number) {    
    this.router.navigate(['/usuario/editar', id]);
  }

  borrarUsuario(id: number) {
    if (confirm('¿Borrar usuario?')) {
      this.usuariosService.deleteUsuario(id).subscribe({
        next: () => this.cargarUsuarios(),
        error: (err) => this.errorMensaje = err.error?.error || 'Error inesperado al borrar el usuario.'
      });
    }
  }

  toggleEstado(data: { id: number, habilitado: boolean }) {
    const nuevoEstado = !data.habilitado;
    this.usuariosService.updateEstadoHabilitado(data.id, nuevoEstado).subscribe({
      next: () => this.cargarUsuarios(),
      error: err =>
        this.errorMensaje = err.error?.error || 'Error al cambiar el estado del usuario.'
    });
  }

  editarRolesUsuario(id: number) {
    this.router.navigate(['/usuario/roles', id]);
  }
}



@Component({
  standalone: true, 
  imports: [FormsModule, CommonModule, FormUsuario], 
  template: `
  <div class="form-container">
    <div class="title">
      <h2>{{ esEdicion ? 'Editar usuario' : 'Nuevo usuario' }}</h2>
    </div>
    
    <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>
    
    <ng-container *ngIf="!loading; else cargando">
      <form-usuario [usuario]="usuario" [esEdicion]="esEdicion" (onSubmit)="guardarUsuario($event)"></form-usuario>
    </ng-container>
    <ng-template #cargando>
      <p>Cargando usuario...</p>
    </ng-template>
  </div>
  `,
})
export class FormUsuarioPage {
  usuario: Usuario = { 
    nombreUsuario: '', 
    email: '', 
    password: '', 
    habilitado: true, 
    especialidad: "",
    roles_id: [] 
  };

  esEdicion = false;
  loading = false;
  errorMensaje: string | null = null;

  constructor(
    private usuarioService: UsuariosService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.errorMensaje = null;
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.esEdicion = true;
      this.loading = true;
      this.usuarioService.getUsuario(+id).subscribe({
        next: (data) => {
          this.usuario = data;
          this.loading = false;
        },
        error: (err) => {
          this.errorMensaje = err.error?.error || 'Error inesperado al cargar el usuario.';
          this.loading = false;
        }
      });
    }
  }

  guardarUsuario(usuario: Usuario) {
    const req = this.esEdicion
      ? this.usuarioService.updateUsuario(usuario)
      : this.usuarioService.crearUsuario(usuario);

    req.subscribe({
      next: () => this.router.navigate(['/usuario']),
      error: (err) => this.errorMensaje = err.error?.error || 'Error inesperado al guardar el usuario.'
    });
  }

}



@Component({
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
   <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">Editar roles de {{ nombreUsuario }}</h1>
    </div>

    <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>

    <ng-container *ngIf="!loading; else cargandoBlock">
      <div class="form-container">
        <div class="form-group">
          <div class="form-label">
            <h3>Roles asignados</h3>
          </div>
          <ul class="list-ul">
            <li class="list-item" *ngFor="let rol of rolesAsignados">
                <button (click)="quitarRol(rol.id!)" class="btn btn-delete">Quitar</button>  
                {{ rol.nombre }}            
            </li>
          </ul>
        </div>
        
        <div class="form-group">
          <div class="form-label">
            <h3>Roles no asignados</h3>
          </div>
          <ul class="list-ul">
            <li class="list-item" *ngFor="let rol of rolesNoAsignados">
              <button (click)="asignarRol(rol.id!)" class="btn btn-create">Asignar</button>
              {{ rol.nombre }}
            </li>
          </ul>
        </div>
      </div>
    </ng-container>

    <ng-template #cargandoBlock>
      <p>Cargando datos...</p>
      {{loading}}
    </ng-template>
  </div>
    
  `,
  styleUrls: ['../../../styles.css']
})
export class UsuarioRolesPage implements OnInit {
  nombreUsuario = '';
  rolesAsignados: Rol[] = [];
  rolesNoAsignados: Rol[] = [];
  errorMensaje: string | null = null;
  loading = false;
  usuarioId!: number; // Definila como propiedad para acceder desde todo el componente

  constructor(
    private route: ActivatedRoute,
    private usuarioService: UsuariosService,
    private rolesService: RolesService,
    private cdr: ChangeDetectorRef
  ) {}
  

  ngOnInit(): void {
    this.usuarioId = +this.route.snapshot.paramMap.get('id')!;
    this.cargarDatos();
  }

  cargarDatos() {
    this.loading = true;
    this.usuarioService.getUsuario(this.usuarioId).subscribe({
      next: usuario => {
        this.nombreUsuario = usuario.nombreUsuario;
        const rolesUsuarioIds = (usuario.roles ?? []).map(r => r.id);

        this.rolesService.getRoles().subscribe({
          next: roles => {
            this.rolesAsignados = roles.filter(r => rolesUsuarioIds.includes(r.id!));
            this.rolesNoAsignados = roles.filter(r => !rolesUsuarioIds.includes(r.id!));
            this.loading = false;
            this.cdr.detectChanges();
          },
          error: (err) => {
            this.errorMensaje =  err.error?.error || 'Error al cargar roles.';
            this.loading = false;
            this.cdr.detectChanges();
          }
        });
      },
      error: (err) => {
        this.errorMensaje =  err.error?.error || "Error al cargar usuario";
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  asignarRol(rolId: number) {
    this.usuarioService.agregarRol(this.usuarioId, rolId).subscribe({
      next: () => this.cargarDatos(), // Refrescamos la lista tras el cambio
      error: (err) => this.errorMensaje =  err.error?.error || 'Error al asignar rol.'
    });
  }

  quitarRol(rolId: number) {
    this.usuarioService.quitarRol(this.usuarioId, rolId).subscribe({
      next: () => this.cargarDatos(),
      error: (err) => this.errorMensaje =  err.error?.error || 'Error al quitar rol.'
    });
  }
}
