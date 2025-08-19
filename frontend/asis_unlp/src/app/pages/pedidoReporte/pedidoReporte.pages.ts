import { ChangeDetectorRef, Component, inject } from "@angular/core";
import { CommonModule } from "@angular/common";
import { OnInit } from "@angular/core";
import { Router, RouterModule } from "@angular/router";
import { ReporteService } from "../../services/reporte.service";
import { ListarPedidosComponent, ReporteInputPedidoComponent } from "../../components/pedidoReporte/pedido-reporte";
import { PedidoService } from "../../services/pedido.service";
import { Observable } from "rxjs";
import { AuthService } from "../../services/auth.service";
import { FormsModule } from "@angular/forms";
import { FormPedido } from "../../components/pedidoReporte/form-pedido";
import { PedidoReporte } from "../../models/pedido.model";

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
            [downloading]="downloading"
            (take)="onTakePedido($event)"
            (complete)="openCompleteDialog($event)"
            (download)="onDownloadReporte($event)"
            (closeDialog)="closeDialog()"
            (completeWithReporte)="onCompleteWithReporte($event)"
            (release)="onSoltar($event)"
            (delete)="eliminarPedido($event)">
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
          [downloading]="downloading"
          (take)="onTakePedido($event)"
          (complete)="openCompleteDialog($event)"
          (download)="onDownloadReporte($event)"
          (closeDialog)="closeDialog()"
          (completeWithReporte)="onCompleteWithReporte($event)"
          (release)="onSoltar($event)"
          (delete)="eliminarPedido($event)">
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
  downloading: boolean = false;

  // Estado para controlar el modal
  showCompleteDialog = false;
  selectedPedidoId: number | null = null;

  constructor(
    private pedidoService: PedidoService,
    private reporteService: ReporteService,
    private router: Router,
    private authService: AuthService,
    private cdRef: ChangeDetectorRef
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
    this.cdRef.detectChanges(); 
    this.downloading = true;
    const pedido = this.pedidoService.getPedidoById(pedidoId).subscribe((pedido) => {
      this.reporteService.descargarPDF(pedido.reporte_id).subscribe({
        next: (blob: Blob) => {
          // Crear nombre de archivo con la fecha actual
          const fecha = new Date().toISOString().slice(0, 10);
          const nombreArchivo = `reporte_${pedido.reporte_id}_${fecha}.pdf`;

          // Descargar el archivo
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = nombreArchivo;
          document.body.appendChild(a);
          a.click();
          document.body.removeChild(a);
          window.URL.revokeObjectURL(url);
          this.downloading = false;
          this.cdRef.detectChanges(); 
        },
        error: (err: any) => {
          console.error('Error descargando PDF:', err);
          this.errorMensaje = 'Error descargando el PDF';
          this.downloading = false;
          this.cdRef.detectChanges(); 
        }
      });
    });
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

  eliminarPedido(pedidoId: number) {
    if (confirm('¿Está seguro de que desea eliminar este pedido? Esta acción no se puede deshacer.')) {
      this.pedidoService.eliminarPedido(pedidoId).subscribe({
        next: () => {
          this.cargarPedidos();
          this.errorMensaje = null;
        },
        error: (err: any) => {
          console.error('Error eliminando pedido', err);
          this.errorMensaje = 'Error al eliminar el pedido';
        }
      });
    }
  }
}

@Component({
    standalone: true,
    imports: [CommonModule, FormsModule, FormPedido],
    template: `
    <div class="form-container">
      <div class="title">
        <h1>Nuevo pedido de Reporte</h1>
      </div>
    
    <div *ngIf="errorMensaje" class="error-box">
      {{ errorMensaje }}
    </div>


    <ng-container *ngIf="!loading; else cargando">
      <form-pedido 
      [pedido]="pedido" 
      (onSubmit)="crearPedido($event)">
      </form-pedido>
    </ng-container>
    <ng-template #cargando>
      <p>Cargando pedido...</p>
    </ng-template>
    </div>
  `
  })

  export class FormPedidoPage implements OnInit {
    pedido: PedidoReporte = { id: 0, nombre: '', camposPedidos: '', creadoPor_id: 0, estado: '' };
    loading = false;
    errorMensaje: string | null = null;
    id: number | null = null;

  
    constructor(
      private pedidoService: PedidoService,
      private router: Router,
      private authService: AuthService
    ) {}
  
    ngOnInit(): void {
      this.errorMensaje = null;
      this.id = this.authService.getUsuarioId();
      if (!this.id) {
        this.router.navigate(['/login']);
        return;
      }
    }

    crearPedido(pedido: PedidoReporte) {
      if (!this.id) {
        this.router.navigate(['/login']);
        return;
      }
      
      pedido.creadoPor_id = this.id;
      pedido.estado = 'pendiente';

      this.pedidoService.crearPedido(pedido).subscribe({
        next: () => this.router.navigate(['/pedidos']),
        error: (err) => {
          this.errorMensaje = err.error?.error || 'Error inesperado al guardar el pedido.';
        }
      });
    }
    
  }