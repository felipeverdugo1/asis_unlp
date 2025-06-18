package controller;

import controller.dto.EncuestadorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Barrio;
import model.Encuestador;
import service.EncuestadorService;

import java.util.Optional;


@Path("/encuestador")
@Tag(
        name = "Encuestador",
        description = "Controller que nos permite hacer operaciones sobre los encuestadores"
)
public class EncuestadorController {

    @Inject
    EncuestadorService service;
    @Inject
    private EncuestadorService encuestadorService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener todos los encuestadores registrados.")
    public Response get() {
        return Response.ok(service.listarTodos()).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener el encuestador a partir de un id",
            parameters = @Parameter(name = "encuestador id"))
    public Response get(@PathParam("id") Long id) {
        Optional<Encuestador> objeto = service.buscarPorId(id);
        if (objeto.isPresent()) {
            return Response.ok(objeto.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite crear un encuestador.",
            requestBody = @RequestBody(description = "un nuevo encuestador en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Encuestador de prueba",
                                    summary = "Encuestador de prueba",
                                    value = """
                            {
                               "nombre": "Señor Encuestador",
                               "dni": "35648548",
                               "edad": 28,
                               "genero": "Masculino",
                               "ocupacion": "Lector de diarios"
                            }
                            """
                            )}
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Actualizacion exitosa"),
                    @ApiResponse(responseCode = "400", description = "Error de validacion."),
                    @ApiResponse(responseCode = "500", description = "Error interno.")
            }
    )
    public Response post(EncuestadorDTO encuestadorDTO) {
        Encuestador encuestador = service.crear(encuestadorDTO);
        return Response.status(Response.Status.CREATED).entity(encuestador).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite actualizar un encuestador.",
            parameters = @Parameter(name = "encuestador id"),
            requestBody = @RequestBody(description = "un encuestador en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Encuestador de prueba",
                                    summary = "Encuestador de prueba",
                                    value = """
                            {
                               "nombre": "Señor Encuestador",
                               "dni": "35648548",
                               "edad": "28",
                               "genero": "Masculino",
                               "ocupacion": "Lector de diarios"
                            }
                            """
                            )}
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Actualizacion exitosa"),
                    @ApiResponse(responseCode = "400", description = "Error de validacion."),
                    @ApiResponse(responseCode = "500", description = "Error interno.")
            }
    )
    public Response put(@PathParam("id") Long id, EncuestadorDTO encuestadorDTO) {
        Encuestador encuestador = encuestadorService.actualizar(id, encuestadorDTO);
        return Response.status(Response.Status.OK).entity(encuestador).build();
    }

    @DELETE
    @Path("{id}")
    @Operation(description = "Este endpoint nos permite eliminar el encuestador a partir de un id",
            parameters = @Parameter(name = "encuestador id"))
    public Response delete(@PathParam("id") Long id) {
        encuestadorService.eliminar(id);
        return Response.noContent().build();
    }
}


