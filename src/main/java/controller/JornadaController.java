package controller;

import controller.dto.JornadaDTO;
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
import model.Usuario;
import service.JornadaService;

import java.util.Optional;


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
        Optional<Jornada> objeto = service.buscarPorId(id);
        if (objeto.isPresent()) {
            return Response.ok(objeto.get()).build();
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
                               "fechaInicio": "2025-06-13",
                               "fechaFin": "2025-06-13",
                               "campaña_id": "1"
                            }
                            """
                            )}
                    )
            ))
    public Response post(JornadaDTO dto) {
        service.crear(dto);
        return Response.status(Response.Status.CREATED).entity(dto).build();
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
                               "fechaInicio": "2025-06-13",
                               "fechaFin": "2025-06-13",
                               "campaña_id": "1"
                            }
                            """
                            )}
                    )
            ))
    public Response put(@PathParam("id") Long id, JornadaDTO dto) {
            service.actualizar(id, dto);
            return Response.ok().build();
    }


    @DELETE
    @Path("{id}")
    @Operation(description = "Este endpoint nos permite obtener la jornada a partir de un id",
            parameters = @Parameter(name = "jornada id"))
    public Response delete(@PathParam("id") Long id) {
        Optional<Jornada> objeto = service.buscarPorId(id);
        if (objeto.isPresent()) {
            service.eliminar(objeto.get());
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/agregarZona/{id}/{zona_id}")
    @Operation(description = "Este endpoint permite habilitar o deshabilitar un usuario",
            parameters = { @Parameter(name = "usuario id", required = true),
                    @Parameter(name = "zona_id", required = true) })
    @Produces(MediaType.APPLICATION_JSON)
    public Response agregarZona(@PathParam("id") Long id, @PathParam("zona_id") Long zona_id) {
        Jornada jornada = service.agregarZona(id, zona_id);
        return Response.status(Response.Status.CREATED).entity(jornada).build();
    }

    @PUT
    @Path("/quitarZona/{id}/{zona_id}")
    @Operation(description = "Este endpoint permite habilitar o deshabilitar un usuario",
            parameters = { @Parameter(name = "usuario id", required = true),
                    @Parameter(name = "zona_id", required = true) })
    @Produces(MediaType.APPLICATION_JSON)
    public Response quitarZona(@PathParam("id") Long id, @PathParam("zona_id") Long zona_id) {
        Jornada jornada = service.quitarZona(id, zona_id);
        return Response.status(Response.Status.CREATED).entity(jornada).build();
    }
}


