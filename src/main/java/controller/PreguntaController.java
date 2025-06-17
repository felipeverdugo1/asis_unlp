package controller;

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
import service.PreguntaService;


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
        Pregunta objeto = service.buscarPorId(id);
        if (objeto != null) {
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
    public Response post(Pregunta Pregunta) {
        //TODO buscar en la base por id los otros campos y agregarlos al objeto y actualizarlo con service
        service.crear(Pregunta);
        return Response.status(Response.Status.CREATED).entity(Pregunta).build();
    }


    @PUT
    @Path("{id}")
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
    public Response put(@PathParam("id") Long id, Pregunta Pregunta) {
        //TODO buscar en la base por id los otros campos y agregarlos al objeto y actualizarlo con service
        if ( service.buscarPorId(id) != null) {
            service.actualizar(Pregunta);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @DELETE
    @Path("{id}")
    @Operation(description = "Este endpoint nos permite eliminar la pregunta a partir de un id",
            parameters = @Parameter(name = "pregunta id"))
    public Response delete(@PathParam("id") Long id) {
        Pregunta objeto = service.buscarPorId(id);
        if (objeto != null) {
            service.eliminar(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}


