package controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.ws.rs.*;
import model.Barrio;
import service.BarrioService;
import service.GenericService;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;



@Path("/barrio")
public class BarrioController{

    private final GenericService<Barrio, Long> serviceBarrio;


    public BarrioController() {
        this.serviceBarrio = new BarrioService();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@PathParam("id") Long id) {
        System.out.println("Buscando barrio por id: " + id);
        try {
            Barrio resultado = serviceBarrio.buscarPorId(id);

            if (resultado == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Barrio no encontrado\", \"status\": 404}")
                        .build();
            }

            String jsonResponse = String.format(
                    "{\"id\": %d, \"nombre\": \"%s\"}",
                    resultado.getId(),
                    resultado.getNombre()
            );

            return Response.ok(jsonResponse).build();

        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"ID inválido\", \"status\": 400}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al procesar la solicitud\", \"status\": 500}")
                    .build();
        }
    }

}
