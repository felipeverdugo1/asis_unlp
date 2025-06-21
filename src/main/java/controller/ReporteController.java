package controller;

import controller.dto.ReporteDTO;
import controller.dto.ZonaDTO;
import dao.GenericDAOImpl;
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
import model.Reporte;
import model.Usuario;
import model.Zona;
import service.GenericService;
import service.GenericServiceImpl;
import service.ReporteService;

import java.util.Optional;


@Path("/reporte")
@Tag(
        name = "Reportes",
        description = "Controller que nos permite hacer operaciones sobre los reportes"
)
public class ReporteController {

    @Inject
    ReporteService service;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener todos los reportes registrados.")
    public Response get() {
        return Response.ok(service.listarTodos()).build();
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener el reporte a partir de un id",
            parameters = @Parameter(name = "reporte id"))
    public Response get(@PathParam("id") Long id) {
        Optional<Reporte> objeto = service.buscarPorId(id);
        if (objeto.isPresent()) {
            return Response.ok(objeto.get()).build();
        } else {
            throw new EntidadNoEncontradaException("No existe el reporte.");
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite crear el reporte.",
            requestBody = @RequestBody(description = "un nuevo reporte en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Reporte",
                                    summary = "reporte de prueba",
                                    value = """
                            {
                               "fechaCreacion": "2025-04-24",
                               "nombreUnico": "/path/al/reporte.pdf",
                               "descripcion": "Reporte de niños con diabetes",
                               "creadoPor_id": "1",
                               "campaña_id": "2"
                            }
                            """
                            )}
                    )
            ), responses = {
            @ApiResponse(responseCode = "200", description = "Creacion exitosa"),
            @ApiResponse(responseCode = "400", description = "Error de validacion."),
            @ApiResponse(responseCode = "500", description = "Error interno.")
    }
    )

    public Response post(@Valid ReporteDTO dto) {
        //TODO buscar en la base por id los otros campos y agregarlos al objeto y actualizarlo con barrioService
        Reporte reporte = service.crear(dto);
        return Response.status(Response.Status.CREATED).entity(reporte).build();
    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite actualizar el reporte a partir de un id",
            parameters = @Parameter(name = "reporte id"),
            requestBody = @RequestBody(description = "un reporte en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Reporte",
                                    summary = "reporte de prueba",
                                    value = """
                            {
                               "fechaCreacion": "2025-04-24",
                               "nombreUnico": "/path/al/reporte.pdf",
                               "descripcion": "Reporte de niños con diabetes",
                               "creadoPor_id": "1",
                               "campaña_id": "2",
                               "compartidoCon_id": "3" (opcional cuando se comparte)
                            }
                            """
                            )}
                    )
            ))
    public Response put(@PathParam("id") Long id, ReporteDTO dto) {
        Reporte reporte = service.actualizar(id, dto);
        return Response.ok().build();
    }


    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener el reporte a partir de un id",
            parameters = @Parameter(name = "reporte id"))
    public Response delete(@PathParam("id") Long id) {
        service.eliminar(id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/agregarUsuarioCompartido/{id}/{usuario_id}")
    @Operation(description = "Este endpoint permite compartir un reporte a un usuario",
            parameters = { @Parameter(name = "reporte id", required = true),
                    @Parameter(name = "usuario id", required = true) })
    @Produces(MediaType.APPLICATION_JSON)
    public Response agregar(@PathParam("id") Long id, @PathParam("usuario_id") Long compartidoCon_id) {
        Reporte reporte = service.agregarUsuarioCompartido(id, compartidoCon_id);
        return Response.status(Response.Status.CREATED).entity(reporte).build();
    }

    @PUT
    @Path("/quitarUsuarioCompartido/{id}/{usuario_id}")
    @Operation(description = "Este endpoint desvincula un usuario de un reporte",
            parameters = { @Parameter(name = "reporte id", required = true),
                    @Parameter(name = "usuario id", required = true) })
    @Produces(MediaType.APPLICATION_JSON)
    public Response quitarUsuarioCompartido(@PathParam("id") Long id, @PathParam("usuario_id") Long compartidoCon_id) {
        Reporte reporte = service.quitarUsuarioCompartido(id, compartidoCon_id);
        return Response.status(Response.Status.CREATED).entity(reporte).build();
    }


}


