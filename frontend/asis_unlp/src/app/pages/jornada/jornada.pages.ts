import { Component, OnInit } from '@angular/core';
import { JornadaService } from '../../services/jornada.service';
import { Jornada } from '../../models/jornada.model';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ListarJornadas } from '../../components/jornada/listar-jornadas'; 
import { Observable,catchError,lastValueFrom } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { FormJornada, FormAdministrarZonas } from '../../components/jornada/form-jornada';
import { log } from 'console';
import { Zona } from '../../models/zona.model';
import { CampaniaService } from '../../services/campanias.service'
import { ZonaService } from '../../services/zonas.service'

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, ListarJornadas],
  template: `
    <div class="page-container">
      <div class="page-header">
        <h1>Jornadas de Campaña {{ idCampania }}</h1>
        <button [routerLink]="['nuevo']" class="btn btn-create">Agregar Jornada</button>
      </div>

      <div *ngIf="errorMensaje" class="error-box">
        {{ errorMensaje }}
      </div>

      <div class="content-container">
        <listar-jornadas
          [jornadas]="(jornadas$ | async) ?? []"
          (onAdministrarZonas)="administrarZonas($event)"
          (onEdit)="editarJornada($event)"
          (onDelete)="borrarJornada($event)">
        </listar-jornadas>
      </div>
    </div>
  `
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

  administrarZonas(id: number) {
    this.router.navigate(['campania', this.idCampania, 'jornadas', 'administrarZonas', id]);
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
    <div class="form-container">
      <div class="title">
        <h2>{{ esEdicion ? 'Editar Jornada ' + jornada.id + ' de Campaña' + idCampania : 'Nueva jornada para Campaña ' + idCampania }}</h2>
      </div>

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
    </div>
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



  @Component({
    standalone: true,
    imports: [CommonModule, FormAdministrarZonas],
    template: `
    <div class="form-container">
      <div class="title">
        <h2>Administrar Zonas de la Jornada</h2>
      </div>
  
      <div *ngIf="errorMensaje" class="error-box">
        {{ errorMensaje }}
      </div>
      
      <form-administrar-zonas
        [zonas]="(zonasDisponibles$ | async) ?? []"
        (onSubmit)="guardarZonasDesdeForm($event)">
      </form-administrar-zonas>
    </div>
    `,
  })
  export class AdministrarZonasPage implements OnInit {
    zonasDisponibles$!: Observable<(Zona & { seleccionada?: boolean })[]>;
    errorMensaje: string | null = null;
    idCampania!: number;
    idJornada!: number;
  
    constructor(
      private route: ActivatedRoute,
      private router: Router,
      private jornadaService: JornadaService,
      private campaniaService: CampaniaService,
      private zonaService: ZonaService
    ) {}
  
    ngOnInit(): void {
      this.idCampania = +this.route.snapshot.paramMap.get('idCampania')!;
      this.idJornada = +this.route.snapshot.paramMap.get('idJornada')!;
      this.cargarDatos();
    }
  
    cargarDatos() {
      this.zonasDisponibles$ = new Observable<(Zona & { seleccionada?: boolean })[]>(observer => {
        this.jornadaService.getJornada(this.idJornada).subscribe({
          next: (jornada) => {
            this.campaniaService.getCampania(this.idCampania).subscribe({
              next: (campania) => {
                this.zonaService.getZonasPorBarrio(campania.barrio_id).subscribe({
                  next: (zonasBarrio) => {
                    const zonasAsociadas = jornada.zonas ?? [];
                    const zonasConSeleccion = zonasBarrio.map(z => ({
                      ...z,
                      seleccionada: zonasAsociadas.some(za => za.id === z.id)
                    }));
                    observer.next(zonasConSeleccion);
                    observer.complete();
                  },
                  error: (error) => {
                    this.errorMensaje = 'Error al cargar zonas del barrio.';
                    observer.error(error);
                  }
                });
              },
              error: (error) => {
                this.errorMensaje = 'Error al cargar campaña.';
                observer.error(error);
              }
            });
          },
          error: (error) => {
            this.errorMensaje = 'Error al cargar jornada.';
            observer.error(error);
          }
        });
      }).pipe(
        catchError(error => {
          // Si ocurre un error, devolvemos un array vacío
          return [];
        })
      );
    }
  
    
  

    guardarZonasDesdeForm(zonas: (Zona & { seleccionada?: boolean })[]) {
      this.jornadaService.getJornada(this.idJornada).subscribe({
        next: (jornada) => {
          const zonasSeleccionadas = zonas.filter(z => z.seleccionada).map(z => z.id);
          const zonasActuales = jornada.zonas?.map(z => z.id) || [];
          
          const zonasAAgregar = zonasSeleccionadas.filter(id => !zonasActuales.includes(id));
          const zonasAQuitar = zonasActuales.filter(id => !zonasSeleccionadas.includes(id));
          
          const operaciones: Promise<any>[] = [];
          
          zonasAAgregar.forEach(idZona => {
            operaciones.push(lastValueFrom(this.jornadaService.agregarZona(this.idJornada, idZona)));
          });
          
          zonasAQuitar.forEach(idZona => {
            operaciones.push(lastValueFrom(this.jornadaService.quitarZona(this.idJornada, idZona)));
          });
          
          Promise.all(operaciones)
            .then(() => {
              this.router.navigate(['/campania', this.idCampania, 'jornadas']);
            })
            .catch((error) => {
              this.errorMensaje = error.error?.error || 'Error al guardar cambios de zonas.';
            });
        },
        error: (error) => {
          this.errorMensaje = error.error?.error || 'Error al cargar jornada.';
        }
      });
    }
  }