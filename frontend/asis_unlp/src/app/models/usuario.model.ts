import { Rol } from "./rol.model";

export interface Usuario {
    id?: number;
    nombreUsuario: string;
    email: string;
    password: string;
    habilitado: boolean;    // Opcional
    especialidad?: string;   // Opcional
    roles_id: number[];
    roles?: Rol[];
  }
  
  export interface FiltroGuardado {
    id: number;
    nombre: string;
    criterios: string;
  }