package tests_servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import service.*;
import java.io.IOException;
import util.TestDataRegistry;

@WebServlet(
        name="DAO Tests",
        description="Tests de persistencia",
        urlPatterns = "/tests/DAO"
)
public class EntityCRUDTests extends HttpServlet {

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

        Rol administrador = new Rol("ADMINISTRADOR");
        Rol visitante = new Rol("VISITANTE");
        Rol personalSalud = new Rol("PERSONAL_SALUD");
        Rol orgSocial = new Rol("ORG_SOCIAL");

        rolService.crear(administrador);
        rolService.crear(visitante);
        rolService.crear(personalSalud);
        rolService.crear(orgSocial);

        // configuracion de hibernate para usar otra base para los tests

    }

    public static void finish(){
        //limpiar la base de pruebas
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder result = new StringBuilder();
        resp.setContentType("text/plain;charset=UTF-8");

        // crear inicializar la base y los services
        setup();

        try {
            // Se crea un Barrio de prueba
            // se analiza el CRUD del mismo
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
            barrio = barrioService.buscarPorId(barrio.getId());
            // Un barrio tiene varias zonas
            Zona zona1 = new Zona();
            zona1.setNombre("Zona Test 1");
            zona1.setGeolocalizacion("Geo Test Zona");
            zona1.setBarrio(barrio);
            zonaService.crear(zona1);
            TestDataRegistry.registrar(Zona.class, zona1.getId());
            Zona zona2 = new Zona();
            zona2.setNombre("Zona Test 2");
            zona2.setGeolocalizacion("Geo Test Zona");
            zona2.setBarrio(barrio);
            zonaService.crear(zona2);
            TestDataRegistry.registrar(Zona.class, zona2.getId());
            Zona zona3 = new Zona();
            zona3.setNombre("Zona Test 3");
            zona3.setGeolocalizacion("Geo Test Zona");
            zona3.setBarrio(barrio);
            zonaService.crear(zona3);
            TestDataRegistry.registrar(Zona.class, zona3.getId());
            barrio.agregarZona(zona1);
            barrio.agregarZona(zona2);
            barrio.agregarZona(zona3);

            result.append("Barrio creado con zonas: ").append(barrioService.buscarPorId(barrio.getId()).toString()).append("\n");
        } catch (Exception e) {
            result.append("ERROR: ").append(e.getMessage());
            e.printStackTrace(resp.getWriter());
        }

        resp.getWriter().println(result.toString());
    }

}