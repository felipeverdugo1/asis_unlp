package controller;

import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Barrio;
import service.BarrioService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Path("/barrio")
@Tag(
    name = "Barrios",
    description = "Controller que nos permite hacer operaciones sobre los barrios"
)
public class BarrioController {

    @Inject
    BarrioService barrioService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener todos los barrios registrados.")
    public Response get() {
        return Response.ok(barrioService.listarTodos()).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener el barrio a partir de un id",
            parameters = @Parameter(name = "barrio id"))
    public Response get(@PathParam("id") Long id) {
        Barrio objeto = barrioService.buscarPorId(id);
        if (objeto != null) {
            return Response.ok(objeto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite crear un barrio.",
    requestBody = @RequestBody(description = "un nuevo barrio en formato JSON",
            required = true,
            content = @Content(
                mediaType = "application/json",
                examples = {@ExampleObject(
                        name = "Barrio Las Acollaradas",
                        summary = "Barrio Las Acollaradas",
                        value = """
                            {
                               "nombre": "Barrio Las Acollaradas",
                               "geolocalizacion": "-36.234075, -61.120841",
                               "informacion": "Barrio en el centro de la provincia"
                            }
                            """
                )}
            )
    ))
    public Response post(Barrio barrio) {
        barrioService.crear(barrio);
        return Response.status(Response.Status.CREATED).entity(barrio).build();
    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite actualizar el barrio a partir de un id",
            parameters = @Parameter(name = "barrio id"),
            requestBody = @RequestBody(description = "un nuevo barrio en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Barrio Las Acollaradas",
                                    summary = "Barrio Las Acollaradas",
                                    value = """
                            {
                               "nombre": "Barrio Las Acollaradas",
                               "geolocalizacion": "-36.234075, -61.120841",
                               "informacion": "Barrio en el centro de la provincia"
                            }
                            """
                            )}
                    )
    ))
    public Response put(@PathParam("id") Long id, Barrio barrio) {
        if ( barrioService.buscarPorId(id) != null) {
            barrioService.actualizar(barrio);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @DELETE
    @Path("{id}")
    @Operation(description = "Este endpoint nos permite eliminar el barrio a partir de un id",
            parameters = @Parameter(name = "barrio id"))
    public Response delete(@PathParam("id") Long id) {
        Barrio objeto = barrioService.buscarPorId(id);
        if (objeto != null) {
            barrioService.eliminar(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}


