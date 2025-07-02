import { Routes } from '@angular/router';
import { ListaUsuariosPage, FormUsuarioPage } from './pages/usuarios/usuario.pages';
import { ListaBarriosPage, FormBarrioPage } from './pages/barrios/barrio.pages';
import { ListarRolesPage, FormRolPage } from './pages/roles/rol.pages';
import { ListarCampaniaPage, FormCampaniaPage } from './pages/campania/campania.pages';
import { ListarJornadaPage, FormJornadaPage } from './pages/jornada/jornada.pages';
import { Home } from './components/home/home';
import { FormZonaPage, ListarZonaPage } from './pages/zonas/zona.pages';


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
    path: 'campania', 
    children: [
      { path: '', component: ListarCampaniaPage },
      { path: 'nueva', component: FormCampaniaPage },
      { path: 'editar/:id', component: FormCampaniaPage }
    ]
  },
  {
  path: 'campania/:idCampania/jornadas',
  children: [
    { path: '', component: ListarJornadaPage },
    { path: 'nuevo', component: FormJornadaPage },
    { path: 'editar/:idJornada', component: FormJornadaPage }
  ]
}
];