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
import { log } from 'console';


@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, ListarZonas],
  template: `
    <h2>Zonas</h2>
    <listar-zonas 
      [zonas]="(zonas$ | async) ?? []"
      (onEdit)="editarZona($event)"
      (onDelete)="borrarZona($event)">
    </listar-zonas>
    <button (click)="nuevaZona()" class="btn-add">Agregar Zona</button>
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
export class ListarZonaPage implements OnInit {
  zonas$!: Observable<Zona[]>;

  constructor(
    private zonasService: ZonaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarZonas();
  }

  cargarZonas() {
    this.zonas$ = this.zonasService.getZonas();
  }

  nuevaZona() {
    this.router.navigate(['/zona/nueva']);
  }

  editarZona(id: number) {
    this.router.navigate(['/zona/editar', id]);
  }

  borrarZona(id: number) {
    if (confirm('¿Borrar zona?')) {
      this.zonasService.deleteZona(id).subscribe({
        next: () => this.cargarZonas(),
        error: (err) => console.error('Error al borrar:', err)
      });
    }
  }
}


@Component({
    standalone: true,
    imports: [CommonModule, FormsModule,FormZona],
    template: `
    <h2>{{ esEdicion ? 'Editar Zona' : 'Nueva Zona' }}</h2>
    
    <ng-container *ngIf="!loading; else cargando">
      <form-zona 
      [barrios]="barrios"
      [zona]="zona" 
      (onSubmit)="guardarZona($event)">
      </form-zona>
    </ng-container>
    <ng-template #cargando>
      <p>Cargando zona...</p>
    </ng-template>
  `
  })

  export class FormZonaPage implements OnInit {
    zona : Zona = { id: 0, nombre : '',geolocalizacion: '', barrio_id : 0}
    barrios: Barrio[] = [];
    esEdicion = false;
    loading = false;

  
    constructor(
      private zonaService: ZonaService,
      private barrioService: BarriosService,
      private router: Router,
      private route: ActivatedRoute
    ) {}
  
    ngOnInit(): void {

    // Cargar barrios
    this.barrioService.getBarrios().subscribe({
      next: (barrios) => this.barrios = barrios,
      error: (err) => console.error('Error al cargar barrios:', err)
    });



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
            console.error('Error al cargar zona:', err);
            this.loading = false;
          }
        });
      }
    }


    guardarZona(zona: Zona) {
      console.log(zona);
      const req = this.esEdicion
        ? this.zonaService.updateZona(zona)
        : this.zonaService.createZona(zona);
  
      req.subscribe({
        next: () => this.router.navigate(['/zona']),
        error: (err) => console.error('Error al guardar zona:', err)
      });
    }
    
  }

  
  

  