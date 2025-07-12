import { Component, OnInit } from '@angular/core';
import { EncuestadorService } from '../../services/encuestador.service'; 
import { BarriosService } from '../../services/barrios.service';
import { Encuestador} from '../../models/encuestador.model'; 
import { Barrio } from '../../models/barrio.model';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ListarEncuestadores } from '../../components/encuestador/listar-encuestador'; 
import { Observable } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { FormEncuestador } from '../../components/encuestador/form-encuestador';



@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, ListarEncuestadores],
  template: `
    <h2>Encuestadores</h2>

    <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>

    <listar-encuestadores 
      [encuestadores]="(encuestadores$ | async) ?? []"
      (onEdit)="editarEncuestador($event)"
      (onDelete)="borrarEncuestador($event)">
    </listar-encuestadores>
    <button (click)="nuevoEncuestador()" class="btn-add">Agregar encuestador</button>
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
export class ListarEncuestadorPage implements OnInit {
  encuestadores$!: Observable<Encuestador[]>;
  errorMensaje: string | null = null;

  constructor(
    private encuestadoresService: EncuestadorService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarEncuestadores();
    this.errorMensaje = null;
  }

  cargarEncuestadores() {
    this.encuestadores$ = this.encuestadoresService.getEncuestadores();
  }

  nuevoEncuestador() {
    this.router.navigate(['/encuestador/nuevo']);
  }

  editarEncuestador(id: number) {
    this.router.navigate(['/encuestador/editar', id]);
  }

  borrarEncuestador(id: number) {
    if (confirm('¿Borrar encuestador?')) {
      this.encuestadoresService.deleteEncuestador(id).subscribe({
        next: () => this.cargarEncuestadores(),
        error: (err) => this.errorMensaje = err.error?.error || 'Error inesperado al borrar el encuestador.'
      });
    }
  }
}


@Component({
    standalone: true,
    imports: [CommonModule, FormsModule,FormEncuestador],
    template: `
    <h2>{{ esEdicion ? 'Editar encuestador' : 'Nueva encuestador' }}</h2>
    
    <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>

    <ng-container *ngIf="!loading; else cargando">
      <form-encuestador 
      [encuestador]="encuestador" 
      (onSubmit)="guardarEncuestador($event)">
      </form-encuestador>
    </ng-container>
    <ng-template #cargando>
      <p>Cargando encuestador...</p>
    </ng-template>
  `
  })

  export class FormEncuestadorPage implements OnInit {
    encuestador : Encuestador = { id: 0, nombre : '',dni: '', edad : 0 , ocupacion : '' ,genero : ''}
    esEdicion = false;
    loading = false;
    errorMensaje: string | null = null;

  
    constructor(
      private encuestadorService: EncuestadorService,
      private router: Router,
      private route: ActivatedRoute
    ) {}
  
    ngOnInit(): void {


      const id = this.route.snapshot.paramMap.get('id');
      if (id) {
        this.esEdicion = true;
        this.loading = true;
        this.encuestadorService.getEncuestador(+id).subscribe({
          next: (data) => {
            this.encuestador = data;
            this.loading = false;
          },
          error: (err) => {
            this.errorMensaje = err.error?.error || 'Error inesperado al cargar el encuestador.';
            this.loading = false;
          }
        });
      }
    }


    guardarEncuestador(encuestador: Encuestador) {

      const req = this.esEdicion
        ? this.encuestadorService.updateEncuestador(encuestador)
        : this.encuestadorService.createEncuestador(encuestador);
  
      req.subscribe({
        next: () => this.router.navigate(['/encuestador']),
        error: (err) => {
          // Si el backend devuelve { error: 'mensaje' }
          this.errorMensaje = err.error?.error || 'Error inesperado al guardar la encuestador.';
        }
      });
    }
    
  }

  
  

  