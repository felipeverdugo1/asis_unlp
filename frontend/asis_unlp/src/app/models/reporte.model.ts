export interface Reporte {
    id?: number;
    fechaCreacion: string;
    nombreUnico: string;
    descripcion: string;
    creadoPor_id: number;
    campaña_id?: number;
    compartido_con?: number[];
}