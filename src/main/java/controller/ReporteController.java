package controller;

import dao.GenericDAOImpl;
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
import model.Reporte;
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
            return Response.ok(objeto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
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
            ))
    public Response post(Reporte Reporte) {
        //TODO buscar en la base por id los otros campos y agregarlos al objeto y actualizarlo con barrioService
        service.crear(Reporte);
        return Response.status(Response.Status.CREATED).entity(Reporte).build();
    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
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
    public Response put(@PathParam("id") Long id, Reporte Reporte) {
        //TODO buscar en la base por id los otros campos y agregarlos al objeto y actualizarlo con barrioService
        if ( service.buscarPorId(id) != null) {
            service.actualizar(Reporte);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @DELETE
    @Path("{id}")
    @Operation(description = "Este endpoint nos permite obtener el reporte a partir de un id",
            parameters = @Parameter(name = "reporte id"))
    public Response delete(@PathParam("id") Long id) {
        Optional<Reporte> objeto = service.buscarPorId(id);
        if (objeto.isPresent()) {
            service.eliminar(objeto.get());
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}


