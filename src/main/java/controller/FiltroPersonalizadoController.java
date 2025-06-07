package controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.FiltroPersonalizado;
import service.FiltroPersonalizadoService;


@Path("/filtro")
public class FiltroPersonalizadoController {
    protected final FiltroPersonalizadoService service;


    public FiltroPersonalizadoController() {
        this.service = new FiltroPersonalizadoService();
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
        FiltroPersonalizado objeto = service.buscarPorId(id);
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
    public Response post(FiltroPersonalizado filtroPersonalizado) {
        service.crear(filtroPersonalizado);
        return Response.status(Response.Status.CREATED).entity(filtroPersonalizado).build();
    }

    // PUT /usuarios/{id} -> Actualizar un usuario
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response put(@PathParam("id") Long id, FiltroPersonalizado filtroPersonalizado) {
        if ( service.buscarPorId(id) != null) {
            service.actualizar(filtroPersonalizado);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // DELETE /usuarios/{id} -> Eliminar un usuario
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        FiltroPersonalizado objeto = service.buscarPorId(id);
        if (objeto != null) {
            service.eliminar(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}


