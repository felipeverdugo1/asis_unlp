import { Component, OnInit } from '@angular/core';
import { BarriosService } from '../../services/barrios.service';
import { Barrio, BarrioForm } from '../../models/barrio.model';
import { ActivatedRoute,Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';
import { ListarBarrios } from '../../components/barrio/listar-barrios';
import { FormBarrio } from '../../components/barrio/form-barrio';
import { Observable } from 'rxjs';

@Component({
  standalone :true,
  imports : [CommonModule,RouterModule,ListarBarrios],
  template: `
    <h2>Barrios</h2>

    <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>

    <listar-barrios
      [barrios]="(barrios$ | async) ?? []"
      (onEdit)="editarBarrio($event)"
      (onDelete)="borrarBarrio($event)">
    </listar-barrios>
    <button (click)="nuevoBarrio()" class="btn-add"> Agregar Barrio</button>

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
export class ListaBarriosPage implements OnInit {
  barrios$!: Observable<Barrio[]>;
  errorMensaje: string | null = null;

  constructor(
    private barriosService: BarriosService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.barrios$ = this.barriosService.getBarrios();
    this.errorMensaje = null;
  }

  cargarBarrios() {
    this.barrios$ = this.barriosService.getBarrios();
  }

  nuevoBarrio() {
    this.router.navigate(['/barrio/nuevo']);
  }

  editarBarrio(id: number) {
    this.router.navigate(['/barrio/editar', id]);
  }

  borrarBarrio(id: number) {
    if (confirm('¿Borrar barrio?')) {
      this.barriosService.deleteBarrio(id).subscribe({
        next: () => this.cargarBarrios(),
        error: (err) => this.errorMensaje = err.error?.error || 'Error inesperado al borrar el barrio.'
      });
    }
  }
}



@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, FormBarrio],
  template: `
    <h2>{{ esEdicion ? 'Editar Barrio' : 'Nuevo Barrio' }}</h2>

    <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>
    
    <ng-container *ngIf="!loading; else cargando">
      <form-barrio [barrio]="barrio" (onSubmit)="guardarBarrio($event)"></form-barrio>
    </ng-container>
    <ng-template #cargando>
      <p>Cargando barrio...</p>
    </ng-template>
  `
})
export class FormBarrioPage implements OnInit {
  barrio: Barrio = { id : 0 ,nombre: '', geolocalizacion: '', informacion: '' };
  esEdicion = false;
  loading = false;
  errorMensaje: string | null = null;

  constructor(
    private barriosService: BarriosService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.errorMensaje = null;
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.esEdicion = true;
      this.loading = true;
      this.barriosService.getBarrio(+id).subscribe({
        next: (data) => {
          this.barrio = data;
          this.loading = false;
        },
        error: (err) => {
          this.errorMensaje = err.error?.error || 'Error inesperado al cargar el barrio.';
          this.loading = false;
        }
      });
    }
  }

  guardarBarrio(barrio: Barrio) {
    const req = this.esEdicion
      ? this.barriosService.updateBarrio(barrio)
      : this.barriosService.createBarrio(barrio);

    req.subscribe({
      next: () => this.router.navigate(['/barrio']),
      error: (err) => this.errorMensaje = err.error?.error || 'Error inesperado al guardar el barrio.'
    });
  }
}