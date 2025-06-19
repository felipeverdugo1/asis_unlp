package controller;

import controller.dto.PreguntaDTO;
import controller.dto.ReporteDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Pregunta;
import model.Reporte;
import service.PreguntaService;

import java.util.Optional;


@Path("/pregunta")
@Tag(
        name = "Pregunta",
        description = "Controller que nos permite hacer operaciones sobre las preguntas"
)
public class PreguntaController {

    @Inject
    PreguntaService service;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener todas las preguntas registradas.")
    public Response get() {
        return Response.ok(service.listarTodos()).build();
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener la pregunta a partir de un id",
            parameters = @Parameter(name = "pregunta id"))
    public Response get(@PathParam("id") Long id) {
        Optional<Pregunta> objeto = service.buscarPorId(id);
        if (objeto.isPresent()) {
            return Response.ok(objeto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite crear una pregunta.",
            requestBody = @RequestBody(description = "una nueva pregunta en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Pregunta",
                                    summary = "Pregunta",
                                    value = """
                            {
                               "tipo": "salud",
                               "pregunta": "¿Toma medicacion?",
                               "respuesta": "Si",
                               "encuesta_id": "1"
                            }
                            """
                            )}
                    )
            ))
    public Response post(PreguntaDTO dto) {
        //TODO buscar en la base por id los otros campos y agregarlos al objeto y actualizarlo con barrioService
        Pregunta Pregunta = service.crear(dto);
        return Response.status(Response.Status.CREATED).entity(Pregunta).build();
    }


    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite actualizar una pregunta.",
            parameters = @Parameter(name = "pregunta id"),
            requestBody = @RequestBody(description = "una pregunta en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Pregunta",
                                    summary = "Pregunta",
                                    value = """
                            {
                               "tipo": "salud",
                               "pregunta": "¿Toma medicacion?",
                               "respuesta": "Si",
                               "encuesta_id": "1"
                            }
                            """
                            )}
                    )
            ))
    public Response put(@PathParam("id") Long id, PreguntaDTO dto) {
        Pregunta pregunta = service.actualizar(id, dto);
        return Response.ok().build();
    }


    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite eliminar la pregunta a partir de un id",
            parameters = @Parameter(name = "pregunta id"))
    public Response delete(@PathParam("id") Long id) {
        service.eliminar(id);
        return Response.noContent().build();
    }
}


