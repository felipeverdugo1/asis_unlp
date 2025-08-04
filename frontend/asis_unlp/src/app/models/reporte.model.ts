export interface Reporte {
    id?: number;
    fechaCreacion: string;
    nombreUnico: string;
    descripcion: string;
    creadoPor_id: string;
    campaña_id?: number;
    compartido_con?: string[];
}