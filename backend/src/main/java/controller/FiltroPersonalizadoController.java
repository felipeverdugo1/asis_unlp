package controller;

import controller.dto.FiltroDTO;
import controller.dto.ReporteDTO;
import exceptions.EntidadNoEncontradaException;
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
import model.Reporte;
import service.FiltroPersonalizadoService;

import java.util.Optional;


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
        Optional<FiltroPersonalizado> objeto = service.buscarPorId(id);
        if (objeto.isPresent()) {
            return Response.ok(objeto.get()).build();
        } else {
            throw new EntidadNoEncontradaException("No existe el filtro.");
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
    public Response post(FiltroDTO dto) {
        //TODO buscar en la base por id los otros campos y agregarlos al objeto y actualizarlo con barrioService
        FiltroPersonalizado filtroPersonalizado = service.crear(dto);
        return Response.status(Response.Status.CREATED).entity(filtroPersonalizado).build();
    }


    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
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
    public Response put(@PathParam("id") Long id, FiltroDTO dto) {
        FiltroPersonalizado filtroPersonalizado = service.actualizar(id, dto);
        return Response.ok().build();
    }



    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite eliminar el filtro a partir de un id",
            parameters = @Parameter(name = "filtro id"))
    public Response delete(@PathParam("id") Long id) {
        service.eliminar(id);
        return Response.noContent().build();
    }
}