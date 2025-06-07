package controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Encuesta;
import service.EncuestaService;


@Path("/encuesta")
public class EncuestaController {
    protected final EncuestaService service;


    public EncuestaController() {
        this.service = new EncuestaService();
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
    public Response get(@PathParam("id") Long id) {
        Encuesta objeto = service.buscarPorId(id);
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
    public Response post(Encuesta Encuesta) {
        service.crear(Encuesta);
        return Response.status(Response.Status.CREATED).entity(Encuesta).build();
    }

    // PUT /usuarios/{id} -> Actualizar un usuario
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response put(@PathParam("id") Long id, Encuesta Encuesta) {
        if ( service.buscarPorId(id) != null) {
            service.actualizar(Encuesta);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // DELETE /usuarios/{id} -> Eliminar un usuario
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        Encuesta objeto = service.buscarPorId(id);
        if (objeto != null) {
            service.eliminar(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}


