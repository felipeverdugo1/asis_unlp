import { Component, OnInit } from '@angular/core';
import { UsuariosService } from '../../services/usuarios.service';
import { Usuario } from '../../models/usuario.model';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';
import { ListarUsuarios } from '../../components/usuario/listar-usuario';
import { FormUsuario } from '../../components/usuario/form-usuario';
import { Observable } from 'rxjs';

@Component({
  standalone :true,
  imports : [CommonModule,RouterModule,ListarUsuarios],
  template: `
    <h2>Usuarios</h2>

    <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>

    <listar-usuarios 
    [usuarios]="(usuarios$ | async) ?? []"
      (onEdit)="editarUsuario($event)"
      (onDelete)="borrarUsuario($event)">
    </listar-usuarios>
    <button (click)="nuevoUsuario()" class="btn-add"> Agregar Usuario</button>

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
}



@Component({
  standalone: true, 
  imports: [FormsModule, CommonModule, FormUsuario], 
  template: `
    <h2>{{ esEdicion ? 'Editar usuario' : 'Nuevo usuario' }}</h2>

    <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>
    
    <ng-container *ngIf="!loading; else cargando">
      <form-usuario [usuario]="usuario" (onSubmit)="guardarUsuario($event)"></form-usuario>
    </ng-container>
    <ng-template #cargando>
      <p>Cargando usuario...</p>
    </ng-template>
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