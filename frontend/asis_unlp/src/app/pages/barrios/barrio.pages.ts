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

  constructor(
    private barriosService: BarriosService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.barrios$ = this.barriosService.getBarrios();
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
        error: (err) => console.error('Error al borrar:', err)
      });
    }
  }
}



@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, FormBarrio],
  template: `
    <h2>{{ esEdicion ? 'Editar Barrio' : 'Nuevo Barrio' }}</h2>
    
    <ng-container *ngIf="!loading; else cargando">
      <form-barrio [barrio]="barrio" (onSubmit)="guardarBarrio($event)"></form-barrio>
    </ng-container>
    <ng-template #cargando>
      <p>Cargando barrio...</p>
    </ng-template>
  `
})
export class FormBarrioPage implements OnInit {
  barrio: Barrio = { nombre: '', geolocalizacion: '', informacion: '' };
  esEdicion = false;
  loading = false;

  constructor(
    private barriosService: BarriosService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
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
          console.error('Error al cargar barrio:', err);
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
      error: (err) => console.error('Error al guardar barrio:', err)
    });
  }
}