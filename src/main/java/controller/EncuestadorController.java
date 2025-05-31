package controller;

import com.mysql.cj.log.Log;
import dao.GenericDAOImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Encuestador;
import service.GenericService;
import service.GenericServiceImpl;


@Path("/encuestador")
public class EncuestadorController {
    protected final GenericService<Encuestador, Integer> service;


    public EncuestadorController() {
        this.service = new GenericServiceImpl<Encuestador, Integer>(new GenericDAOImpl<Encuestador, Integer>() {
        }) {};
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        return Response.ok(service.listarTodos()).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id) {
        Encuestador objeto = service.buscarPorId(id);
        if (objeto != null) {
            return Response.ok(objeto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Encuestador Encuestador) {
        service.crear(Encuestador);
        return Response.status(Response.Status.CREATED).entity(Encuestador).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response put(@PathParam("id") int id, Encuestador Encuestador) {
        if ( service.buscarPorId(id) != null) {
            service.actualizar(Encuestador);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") int id) {
        Encuestador objeto = service.buscarPorId(id);
        if (objeto != null) {
            service.eliminar(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}


