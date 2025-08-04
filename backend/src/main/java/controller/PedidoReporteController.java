package controller;

import controller.dto.PedidoReporteDTO;
import exceptions.EntidadNoEncontradaException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.PedidoReporte;
import service.PedidoReporteService;

import java.util.Map;
import java.util.Optional;





@Path("/pedidoReporte")
@Tag(
        name = "Pedido de Reporte",
        description = "Controller que nos permite hacer operaciones sobre los pedidos de reporte"
)
public class PedidoReporteController {

    @Inject
    PedidoReporteService service;
    @Inject
    private PedidoReporteService pedidoService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener todos los pedidoes registrados.")
    public Response get() {
        return Response.ok(service.listarTodos()).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener el pedido a partir de un id",
            parameters = @Parameter(name = "pedido id"))
    public Response get(@PathParam("id") Long id) {
        Optional<PedidoReporte> objeto = service.buscarPorId(id);
        if (objeto.isPresent()) {
            return Response.ok(objeto.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite crear un pedido.",
            requestBody = @RequestBody(description = "un nuevo pedido en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "pedido de prueba",
                                    summary = "pedido de prueba",
                                    value = """
                            {
                               "nombre": "Pedido personal general",
                               "camposPedidos": "edad,peso,enfermedad",
                               "estado": "pendiente",
                                "creadoPor_id": 1
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
    public Response post(PedidoReporteDTO pedidoDTO) {
        PedidoReporte pedido = service.crear(pedidoDTO);
        return Response.status(Response.Status.CREATED).entity(pedido).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite actualizar un pedido.",
            parameters = @Parameter(name = "pedido id"),
            requestBody = @RequestBody(description = "un pedido en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "pedido de prueba",
                                    summary = "pedido de prueba",
                                    value = """
                            {
                               "nombre": "Pedido personal general 2",
                               "camposPedidos": "edad",
                               "estado": "tomado",
                                "creadoPor_id": 1,
                               "comentario": "Por parte del personal de salud , vengo a dar mi comentario",
                               "reporte_id": 1,
                               "asignado_a_id":1
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
    public Response put(@PathParam("id") Long id, PedidoReporteDTO pedidoDTO) {
        PedidoReporte pedido = pedidoService.actualizar(id, pedidoDTO);
        return Response.status(Response.Status.OK).entity(pedido).build();
    }

    @DELETE
    @Path("{id}")
    @Operation(description = "Este endpoint nos permite eliminar el pedido a partir de un id",
            parameters = @Parameter(name = "pedido id"))
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) {
        pedidoService.eliminar(id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}/tomar/{usuario_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint tomar un pedido por un personal de salud",
            parameters = { @Parameter(name = "pedido id", required = true),
                    @Parameter(name = "usuario_id", required = true) })
    public Response tomarPedido(@PathParam("id") Long id, @PathParam("usuario_id") Long usuario_id) {
        service.tomarPedido(id, usuario_id);
        return Response.ok().build();
    }


    @PUT
    @Path("/{id}/soltar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response soltarPedido(@PathParam("id") Long id) {
        service.soltarPedido(id);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}/completar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite completar un pedido.",
            requestBody = @RequestBody(description = "completar pedido en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "pedido de prueba",
                                    summary = "pedido de prueba",
                                    value = """
                               {
                               "asignado_a_id": 1,
                               "reporte_id": 1,
                               "comentario": "Por parte del personal de salud , vengo a dar mi comentario"
                            }
                            """
                            )}
                    )
            ))
    public Response completarPedido(@PathParam("id") Long id, PedidoReporteDTO dto) {
        service.completarPedido(id, dto);
        return Response.ok().build();
    }

    @GET
    @Path("/estado/{estado}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener todos los pedidos que esten en cierto estado.",
            parameters = @Parameter(name = "estado"))
    public Response getByEstado(@PathParam("estado") String estado) {
        return Response.ok(service.buscarPedidosPorEstado(estado)).build();
    }

    @GET
    @Path("/referente/{usuario_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener todos los pedidos creados por un usuario.",
            parameters = @Parameter(name = "usuario id"))
    public Response getByEstado(@PathParam("usuario_id") Long user_id) {
        return Response.ok(service.listarPedidosByReferente(user_id)).build();
    }

    @GET
    @Path("/tomados/{usuario_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener todos los pedidos tomados por un usuario.",
            parameters = @Parameter(name = "usuario id"))
    public Response getByAsignado(@PathParam("usuario_id") Long user_id) {
        return Response.ok(service.listarPedidosByAsignado(user_id)).build();
    }

}


