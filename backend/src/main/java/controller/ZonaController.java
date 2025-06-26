package controller;

import exceptions.EntidadExistenteException;
import exceptions.EntidadNoEncontradaException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Zona;
import service.ZonaService;
import controller.dto.ZonaDTO;

import java.util.Optional;


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
        Optional<Zona> objeto = service.buscarPorId(id);
        if (objeto.isPresent()) {
            return Response.ok(objeto.get()).build();
        } else {
            throw new EntidadNoEncontradaException("No existe la zona.");
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
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Creacion exitosa"),
                    @ApiResponse(responseCode = "400", description = "Error de validacion."),
                    @ApiResponse(responseCode = "500", description = "Error interno.")
            }
    )
    public Response post(@Valid ZonaDTO dto) {
        Zona zona = service.crear(dto);
        return Response.status(Response.Status.CREATED).entity(zona).build();
    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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
    public Response put(@PathParam("id") Long id, ZonaDTO dto) {
        Zona zona = service.actualizar(id, dto);
        return Response.ok().build();
    }


    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite eliminar la zona a partir de un id",
            parameters = @Parameter(name = "zona id"))
    public Response delete(@PathParam("id") Long id) {
        service.eliminar(id);
        return Response.noContent().build();
    }
}


