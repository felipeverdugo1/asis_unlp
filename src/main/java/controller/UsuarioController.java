package controller;

import dao.GenericDAO;
import dao.GenericDAOImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Usuario;
import service.GenericService;
import service.GenericServiceImpl;
import service.UsuarioService;


@Path("/usuario")
@ApplicationScoped
public class UsuarioController {

    @Inject
    UsuarioService usuarioService;


    public UsuarioController() {
    }


    //  GET /usuarios -> Listar todos los usuarios
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarios() {
        return Response.ok(usuarioService.listarTodos()).build();
    }

    // GET /usuarios/{id} -> Obtener un usuario por ID
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuario(@PathParam("id") Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario != null) {
            return Response.ok(usuario).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // POST /usuarios -> Crear un nuevo usuario
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearUsuario(Usuario usuario) {
        usuarioService.crear(usuario);
        return Response.status(Response.Status.CREATED).entity(usuario).build();
    }

    // PUT /usuarios/{id} -> Actualizar un usuario
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizarUsuario(@PathParam("id") Long id, Usuario usuarioActualizado) {
        if ( usuarioService.buscarPorId(id) != null) {
            usuarioService.actualizar(usuarioActualizado);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // DELETE /usuarios/{id} -> Eliminar un usuario
    @DELETE
    @Path("{id}")
    public Response eliminarUsuario(@PathParam("id") Long id) {
        Usuario personalDeSalud = usuarioService.buscarPorId(id);
        if (personalDeSalud != null) {
            usuarioService.eliminar(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}

