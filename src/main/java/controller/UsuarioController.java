package controller;

import dao.GenericDAO;
import dao.GenericDAOImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Usuarios",
        description = "Controller que nos permite hacer operaciones sobre los usuarios"
)
public class UsuarioController {

    @Inject
    UsuarioService usuarioService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener todos los barrios registrados.")
    public Response getUsuarios() {
        return Response.ok(usuarioService.listarTodos()).build();
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener el usuario a partir de un id",
            parameters = @Parameter(name = "usuario id"))
    public Response getUsuario(@PathParam("id") Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario != null) {
            return Response.ok(usuario).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite crear un usuario.",
            requestBody = @RequestBody(description = "un nuevo usuario en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Usuario 2",
                                    summary = "Usuario 2",
                                    value = """
                            {
                               "nombreUsuario": "usuarionuevo",
                               "email": "usuarionuevo@gmail.com",
                               "password": "supersecreto",
                               "habilitado": true,
                               "roles_id": ["3", "2"],
                               "especialidad": "Panadero" (opcional para referente de org social)
                            }
                            """
                            )}
                    )
            ))
    public Response crearUsuario(Usuario usuario) {
        // TODO id roles mapeo etc
        usuarioService.crear(usuario);
        return Response.status(Response.Status.CREATED).entity(usuario).build();
    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite actualizar un usuario.",
            parameters = @Parameter(name = "usuario id"),
            requestBody = @RequestBody(description = "un usuario en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Usuario 2",
                                    summary = "Usuario 2",
                                    value = """
                            {
                               "nombreUsuario": "usuarionuevo",
                               "email": "usuarionuevo@gmail.com",
                               "password": "supersecreto",
                               "habilitado": true,
                               "roles_id": ["3", "2"],
                               "especialidad": "Panadero" (opcional para referente de org social)
                            }
                            """
                            )}
                    )
            ))
    public Response actualizarUsuario(@PathParam("id") Long id, Usuario usuarioActualizado) {
        // TODO id roles mapeo etc
        if ( usuarioService.buscarPorId(id) != null) {
            usuarioService.actualizar(usuarioActualizado);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @DELETE
    @Path("{id}")
    @Operation(description = "Este endpoint nos permite obtener el usuario a partir de un id",
            parameters = @Parameter(name = "usuario id"))
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

