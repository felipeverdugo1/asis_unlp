import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { LoginDTO } from '../../models/loginDTO';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports:[CommonModule, FormsModule ],
  templateUrl: './login.html',
  styleUrls: ['../../../styles.css']
})
export class Login {
  email = '';
  clave = '';
  errorMensaje = '';
  cargando = false;

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    const credenciales: LoginDTO = {
      email: this.email,
      clave: this.clave
    };

    this.cargando = true;

    this.authService.login(credenciales).subscribe({
      next: (res) => {
        this.cargando = true;
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