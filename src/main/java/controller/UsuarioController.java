package controller;

import dao.GenericDAO;
import dao.GenericDAOImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Usuario;


@Path("/usuario")
public class UsuarioController {
    protected final GenericDAO<Usuario, Integer> usuarioDAO;


    public UsuarioController() {
        this.usuarioDAO = new GenericDAOImpl<Usuario, Integer>() {}; // Inicialización directa para que mi amigo jersey lo pueda ejecutar
    }


    //  GET /usuarios -> Listar todos los usuarios
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarios() {
        return Response.ok(usuarioDAO.listarTodos()).build();
    }

    // GET /usuarios/{id} -> Obtener un usuario por ID
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuario(@PathParam("id") int id) {
        Usuario usuario = usuarioDAO.buscarPorId(id);
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
        usuarioDAO.crear(usuario);
        return Response.status(Response.Status.CREATED).entity(usuario).build();
    }

    // PUT /usuarios/{id} -> Actualizar un usuario
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizarUsuario(@PathParam("id") int id, Usuario usuarioActualizado) {
        if ( usuarioDAO.buscarPorId(id) != null) {
            usuarioDAO.actualizar(usuarioActualizado);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // DELETE /usuarios/{id} -> Eliminar un usuario
    @DELETE
    @Path("{id}")
    public Response eliminarUsuario(@PathParam("id") int id) {
        Usuario personalDeSalud = usuarioDAO.buscarPorId(id);
        if (personalDeSalud != null) {
            usuarioDAO.eliminar(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}


