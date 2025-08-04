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
  imports: [CommonModule, ListarPedidosComponent, RouterModule, ReporteInputPedidoComponent],
  template: `
    <div class="page-container">
      <div class="page-header">
        <ng-container *ngIf="auth.rolReferente()">
          <h1 class="page-title">Mis Pedidos de Reporte</h1>
          <button (click)="nuevoPedido()" class="btn btn-create">Crear Pedido</button>
        </ng-container>
        <ng-container *ngIf="auth.rolAdmin()">
          <h1 class="page-title">Todos los Pedidos de Reporte</h1>
          <button (click)="nuevoPedido()" class="btn btn-create">Crear Pedido</button>
        </ng-container>
        <ng-container *ngIf="auth.rolSalud()">
          <h1 class="page-title">Pedidos de Reporte Tomados</h1>
        </ng-container>
      </div>

      <div *ngIf="errorMensaje" class="error-box">
        {{ errorMensaje }}
      </div>

      <ng-container *ngIf="auth.rolSalud()">
        <div class="content-container">
          <listar-pedidos
            [pedidos]="(pedidosTomados$ | async) || []"
            [showCompleteDialog]="showCompleteDialog"
            [reportes]="reportes"
            (take)="onTakePedido($event)"
            (complete)="openCompleteDialog($event)"
            (download)="onDownloadReporte($event)"
            (closeDialog)="closeDialog()"
            (completeWithReporte)="onCompleteWithReporte($event)"
            (release)="onSoltar($event)">
          </listar-pedidos>
        </div>
        <hr />
        <div class="page-header">
          <h1 class="page-title">Pedidos Pendientes</h1>
        </div>
      </ng-container>

      <div class="content-container">
        <listar-pedidos
          [pedidos]="(pedidos$ | async) || []"
          [showCompleteDialog]="showCompleteDialog"
          [reportes]="reportes"
          (take)="onTakePedido($event)"
          (complete)="openCompleteDialog($event)"
          (download)="onDownloadReporte($event)"
          (closeDialog)="closeDialog()"
          (completeWithReporte)="onCompleteWithReporte($event)"
          (release)="onSoltar($event)">
        </listar-pedidos>
      </div>

      @if (showCompleteDialog) {
        <reporte-input-dialog-pedido
          [reportes]="reportes"
          (saved)="onCompleteWithReporte($event)"
          (closed)="closeDialog()">
        </reporte-input-dialog-pedido>
      }
    </div>
  `,
  styleUrls: ['../../../styles.css']
})
export class ListaPedidosPage implements OnInit {

  pedidos$: Observable<any[]> = new Observable();
  pedidosTomados$: Observable<any[]> = new Observable();
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
    this.cargarPedidos();
  }

  cargarPedidos() {
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
      this.pedidosTomados$ = this.pedidoService.getPedidosTomadosPorUsuario(this.id);
      this.pedidos$ = this.pedidoService.getPedidosPendientes();
    } else if (this.authService.rolAdmin()) {
      this.pedidos$ = this.pedidoService.getPedidos();
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

  onTakePedido(pedidoId: number) {
    if (!this.id) {
      this.router.navigate(['/login']);
      return;
    }
    this.pedidoService.tomarPedido(pedidoId, this.id).subscribe({
      next: () => {
        this.cargarPedidos();
        this.errorMensaje = null;
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

  onSoltar(pedidoId: number) {
    this.pedidoService.soltarPedido(pedidoId).subscribe({
      next: () => {
        this.cargarPedidos();
        this.errorMensaje = null;
      },
      error: (err: any) => {
        console.error('Error soltando pedido', err);
        this.errorMensaje = 'Error al soltar el pedido';
      }
    });
  }

  openCompleteDialog(pedidoId: number) {
    this.selectedPedidoId = pedidoId;
    const userId = this.auth.getUsuarioId();
    
    if (!userId) {
      this.router.navigate(['/login']);
      return;
    }

    this.reporteService.getReportesByUserId(userId).subscribe({
      next: (data) => {
        this.reportes = data;
        this.showCompleteDialog = true;
      },
      error: (err) => console.error('Error cargando reportes', err)
    });
  }

  onCompleteWithReporte(data: {reporteId: number, comentario?: string}) {
    if (!this.selectedPedidoId) return;

    this.pedidoService.completarPedido(
      this.selectedPedidoId, 
      data.reporteId,
      data.comentario
    ).subscribe({
      next: () => {
        this.closeDialog();
        this.cargarPedidos();
      },
      error: (err) => console.error('Error completando pedido', err)
    });
  }

  closeDialog() {
    this.showCompleteDialog = false;
    this.selectedPedidoId = null;
  }
}

