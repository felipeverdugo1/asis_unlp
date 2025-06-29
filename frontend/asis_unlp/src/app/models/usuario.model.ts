export interface Usuario {
    id: number;
    nombreUsuario: string;
    email: string;
    password?: string;       // Opcional 
    habilitado?: boolean;    // Opcional
    especialidad?: string;   // Opcional
  }
  

  export interface Rol {
    id: number;
    nombre: string;
  }
  
  export interface FiltroGuardado {
    id: number;
    nombre: string;
    criterios: string;
  }