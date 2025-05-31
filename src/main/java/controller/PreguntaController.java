package controller;

import dao.GenericDAOImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Pregunta;
import service.GenericService;
import service.GenericServiceImpl;


@Path("/pregunta")
public class PreguntaController {
    protected final GenericService<Pregunta, Integer> service;


    public PreguntaController() {
        this.service = new GenericServiceImpl<Pregunta, Integer>(new GenericDAOImpl<Pregunta, Integer>() {
        }) {};;
    }


    //  GET /usuarios -> Listar todos los usuarios
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        return Response.ok(service.listarTodos()).build();
    }

    // GET /usuarios/{id} -> Obtener un usuario por ID
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id) {
        Pregunta objeto = service.buscarPorId(id);
        if (objeto != null) {
            return Response.ok(objeto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // POST /usuarios -> Crear un nuevo usuario
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Pregunta Pregunta) {
        service.crear(Pregunta);
        return Response.status(Response.Status.CREATED).entity(Pregunta).build();
    }

    // PUT /usuarios/{id} -> Actualizar un usuario
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response put(@PathParam("id") int id, Pregunta Pregunta) {
        if ( service.buscarPorId(id) != null) {
            service.actualizar(Pregunta);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // DELETE /usuarios/{id} -> Eliminar un usuario
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") int id) {
        Pregunta objeto = service.buscarPorId(id);
        if (objeto != null) {
            service.eliminar(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}


