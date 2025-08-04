import { Component, inject } from "@angular/core";
import { CommonModule } from "@angular/common";
import { OnInit } from "@angular/core";
import { Router, RouterModule } from "@angular/router";
import { ReporteService } from "../../services/reporte.service";
import { ListarPedidosComponent, ReporteInputPedidoComponent } from "../../components/reporte/pedido-reporte";
import { PedidoService } from "../../services/pedido.service";
import { Observable } from "rxjs";
import { AuthService } from "../../services/auth.service";

@Component({
  standalone: true,
  imports: [CommonModule, ListarPedidosComponent, ReporteInputPedidoComponent],
  template: `
    <div class="page-container">
      <div class="page-header">
        <ng-container *ngIf="auth.rolReferente()">
          <h1 class="page-title">Mis Pedidos de Reporte</h1>
          <button (click)="nuevoPedido()" class="btn btn-create">Crear Pedido</button>
        </ng-container>
        <ng-container *ngIf="auth.rolSalud()">
          <h1 class="page-title">Pedidos de Reporte Pendientes</h1>
        </ng-container>
        <ng-container *ngIf="auth.rolAdmin()">
          <h1 class="page-title">Todos los Pedidos de Reporte</h1>
          <button (click)="nuevoPedido()" class="btn btn-create">Crear Pedido</button>
        </ng-container>
      </div>

      <div *ngIf="errorMensaje" class="error-box">
        {{ errorMensaje }}
      </div>

      <div class="content-container">
        <listar-pedidos
          [pedidos]="(pedidos$ | async) || []"
          [showCompleteDialog]="showCompleteDialog"
          [selectedPedidoId]="selectedPedidoId"
          (onTake)="onTakePedido($event)"
          (onComplete)="onCompletePedido($event)"
          (onDownload)="onDownloadReporte($event)"
          (onCloseDialog)="closeDialog()">
        </listar-pedidos>
      </div>
    </div>
  `,
  styleUrls: ['../../../styles.css']
})
export class ListaPedidosPage implements OnInit {

  pedidos$: Observable<any[]> = new Observable();
  reportes: any[] = [];
  errorMensaje: string | null = null;
  id: number | null = null;
  
  // Estado para controlar el modal
  showCompleteDialog = false;
  selectedPedidoId: number | null = null;

  constructor(
    private pedidoService: PedidoService,
    private reporteService: ReporteService,
    private router: Router,
    private authService: AuthService
  ) {}

  auth = inject(AuthService);

  ngOnInit(): void {
    if (!this.authService.loggedIn()) {
      this.router.navigate(['/login']);
      return;
    }
    this.id = this.authService.getUsuarioId();
    if (!this.id) {
      this.errorMensaje = 'Usuario no encontrado';
      return;
    }
    if (this.authService.rolReferente()) {
      this.pedidos$ = this.pedidoService.getPedidosReferente(this.id);
    } else if (this.authService.rolSalud()) {
      this.loadReportes();
      this.pedidos$ = this.pedidoService.getPedidosPendientes();
    } else if (this.authService.rolAdmin()) {
      this.pedidos$ = this.pedidoService.getPedidos();
      this.loadReportes();
    }
  }

  loadReportes() {
    if (!this.id) {
      this.router.navigate(['/login']);
      return;
    }
    this.reporteService.getReportesByUserId(this.id).subscribe({
      next: (reportes) => this.reportes = reportes,
      error: (err) => console.error('Error cargando reportes', err)
    });
  }

  onCompletePedido(pedidoId: number) {
    this.selectedPedidoId = pedidoId;
    this.showCompleteDialog = true;
  }

  onCompleteWithReporte(reporteId: number) {
    if (!this.selectedPedidoId) return;

    this.pedidoService.completarPedido(this.selectedPedidoId, reporteId).subscribe({
      next: () => {
        this.showCompleteDialog = false;
        this.selectedPedidoId = null;
        this.pedidos$ = this.pedidoService.getPedidos();
      },
      error: (err: any) => {
        console.error('Error completando pedido', err);
        this.errorMensaje = 'Error al completar el pedido';
      }
    });
  }

  onTakePedido(pedidoId: number) {
    if (!this.id) {
      this.router.navigate(['/login']);
      return;
    }
    this.pedidoService.tomarPedido(pedidoId, this.id).subscribe({
      next: () => {
        // Recargar los pedidos
        this.pedidos$ = this.pedidoService.getPedidos();
      },
      error: (err: any) => {
        console.error('Error tomando pedido', err);
        this.errorMensaje = 'Error al tomar el pedido';
      }
    });
  }

  onDownloadReporte(pedidoId: number) {
    this.pedidoService.descargarReporte(pedidoId);
  }

  nuevoPedido() {
    this.router.navigate(['/pedidos/nuevo']);
  }

  closeDialog() {
    this.showCompleteDialog = false;
    this.selectedPedidoId = null;
  }
}