import { Component, OnInit } from '@angular/core';
import { UsuariosService } from '../../services/usuarios.service';
import { Usuario } from '../../models/usuario.model';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TablaUsuarios } from '../../components/usuario/usuario';
import { Observable } from 'rxjs';

@Component({
  standalone :true,
  imports : [CommonModule,RouterModule,TablaUsuarios],
  template: `
    <h2>Usuarios</h2>
    <tabla-usuarios 
    [usuarios]="(usuarios$ | async) ?? []"
      (onEdit)="editarUsuario($event)"
      (onDelete)="borrarUsuario($event)">
    </tabla-usuarios>
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

  constructor(
    private usuariosService: UsuariosService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.usuarios$ = this.usuariosService.getUsuarios();
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
        error: (err) => console.error('Error al borrar:', err)
      });
    }
  }
}


