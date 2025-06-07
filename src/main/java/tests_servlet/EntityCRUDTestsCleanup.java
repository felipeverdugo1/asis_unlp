package tests_servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import service.*;
import util.TestDataRegistry;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

@WebServlet("/tests/DAO/cleanup")
public class EntityCRUDTestsCleanup extends HttpServlet {
    private final BarrioService barrioService = new BarrioService();
    private final ZonaService zonaService = new ZonaService();
    private final CampañaService campañaService = new CampañaService();
    private final JornadaService jornadaService = new JornadaService();
    private final EncuestaService encuestaService = new EncuestaService();
    private final EncuestadorService encuestadorService = new EncuestadorService();
    private final FiltroPersonalizadoService filtroPersonalizadoService = new FiltroPersonalizadoService();
    private final ReporteService reporteService = new ReporteService();
    private final PreguntaService preguntaService = new PreguntaService();
    private final OrganizacionSocialService organizacionSocialService = new OrganizacionSocialService();
    private final RolService rolService = new RolService();
    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Borra todos los ids que se guardaron en el HASH
        Map<Class<?>, List<Long>> registros = TestDataRegistry.obtenerTodo();
        if (registros != null) {
            for (Map.Entry<Class<?>, List<Long>> entry : registros.entrySet()) {
                Class<?> tipo = entry.getKey();
                for (Long id : entry.getValue()) {
                    if (tipo.equals(Barrio.class)) barrioService.eliminar(id);
                    else if (tipo.equals(Zona.class)) zonaService.eliminar(id);
                    else if (tipo.equals(Campaña.class)) campañaService.eliminar(id);
                    else if (tipo.equals(Jornada.class)) jornadaService.eliminar(id);
                    else if (tipo.equals(Encuesta.class)) encuestaService.eliminar(id);
                    else if (tipo.equals(Encuestador.class)) encuestadorService.eliminar(id);
                    else if (tipo.equals(FiltroPersonalizado.class)) filtroPersonalizadoService.eliminar(id);
                    else if (tipo.equals(Reporte.class)) reporteService.eliminar(id);
                    else if (tipo.equals(Pregunta.class)) preguntaService.eliminar(id);
                    else organizacionSocialService.eliminar(id);
                }
            }
        }

        TestDataRegistry.limpiar();

        List<Barrio> barrios = barrioService.buscarTodosPorCampoLike("nombre", "TEST_%");
        if ( barrios != null ){
            for (Barrio b : barrios) barrioService.eliminar(b.getId());
        }
        List<Usuario> usuarios = usuarioService.buscarTodosPorCampoLike("nombreUsuario", "TEST_%");
        if (usuarios != null) for (Usuario u : usuarios) {
            Set<Rol> roles = new HashSet<>(u.getRoles());
            for (Rol r : roles) {
                    u.quitarRol(r);
            }
            usuarioService.actualizar(u);
            usuarioService.eliminar(u.getId());
        }
        List<Rol> roles = rolService.buscarTodosPorCampoLike("nombre", "TEST_%");
        if (roles != null) for (Rol r : roles) rolService.eliminar(r.getId());
        List<Campaña> campañas = campañaService.buscarTodosPorCampoLike("nombre", "TEST_%");
        if (campañas != null) for (Campaña c : campañas) campañaService.eliminar(c.getId());
        List<Encuestador> encuestadores = encuestadorService.buscarTodosPorCampoLike("nombre", "TEST_%");
        if (encuestadores != null) for (Encuestador a : encuestadores) encuestadorService.eliminar(a.getId());
        List<OrganizacionSocial> organizaciones = organizacionSocialService.buscarTodosPorCampoLike("nombre", "TEST_%");
        if (organizaciones != null) for (OrganizacionSocial a : organizaciones) organizacionSocialService.eliminar(a.getId());
        List<FiltroPersonalizado> filtros = filtroPersonalizadoService.buscarTodosPorCampoLike("nombre", "TEST_%");
        if (filtros != null) for (FiltroPersonalizado a : filtros) filtroPersonalizadoService.eliminar(a.getId());

        resp.getWriter().write("Cleanup completo");
    }
}