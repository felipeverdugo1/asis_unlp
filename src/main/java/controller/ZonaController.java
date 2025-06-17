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
import model.Zona;
import service.ZonaService;


@Path("/zona")
@Tag(
        name = "Zonas",
        description = "Controller que nos permite hacer operaciones sobre las zonas"
)
public class ZonaController {

    @Inject
    ZonaService service;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener todas las zonas registradas.")
    public Response get() {
        return Response.ok(service.listarTodos()).build();
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener la zona a partir de un id",
            parameters = @Parameter(name = "zona id"))
    public Response get(@PathParam("id") Long id) {
        Zona objeto = service.buscarPorId(id);
        if (objeto != null) {
            return Response.ok(objeto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite crear una zona.",
            requestBody = @RequestBody(description = "una nueva zona en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Zona 1",
                                    summary = "Zona 1",
                                    value = """
                            {
                               "nombre": "Zona 1",
                               "geolocalizacion": "-34.987, 45.8776",
                               "barrio_id": "1"
                            }
                            """
                            )}
                    )
            ))
    public Response post(Zona Zona) {
        //TODO buscar en la base por id los otros campos y agregarlos al objeto y actualizarlo con service
        service.crear(Zona);
        return Response.status(Response.Status.CREATED).entity(Zona).build();
    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite actualizar una zona.",
            parameters = @Parameter(name = "zona id"),
            requestBody = @RequestBody(description = "una zona en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Zona 1",
                                    summary = "Zona 1",
                                    value = """
                            {
                               "nombre": "Zona 1",
                               "geolocalizacion": "-34.987, 45.8776",
                               "barrio_id": "1"
                            }
                            """
                            )}
                    )
            ))
    public Response put(@PathParam("id") Long id, Zona Zona) {
        //TODO buscar en la base por id los otros campos y agregarlos al objeto y actualizarlo con service
        if ( service.buscarPorId(id) != null) {
            service.actualizar(Zona);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @DELETE
    @Path("{id}")
    @Operation(description = "Este endpoint nos permite eliminar la zona a partir de un id",
            parameters = @Parameter(name = "zona id"))
    public Response delete(@PathParam("id") Long id) {
        Zona objeto = service.buscarPorId(id);
        if (objeto != null) {
            service.eliminar(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}


