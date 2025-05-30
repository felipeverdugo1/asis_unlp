package controller;


import dao.GenericDAO;
import dao.GenericDAOImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.PersonalDeSalud;
import service.GenericService;
import service.GenericServiceImpl;

import java.util.List;

@Path("/generic")
public class GenericResource {

    private   GenericServiceImpl<PersonalDeSalud, Integer> usuarioService ;





    // GET /usuarios -> Listar todos los usuarios
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarios() {
        return Response.ok(usuarioService.listarTodos()).build();
    }

    // GET /usuarios/{id} -> Obtener un usuario por ID
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuario(@PathParam("id") int id) {
        PersonalDeSalud personalDeSalud = usuarioService.buscarPorId(id);
        if (personalDeSalud != null) {
            return Response.ok(personalDeSalud).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // POST /usuarios -> Crear un nuevo usuario
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearUsuario(PersonalDeSalud usuario) {
        usuarioService.crear(usuario);
        return Response.status(Response.Status.CREATED).entity(usuario).build();
    }

    // PUT /usuarios/{id} -> Actualizar un usuario
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizarUsuario(@PathParam("id") int id, PersonalDeSalud usuarioActualizado) {
        if ( usuarioService.actualizar(usuarioActualizado) != null) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

//    // DELETE /usuarios/{id} -> Eliminar un usuario
//    @DELETE
//    @Path("{id}")
//    public Response eliminarUsuario(@PathParam("id") int id) {
//        Usuario usuario = usuarioDAO.buscarPorId(id);
//        if (usuario != null) {
//            usuarioDAO.eliminar(id);
//            return Response.noContent().build();
//        } else {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//    }
}


