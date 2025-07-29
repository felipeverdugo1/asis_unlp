import { Component, OnInit } from '@angular/core';
import { CampaniaService } from '../../services/campanias.service'; 
import { BarriosService } from '../../services/barrios.service';
import { Campania } from '../../models/campania.model'; 
import { Barrio } from '../../models/barrio.model';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ListarCampanias } from '../../components/campania/listar-campania'; 
import { Observable } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { FormCampania, FormActualizarCampania } from '../../components/campania/form-campania';
import { log } from 'console';


@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, ListarCampanias],
  template: `

    <div class="page-container">
      <div class="page-header">
        <h1 class="page-title">Campañas</h1>
        <button (click)="nuevaCampania()" class="btn btn-create">Agregar campaña</button>
      </div>

      <div *ngIf="errorMensaje" class="error-box">
        {{ errorMensaje }}
      </div>

      <div class="content-container">
        <listar-campanias 
          [campanias]="(campanias$ | async) ?? []"
          (onEdit)="editarCampania($event)"
          (onDelete)="borrarCampania($event)">
        </listar-campanias>
      </div>
    </div>
  `
})
export class ListarCampaniaPage implements OnInit {
  campanias$!: Observable<Campania[]>;
  errorMensaje: string | null = null;

  constructor(
    private campaniasService: CampaniaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarCampanias();
    this.errorMensaje = null;
  }

  cargarCampanias() {
    this.campanias$ = this.campaniasService.getCampanias();
  }

  nuevaCampania() {
    this.router.navigate(['/campania/nueva']);
  }

  editarCampania(id: number) {
    this.router.navigate(['/campania/editar', id]);
  }

  borrarCampania(id: number) {
    if (confirm('¿Borrar campaña?')) {
      this.campaniasService.deleteCampania(id).subscribe({
        next: () => this.cargarCampanias(),
        error: (err) => this.errorMensaje = err.error?.error || 'Error inesperado al borrar la campaña.'
      });
    }
  }
}


@Component({
    standalone: true,
    imports: [CommonModule, FormsModule, FormCampania],
    template: `
    <div class="form-container">
      <div class="title">
        <h1>{{ esEdicion ? 'Editar campaña' : 'Nueva campaña' }}</h1>
      </div>

      <div *ngIf="errorMensaje" class="error-box">
        {{ errorMensaje }}
      </div>

      <ng-container *ngIf="!loading; else cargando">
        <form-campania
          [barrios]="barrios"
          [campania]="campania"
        (onSubmit)="guardarCampania($event)">
        </form-campania>
      </ng-container>
      <ng-template #cargando>
        <p>Cargando campaña...</p>
      </ng-template>
    </div>
  `
  })

  export class FormCampaniaPage implements OnInit {
    campania: Campania = { id: 0, nombre: '', fechaFin: '', fechaInicio: '', barrio_id: 0 };
    barrios: Barrio[] = [];
    esEdicion = false;
    loading = false;
    errorMensaje: string | null = null;
  
    constructor(
      private campaniaService: CampaniaService,
      private barrioService: BarriosService,
      private router: Router,
      private route: ActivatedRoute
    ) {}
  
    ngOnInit(): void {
      this.errorMensaje = null;
      // Cargar barrios
      this.barrioService.getBarrios().subscribe({
        next: (barrios) => this.barrios = barrios,
        error: (err) => this.errorMensaje = err.error?.error || 'Error inesperado al cargar los barrios.'
      });

      const id = this.route.snapshot.paramMap.get('id');
      if (id) {
        this.esEdicion = true;
        this.loading = true;
        this.campaniaService.getCampania(+id).subscribe({
          next: (data) => {
            this.campania = data;
            this.loading = false;
          },
          error: (err) => {
            this.errorMensaje = err.error?.error || 'Error inesperado al cargar la campaña.';
            this.loading = false;
          }
        });
      }
    }


    guardarCampania(campania: Campania) {
      console.log(campania);
      const req = this.esEdicion
        ? this.campaniaService.updateCampania(campania)
        : this.campaniaService.createCampania(campania);

      req.subscribe({
        next: () => this.router.navigate(['/campania']),
        error: (err) => this.errorMensaje = err.error?.error || 'Error inesperado al guardar la campaña.'
      });
    }
    
  }

  
  

  