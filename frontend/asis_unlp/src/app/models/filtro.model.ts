export interface Filtro {
    id?: number;
    nombre: string;
    geolocalizacion: string;
    informacion: string;
}

export interface FiltroForm {
    nombre: string;
    geolocalizacion: string;
    informacion: string;
}