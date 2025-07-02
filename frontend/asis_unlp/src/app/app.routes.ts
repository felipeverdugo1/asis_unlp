import { Routes } from '@angular/router';
import { ListaUsuariosPage, FormUsuarioPage } from './pages/usuarios/usuario.pages';
import { ListaBarriosPage, FormBarrioPage } from './pages/barrios/barrio.pages';
import { ListarOrgaSocialPage , FormOrgaSocialPage } from './pages/orgaSocial/orgaSocial.pages';
import { ListarEncuestadorPage , FormEncuestadorPage } from './pages/encuestador/encuestador.pages';
// import { ListaBarriosPage, FormZonaPage } from './pages/filtro/filtro.pages';
import { FormZonaPage, ListarZonaPage } from './pages/zonas/zona.pages';
import { Home } from './components/home/home';


export const routes: Routes = [
  { path: '', component: Home }, 
  { 
    path: 'usuario', 
    children: [
      { path: '', component: ListaUsuariosPage },
      { path: 'nuevo', component: FormUsuarioPage }
    ]
  },
  {
    path: 'barrio',
    children: [
      { path: '', component: ListaBarriosPage },
      { path: 'nuevo', component: FormBarrioPage },
      { path: 'editar/:id', component: FormBarrioPage }
]},
  { 
    path: 'zona', 
    children: [
      { path: '', component: ListarZonaPage },
      { path: 'nueva', component: FormZonaPage },
      { path: 'editar/:id', component: FormZonaPage }
    ]
  },
  { 
    path: 'encuestador', 
    children: [
      { path: '', component: ListarEncuestadorPage },
      { path: 'nuevo', component: FormEncuestadorPage },
      { path: 'editar/:id', component: FormEncuestadorPage }
    ]
  },
  { 
    path: 'organizacionSocial', 
    children: [
      { path: '', component: ListarOrgaSocialPage },
      { path: 'nueva', component: FormOrgaSocialPage },
      { path: 'editar/:id', component: FormOrgaSocialPage }
    ]
  }
];