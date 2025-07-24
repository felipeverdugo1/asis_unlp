export interface Filtro {
    id?: number;
    nombre: string;
    criterios: string;
    propietario: string;
}

export interface FiltroForm {
    nombre: string;
    criterios: string;
    propietario: string;
}