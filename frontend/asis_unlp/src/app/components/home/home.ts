import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  standalone: true,
  selector: 'app-home',
  imports: [CommonModule,RouterModule],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {
  constructor(public auth: AuthService) {}

  rolAdmin(): boolean {
    return this.auth.tieneRol('admin');
  }

  rolSalud(): boolean {
    return this.auth.tieneRol('salud');
  }

  rolReferente(): boolean {
    return this.auth.tieneRol('referente');
  }
}