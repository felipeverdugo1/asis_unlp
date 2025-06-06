package tests_servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Barrio;
import model.Zona;
import service.BarrioService;
import service.ZonaService;
import util.TestDataRegistry;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/tests/DAO/cleanup")
public class EntityCRUDTestsCleanup extends HttpServlet {
    private final BarrioService barrioService = new BarrioService();
    private final ZonaService zonaService = new ZonaService(); // si tenés más, agregalos

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Borra todos los ids que se guardaron en el HASH
        for (Map.Entry<Class<?>, List<Long>> entry : TestDataRegistry.obtenerTodo().entrySet()) {
            Class<?> tipo = entry.getKey();
            for (Long id : entry.getValue()) {
                if (tipo.equals(Barrio.class)) barrioService.eliminar(id);
                else if (tipo.equals(Zona.class)) zonaService.eliminar(id);
                // agregar más clases si las usás
            }
        }

        TestDataRegistry.limpiar();

        List<Barrio> barrios = barrioService.buscarTodosPorCampoLike("nombre", "TEST_%");
        for (Barrio b : barrios) barrioService.eliminar(b.getId());

        resp.getWriter().write("Cleanup completo");
    }
}