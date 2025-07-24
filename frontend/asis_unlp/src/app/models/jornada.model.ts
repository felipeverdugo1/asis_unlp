import { Zona } from "./zona.model";

export interface Jornada {
    id: number;
    fechaInicio: string;
    fechaFin: string;
    campaña_id: number;
    zonas?: Zona[]
  }
