package controller;

import controller.dto.RolDTO;
import controller.dto.ZonaDTO;
import exceptions.EntidadNoEncontradaException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Rol;
import service.RolService;

import java.util.Optional;


@Path("/rol")
@Tag(
        name = "Roles",
        description = "Controller que nos permite hacer operaciones sobre los roles"
)
public class RolController {

    @Inject
    RolService service;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener todas los roles registrados.")
    public Response get() {
        return Response.ok(service.listarTodos()).build();
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener el rol a partir de un id",
            parameters = @Parameter(name = "zona id"))
    public Response get(@PathParam("id") Long id) {
        Optional<Rol> objeto = service.buscarPorId(id);
        if (objeto.isPresent()) {
            return Response.ok(objeto.get()).build();
        } else {
            throw new EntidadNoEncontradaException("No existe el rol.");
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite crear un rol.",
            requestBody = @RequestBody(description = "un nuevo rol en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Rol de prueba",
                                    summary = "Rol de prueba",
                                    value = """
                            {
                               "nombre": "rol_prueba"
                            }
                            """
                            )}
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Creacion exitosa"),
                    @ApiResponse(responseCode = "400", description = "Error de validacion."),
                    @ApiResponse(responseCode = "500", description = "Error interno.")
            }
    )
    public Response post(@Valid RolDTO dto) {
        Rol rol = service.crear(dto);
        return Response.status(Response.Status.CREATED).entity(rol).build();
    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite actualizar un rol.",
            parameters = @Parameter(name = "rol id"),
            requestBody = @RequestBody(description = "una zona en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Rol de prueba",
                                    summary = "Rol de prueba",
                                    value = """
                            {
                               "nombre": "rol_prueba_actualizado"
                            }
                            """
                            )}
                    )
            ))
    public Response put(@PathParam("id") Long id, RolDTO dto) {
        Rol zona = service.actualizar(id, dto);
        return Response.ok().build();
    }


    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite eliminar un rol a partir de un id",
            parameters = @Parameter(name = "rol id"))
    public Response delete(@PathParam("id") Long id) {
        service.eliminar(id);
        return Response.noContent().build();
    }
}


