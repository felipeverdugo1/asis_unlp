import { Routes } from '@angular/router';
import { ListaUsuariosPage } from './pages/usuarios/lista-usuarios';
import { FormUsuarioPage } from './pages/usuarios/form-usuario';
import { Home } from './components/home/home';

export const routes: Routes = [
  { path: '', component: Home }, 
  { 
    path: 'usuario', 
    children: [
      { path: '', component: ListaUsuariosPage },
      { path: 'nuevo', component: FormUsuarioPage }
    ]
  }
];