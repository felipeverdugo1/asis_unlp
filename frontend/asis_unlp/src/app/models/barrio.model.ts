export interface Barrio {
    id: number;
    nombre: string;
    geolocalizacion: string;
    informacion: string;
}

export interface BarrioForm {
    nombre: string;
    geolocalizacion: string;
    informacion: string;
}