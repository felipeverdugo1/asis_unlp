package controller;

import controller.dto.CampañaDTO;
import controller.dto.CampañaFechasDTO;
import controller.dto.ReporteDTO;
import exceptions.EntidadNoEncontradaException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Campaña;
import model.Reporte;
import service.CampañaService;

import javax.swing.text.html.Option;
import java.util.Optional;


@Path("/campania")
@Tag(
        name = "Campañas",
        description = "Controller que nos permite hacer operaciones sobre las campañas"
)
public class CampañaController {

    @Inject
    CampañaService service;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener todas las campañas registradas.")
    public Response get() {
        return Response.ok(service.listarTodos()).build();
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener el reporte a partir de un id",
            parameters = @Parameter(name = "campania id"))
    public Response get(@PathParam("id") Long id) {
        Optional<Campaña> objeto = service.buscarPorId(id);
        if (objeto.isPresent()) {
            CampañaFechasDTO campañaFechasDTO = new CampañaFechasDTO();
            campañaFechasDTO.setId(objeto.get().getId());
            campañaFechasDTO.setNombre(objeto.get().getNombre());
            campañaFechasDTO.setFechaInicio(objeto.get().getFechaInicio().toString());
            campañaFechasDTO.setFechaFin(objeto.get().getFechaFin().toString());
            campañaFechasDTO.setBarrio_id(objeto.get().getBarrio().getId());
            return Response.ok(campañaFechasDTO).build();
        } else {
            throw new EntidadNoEncontradaException("No existe la campaña.");
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite crear una campaña.",
            requestBody = @RequestBody(description = "una nueva campaña en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Campaña 1",
                                    summary = "Campaña 1",
                                    value = """
                            {
                               "nombre": "Campaña 1",
                               "fechaInicio": "2025-06-13",
                               "fechaFin": "2025-06-13",
                               "barrio_id": "1"
                            }
                            """
                            )}
                    )
            ))
    public Response post(CampañaDTO dto) {
        Campaña campaña = service.crear(dto);
        return Response.status(Response.Status.CREATED).entity(campaña).build();
    }


    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite actualizar una campaña.",
            parameters = @Parameter(name = "campaña id"),
            requestBody = @RequestBody(description = "una campaña en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Campaña 1",
                                    summary = "Campaña 1",
                                    value = """
                            {
                               "nombre": "Campaña 1",
                               "fechaInicio": "2025-06-13",
                               "fechaFin": "2025-06-13",
                               "barrio_id": "1"
                            }
                            """
                            )}
                    )
            ))
    public Response put(@PathParam("id") Long id, CampañaDTO dto) {
        Campaña campaña = service.actualizar(id, dto);
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite eliminar la campaña a partir de un id",
            parameters = @Parameter(name = "campaña id"))
    public Response delete(@PathParam("id") Long id) {
        service.eliminar(id);
        return Response.noContent().build();
    }

    @GET
    @Path("barrio/{barrio_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener las campañas a partir de un barrio id.",
            parameters = @Parameter(name = "barrio id"))
    public Response getCampaniasByBarrio(@PathParam("barrio_id") Long barrio_id) {
        return Response.ok(service.listarCampaniasByBarrio(barrio_id)).build();
    }
}


