import { Routes } from '@angular/router';
import { ListaUsuariosPage, FormUsuarioPage } from './pages/usuarios/usuario.pages';
import { ListaBarriosPage, FormBarrioPage } from './pages/barrios/barrio.pages';
import { ListarRolesPage, FormRolPage } from './pages/roles/rol.pages';
import { Home } from './components/home/home';


export const routes: Routes = [
  { path: '', component: Home }, 
  { 
    path: 'usuario', 
    children: [
      { path: '', component: ListaUsuariosPage },
      { path: 'nuevo', component: FormUsuarioPage },
      { path: 'editar/:id', component: FormUsuarioPage }
    ]
  },
  {
    path: 'rol', 
    children: [
      { path: '', component: ListarRolesPage },
      { path: 'nuevo', component: FormRolPage },
      { path: 'editar/:id', component: FormRolPage }
    ],
  },
  {
    path: 'barrio',
    children: [
      { path: '', component: ListaBarriosPage },
      { path: 'nuevo', component: FormBarrioPage },
      { path: 'editar/:id', component: FormBarrioPage }
    ]
  }
];