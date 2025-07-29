import { Injectable } from "@angular/core";
import { CanActivate, Router } from "@angular/router";
import { AuthService } from '../services/auth.service';

// AuthGuard verifica si el usuario esta autenticado
@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}

  canActivate(): boolean {
    if (!this.auth.loggedIn()) {
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }
}

// AdminGuard verifica si el usuario tiene el rol 'admin'
// Si no tiene el rol, redirige al usuario a la página de inicio
@Injectable({ providedIn: 'root' })
export class AdminGuard implements CanActivate {
  constructor(private auth: AuthService) {}

  canActivate(): boolean {
    return this.auth.tieneRol('admin');
  }
}