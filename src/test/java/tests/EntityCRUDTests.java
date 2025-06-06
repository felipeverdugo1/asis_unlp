package tests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import model.*;
import net.bytebuddy.matcher.CachingMatcher;
import org.junit.jupiter.api.*;
import util.TestHibernateUtil;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.ArrayList;

import model.*;
import service.*;

/**
 *
 *
 * Tests CRUD para todos los modelos
 *
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EntityCRUDTests {

    private static UsuarioService usuarioService;
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

    @BeforeAll
    public static void setup() {
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

        Rol administrador = new Rol("ADMINISTRADOR");
        Rol visitante = new Rol("VISITANTE");
        Rol personalSalud = new Rol("PERSONAL_SALUD");
        Rol orgSocial = new Rol("ORG_SOCIAL");

        rolService.crear(administrador);
        rolService.crear(visitante);
        rolService.crear(personalSalud);
        rolService.crear(orgSocial);

    }

    // Tests for Barrio entity
    @Test
    @Order(1)
    public void testBarrioCRUD() {
        // Create
        Barrio barrio = new Barrio();
        barrio.setNombre("Barrio Test");
        barrio.setGeolocalizacion("Geo Test");
        barrio.setInformacion("Info Test");

        Zona zona1 = new Zona().setNombre("Zona 1").setGeolocalizacion("Geo 1");
        Zona zona2 = new Zona().setNombre("Zona 2").setGeolocalizacion("Geo 2");

        barrio.agregarZonas(zona1);
        barrio.agregarZonas(zona2);

        barrioService.crear(barrio);
        Assertions.assertNotNull(barrio.getId(), "El ID del barrio no debería ser null luego de persistir.");

        Barrio foundBarrio = barrioService.buscarPorId(barrio.getId());
        Assertions.assertNotNull(foundBarrio, "El barrio debería existir.");
        Assertions.assertEquals("Barrio Test", foundBarrio.getNombre());

        foundBarrio.setNombre("Barrio Test Updated");
        barrioService.actualizar(foundBarrio);

        Barrio updatedBarrio = barrioService.buscarPorId(barrio.getId());
        Assertions.assertEquals("Barrio Test Updated", updatedBarrio.getNombre());
        Assertions.assertEquals(2, updatedBarrio.getZonas().size());


        // Probar que no se puede guardar otro barrio con nombre duplicado
        Barrio barrioNombreDuplicado = new Barrio();
        barrioNombreDuplicado.setNombre("Barrio Test Updated"); // mismo nombre actualizado
        barrioNombreDuplicado.setGeolocalizacion("Geo Test duplicado");
        barrioNombreDuplicado.setInformacion("Info duplicada");

        Assertions.assertThrows(PersistenceException.class, () -> {
            barrioService.crear(barrioNombreDuplicado);
            barrioService.flush(); // forzar un flush() para que la excepción por duplicado se dispare
        }, "Debería fallar por nombre duplicado");

        //barrioService.eliminar(updatedBarrio.getId());

        //Barrio deletedBarrio = barrioService.buscarPorId(barrio.getId());
        //Assertions.assertNull(deletedBarrio, "El barrio debería haber sido eliminado.");
    }

    // Tests for Usuario entity
    @Test
    @Order(2)
    public void testUsuarioCRUD() {
        // Create

        Rol rol1 = rolService.buscarPorId(1L);
        Rol rol2 = rolService.buscarPorId(2L);

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("Usuario Test");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");
        usuario.setHabilitado(true);
        usuario.agregarRol(rol1);
        usuario.agregarRol(rol2);

        usuarioService.crear(usuario);
        Assertions.assertNotNull(usuario.getId(), "El ID del usuario no debería ser null luego de persistir.");

        Usuario foundUsuario = usuarioService.buscarPorId(usuario.getId());
        Assertions.assertNotNull(foundUsuario, "El usuario debería existir.");
        Assertions.assertEquals("Usuario Test", foundUsuario.getNombreUsuario());
        Assertions.assertEquals(2, foundUsuario.getRoles().size());

        foundUsuario.setNombreUsuario("Usuario Test Updated");
        usuarioService.actualizar(foundUsuario);

        Usuario updatedUsuario = usuarioService.buscarPorId(usuario.getId());
        Assertions.assertEquals("Usuario Test Updated", updatedUsuario.getNombreUsuario());

        // Delete
        /*usuarioService.eliminar(updatedUsuario.getId());

        Usuario deletedUsuario = usuarioService.buscarPorId(usuario.getId());
        Assertions.assertNull(deletedUsuario, "El usuario debería haber sido eliminado.");*/
    }

    // Tests for Zona entity
    /*@Test
    @Order(3)
    public void testZonaCRUD() {
        // First create a Barrio for the Zona
        Barrio barrio = new Barrio();
        barrio.setNombre("Barrio Para Zona");
        barrio.setGeolocalizacion("Geo Test");
        barrio.setInformacion("Info Test");
        barrioService.crear(barrio);

        Zona zona = new Zona();
        zona.setNombre("Zona Test");
        zona.setGeolocalizacion("Geo Test Zona");
        zona.setBarrio(barrio);

        zonaService.crear(zona);
        Assertions.assertNotNull(zona.getId(), "El ID de la zona no debería ser null luego de persistir.");

        Zona foundZona = zonaService.buscarPorId(zona.getId());
        Assertions.assertNotNull(foundZona, "La zona debería existir.");
        Assertions.assertEquals("Zona Test", foundZona.getNombre());

        foundZona.setNombre("Zona Test Updated");
        zonaService.actualizar(foundZona);

        Zona updatedZona = zonaService.buscarPorId(zona.getId());
        Assertions.assertEquals("Zona Test Updated", updatedZona.getNombre());

        zonaService.eliminar(zona.getId());

        Zona deletedZona = zonaService.buscarPorId(zona.getId());
        Assertions.assertNull(deletedZona, "La zona debería haber sido eliminada.");

        // Clean up the Barrio
        barrioService.eliminar(barrio.getId());
    }

    // Tests for Campaña entity
    @Test
    @Order(4)
    public void testCampañaCRUD() {
        // First create a Barrio for the Campaña
        Barrio barrio = new Barrio();
        barrio.setNombre("Barrio Para Campaña");
        barrio.setGeolocalizacion("Geo Test");
        barrio.setInformacion("Info Test");
        barrioService.crear(barrio);

        // Create
        Campaña campaña = new Campaña();
        campaña.setNombre("Campaña Test");
        campaña.setFechaInicio(LocalDate.now());
        campaña.setFechaFin(LocalDate.now().plusDays(30));
        campaña.setBarrio(barrio);

        campañaService.crear(campaña);
        Assertions.assertNotNull(campaña.getId(), "El ID de la campaña no debería ser null luego de persistir.");

        // ReadcampañaService.
        Campaña foundCampaña = campañaService.buscarPorId(campaña.getId());
        Assertions.assertNotNull(foundCampaña, "La campaña debería existir.");
        Assertions.assertEquals("Campaña Test", foundCampaña.getNombre());

        // Update
        foundCampaña.setNombre("Campaña Test Updated");
        campañaService.actualizar(foundCampaña);

        Campaña updatedCampaña = campañaService.buscarPorId(campaña.getId());
        Assertions.assertEquals("Campaña Test Updated", updatedCampaña.getNombre());

        // Delete
        campañaService.eliminar(updatedCampaña.getId());

        Campaña deletedCampaña = campañaService.buscarPorId(campaña.getId());
        Assertions.assertNull(deletedCampaña, "La campaña debería haber sido eliminada.");

        // Clean up the Barrio
        barrioService.eliminar(barrio.getId());
    }

    // Tests for Encuesta entity
    @Test
    @Order(5)
    public void testEncuestaCRUD() {
        // First create a Barrio for the Campaña
        Barrio barrio = new Barrio();
        barrio.setNombre("Barrio Para Encuesta");
        barrio.setGeolocalizacion("Geo Test");
        barrio.setInformacion("Info Test");
        barrioService.crear(barrio);

        // Then create a Campaña for the Jornada
        Campaña campaña = new Campaña();
        campaña.setNombre("Campaña para Jornada");
        campaña.setFechaInicio(LocalDate.now());
        campaña.setFechaFin(LocalDate.now().plusDays(30));
        campaña.setBarrio(barrio);
        campañaService.crear(campaña);

        // Then create a Jornada for the Encuesta
        Jornada jornada = new Jornada();
        jornada.setFechaInicio(LocalDate.now());
        jornada.setFechaFin(LocalDate.now().plusDays(1));
        jornada.setCampaña(campaña);
        jornadaService.crear(jornada);

        // Create
        Encuesta encuesta = new Encuesta();
        encuesta.setFecha(LocalDate.now());
        encuesta.setJornada(jornada);

        encuestaService.crear(encuesta);
        Assertions.assertNotNull(encuesta.getId(), "El ID de la encuesta no debería ser null luego de persistir.");

        // Read
        Encuesta foundEncuesta = encuestaService.buscarPorId(encuesta.getId());
        Assertions.assertNotNull(foundEncuesta, "La encuesta debería existir.");
        Assertions.assertEquals(LocalDate.now(), foundEncuesta.getFecha());

        // Update
        foundEncuesta.setFecha(LocalDate.now().plusDays(1));
        encuestaService.actualizar(foundEncuesta);

        Encuesta updatedEncuesta = encuestaService.buscarPorId(encuesta.getId());
        Assertions.assertEquals(LocalDate.now().plusDays(1), updatedEncuesta.getFecha());

        // Delete
        encuestaService.eliminar(updatedEncuesta.getId());

        Encuesta deletedEncuesta = encuestaService.buscarPorId(encuesta.getId());
        Assertions.assertNull(deletedEncuesta, "La encuesta debería haber sido eliminada.");

        // Clean up the Jornada
        jornadaService.eliminar(jornada.getId());

        // Clean up the Campaña
        campañaService.eliminar(campaña.getId());

        // Clean up the Barrio
        barrioService.eliminar(barrio.getId());
    }

    // Tests for Encuestador entity
    @Test
    @Order(6)
    public void testEncuestadorCRUD() {
        // Create
        Encuestador encuestador = new Encuestador();
        encuestador.setNombre("Encuestador Test");
        encuestador.setDni("12345678");
        encuestador.setEdad(30);
        encuestador.setGenero("Masculino");
        encuestador.setOcupacion("Estudiante");

        encuestadorService.crear(encuestador);
        Assertions.assertNotNull(encuestador.getId(), "El ID del encuestador no debería ser null luego de persistir.");

        // Read
        Encuestador foundEncuestador = encuestadorService.buscarPorId(encuestador.getId());
        Assertions.assertNotNull(foundEncuestador, "El encuestador debería existir.");
        Assertions.assertEquals("Encuestador Test", foundEncuestador.getNombre());

        // Update
        foundEncuestador.setNombre("Encuestador Test Updated");
        encuestadorService.actualizar(foundEncuestador);

        Encuestador updatedEncuestador = encuestadorService.buscarPorId(encuestador.getId());
        Assertions.assertEquals("Encuestador Test Updated", updatedEncuestador.getNombre());

        // Delete
        encuestadorService.eliminar(updatedEncuestador.getId());

        Encuestador deletedEncuestador = encuestadorService.buscarPorId(encuestador.getId());
        Assertions.assertNull(deletedEncuestador, "El encuestador debería haber sido eliminado.");
    }

    // Tests for FiltroPersonalizado entity
    @Test
    @Order(7)
    public void testFiltroPersonalizadoCRUD() {
        // First create a Usuario for the FiltroPersonalizado
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("Usuario Para Filtro");
        usuario.setEmail("filtro@example.com");
        usuario.setPassword("password123");
        usuario.setHabilitado(true);
        usuario.setRol(Rol.ADMINISTRADOR);
        usuarioService.crear(usuario);

        // Create
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.setNombre("Filtro Test");
        filtro.setCriterios("criterio1=valor1;criterio2=valor2");
        filtro.setUsuario(usuario);

        filtroPersonalizadoService.crear(filtro);
        Assertions.assertNotNull(filtro.getId(), "El ID del filtro no debería ser null luego de persistir.");

        // Read
        FiltroPersonalizado foundFiltro = filtroPersonalizadoService.buscarPorId(filtro.getId());
        Assertions.assertNotNull(foundFiltro, "El filtro debería existir.");
        Assertions.assertEquals("Filtro Test", foundFiltro.getNombre());

        // Update
        foundFiltro.setNombre("Filtro Test Updated");
        filtroPersonalizadoService.actualizar(foundFiltro);

        FiltroPersonalizado updatedFiltro = filtroPersonalizadoService.buscarPorId(filtro.getId());
        Assertions.assertEquals("Filtro Test Updated", updatedFiltro.getNombre());

        // Delete
        filtroPersonalizadoService.eliminar(updatedFiltro.getId());

        FiltroPersonalizado deletedFiltro = filtroPersonalizadoService.buscarPorId(filtro.getId());
        Assertions.assertNull(deletedFiltro, "El filtro debería haber sido eliminado.");

        // Clean up the Usuario
        usuarioService.eliminar(usuario.getId());
    }

    // Tests for Jornada entity
    @Test
    @Order(8)
    public void testJornadaCRUD() {
        // First create a Barrio for the Campaña
        Barrio barrio = new Barrio();
        barrio.setNombre("Barrio Para Jornada");
        barrio.setGeolocalizacion("Geo Test");
        barrio.setInformacion("Info Test");
        barrioService.crear(barrio);

        // Then create a Campaña for the Jornada
        Campaña campaña = new Campaña();
        campaña.setNombre("Campaña Para Jornada");
        campaña.setFechaInicio(LocalDate.now());
        campaña.setFechaFin(LocalDate.now().plusDays(30));
        campaña.setBarrio(barrio);
        campañaService.crear(campaña);

        // Create
        Jornada jornada = new Jornada();
        jornada.setFechaInicio(LocalDate.now());
        jornada.setFechaFin(LocalDate.now().plusDays(1));
        jornada.setCampaña(campaña);

        jornadaService.crear(jornada);
        Assertions.assertNotNull(jornada.getId(), "El ID de la jornada no debería ser null luego de persistir.");

        // Read
        Jornada foundJornada = jornadaService.buscarPorId(jornada.getId());
        Assertions.assertNotNull(foundJornada, "La jornada debería existir.");
        Assertions.assertEquals(LocalDate.now(), foundJornada.getFechaInicio());

        // Update
        foundJornada.setFechaInicio(LocalDate.now().plusDays(1));
        jornadaService.actualizar(foundJornada);

        Jornada updatedJornada = jornadaService.buscarPorId(jornada.getId());
        Assertions.assertEquals(LocalDate.now().plusDays(1), updatedJornada.getFechaInicio());

        // Delete
        jornadaService.eliminar(updatedJornada.getId());

        Jornada deletedJornada = jornadaService.buscarPorId(jornada.getId());
        Assertions.assertNull(deletedJornada, "La jornada debería haber sido eliminada.");

        // Clean up the Campaña
        campañaService.eliminar(campaña.getId());

        // Clean up the Barrio
        barrioService.eliminar(barrio.getId());
    }

    // Tests for OrganizacionSocial entity
    @Test
    @Order(9)
    public void testOrganizacionSocialCRUD() {
        // First create a Barrio for the OrganizacionSocial (optional)
        Barrio barrio = new Barrio();
        barrio.setNombre("Barrio Para Organizacion");
        barrio.setGeolocalizacion("Geo Test");
        barrio.setInformacion("Info Test");
        barrioService.crear(barrio);

        // Create
        OrganizacionSocial organizacion = new OrganizacionSocial();
        organizacion.setNombre("Organizacion Test");
        organizacion.setDomicilio("Domicilio Test");
        organizacion.setActividadPrincipal("Actividad Test");
        organizacion.setBarrio(barrio);

        organizacionSocialService.crear(organizacion);
        Assertions.assertNotNull(organizacion.getId(), "El ID de la organización no debería ser null luego de persistir.");

        // Read
        OrganizacionSocial foundOrganizacion = organizacionSocialService.buscarPorId(organizacion.getId());
        Assertions.assertNotNull(foundOrganizacion, "La organización debería existir.");
        Assertions.assertEquals("Organizacion Test", foundOrganizacion.getNombre());

        // Update
        foundOrganizacion.setNombre("Organizacion Test Updated");
        organizacionSocialService.actualizar(foundOrganizacion);

        OrganizacionSocial updatedOrganizacion = organizacionSocialService.buscarPorId(organizacion.getId());
        Assertions.assertEquals("Organizacion Test Updated", updatedOrganizacion.getNombre());

        // Delete
        organizacionSocialService.eliminar(updatedOrganizacion.getId());

        OrganizacionSocial deletedOrganizacion = organizacionSocialService.buscarPorId(organizacion.getId());
        Assertions.assertNull(deletedOrganizacion, "La organización debería haber sido eliminada.");

        // Clean up the Barrio
        barrioService.eliminar(barrio.getId());
    }

    // Tests for Pregunta entity
    @Test
    @Order(10)
    public void testPreguntaCRUD() {
        // First create a Barrio for the Campaña
        Barrio barrio = new Barrio();
        barrio.setNombre("Barrio Para Pregunta");
        barrio.setGeolocalizacion("Geo Test");
        barrio.setInformacion("Info Test");
        barrioService.crear(barrio);

        // Then create a Campaña for the Jornada
        Campaña campaña = new Campaña();
        campaña.setNombre("Campaña Para Pregunta");
        campaña.setFechaInicio(LocalDate.now());
        campaña.setFechaFin(LocalDate.now().plusDays(30));
        campaña.setBarrio(barrio);
        campañaService.crear(campaña);

        // Then create a Jornada for the Encuesta
        Jornada jornada = new Jornada();
        jornada.setFechaInicio(LocalDate.now());
        jornada.setFechaFin(LocalDate.now().plusDays(1));
        jornada.setCampaña(campaña);
        jornadaService.crear(jornada);

        // Then create an Encuesta for the Pregunta
        Encuesta encuesta = new Encuesta();
        encuesta.setFecha(LocalDate.now());
        encuesta.setJornada(jornada);
        encuestaService.crear(encuesta);

        // Create
        Pregunta pregunta = new Pregunta();
        pregunta.setTipo("Multiple Choice");
        pregunta.setPregunta("¿Cuál es su edad?");
        pregunta.setRespuesta("30");
        pregunta.setEncuesta(encuesta);

        preguntaService.crear(pregunta);
        Assertions.assertNotNull(pregunta.getId(), "El ID de la pregunta no debería ser null luego de persistir.");

        // Read
        Pregunta foundPregunta = preguntaService.buscarPorId(pregunta.getId());
        Assertions.assertNotNull(foundPregunta, "La pregunta debería existir.");
        Assertions.assertEquals("¿Cuál es su edad?", foundPregunta.getPregunta());

        // Update
        foundPregunta.setPregunta("¿Cuál es su nombre?");
        preguntaService.actualizar(foundPregunta);

        Pregunta updatedPregunta = preguntaService.buscarPorId(pregunta.getId());
        Assertions.assertEquals("¿Cuál es su nombre?", updatedPregunta.getPregunta());

        // Delete
        preguntaService.eliminar(updatedPregunta.getId());

        Pregunta deletedPregunta = preguntaService.buscarPorId(pregunta.getId());
        Assertions.assertNull(deletedPregunta, "La pregunta debería haber sido eliminada.");

        // Clean up the Encuesta
        encuestaService.eliminar(encuesta.getId());

        // Clean up the Jornada
        jornadaService.eliminar(jornada.getId());

        // Clean up the Campaña
        campañaService.eliminar(campaña.getId());

        // Clean up the Barrio
        barrioService.eliminar(barrio.getId());
    }

    // Tests for Reporte entity
    @Test
    @Order(11)
    public void testReporteCRUD() {
        // First create a Usuario for the Reporte (optional)
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("Usuario Para Reporte");
        usuario.setEmail("reporte@example.com");
        usuario.setPassword("password123");
        usuario.setHabilitado(true);
        usuario.setRol(Rol.ADMINISTRADOR);
        usuarioService.crear(usuario);

        // Create
        Reporte reporte = new Reporte();
        reporte.setNombre("Reporte Test");
        reporte.setDescripcion("Descripcion Test");
        reporte.setFechaCreacion(LocalDate.now());
        reporte.setCreadoPor(usuario);

        reporteService.crear(reporte);
        Assertions.assertNotNull(reporte.getId(), "El ID del reporte no debería ser null luego de persistir.");

        // Read
        Reporte foundReporte = reporteService.buscarPorId(reporte.getId());
        Assertions.assertNotNull(foundReporte, "El reporte debería existir.");
        Assertions.assertEquals("Reporte Test", foundReporte.getNombre());

        // Update
        foundReporte.setNombre("Reporte Test Updated");
        reporteService.actualizar(foundReporte);

        Reporte updatedReporte = reporteService.buscarPorId(reporte.getId());
        Assertions.assertEquals("Reporte Test Updated", updatedReporte.getNombre());

        // Delete
        reporteService.eliminar(updatedReporte.getId());

        Reporte deletedReporte = reporteService.buscarPorId(reporte.getId());
        Assertions.assertNull(deletedReporte, "El reporte debería haber sido eliminado.");

        // Clean up the Usuario
        usuarioService.eliminar(usuario.getId());
    }
    */
    // Add more tests for other entities as needed
}
