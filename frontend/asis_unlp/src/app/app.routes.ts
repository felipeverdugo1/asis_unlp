import { Routes } from '@angular/router';
import { ListaUsuariosPage, FormUsuarioPage, UsuarioRolesPage } from './pages/usuarios/usuario.pages';
import { ListaBarriosPage, FormBarrioPage } from './pages/barrios/barrio.pages';
import { ListarRolesPage, FormRolPage } from './pages/roles/rol.pages';
import { ListarCampaniaPage, FormCampaniaPage } from './pages/campania/campania.pages';
import { ListarJornadaPage, FormJornadaPage, AdministrarZonasPage } from './pages/jornada/jornada.pages';
import { ListarOrgaSocialPage , FormOrgaSocialPage } from './pages/orgaSocial/orgaSocial.pages';
import { ListarEncuestadorPage , FormEncuestadorPage } from './pages/encuestador/encuestador.pages';
import { FiltroPage, FiltrosListPage } from './pages/filtro/filtro.pages';
import { ReportePage } from './pages/reporte/reporte.pages';
// import { ListaBarriosPage, FormZonaPage } from './pages/filtro/filtro.pages';
import { FormZonaPage, ListarZonaPage } from './pages/zonas/zona.pages';
import { Home } from './components/home/home';
import { Login } from './components/login/login';
import { AuthGuard, AdminGuard } from './guard/auth.guard';
import { ListaPedidosPage, FormPedidoPage } from './pages/pedidoReporte/pedidoReporte.pages';
import { CargaCsvPage, ListarEncuestaPage } from './pages/encuesta/encuesta.pages';


export const routes: Routes = [
  { path: '', component: Home }, 
  { path: 'login', component: Login },
  { 
    path: 'usuario', 
    data: { title: 'Usuarios' },
    children: [
      { path: '', component: ListaUsuariosPage, canActivate: [AuthGuard, AdminGuard] },
      { path: 'nuevo', component: FormUsuarioPage, canActivate: [AuthGuard, AdminGuard] },
      { path: 'editar/:id', component: FormUsuarioPage, canActivate: [AuthGuard, AdminGuard] },
      { path: 'roles/:id', component: UsuarioRolesPage, canActivate: [AuthGuard, AdminGuard] }
    ]
  },
  {
    path: 'rol', 
    data: { title: 'Roles' },
    children: [
      { path: '', component: ListarRolesPage, canActivate: [AuthGuard, AdminGuard] },
      { path: 'nuevo', component: FormRolPage, canActivate: [AuthGuard, AdminGuard] },
      { path: 'editar/:id', component: FormRolPage, canActivate: [AuthGuard, AdminGuard] }
    ],
  },
  {
    path: 'barrio',
    data: { title: 'Barrios' },
    children: [
      { path: '', component: ListaBarriosPage, canActivate: [AuthGuard, AdminGuard] },
      { path: 'nuevo', component: FormBarrioPage, canActivate: [AuthGuard, AdminGuard] },
      { path: 'editar/:id', component: FormBarrioPage, canActivate: [AuthGuard, AdminGuard] },
      {
        path: ':idBarrio/zonas',
        data: { title: 'Zonas' },
        children: [
          { path: '', component: ListarZonaPage, canActivate: [AuthGuard, AdminGuard] },
          { path: 'nueva', component: FormZonaPage, canActivate: [AuthGuard, AdminGuard] },
          { path: 'editar/:id', component: FormZonaPage, canActivate: [AuthGuard, AdminGuard] }
        ]
      }
    ]
  },
  { 
    path: 'campania',
    data: { title: 'Campañas' },
    children: [
      { path: '', component: ListarCampaniaPage, canActivate: [AuthGuard, AdminGuard] },
      { path: 'nueva', component: FormCampaniaPage, canActivate: [AuthGuard, AdminGuard] },
      { path: 'editar/:id', component: FormCampaniaPage, canActivate: [AuthGuard, AdminGuard] },
      {
        path: ':idCampania/jornadas',
        data: { title: 'Jornadas' },
        children: [
          { path: '', component: ListarJornadaPage, canActivate: [AuthGuard, AdminGuard] },
          { path: 'nuevo', component: FormJornadaPage, canActivate: [AuthGuard, AdminGuard] },
          { path: 'editar/:idJornada', component: FormJornadaPage, canActivate: [AuthGuard, AdminGuard] },
          { path: 'administrarZonas/:idJornada', component: AdministrarZonasPage, canActivate: [AuthGuard, AdminGuard] }
        ]
      }
    ]
  },
  { 
    path: 'encuestador', 
    data: { title: 'Encuestadores' },
    children: [
      { path: '', component: ListarEncuestadorPage, canActivate: [AuthGuard, AdminGuard] },
      { path: 'nuevo', component: FormEncuestadorPage, canActivate: [AuthGuard, AdminGuard] },
      { path: 'editar/:id', component: FormEncuestadorPage, canActivate: [AuthGuard, AdminGuard] }
    ]
  },
  { 
    path: 'organizacionSocial', 
    data: { title: 'Org. Sociales' },
    children: [
      { path: '', component: ListarOrgaSocialPage, canActivate: [AuthGuard, AdminGuard] },
      { path: 'nueva', component: FormOrgaSocialPage, canActivate: [AuthGuard, AdminGuard] },
      { path: 'editar/:id', component: FormOrgaSocialPage, canActivate: [AuthGuard, AdminGuard] }
    ]
  },
  {
    path: 'filtro',
    data: { title: 'Filtros' },
    children: [// TODO agregar guard de personal de salud
      { path: 'nuevo', component: FiltroPage, data: { title: 'Generar Reporte' }, canActivate: [AuthGuard] },
      { path: 'resultado', component: ReportePage, data: { title: 'Reporte Generado' }, canActivate: [AuthGuard] },
      { path: '', component: FiltrosListPage, data: { title: 'Filtros Guardados' }, canActivate: [AuthGuard] }
    ]
  },
  {
    path: 'pedidos',
    data: { title: 'Pedidos de Reportes' },
    children: [
      { path: '', component: ListaPedidosPage, canActivate: [AuthGuard] },
      { path: 'nuevo', component: FormPedidoPage, canActivate: [AuthGuard] },
    ]
  },
  {
    path: 'encuestas',
    data: { title: 'Encuestas' },
    children: [
      { path: '', component: ListarEncuestaPage, canActivate: [AuthGuard, AdminGuard] },
      { path: 'cargar-csv', component: CargaCsvPage, canActivate: [AuthGuard, AdminGuard] }
    ]
  }
];