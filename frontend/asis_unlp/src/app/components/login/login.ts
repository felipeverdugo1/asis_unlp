import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { LoginDTO } from '../../models/loginDTO';
import { Router } from '@angular/router';
import { error } from 'console';

@Component({
  selector: 'app-login',
  imports:[CommonModule, FormsModule, HttpClientModule ],
  templateUrl: './login.html',
})
export class Login {
  email = '';
  clave = '';
  errorMensaje = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    const credenciales: LoginDTO = {
      email: this.email,
      clave: this.clave
    };

    this.authService.login(credenciales).subscribe({
      next: (res) => {
        this.authService.guardarToken(res.token);
        this.router.navigate(['/']); 
      },
      error: (err) => {
        this.errorMensaje = err.error?.error;
        console.error(err);
      }
    });
  }
}