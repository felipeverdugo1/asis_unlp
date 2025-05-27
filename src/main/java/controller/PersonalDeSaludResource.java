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



@Path("/personal_de_salud")
public class PersonalDeSaludResource {
    protected final GenericDAO<PersonalDeSalud, Integer> personalDAO;


    public PersonalDeSaludResource() {
        this.personalDAO = new GenericDAOImpl<PersonalDeSalud, Integer>() {}; // Inicialización directa para que mi amigo jersey lo pueda ejecutar
    }


    //     GET /usuarios -> Listar todos los usuarios
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarios() {
        return Response.ok(personalDAO.listarTodos()).build();
    }

    // GET /usuarios/{id} -> Obtener un usuario por ID
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuario(@PathParam("id") int id) {
        PersonalDeSalud personalDeSalud = personalDAO.buscarPorId(id);
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
        personalDAO.crear(usuario);
        return Response.status(Response.Status.CREATED).entity(usuario).build();
    }

    // PUT /usuarios/{id} -> Actualizar un usuario
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizarUsuario(@PathParam("id") int id, PersonalDeSalud usuarioActualizado) {
        if ( personalDAO.buscarPorId(id) != null) {
            personalDAO.actualizar(usuarioActualizado);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // DELETE /usuarios/{id} -> Eliminar un usuario
    @DELETE
    @Path("{id}")
    public Response eliminarUsuario(@PathParam("id") int id) {
        PersonalDeSalud personalDeSalud = personalDAO.buscarPorId(id);
        if (personalDeSalud != null) {
            personalDAO.eliminar(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}


