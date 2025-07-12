import { Component, OnInit } from '@angular/core';
import { OrgaSocialService } from '../../services/orgaSocial.service'; 
import { UsuariosService } from '../../services/usuarios.service';
import { BarriosService } from '../../services/barrios.service';
import { OrgaSocial } from '../../models/orgaSocial.model'; 
import { Barrio } from '../../models/barrio.model';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ListarOrgaSocial } from '../../components/orgaSocial/listar-orgaSocial'; 
import { Observable } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { FormOrgaSocial } from '../../components/orgaSocial/form-orgaSocial';
import { Usuario } from '../../models/usuario.model';



@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, ListarOrgaSocial],
  template: `
    <h2>OrgaSocial</h2>
    <listar-orgaSociales 
      [orgaSociales]="(orgaSociales$ | async) ?? []"
      (onEdit)="editarOrgaSocial($event)"
      (onDelete)="borrarOrgaSocial($event)">
    </listar-orgaSociales>
    <button (click)="nuevaOrgaSocial()" class="btn-add">Agregar Organizacion Social</button>
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
export class ListarOrgaSocialPage implements OnInit {
  orgaSociales$!: Observable<OrgaSocial[]>;

  constructor(
    private orgaSocialService: OrgaSocialService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarOrgaSocial();
  }

  cargarOrgaSocial() {
    this.orgaSociales$ = this.orgaSocialService.getOrgaSociales();
  }

  nuevaOrgaSocial() {
    this.router.navigate(['/organizacionSocial/nueva']);
  }

  editarOrgaSocial(id: number) {
    this.router.navigate(['/organizacionSocial/editar', id]);
  }

  borrarOrgaSocial(id: number) {
    if (confirm('¿Borrar organizacion social?')) {
      this.orgaSocialService.deleteOrgaSocial(id).subscribe({
        next: () => this.cargarOrgaSocial(),
        error: (err) => console.error('Error al borrar:', err)
      });
    }
  }
}


@Component({
    standalone: true,
    imports: [CommonModule, FormsModule,FormOrgaSocial],
    template: `
    <h2>{{ esEdicion ? 'Editar OrgaSocial' : 'Nueva OrgaSocial' }}</h2>
    
    <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>


    <ng-container *ngIf="!loading; else cargando">
      <form-orgaSocial 
      [usuarios]="usuarios"
      [barrios]="barrios"
      [orgaSocial]="orgaSocial" 
      (onSubmit)="guardarOrgaSocial($event)">
      </form-orgaSocial>
    </ng-container>
    <ng-template #cargando>
      <p>Cargando OrgaSocial...</p>
    </ng-template>
  `
  })

  export class FormOrgaSocialPage implements OnInit {
    orgaSocial : OrgaSocial = { id: 0, nombre : '', domicilio : '',actividadPrincipal: '', barrio_id: 0,referente_id : 0 }
    usuarios: Usuario[] = [];
    barrios: Barrio [] = []
    esEdicion = false;
    loading = false;
    errorMensaje: string | null = null;

  
    constructor(
      private orgaSocialService: OrgaSocialService,
      private usuarioService: UsuariosService,
      private barrioService : BarriosService,
      private router: Router,
      private route: ActivatedRoute
    ) {}
  
    ngOnInit(): void {

    // Cargar usuarios
    this.errorMensaje = null;
    this.usuarioService.getUsuarios().subscribe({
      next: (usuarios) => this.usuarios = usuarios,
      error: (err) => console.error('Error al cargar usuarios:', err)
    });


        // Cargar barrios
        this.errorMensaje = null;
        this.barrioService.getBarrios().subscribe({
          next: (barrios) => this.barrios = barrios,
          error: (err) => console.error('Error al cargar usuarios:', err)
        });
    


      const id = this.route.snapshot.paramMap.get('id');
      if (id) {
        this.esEdicion = true;
        this.loading = true;
        this.orgaSocialService.getOrgaSocial(+id).subscribe({
          next: (data) => {
            this.orgaSocial = data;
            this.loading = false;
          },
          error: (err) => {
            console.error('Error al cargar OrgaSocial:', err);
            this.loading = false;
          }
        });
      }
    }


    guardarOrgaSocial(orgaSocial: OrgaSocial) {

      const req = this.esEdicion
        ? this.orgaSocialService.updateOrgaSocial(orgaSocial)
        : this.orgaSocialService.createOrgaSocial(orgaSocial);
  
      req.subscribe({
        next: () => this.router.navigate(['/organizacionSocial']),
        error: (err) => {
          // Si el backend devuelve { error: 'mensaje' }
          this.errorMensaje = err.error?.error || 'Error inesperado al guardar la OrgaSocial.';
        }
      });
    }
    
  }

  
  

  