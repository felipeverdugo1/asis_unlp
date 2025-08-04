export interface PedidoReporte {
    id?: number;
    nombre: string;
    camposPedidos: string;
    creadoPor_id: number;
    estado: string;
    comentario?: string;
    asignado_a_id?: number;
    reporte_id?: number;
}

export interface PedidoReporteCompletado {
    asignado_a_id: number;
    reporte_id: number;
    comentario: string;
}