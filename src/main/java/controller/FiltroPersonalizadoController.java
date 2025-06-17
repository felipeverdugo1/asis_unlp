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
import model.FiltroPersonalizado;
import service.FiltroPersonalizadoService;


@Path("/filtro")
@Tag(
        name = "Filtro Personalizado",
        description = "Controller que nos permite hacer operaciones sobre los filtros guardados por un usuario"
)
public class FiltroPersonalizadoController {

    @Inject
    FiltroPersonalizadoService service;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener todos los filtros registrados.")
    public Response get() {
        return Response.ok(service.listarTodos()).build();
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener el filtro a partir de un id",
            parameters = @Parameter(name = "filtro id"))
    public Response get(@PathParam("id") Long id) {
        FiltroPersonalizado objeto = service.buscarPorId(id);
        if (objeto != null) {
            return Response.ok(objeto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite crear un filtro.",
            requestBody = @RequestBody(description = "un nuevo filtro en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Filtro personalizado",
                                    summary = "Filtro personalizado",
                                    value = """
                            {
                               "nombre": "Filtro para diabetes en niños",
                               "criterios": "string con formato de los filtros a aplicar",
                               "propietario_id": "1"
                            }
                            """
                            )}
                    )
            ))
    public Response post(FiltroPersonalizado filtroPersonalizado) {
        //TODO buscar en la base por id los otros campos y agregarlos al objeto y actualizarlo con service
        service.crear(filtroPersonalizado);
        return Response.status(Response.Status.CREATED).entity(filtroPersonalizado).build();
    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite actualizar un filtro.",
            parameters = @Parameter(name = "filtro id"),
            requestBody = @RequestBody(description = "un filtro en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Filtro personalizado",
                                    summary = "Filtro personalizado",
                                    value = """
                            {
                               "nombre": "Filtro para diabetes en niños",
                               "criterios": "string con formato de los filtros a aplicar",
                               "propietario_id": "1"
                            }
                            """
                            )}
                    )
            ))
    public Response put(@PathParam("id") Long id, FiltroPersonalizado filtroPersonalizado) {
        //TODO buscar en la base por id los otros campos y agregarlos al objeto y actualizarlo con service
        if ( service.buscarPorId(id) != null) {
            service.actualizar(filtroPersonalizado);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Operation(description = "Este endpoint nos permite eliminar el filtro a partir de un id",
            parameters = @Parameter(name = "filtro id"))
    public Response delete(@PathParam("id") Long id) {
        FiltroPersonalizado objeto = service.buscarPorId(id);
        if (objeto != null) {
            service.eliminar(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
