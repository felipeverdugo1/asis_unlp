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
import model.Campaña;
import service.CampañaService;


@Path("/campaña")
@Tag(
        name = "Campañas",
        description = "Controller que nos permite hacer operaciones sobre las campañas"
)
public class CampañaController {

    @Inject
    CampañaService service;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener todas las campañas registradas.")
    public Response get() {
        return Response.ok(service.listarTodos()).build();
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener la campaña a partir de un id",
            parameters = @Parameter(name = "campaña id"))
    public Response get(@PathParam("id") Long id) {
        Campaña objeto = service.buscarPorId(id);
        if (objeto != null) {
            return Response.ok(objeto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite crear una campaña.",
            requestBody = @RequestBody(description = "una nueva campaña en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Campaña 1",
                                    summary = "Campaña 1",
                                    value = """
                            {
                               "nombre": "Campaña 1",
                               "fechaInicio": "2025-06-13",
                               "fechaFin": "2025-06-13",
                               "barrio_id": "1"
                            }
                            """
                            )}
                    )
            ))
    public Response post(Campaña Campaña) {
        // TODO id buscar vos sabe
        service.crear(Campaña);
        return Response.status(Response.Status.CREATED).entity(Campaña).build();
    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite actualizar una campaña.",
            parameters = @Parameter(name = "campaña id"),
            requestBody = @RequestBody(description = "una campaña en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Campaña 1",
                                    summary = "Campaña 1",
                                    value = """
                            {
                               "nombre": "Campaña 1",
                               "fechaInicio": "2025-06-13",
                               "fechaFin": "2025-06-13",
                               "barrio_id": "1",
                               "jornadas_id": ["2", "3"], (opcional)
                               "reportes_id": ["3"] (opcional)
                            }
                            """
                            )}
                    )
            ))
    public Response put(@PathParam("id") Long id, Campaña Campaña) {
        // TODO id buscar vos sabe
        if ( service.buscarPorId(id) != null) {
            service.actualizar(Campaña);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Operation(description = "Este endpoint nos permite eliminar la campaña a partir de un id",
            parameters = @Parameter(name = "campaña id"))
    public Response delete(@PathParam("id") Long id) {
        Campaña objeto = service.buscarPorId(id);
        if (objeto != null) {
            service.eliminar(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}


