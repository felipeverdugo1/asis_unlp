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
import model.Jornada;
import service.JornadaService;


@Path("/jornada")
@Tag(
        name = "Jornada",
        description = "Controller que nos permite hacer operaciones sobre las jornadas"
)
public class JornadaController {

    @Inject
    JornadaService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener todas las jornadas registradas.")
    public Response get() {
        return Response.ok(service.listarTodos()).build();
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener la jornada a partir de un id",
            parameters = @Parameter(name = "jornada id"))
    public Response get(@PathParam("id") Long id) {
        Jornada objeto = service.buscarPorId(id);
        if (objeto != null) {
            return Response.ok(objeto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite crear una jornada.",
            requestBody = @RequestBody(description = "una nueva jornada en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Jornada 1",
                                    summary = "Jornada 1",
                                    value = """
                            {
                               "nombre": "Campaña 1",
                               "fechaInicio": "2025-06-13",
                               "fechaFin": "2025-06-13",
                               "campaña_id": "1",
                               "zonas_id": ["1","3"]
                            }
                            """
                            )}
                    )
            ))
    public Response post(Jornada Jornada) {
        //TODO buscar en la base por id los otros campos y agregarlos al objeto y actualizarlo con service
        service.crear(Jornada);
        return Response.status(Response.Status.CREATED).entity(Jornada).build();
    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite crear una jornada.",
            parameters = @Parameter(name = "jornada id"),
            requestBody = @RequestBody(description = "una jornada en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Jornada 1",
                                    summary = "Jornada 1",
                                    value = """
                            {
                               "nombre": "Campaña 1",
                               "fechaInicio": "2025-06-13",
                               "fechaFin": "2025-06-13",
                               "campaña_id": "1",
                               "zonas_id": ["1","3"]
                            }
                            """
                            )}
                    )
            ))
    public Response put(@PathParam("id") Long id, Jornada Jornada) {
        //TODO buscar en la base por id los otros campos y agregarlos al objeto y actualizarlo con service
        if ( service.buscarPorId(id) != null) {
            service.actualizar(Jornada);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @DELETE
    @Path("{id}")
    @Operation(description = "Este endpoint nos permite obtener la jornada a partir de un id",
            parameters = @Parameter(name = "jornada id"))
    public Response delete(@PathParam("id") Long id) {
        Jornada objeto = service.buscarPorId(id);
        if (objeto != null) {
            service.eliminar(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}


