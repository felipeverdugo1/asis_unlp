import { Component, OnInit } from '@angular/core';
import { JornadaService } from '../../services/jornada.service';
import { Jornada } from '../../models/jornada.model';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ListarJornadas } from '../../components/jornada/listar-jornadas'; 
import { Observable } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { FormJornada, FormActualizarJornada } from '../../components/jornada/form-jornada';
import { log } from 'console';


@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, ListarJornadas],
  template: `
    <h2>Jornadas de Campaña {{ idCampania }}</h2>

    <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>

    <listar-jornadas
      [jornadas]="(jornadas$ | async) ?? []"
      (onEdit)="editarJornada($event)"
      (onDelete)="borrarJornada($event)">
    </listar-jornadas>
    <button [routerLink]="['nuevo']" class="btn-add">
        Nueva Jornada
    </button>
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
export class ListarJornadaPage implements OnInit {
  jornadas$!: Observable<Jornada[]>;
  idCampania!: number;
  errorMensaje: string | null = null;

  constructor(
    private jornadaService: JornadaService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.idCampania = +this.route.snapshot.paramMap.get('idCampania')!;
    this.cargarJornadas();
    this.errorMensaje = null;
  }

  cargarJornadas() {
    this.jornadas$ = this.jornadaService.getJornadasPorCampania(this.idCampania);
  }

  //nuevaJornada() {
  //  this.router.navigate(['/jornada/nueva']);
  //}

  editarJornada(id: number) {
    this.router.navigate(['campania', this.idCampania, 'jornadas', 'editar', id]);
  }

  borrarJornada(id: number) {
    if (confirm('¿Borrar jornada?')) {
      this.jornadaService.deleteJornada(id).subscribe({
        next: () => this.cargarJornadas(),
        error: (err) => this.errorMensaje = err.error?.error || 'Error inesperado al borrar la jornada.'
      });
    }
  }
}


@Component({
    standalone: true,
    imports: [CommonModule, FormsModule, FormJornada],
    template: `
    <h2>{{ esEdicion ? 'Editar Jornada ' + jornada.id + ' de Campaña' + idCampania : 'Nueva jornada para Campaña ' + idCampania }}</h2>

    <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>

    <ng-container *ngIf="!loading; else cargando">
      <form-jornada
        [jornada]="jornada"
      (onSubmit)="guardarJornada($event)">
      </form-jornada>
    </ng-container>
    <ng-template #cargando>
      <p>Cargando campaña...</p>
    </ng-template>
  `
  })

  export class FormJornadaPage implements OnInit {
    jornada: Jornada = { id: 0, fechaInicio: '', fechaFin: '', campaña_id: 0 };
    idCampania!: number;
    esEdicion = false;
    loading = false;
    errorMensaje: string | null = null;
  
    constructor(
      private jornadaService: JornadaService,
      private router: Router,
      private route: ActivatedRoute
    ) {}
  
    ngOnInit(): void {
    this.idCampania = +this.route.snapshot.paramMap.get('idCampania')!;
    this.errorMensaje = null;

    // Cargar zonas
    //this.zonaService.getZonas().subscribe({
    //  next: (zonas) => this.zonas = zonas,
    //  error: (err) => console.error('Error al cargar zonas:', err)
    //});

      const id = this.route.snapshot.paramMap.get('idJornada');
      if (id) {
        this.esEdicion = true;
        this.loading = true;
        this.jornadaService.getJornada(+id).subscribe({
          next: (data) => {
            console.log('Jornada cargada:', data);
            this.jornada = data;
            this.loading = false;
          },
          error: (err) => {
            this.errorMensaje = err.error?.error || 'Error inesperado al cargar la jornada.';
            this.loading = false;
          }
        });
      }
    }


    guardarJornada(jornada: Jornada) {
      console.log(jornada);
      if (!this.esEdicion) {
        jornada.campaña_id = this.idCampania;
      }

      const req = this.esEdicion
        ? this.jornadaService.updateJornada(jornada)
        : this.jornadaService.createJornada(jornada);

      req.subscribe({
        next: () => this.router.navigate(['/campania', this.idCampania, 'jornadas']),
        error: (err) => {
          this.errorMensaje = err.error?.error || 'Error inesperado al guardar la jornada.';
        }
      });
    }
    
  }