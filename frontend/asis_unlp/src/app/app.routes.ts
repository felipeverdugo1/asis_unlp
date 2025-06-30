import { Routes } from '@angular/router';
import { ListaUsuariosPage, FormUsuarioPage } from './pages/usuarios/usuario.pages';
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