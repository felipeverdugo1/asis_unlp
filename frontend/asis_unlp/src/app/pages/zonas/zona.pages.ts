import { Component, OnInit } from '@angular/core';
import { ZonaService } from '../../services/zonas.service'; 
import { BarriosService } from '../../services/barrios.service';
import { Zona } from '../../models/zona.model'; 
import { Barrio } from '../../models/barrio.model';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ListarZonas } from '../../components/zona/listar-zona'; 
import { Observable } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { FormZona } from '../../components/zona/form-zona';



@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, ListarZonas],
  template: `
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">Zonas</h1>
      <button [routerLink]="['nueva']" class="btn btn-create">Agregar Zona</button>
    </div>

    <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>
    
    <div class="content-container">
      <listar-zonas 
        [zonas]="(zonas$ | async) ?? []"
        (onEdit)="editarZona($event)"
        (onDelete)="borrarZona($event)">
      </listar-zonas>
    </div>
  </div>
  `
})
export class ListarZonaPage implements OnInit {
  zonas$!: Observable<Zona[]>;
  idBarrio!: number;
  errorMensaje: string | null = null;

  constructor(
    private zonasService: ZonaService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.idBarrio = +this.route.snapshot.paramMap.get('idBarrio')!;
    this.cargarZonas();
    this.errorMensaje = null;
  }

  cargarZonas() {
    this.zonas$ = this.zonasService.getZonasPorBarrio(this.idBarrio);
  }

  editarZona(id: number) {
    this.router.navigate(['/barrio', this.idBarrio, 'zonas', 'editar', id]);
  }

  borrarZona(id: number) {
    if (confirm('¿Borrar zona?')) {
      this.zonasService.deleteZona(id).subscribe({
        next: () => this.cargarZonas(),
        error: (err) => this.errorMensaje = err.error?.error || 'Error inesperado al borrar la zona.'
      });
    }
  }
}


@Component({
    standalone: true,
    imports: [CommonModule, FormsModule,FormZona],
    template: `
    <div class="form-container">
      <div class="title">
        <h1>{{ esEdicion ? 'Editar Zona ' + zona.nombre + ' de Barrio' + idBarrio : 'Nueva Zona para Barrio ' + idBarrio }}</h1>
      </div>
    
    <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>


    <ng-container *ngIf="!loading; else cargando">
      <form-zona 
      [zona]="zona" 
      (onSubmit)="guardarZona($event)">
      </form-zona>
    </ng-container>
    <ng-template #cargando>
      <p>Cargando zona...</p>
    </ng-template>
    </div>
  `
  })

  export class FormZonaPage implements OnInit {
    zona : Zona = { id: 0, nombre : '',geolocalizacion: '', barrio_id : 0}
    idBarrio!: number;
    esEdicion = false;
    loading = false;
    errorMensaje: string | null = null;

  
    constructor(
      private zonaService: ZonaService,
      private barrioService: BarriosService,
      private router: Router,
      private route: ActivatedRoute
    ) {}
  
    ngOnInit(): void {
      this.idBarrio = +this.route.snapshot.paramMap.get('idBarrio')!;
      this.errorMensaje = null;

      const id = this.route.snapshot.paramMap.get('id');
      if (id) {
        this.esEdicion = true;
        this.loading = true;
        this.zonaService.getZona(+id).subscribe({
          next: (data) => {
            this.zona = data;
            this.loading = false;
          },
          error: (err) => {
            this.errorMensaje = err.error?.error || 'Error inesperado al cargar la zona.';
            this.loading = false;
          }
        });
      }
    }


    guardarZona(zona: Zona) {
      if (!this.esEdicion) {
        zona.barrio_id = this.idBarrio;
      }

      const req = this.esEdicion
        ? this.zonaService.updateZona(zona)
        : this.zonaService.createZona(zona);
  
      req.subscribe({
        next: () => this.router.navigate(['/barrio', this.idBarrio, 'zonas']),
        error: (err) => {
          this.errorMensaje = err.error?.error || 'Error inesperado al guardar la zona.';
        }
      });
    }
    
  }

  
  

  