package tests_servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import service.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

import util.TestDataRegistry;

@WebServlet(
        name="DAO Tests",
        description="Tests de persistencia",
        urlPatterns = "/tests/DAO"
)
public class EntityCRUDTests extends HttpServlet {

    private static UsuarioService usuarioService ;
    private static BarrioService barrioService;
    private static ZonaService zonaService;
    private static CampañaService campañaService;
    private static JornadaService jornadaService;
    private static EncuestaService encuestaService;
    private static EncuestadorService encuestadorService;
    private static FiltroPersonalizadoService filtroPersonalizadoService;
    private static ReporteService reporteService;
    private static PreguntaService preguntaService;
    private static OrganizacionSocialService organizacionSocialService;
    private static RolService rolService;

    public static void setup(){

        usuarioService = new UsuarioService();
        barrioService = new BarrioService();
        zonaService = new ZonaService();
        campañaService = new CampañaService();
        jornadaService = new JornadaService();
        encuestaService = new EncuestaService();
        encuestadorService = new EncuestadorService();
        filtroPersonalizadoService = new FiltroPersonalizadoService();
        reporteService = new ReporteService();
        preguntaService = new PreguntaService();
        organizacionSocialService = new OrganizacionSocialService();
        rolService = new RolService();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder result = new StringBuilder();
        resp.setContentType("text/plain;charset=UTF-8");

        // crear inicializar la base y los services
        setup();

        try {
            Rol administrador = new Rol("TEST_ADMINISTRADOR");
            Rol visitante = new Rol("TEST_VISITANTE");
            Set<Rol> visit = Set.of(visitante);
            Rol personalSalud = new Rol("TEST_PERSONAL_SALUD");
            Set<Rol> personal = Set.of(personalSalud);
            Rol orgSocial = new Rol("TEST_ORG_SOCIAL");
            Set<Rol> org = Set.of(orgSocial);
            Set<Rol> admin = Set.of(administrador, personalSalud);

            rolService.crear(administrador);
            rolService.crear(visitante);
            rolService.crear(personalSalud);
            rolService.crear(orgSocial);
            // Se crea un Barrio de prueba para probar el CRUD generico
            Barrio barrioCRUD = new Barrio();
            barrioCRUD.setNombre("TEST_Barrio Test");
            barrioCRUD.setGeolocalizacion("Geo Test");
            barrioCRUD.setInformacion("Info Test");
            barrioService.crear(barrioCRUD);
            Barrio foundBarrio = barrioService.buscarPorId(barrioCRUD.getId());
            result.append("Nombre del Barrio creado: ").append(foundBarrio.getNombre()).append("\n");
            foundBarrio.setNombre("Barrio Test Updated");
            barrioService.actualizar(foundBarrio);
            result.append("Nombre del Barrio actualizado: ").append(barrioCRUD.getNombre()).append("\n");
            barrioService.eliminar(barrioCRUD.getId());
            result.append("Nombre del Barrio eliminado: ").append(barrioCRUD.getNombre()).append("\n");

            // Se crea un set de entidades para probar las relaciones
            Barrio barrio = new Barrio();
            barrio.setNombre("TEST_Barrio Test");
            barrio.setGeolocalizacion("Geo Test");
            barrio.setInformacion("Info Test");
            barrioService.crear(barrio);
            TestDataRegistry.registrar(Barrio.class, barrio.getId());
            // Un barrio tiene varias zonas
            Zona zona1 = new Zona();
            zona1.setNombre("Zona Test 1");
            zona1.setGeolocalizacion("Geo Test Zona");
            zona1.setBarrio(barrio);
            Zona zona2 = new Zona();
            zona2.setNombre("Zona Test 2");
            zona2.setGeolocalizacion("Geo Test Zona");
            zona2.setBarrio(barrio);
            Zona zona3 = new Zona();
            zona3.setNombre("Zona Test 3");
            zona3.setGeolocalizacion("Geo Test Zona");
            zona3.setBarrio(barrio);
            barrio.agregarZona(zona1);
            barrio.agregarZona(zona2);
            barrio.agregarZona(zona3);
            // Por el cascade las zonas se crean al actualizar el barrio
            barrioService.actualizar(barrio);
            result.append("Barrio creado con distintas zonas: ")
                    .append(barrioService.buscarPorId(barrio.getId()).getNombre()).append("\n");

            // Creacion de Campaña con distintas jornadas, reportes
            Usuario admin_user = new Usuario();
            admin_user.setNombreUsuario("TEST_admin");
            admin_user.setPassword("TEST_admin");
            admin_user.setEmail("TEST_admin@test.com");
            admin_user.setHabilitado(true);
            admin_user.setRoles(admin);
            usuarioService.crear(admin_user);
            result.append("Usuario admin creado con roles admin y personal de salud: ")
                    .append(usuarioService.buscarPorId(admin_user.getId()).getNombreUsuario()).append("\n");
            Usuario referente = new Usuario();
            referente.setNombreUsuario("TEST_Referente test 1");
            referente.setPassword("password");
            referente.setEmail("usuario1@gmail.com");
            referente.setHabilitado(true);
            referente.setRoles(org);
            usuarioService.crear(referente);
            Usuario personal1 = new Usuario();
            personal1.setNombreUsuario("TEST_Personal test 1");
            personal1.setPassword("password");
            personal1.setEmail("usuario2@gmail.com");
            personal1.setHabilitado(true);
            personal1.setRoles(personal);
            usuarioService.crear(personal1);
            Usuario personal2 = new Usuario();
            personal2.setNombreUsuario("TEST_Personal test 2");
            personal2.setPassword("password");
            personal2.setEmail("usuario3@gmail.com");
            personal2.setHabilitado(true);
            personal2.setRoles(personal);
            usuarioService.crear(personal2);
            Usuario personal3 = new Usuario();
            personal3.setNombreUsuario("TEST_Personal test 3");
            personal3.setPassword("password");
            personal3.setEmail("usuario4@gmail.com");
            personal3.setHabilitado(true);
            personal3.setRoles(personal);
            usuarioService.crear(personal3);
            result.append("Se crean usuarios personales de salud y referente con sus respectivos roles.").append("\n");
            Campaña campaña = new Campaña();
            campaña.setNombre("TEST_Campaña Test 1");
            campaña.setFechaInicio(LocalDate.now());
            campaña.setFechaFin(LocalDate.now().plusDays(10));
            campaña.setBarrio(barrio);
            campañaService.crear(campaña);
            result.append("Campaña creada con un barrio: ")
                    .append(campañaService.buscarPorId(campaña.getId()).getNombre()).append("\n");

            Jornada jornada1 = new Jornada();
            jornada1.setFechaInicio(LocalDate.now());
            jornada1.setFechaFin(LocalDate.now().plusDays(3));
            Jornada jornada2 = new Jornada();
            jornada2.setFechaInicio(LocalDate.now().plusDays(4));
            jornada2.setFechaFin(LocalDate.now().plusDays(7));
            Jornada jornada3 = new Jornada();
            jornada3.setFechaInicio(LocalDate.now().plusDays(8));
            jornada3.setFechaFin(LocalDate.now().plusDays(10));
            campaña.agregarJornada(jornada1);
            campaña.agregarJornada(jornada2);
            campaña.agregarJornada(jornada3);
            Reporte reporte1 = new Reporte();
            reporte1.setFechaCreacion(LocalDate.now());
            reporte1.setDescripcion("Reporte de prueba");
            reporte1.setNombreUnico("Reporte de prueba");
            reporte1.setCreadoPor(personal1);
            Reporte reporte2 = new Reporte();
            reporte2.setFechaCreacion(LocalDate.now());
            reporte2.setDescripcion("Reporte de prueba");
            reporte2.setNombreUnico("Reporte de prueba");
            reporte2.setCreadoPor(personal2);
            Reporte reporte3 = new Reporte();
            reporte3.setFechaCreacion(LocalDate.now());
            reporte3.setDescripcion("Reporte de prueba");
            reporte3.setNombreUnico("Reporte de prueba");
            reporte3.setCreadoPor(personal3);
            reporte3.setCompartidoCon(referente);
            campaña.agregarReporte(reporte1);
            campaña.agregarReporte(reporte2);
            campaña.agregarReporte(reporte3);
            campañaService.actualizar(campaña);
            TestDataRegistry.registrar(Campaña.class, campaña.getId());

            result.append("Campaña creada con un barrio: ")
                    .append(campañaService.buscarPorId(campaña.getId()).getNombre()).append("\n");
            result.append("Reportes creado para la campaña anterior.").append("\n");
            result.append("Reporte creado para la campaña anterior con creadoPor y compartidoCon")
                    .append(reporteService.buscarPorId(reporte3.getId()).getNombreUnico()).append("\n");

            //Crear encuestas con preguntas y Encuestador para las jornadas creadas
            Encuesta encuesta1 = new Encuesta();
            encuesta1.setNombreUnico("Encuesta de prueba");
            encuesta1.setFecha(LocalDate.now());
            encuesta1.setZona(zona1);
            Pregunta pregunta1_1 = new Pregunta();
            pregunta1_1.setTipo("Social");
            pregunta1_1.setPregunta("¿Pregunta?");
            pregunta1_1.setRespuesta("Respusta");
            Pregunta pregunta1_2 = new Pregunta();
            pregunta1_2.setTipo("Social");
            pregunta1_2.setPregunta("¿pregunta?");
            pregunta1_2.setRespuesta("Respuesta");
            Encuestador encuestador1 = new Encuestador();
            encuestador1.setNombre("TEST_Encuestador de prueba");
            encuestador1.setGenero("Masculino");
            encuestador1.setDni("41999111");
            encuestador1.setEdad(32);
            encuestador1.setOcupacion("Psicologo");
            encuestadorService.crear(encuestador1);
            TestDataRegistry.registrar(Encuestador.class, encuestador1.getId());
            encuesta1.setEncuestador(encuestador1);
            encuesta1.agregarPregunta(pregunta1_1);
            encuesta1.agregarPregunta(pregunta1_2);
            encuesta1.setJornada(jornada1);
            encuestaService.crear(encuesta1);

            result.append("Encuesta creada con un encuestador y preguntas: ")
                    .append(encuestadorService.buscarPorId(encuesta1.getId()).getNombre()).append("\n");

            Encuesta encuesta2 = new Encuesta();
            encuesta2.setNombreUnico("Encuesta de prueba");
            encuesta2.setFecha(LocalDate.now().plusDays(3));
            encuesta2.setZona(zona2);
            Pregunta pregunta2_1 = new Pregunta();
            pregunta2_1.setTipo("Social");
            pregunta2_1.setPregunta("¿Pregunta?");
            pregunta2_1.setRespuesta("Respuesta");
            Encuestador encuestador2 = new Encuestador();
            encuestador2.setNombre("TEST_otro de prueba");
            encuestador2.setGenero("Masculino");
            encuestador2.setDni("41999111");
            encuestador2.setEdad(12);
            encuestador2.setOcupacion("Medico");
            encuestadorService.crear(encuestador2);
            TestDataRegistry.registrar(Encuestador.class, encuestador2.getId());
            encuesta2.setEncuestador(encuestador2);
            encuesta2.agregarPregunta(pregunta2_1);
            encuesta2.setJornada(jornada2);
            encuestaService.crear(encuesta2);

            result.append("Encuesta creada con un encuestador y preguntas: ")
                    .append(encuestadorService.buscarPorId(encuesta2.getId()).getNombre()).append("\n");

            result.append("Jornada actualizada con encuestas: ")
                    .append(jornadaService.buscarPorId(jornada1.getId()).getId()).append("\n");

            // Creacion de una organizacion social
            OrganizacionSocial organizacion_social = new OrganizacionSocial();
            organizacion_social.setNombre("TEST_org social 1");
            organizacion_social.setDomicilio("Docimiliooo");
            organizacion_social.setActividadPrincipal("Trabajos duros");
            organizacion_social.setBarrio(barrio);
            organizacion_social.setReferente(referente);
            organizacionSocialService.crear(organizacion_social);
            TestDataRegistry.registrar(OrganizacionSocial.class, organizacion_social.getId());

            result.append("Organizacion Social creada con Barrio y Referente: ")
                    .append(organizacionSocialService.buscarPorId(organizacion_social.getId())
                            .getNombre()).append("\n");

            // Creacion de filtros personalizados
            FiltroPersonalizado filtroPersonalizado = new FiltroPersonalizado();
            filtroPersonalizado.setNombre("TEST_filtro");
            filtroPersonalizado.setCriterios("criterios");
            filtroPersonalizado.setPropietario(personal2);
            filtroPersonalizadoService.crear(filtroPersonalizado);
            TestDataRegistry.registrar(FiltroPersonalizado.class, filtroPersonalizado.getId());
            FiltroPersonalizado filtroPersonalizado2 = new FiltroPersonalizado();
            filtroPersonalizado2.setNombre("TEST_filtro2");
            filtroPersonalizado2.setCriterios("criterios");
            filtroPersonalizado.setPropietario(personal2);
            filtroPersonalizadoService.crear(filtroPersonalizado2);
            TestDataRegistry.registrar(FiltroPersonalizado.class, filtroPersonalizado2.getId());

            result.append("Filtros guardados para personal de salud 2.").append("\n\n");

        } catch (Exception e) {
            result.append("ERROR: ").append(e.getMessage());
            e.printStackTrace(resp.getWriter());
        }

        resp.getWriter().println(result.toString());
    }

}