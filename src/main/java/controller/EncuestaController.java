package controller;

import controller.dto.EncuestaDTO;
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
import model.Encuesta;
import model.Encuestador;
import service.EncuestaService;

import java.util.Optional;


@Path("/encuesta")
@Tag(
        name = "Encuesta",
        description = "Controller que nos permite hacer operaciones sobre las encuestas"
)
public class EncuestaController {

    @Inject
    EncuestaService service;
    @Inject
    private EncuestaService encuestaService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener todas las encuestas registradas.")
    public Response get() {
        return Response.ok(service.listarTodos()).build();
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener la encuesta a partir de un id",
            parameters = @Parameter(name = "encuesta id"))
    public Response get(@PathParam("id") Long id) {
        Optional<Encuesta> objeto = service.buscarPorId(id);
        if (objeto.isPresent()) {
            return Response.ok(objeto.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite crear una encuesta, teniendo antes creados encuestador, zona y jornada.",
            requestBody = @RequestBody(description = "una nueva encuesta en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Encuesta 1",
                                    summary = "Encuesta 1",
                                    value = """
                            {
                               "nombreUnico": "/path/a/encuesta.xlsx",
                               "fecha": "2025-06-13",
                               "encuestador_id": 3,
                               "zona_id": 1,
                               "jornada_id": 1
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
    public Response post(EncuestaDTO encuestaDTO) {
        //TODO buscar en la base por id los otros campos y agregarlos al objeto y actualizarlo con barrioService
        Encuesta encuesta = service.crear(encuestaDTO);
        return Response.status(Response.Status.CREATED).entity(encuesta).build();
    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite actualizar una encuesta",
            parameters = @Parameter(name = "encuesta id"),
            requestBody = @RequestBody(description = "una encuesta en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Encuesta 1",
                                    summary = "Encuesta 1",
                                    value = """
                            {
                               "nombreUnico": "/path/a/encuesta.xlsx",
                               "fecha": "2025-06-13",
                               "encuestador_id": 3,
                               "zona_id": 1,
                               "jornada_id": 1
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
    public Response put(@PathParam("id") Long id, EncuestaDTO encuestaDTO) {
        Encuesta encuesta = encuestaService.actualizar(id, encuestaDTO);
        return Response.status(Response.Status.OK).entity(encuesta).build();
    }


    @DELETE
    @Path("{id}")
    @Operation(description = "Este endpoint nos permite eliminar la encuesta a partir de un id",
            parameters = @Parameter(name = "encuesta id"))
    public Response delete(@PathParam("id") Long id) {
        Optional<Encuesta> objeto = service.buscarPorId(id);
        if (objeto.isPresent()) {
            service.eliminar(objeto.get());
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}


