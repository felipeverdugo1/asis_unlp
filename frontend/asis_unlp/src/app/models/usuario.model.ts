export interface Usuario {
    id?: number;
    nombreUsuario: string;
    email: string;
    password: string;
    habilitado?: boolean;    // Opcional
    especialidad?: string;   // Opcional
    roles_id: number[];
  }
  
  export interface FiltroGuardado {
    id: number;
    nombre: string;
    criterios: string;
  }