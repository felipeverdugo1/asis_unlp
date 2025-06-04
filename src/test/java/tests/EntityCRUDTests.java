package tests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.*;
import org.junit.jupiter.api.*;
import util.TestHibernateUtil;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Tests CRUD operations for all entity classes using the test database.
 * This test class directly uses an EntityManager connected to the test database.
 * 
 * IMPORTANT: Before running these tests, you must manually create a MySQL database named "asis_unlp_test".
 * You can do this with the following SQL command:
 * CREATE DATABASE asis_unlp_test;
 * 
 * The database should use the same credentials as specified in persistence.xml (username: root, password: admin).
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EntityCRUDTests {

    private static EntityManager em;

    @BeforeAll
    public static void setup() {
        // Get an EntityManager connected to the test database
        em = TestHibernateUtil.getEntityManager();
    }

    @AfterAll
    public static void tearDown() {
        // Close the EntityManager
        TestHibernateUtil.closeEntityManager(em);

        // Close the EntityManagerFactory
        TestHibernateUtil.closeEntityManagerFactory();
    }

    /**
     * Helper method to create an entity
     */
    private <T> void crear(T entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    /**
     * Helper method to update an entity
     */
    private <T> void actualizar(T entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    /**
     * Helper method to delete an entity
     */
    private <T> void eliminar(T entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    /**
     * Helper method to find an entity by ID
     */
    private <T> T buscarPorId(Class<T> entityClass, Object id) {
        return em.find(entityClass, id);
    }

    /**
     * Helper method to create an instance of an entity using reflection
     * This bypasses the protected constructor restriction
     */
    private <T> T createInstance(Class<T> entityClass) {
        try {
            Constructor<T> constructor = entityClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Error creating instance of " + entityClass.getName(), e);
        }
    }

    // Tests for Barrio entity
    @Test
    @Order(1)
    public void testBarrioCRUD() {
        // Create
        Barrio barrio = createInstance(Barrio.class);
        barrio.setNombre("Barrio Test");
        barrio.setGeolocalizacion("Geo Test");
        barrio.setInformacion("Info Test");

        crear(barrio);
        Assertions.assertNotNull(barrio.getId(), "El ID del barrio no debería ser null luego de persistir.");

        // Read
        Barrio foundBarrio = buscarPorId(Barrio.class, barrio.getId());
        Assertions.assertNotNull(foundBarrio, "El barrio debería existir.");
        Assertions.assertEquals("Barrio Test", foundBarrio.getNombre());

        // Update
        foundBarrio.setNombre("Barrio Test Updated");
        actualizar(foundBarrio);

        Barrio updatedBarrio = buscarPorId(Barrio.class, barrio.getId());
        Assertions.assertEquals("Barrio Test Updated", updatedBarrio.getNombre());

        // Delete
        eliminar(updatedBarrio);

        Barrio deletedBarrio = buscarPorId(Barrio.class, barrio.getId());
        Assertions.assertNull(deletedBarrio, "El barrio debería haber sido eliminado.");
    }

    // Tests for Usuario entity
    @Test
    @Order(2)
    public void testUsuarioCRUD() {
        // Create
        Usuario usuario = createInstance(Usuario.class);
        usuario.setNombreUsuario("Usuario Test");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");
        usuario.setHabilitado(true);
        usuario.setRol(Rol.ADMINISTRADOR);

        crear(usuario);
        Assertions.assertNotNull(usuario.getId(), "El ID del usuario no debería ser null luego de persistir.");

        // Read
        Usuario foundUsuario = buscarPorId(Usuario.class, usuario.getId());
        Assertions.assertNotNull(foundUsuario, "El usuario debería existir.");
        Assertions.assertEquals("Usuario Test", foundUsuario.getNombreUsuario());

        // Update
        foundUsuario.setNombreUsuario("Usuario Test Updated");
        actualizar(foundUsuario);

        Usuario updatedUsuario = buscarPorId(Usuario.class, usuario.getId());
        Assertions.assertEquals("Usuario Test Updated", updatedUsuario.getNombreUsuario());

        // Delete
        eliminar(updatedUsuario);

        Usuario deletedUsuario = buscarPorId(Usuario.class, usuario.getId());
        Assertions.assertNull(deletedUsuario, "El usuario debería haber sido eliminado.");
    }

    // Tests for Zona entity
    @Test
    @Order(3)
    public void testZonaCRUD() {
        // First create a Barrio for the Zona
        Barrio barrio = createInstance(Barrio.class);
        barrio.setNombre("Barrio Para Zona");
        barrio.setGeolocalizacion("Geo Test");
        barrio.setInformacion("Info Test");
        crear(barrio);

        // Create
        Zona zona = createInstance(Zona.class);
        zona.setNombre("Zona Test");
        zona.setGeolocalizacion("Geo Test Zona");
        zona.setBarrio(barrio);

        crear(zona);
        Assertions.assertNotNull(zona.getId(), "El ID de la zona no debería ser null luego de persistir.");

        // Read
        Zona foundZona = buscarPorId(Zona.class, zona.getId());
        Assertions.assertNotNull(foundZona, "La zona debería existir.");
        Assertions.assertEquals("Zona Test", foundZona.getNombre());

        // Update
        foundZona.setNombre("Zona Test Updated");
        actualizar(foundZona);

        Zona updatedZona = buscarPorId(Zona.class, zona.getId());
        Assertions.assertEquals("Zona Test Updated", updatedZona.getNombre());

        // Delete
        eliminar(updatedZona);

        Zona deletedZona = buscarPorId(Zona.class, zona.getId());
        Assertions.assertNull(deletedZona, "La zona debería haber sido eliminada.");

        // Clean up the Barrio
        eliminar(barrio);
    }

    // Tests for Campaña entity
    @Test
    @Order(4)
    public void testCampañaCRUD() {
        // First create a Barrio for the Campaña
        Barrio barrio = createInstance(Barrio.class);
        barrio.setNombre("Barrio Para Campaña");
        barrio.setGeolocalizacion("Geo Test");
        barrio.setInformacion("Info Test");
        crear(barrio);

        // Create
        Campaña campaña = createInstance(Campaña.class);
        campaña.setNombre("Campaña Test");
        campaña.setFechaInicio(LocalDate.now());
        campaña.setFechaFin(LocalDate.now().plusDays(30));
        campaña.setBarrio(barrio);

        crear(campaña);
        Assertions.assertNotNull(campaña.getId(), "El ID de la campaña no debería ser null luego de persistir.");

        // Read
        Campaña foundCampaña = buscarPorId(Campaña.class, campaña.getId());
        Assertions.assertNotNull(foundCampaña, "La campaña debería existir.");
        Assertions.assertEquals("Campaña Test", foundCampaña.getNombre());

        // Update
        foundCampaña.setNombre("Campaña Test Updated");
        actualizar(foundCampaña);

        Campaña updatedCampaña = buscarPorId(Campaña.class, campaña.getId());
        Assertions.assertEquals("Campaña Test Updated", updatedCampaña.getNombre());

        // Delete
        eliminar(updatedCampaña);

        Campaña deletedCampaña = buscarPorId(Campaña.class, campaña.getId());
        Assertions.assertNull(deletedCampaña, "La campaña debería haber sido eliminada.");

        // Clean up the Barrio
        eliminar(barrio);
    }

    // Tests for Encuesta entity
    @Test
    @Order(5)
    public void testEncuestaCRUD() {
        // First create a Barrio for the Campaña
        Barrio barrio = createInstance(Barrio.class);
        barrio.setNombre("Barrio Para Encuesta");
        barrio.setGeolocalizacion("Geo Test");
        barrio.setInformacion("Info Test");
        crear(barrio);

        // Then create a Campaña for the Jornada
        Campaña campaña = createInstance(Campaña.class);
        campaña.setNombre("Campaña para Jornada");
        campaña.setFechaInicio(LocalDate.now());
        campaña.setFechaFin(LocalDate.now().plusDays(30));
        campaña.setBarrio(barrio);
        crear(campaña);

        // Then create a Jornada for the Encuesta
        Jornada jornada = createInstance(Jornada.class);
        jornada.setFechaInicio(LocalDate.now());
        jornada.setFechaFin(LocalDate.now().plusDays(1));
        jornada.setCampaña(campaña);
        crear(jornada);

        // Create
        Encuesta encuesta = createInstance(Encuesta.class);
        encuesta.setFecha(LocalDate.now());
        encuesta.setJornada(jornada);

        crear(encuesta);
        Assertions.assertNotNull(encuesta.getId(), "El ID de la encuesta no debería ser null luego de persistir.");

        // Read
        Encuesta foundEncuesta = buscarPorId(Encuesta.class, encuesta.getId());
        Assertions.assertNotNull(foundEncuesta, "La encuesta debería existir.");
        Assertions.assertEquals(LocalDate.now(), foundEncuesta.getFecha());

        // Update
        foundEncuesta.setFecha(LocalDate.now().plusDays(1));
        actualizar(foundEncuesta);

        Encuesta updatedEncuesta = buscarPorId(Encuesta.class, encuesta.getId());
        Assertions.assertEquals(LocalDate.now().plusDays(1), updatedEncuesta.getFecha());

        // Delete
        eliminar(updatedEncuesta);

        Encuesta deletedEncuesta = buscarPorId(Encuesta.class, encuesta.getId());
        Assertions.assertNull(deletedEncuesta, "La encuesta debería haber sido eliminada.");

        // Clean up the Jornada
        eliminar(jornada);

        // Clean up the Campaña
        eliminar(campaña);

        // Clean up the Barrio
        eliminar(barrio);
    }

    // Tests for Encuestador entity
    @Test
    @Order(6)
    public void testEncuestadorCRUD() {
        // Create
        Encuestador encuestador = createInstance(Encuestador.class);
        encuestador.setNombre("Encuestador Test");
        encuestador.setDni("12345678");
        encuestador.setEdad(30);
        encuestador.setGenero("Masculino");
        encuestador.setOcupacion("Estudiante");

        crear(encuestador);
        Assertions.assertNotNull(encuestador.getId(), "El ID del encuestador no debería ser null luego de persistir.");

        // Read
        Encuestador foundEncuestador = buscarPorId(Encuestador.class, encuestador.getId());
        Assertions.assertNotNull(foundEncuestador, "El encuestador debería existir.");
        Assertions.assertEquals("Encuestador Test", foundEncuestador.getNombre());

        // Update
        foundEncuestador.setNombre("Encuestador Test Updated");
        actualizar(foundEncuestador);

        Encuestador updatedEncuestador = buscarPorId(Encuestador.class, encuestador.getId());
        Assertions.assertEquals("Encuestador Test Updated", updatedEncuestador.getNombre());

        // Delete
        eliminar(updatedEncuestador);

        Encuestador deletedEncuestador = buscarPorId(Encuestador.class, encuestador.getId());
        Assertions.assertNull(deletedEncuestador, "El encuestador debería haber sido eliminado.");
    }

    // Tests for FiltroPersonalizado entity
    @Test
    @Order(7)
    public void testFiltroPersonalizadoCRUD() {
        // First create a Usuario for the FiltroPersonalizado
        Usuario usuario = createInstance(Usuario.class);
        usuario.setNombreUsuario("Usuario Para Filtro");
        usuario.setEmail("filtro@example.com");
        usuario.setPassword("password123");
        usuario.setHabilitado(true);
        usuario.setRol(Rol.ADMINISTRADOR);
        crear(usuario);

        // Create
        FiltroPersonalizado filtro = createInstance(FiltroPersonalizado.class);
        filtro.setNombre("Filtro Test");
        filtro.setCriterios("criterio1=valor1;criterio2=valor2");
        filtro.setUsuario(usuario);

        crear(filtro);
        Assertions.assertNotNull(filtro.getId(), "El ID del filtro no debería ser null luego de persistir.");

        // Read
        FiltroPersonalizado foundFiltro = buscarPorId(FiltroPersonalizado.class, filtro.getId());
        Assertions.assertNotNull(foundFiltro, "El filtro debería existir.");
        Assertions.assertEquals("Filtro Test", foundFiltro.getNombre());

        // Update
        foundFiltro.setNombre("Filtro Test Updated");
        actualizar(foundFiltro);

        FiltroPersonalizado updatedFiltro = buscarPorId(FiltroPersonalizado.class, filtro.getId());
        Assertions.assertEquals("Filtro Test Updated", updatedFiltro.getNombre());

        // Delete
        eliminar(updatedFiltro);

        FiltroPersonalizado deletedFiltro = buscarPorId(FiltroPersonalizado.class, filtro.getId());
        Assertions.assertNull(deletedFiltro, "El filtro debería haber sido eliminado.");

        // Clean up the Usuario
        eliminar(usuario);
    }

    // Tests for Jornada entity
    @Test
    @Order(8)
    public void testJornadaCRUD() {
        // First create a Barrio for the Campaña
        Barrio barrio = createInstance(Barrio.class);
        barrio.setNombre("Barrio Para Jornada");
        barrio.setGeolocalizacion("Geo Test");
        barrio.setInformacion("Info Test");
        crear(barrio);

        // Then create a Campaña for the Jornada
        Campaña campaña = createInstance(Campaña.class);
        campaña.setNombre("Campaña Para Jornada");
        campaña.setFechaInicio(LocalDate.now());
        campaña.setFechaFin(LocalDate.now().plusDays(30));
        campaña.setBarrio(barrio);
        crear(campaña);

        // Create
        Jornada jornada = createInstance(Jornada.class);
        jornada.setFechaInicio(LocalDate.now());
        jornada.setFechaFin(LocalDate.now().plusDays(1));
        jornada.setCampaña(campaña);

        crear(jornada);
        Assertions.assertNotNull(jornada.getId(), "El ID de la jornada no debería ser null luego de persistir.");

        // Read
        Jornada foundJornada = buscarPorId(Jornada.class, jornada.getId());
        Assertions.assertNotNull(foundJornada, "La jornada debería existir.");
        Assertions.assertEquals(LocalDate.now(), foundJornada.getFechaInicio());

        // Update
        foundJornada.setFechaInicio(LocalDate.now().plusDays(1));
        actualizar(foundJornada);

        Jornada updatedJornada = buscarPorId(Jornada.class, jornada.getId());
        Assertions.assertEquals(LocalDate.now().plusDays(1), updatedJornada.getFechaInicio());

        // Delete
        eliminar(updatedJornada);

        Jornada deletedJornada = buscarPorId(Jornada.class, jornada.getId());
        Assertions.assertNull(deletedJornada, "La jornada debería haber sido eliminada.");

        // Clean up the Campaña
        eliminar(campaña);

        // Clean up the Barrio
        eliminar(barrio);
    }

    // Tests for OrganizacionSocial entity
    @Test
    @Order(9)
    public void testOrganizacionSocialCRUD() {
        // First create a Barrio for the OrganizacionSocial (optional)
        Barrio barrio = createInstance(Barrio.class);
        barrio.setNombre("Barrio Para Organizacion");
        barrio.setGeolocalizacion("Geo Test");
        barrio.setInformacion("Info Test");
        crear(barrio);

        // Create
        OrganizacionSocial organizacion = createInstance(OrganizacionSocial.class);
        organizacion.setNombre("Organizacion Test");
        organizacion.setDomicilio("Domicilio Test");
        organizacion.setActividadPrincipal("Actividad Test");
        organizacion.setBarrio(barrio);

        crear(organizacion);
        Assertions.assertNotNull(organizacion.getId(), "El ID de la organización no debería ser null luego de persistir.");

        // Read
        OrganizacionSocial foundOrganizacion = buscarPorId(OrganizacionSocial.class, organizacion.getId());
        Assertions.assertNotNull(foundOrganizacion, "La organización debería existir.");
        Assertions.assertEquals("Organizacion Test", foundOrganizacion.getNombre());

        // Update
        foundOrganizacion.setNombre("Organizacion Test Updated");
        actualizar(foundOrganizacion);

        OrganizacionSocial updatedOrganizacion = buscarPorId(OrganizacionSocial.class, organizacion.getId());
        Assertions.assertEquals("Organizacion Test Updated", updatedOrganizacion.getNombre());

        // Delete
        eliminar(updatedOrganizacion);

        OrganizacionSocial deletedOrganizacion = buscarPorId(OrganizacionSocial.class, organizacion.getId());
        Assertions.assertNull(deletedOrganizacion, "La organización debería haber sido eliminada.");

        // Clean up the Barrio
        eliminar(barrio);
    }

    // Tests for Pregunta entity
    @Test
    @Order(10)
    public void testPreguntaCRUD() {
        // First create a Barrio for the Campaña
        Barrio barrio = createInstance(Barrio.class);
        barrio.setNombre("Barrio Para Pregunta");
        barrio.setGeolocalizacion("Geo Test");
        barrio.setInformacion("Info Test");
        crear(barrio);

        // Then create a Campaña for the Jornada
        Campaña campaña = createInstance(Campaña.class);
        campaña.setNombre("Campaña Para Pregunta");
        campaña.setFechaInicio(LocalDate.now());
        campaña.setFechaFin(LocalDate.now().plusDays(30));
        campaña.setBarrio(barrio);
        crear(campaña);

        // Then create a Jornada for the Encuesta
        Jornada jornada = createInstance(Jornada.class);
        jornada.setFechaInicio(LocalDate.now());
        jornada.setFechaFin(LocalDate.now().plusDays(1));
        jornada.setCampaña(campaña);
        crear(jornada);

        // Then create an Encuesta for the Pregunta
        Encuesta encuesta = createInstance(Encuesta.class);
        encuesta.setFecha(LocalDate.now());
        encuesta.setJornada(jornada);
        crear(encuesta);

        // Create
        Pregunta pregunta = createInstance(Pregunta.class);
        pregunta.setTipo("Multiple Choice");
        pregunta.setPregunta("¿Cuál es su edad?");
        pregunta.setRespuesta("30");
        pregunta.setEncuesta(encuesta);

        crear(pregunta);
        Assertions.assertNotNull(pregunta.getId(), "El ID de la pregunta no debería ser null luego de persistir.");

        // Read
        Pregunta foundPregunta = buscarPorId(Pregunta.class, pregunta.getId());
        Assertions.assertNotNull(foundPregunta, "La pregunta debería existir.");
        Assertions.assertEquals("¿Cuál es su edad?", foundPregunta.getPregunta());

        // Update
        foundPregunta.setPregunta("¿Cuál es su nombre?");
        actualizar(foundPregunta);

        Pregunta updatedPregunta = buscarPorId(Pregunta.class, pregunta.getId());
        Assertions.assertEquals("¿Cuál es su nombre?", updatedPregunta.getPregunta());

        // Delete
        eliminar(updatedPregunta);

        Pregunta deletedPregunta = buscarPorId(Pregunta.class, pregunta.getId());
        Assertions.assertNull(deletedPregunta, "La pregunta debería haber sido eliminada.");

        // Clean up the Encuesta
        eliminar(encuesta);

        // Clean up the Jornada
        eliminar(jornada);

        // Clean up the Campaña
        eliminar(campaña);

        // Clean up the Barrio
        eliminar(barrio);
    }

    // Tests for Reporte entity
    @Test
    @Order(11)
    public void testReporteCRUD() {
        // First create a Usuario for the Reporte (optional)
        Usuario usuario = createInstance(Usuario.class);
        usuario.setNombreUsuario("Usuario Para Reporte");
        usuario.setEmail("reporte@example.com");
        usuario.setPassword("password123");
        usuario.setHabilitado(true);
        usuario.setRol(Rol.ADMINISTRADOR);
        crear(usuario);

        // Create
        Reporte reporte = createInstance(Reporte.class);
        reporte.setNombre("Reporte Test");
        reporte.setDescripcion("Descripcion Test");
        reporte.setFechaCreacion(LocalDate.now());
        reporte.setCreadoPor(usuario);

        crear(reporte);
        Assertions.assertNotNull(reporte.getId(), "El ID del reporte no debería ser null luego de persistir.");

        // Read
        Reporte foundReporte = buscarPorId(Reporte.class, reporte.getId());
        Assertions.assertNotNull(foundReporte, "El reporte debería existir.");
        Assertions.assertEquals("Reporte Test", foundReporte.getNombre());

        // Update
        foundReporte.setNombre("Reporte Test Updated");
        actualizar(foundReporte);

        Reporte updatedReporte = buscarPorId(Reporte.class, reporte.getId());
        Assertions.assertEquals("Reporte Test Updated", updatedReporte.getNombre());

        // Delete
        eliminar(updatedReporte);

        Reporte deletedReporte = buscarPorId(Reporte.class, reporte.getId());
        Assertions.assertNull(deletedReporte, "El reporte debería haber sido eliminado.");

        // Clean up the Usuario
        eliminar(usuario);
    }

    // Add more tests for other entities as needed
}
