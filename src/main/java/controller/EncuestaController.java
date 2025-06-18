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
import model.Encuesta;
import service.EncuestaService;


@Path("/encuesta")
@Tag(
        name = "Encuesta",
        description = "Controller que nos permite hacer operaciones sobre las encuestas"
)
public class EncuestaController {

    @Inject
    EncuestaService service;


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
        Encuesta objeto = service.buscarPorId(id);
        if (objeto != null) {
            return Response.ok(objeto).build();
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
                               "encuestador_id": "3",
                               "zona_id": "1",
                               "jornada_id": "1"
                            }
                            """
                            )}
                    )
            ))
    public Response post(Encuesta Encuesta) {
        //TODO buscar en la base por id los otros campos y agregarlos al objeto y actualizarlo con service
        service.crear(Encuesta);
        return Response.status(Response.Status.CREATED).entity(Encuesta).build();
    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
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
                               "encuestador_id": "3",
                               "zona_id": "1",
                               "jornada_id": "1",
                               "preguntas_id": ["3", "4"] (opcional)
                            }
                            """
                            )}
                    )
            ))
    public Response put(@PathParam("id") Long id, Encuesta Encuesta) {
        //TODO buscar en la base por id los otros campos y agregarlos al objeto y actualizarlo con service
        if ( service.buscarPorId(id) != null) {
            service.actualizar(Encuesta);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @DELETE
    @Path("{id}")
    @Operation(description = "Este endpoint nos permite eliminar la encuesta a partir de un id",
            parameters = @Parameter(name = "encuesta id"))
    public Response delete(@PathParam("id") Long id) {
        Encuesta objeto = service.buscarPorId(id);
        if (objeto != null) {
            service.eliminar(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}


