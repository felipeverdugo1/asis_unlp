package controller;

import controller.dto.UsuarioDTO;
import dao.GenericDAO;
import dao.GenericDAOImpl;
import exceptions.EntidadNoEncontradaException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Usuario;
import service.GenericService;
import service.GenericServiceImpl;
import service.UsuarioService;

import java.util.List;
import java.util.Optional;


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
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        if (usuario.isPresent()) {
            return Response.ok(usuario.get()).build();
        } else {
            throw new EntidadNoEncontradaException("Usuario no encontrado");
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
                               "especialidad": "Panadero (opcional para referente de org social)"
                            }
                            """
                            )}
                    )
            ))
    public Response crearUsuario(UsuarioDTO dto) {
        Usuario usuario = usuarioService.crear(dto);
        return Response.status(Response.Status.CREATED).entity(usuario).build();
    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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
                               "especialidad": "Panadero (opcional para referente de org social)"
                            }
                            """
                            )}
                    )
            ))
    public Response actualizarUsuario(@PathParam("id") Long id, UsuarioDTO dto) {
        usuarioService.actualizar(id, dto);
        return Response.ok().build();
    }


    @DELETE
    @Path("{id}")
    @Operation(description = "Este endpoint nos permite obtener el usuario a partir de un id",
            parameters = @Parameter(name = "usuario id", required = true))
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarUsuario(@PathParam("id") Long id) {
        usuarioService.eliminar(id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/habilitacion/{id}/{habilitacion}")
    @Operation(description = "Este endpoint permite habilitar o deshabilitar un usuario",
            parameters = { @Parameter(name = "usuario id", required = true),
                    @Parameter(name = "habilitacion", required = true) })
    @Produces(MediaType.APPLICATION_JSON)
    public Response habilitarUsuario(@PathParam("id") Long id, @PathParam("habilitacion") Boolean habilitado) {
        Usuario usuario = usuarioService.habilitacionUsuario(id, habilitado);
        return Response.status(Response.Status.CREATED).entity(usuario).build();
    }

    @PUT
    @Path("/agregarRol/{id}/{rol_id}")
    @Operation(description = "Este endpoint permite habilitar o deshabilitar un usuario",
            parameters = { @Parameter(name = "usuario id", required = true),
                    @Parameter(name = "rol_id", required = true) })
    @Produces(MediaType.APPLICATION_JSON)
    public Response agregarRol(@PathParam("id") Long id, @PathParam("rol_id") Long rol_id) {
        Usuario usuario = usuarioService.agregarRol(id, rol_id);
        return Response.status(Response.Status.CREATED).entity(usuario).build();
    }

    @PUT
    @Path("/quitarRol/{id}/{rol_id}")
    @Operation(description = "Este endpoint permite habilitar o deshabilitar un usuario",
            parameters = { @Parameter(name = "usuario id", required = true),
                    @Parameter(name = "rol_id", required = true) })
    @Produces(MediaType.APPLICATION_JSON)
    public Response quitarRol(@PathParam("id") Long id, @PathParam("rol_id") Long rol_id) {
        Usuario usuario = usuarioService.quitarRol(id, rol_id);
        return Response.status(Response.Status.CREATED).entity(usuario).build();
    }

    @GET
    @Path("/rol/{rol_nombre}")
    @Operation(description = "Este endpoint permite obtener todos los usuarios con un rol en especifico",
            parameters = { @Parameter(name = "nombre rol", required = true)})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuariosConRol(@PathParam("rol_nombre") String nombreRol) {
        List<Usuario> usuarios = usuarioService.getUsuariosByRolName(nombreRol);
        return Response.status(Response.Status.OK).entity(usuarios).build();
    }
}

