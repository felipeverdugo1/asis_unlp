import { Routes } from '@angular/router';
import { ListaUsuariosPage, FormUsuarioPage } from './pages/usuarios/usuario.pages';
import { ListaBarriosPage, FormBarrioPage } from './pages/barrios/barrio.pages';
import { ListarRolesPage, FormRolPage } from './pages/roles/rol.pages';
import { ListarCampaniaPage, FormCampaniaPage } from './pages/campania/campania.pages';
import { ListarJornadaPage, FormJornadaPage, AdministrarZonasPage } from './pages/jornada/jornada.pages';
import { ListarOrgaSocialPage , FormOrgaSocialPage } from './pages/orgaSocial/orgaSocial.pages';
import { ListarEncuestadorPage , FormEncuestadorPage } from './pages/encuestador/encuestador.pages';
// import { ListaBarriosPage, FormZonaPage } from './pages/filtro/filtro.pages';
import { FormZonaPage, ListarZonaPage } from './pages/zonas/zona.pages';
import { Home } from './components/home/home';



export const routes: Routes = [
  { path: '', component: Home }, 
  { 
    path: 'usuario', 
    data: { title: 'Usuarios' },
    children: [
      { path: '', component: ListaUsuariosPage },
      { path: 'nuevo', component: FormUsuarioPage },
      { path: 'editar/:id', component: FormUsuarioPage }
    ]
  },
  {
    path: 'rol', 
    data: { title: 'Roles' },
    children: [
      { path: '', component: ListarRolesPage },
      { path: 'nuevo', component: FormRolPage },
      { path: 'editar/:id', component: FormRolPage }
    ],
  },
  {
    path: 'barrio',
    data: { title: 'Barrios' },
    children: [
      { path: '', component: ListaBarriosPage },
      { path: 'nuevo', component: FormBarrioPage },
      { path: 'editar/:id', component: FormBarrioPage },
      {
        path: ':idBarrio/zonas',
        data: { title: 'Zonas' },
        children: [
          { path: '', component: ListarZonaPage },
          { path: 'nueva', component: FormZonaPage },
          { path: 'editar/:id', component: FormZonaPage }
        ]
      }
    ]
  },
  { 
    path: 'campania',
    data: { title: 'Campañas' },
    children: [
      { path: '', component: ListarCampaniaPage },
      { path: 'nueva', component: FormCampaniaPage },
      { path: 'editar/:id', component: FormCampaniaPage },
      {
        path: ':idCampania/jornadas',
        data: { title: 'Jornadas' },
        children: [
          { path: '', component: ListarJornadaPage },
          { path: 'nuevo', component: FormJornadaPage },
          { path: 'editar/:idJornada', component: FormJornadaPage },
          { path: 'administrarZonas/:idJornada', component: AdministrarZonasPage }
        ]
      }
    ]
  },
  { 
    path: 'encuestador', 
    data: { title: 'Encuestadores' },
    children: [
      { path: '', component: ListarEncuestadorPage },
      { path: 'nuevo', component: FormEncuestadorPage },
      { path: 'editar/:id', component: FormEncuestadorPage }
    ]
  },
  { 
    path: 'organizacionSocial', 
    data: { title: 'Org. Sociales' },
    children: [
      { path: '', component: ListarOrgaSocialPage },
      { path: 'nueva', component: FormOrgaSocialPage },
      { path: 'editar/:id', component: FormOrgaSocialPage }
    ]
  }
];