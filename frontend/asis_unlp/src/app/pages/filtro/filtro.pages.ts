// import { Component, OnInit } from '@angular/core';
// import { FiltroService } from '../../services/filtro.service'; 
// import { BarriosService } from '../../services/barrios.service';
// import { Filtro } from '../../models/filtro.model'; 
// import { Barrio } from '../../models/barrio.model';
// import { ActivatedRoute, Router, RouterModule } from '@angular/router';
// import { CommonModule } from '@angular/common';
// import { ListarFiltros } from '../../components/filtro/listar-filtro'; 
// import { Observable } from 'rxjs';
// import { FormsModule } from '@angular/forms';
// import { FormFiltro } from '../../components/filtro/form-filtro';



// @Component({
//   standalone: true,
//   imports: [CommonModule, RouterModule, ListarFiltros],
//   template: `
//     <h2>Filtros</h2>
//     <listar-filtros 
//       [filtros]="(filtros$ | async) ?? []"
//       (onEdit)="editarFiltro($event)"
//       (onDelete)="borrarFiltro($event)">
//     </listar-filtros>
//     <button (click)="nuevaFiltro()" class="btn-add">Agregar Filtro</button>
//   `,
//   styles: [`
//     .btn-add {
//       margin-bottom: 20px;
//       padding: 8px 16px;
//       background: #4CAF50;
//       color: white;
//       border: none;
//       border-radius: 4px;
//       cursor: pointer;
//     }
//   `]
// })
// export class ListarFiltroPage implements OnInit {
//   filtros$!: Observable<Filtro[]>;

//   constructor(
//     private filtrosService: FiltroService,
//     private router: Router
//   ) {}

//   ngOnInit(): void {
//     this.cargarFiltros();
//   }

//   cargarFiltros() {
//     this.filtros$ = this.filtrosService.getFiltros();
//   }

//   nuevaFiltro() {
//     this.router.navigate(['/filtro/nueva']);
//   }

//   editarFiltro(id: number) {
//     this.router.navigate(['/filtro/editar', id]);
//   }

//   borrarFiltro(id: number) {
//     if (confirm('¿Borrar filtro?')) {
//       this.filtrosService.deleteFiltro(id).subscribe({
//         next: () => this.cargarFiltros(),
//         error: (err) => console.error('Error al borrar:', err)
//       });
//     }
//   }
// }


// @Component({
//     standalone: true,
//     imports: [CommonModule, FormsModule,FormFiltro],
//     template: `
//     <h2>{{ esEdicion ? 'Editar Filtro' : 'Nueva Filtro' }}</h2>
    
//     <div *ngIf="errorMensaje" class="error-box">
//       {{ errorMensaje }}
//     </div>


//     <ng-container *ngIf="!loading; else cargando">
//       <form-filtro 
//       [barrios]="barrios"
//       [filtro]="filtro" 
//       (onSubmit)="guardarFiltro($event)">
//       </form-filtro>
//     </ng-container>
//     <ng-template #cargando>
//       <p>Cargando filtro...</p>
//     </ng-template>
//   `
//   })

//   export class FormFiltroPage implements OnInit {
//     filtro : Filtro = { id: 0, nombre : '',geolocalizacion: '', barrio_id : 0}
//     barrios: Barrio[] = [];
//     esEdicion = false;
//     loading = false;
//     errorMensaje: string | null = null;

  
//     constructor(
//       private filtroService: FiltroService,
//       private barrioService: BarriosService,
//       private router: Router,
//       private route: ActivatedRoute
//     ) {}
  
//     ngOnInit(): void {

//     // Cargar barrios
//     this.errorMensaje = null;
//     this.barrioService.getBarrios().subscribe({
//       next: (barrios) => this.barrios = barrios,
//       error: (err) => console.error('Error al cargar barrios:', err)
//     });



//       const id = this.route.snapshot.paramMap.get('id');
//       if (id) {
//         this.esEdicion = true;
//         this.loading = true;
//         this.filtroService.getFiltro(+id).subscribe({
//           next: (data) => {
//             this.filtro = data;
//             this.loading = false;
//           },
//           error: (err) => {
//             console.error('Error al cargar filtro:', err);
//             this.loading = false;
//           }
//         });
//       }
//     }


//     guardarFiltro(filtro: Filtro) {

//       const req = this.esEdicion
//         ? this.filtroService.updateFiltro(filtro)
//         : this.filtroService.createFiltro(filtro);
  
//       req.subscribe({
//         next: () => this.router.navigate(['/filtro']),
//         error: (err) => {
//           // Si el backend devuelve { error: 'mensaje' }
//           this.errorMensaje = err.error?.error || 'Error inesperado al guardar la filtro.';
//         }
//       });
//     }
    
//   }

  
  

  