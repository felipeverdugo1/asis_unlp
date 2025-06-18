package controller;

import controller.dto.BarrioDTO;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Barrio;
import model.Zona;
import service.BarrioService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

import java.util.Optional;

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
        Optional<Barrio> objeto = barrioService.buscarPorId(id);
        if (objeto.isPresent()) {
            return Response.ok(objeto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of(
                    "error", "Barrio no encontrado",
                    "code", 404
            )).build();
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
        ),
        responses = {
                @ApiResponse(responseCode = "200", description = "Creacion exitosa"),
                @ApiResponse(responseCode = "400", description = "Error de validacion."),
                @ApiResponse(responseCode = "500", description = "Error interno.")
        }
    )
    public Response post(BarrioDTO barrioDTO) {
        Barrio barrio = barrioService.crear(barrioDTO);
        return Response.status(Response.Status.CREATED).entity(barrio).build();
    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Actualizacion exitosa"),
                    @ApiResponse(responseCode = "400", description = "Error de validacion."),
                    @ApiResponse(responseCode = "500", description = "Error interno.")
            }
    )
    public Response put(@PathParam("id") Long id, BarrioDTO barrioDTO) {
        Barrio barrio = barrioService.actualizar(id, barrioDTO);
        return Response.status(Response.Status.OK).entity(barrio).build();
    }


    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite eliminar el barrio a partir de un id",
            parameters = @Parameter(name = "barrio id"))
    public Response delete(@PathParam("id") Long id) {
        barrioService.eliminar(id);
        return Response.noContent().build();

    }
}


