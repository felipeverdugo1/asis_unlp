package controller;

import controller.dto.OrganizacionSocialDTO;
import controller.dto.ReporteDTO;
import controller.dto.ZonaDTO;
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
import model.OrganizacionSocial;
import model.Reporte;
import service.OrganizacionSocialService;

import java.util.Optional;


@Path("/organizacionSocial")
@Tag(
        name = "Organizacion Social",
        description = "Controller que nos permite hacer operaciones sobre las organizaciones sociales"
)
public class OrganizacionSocialController {

    @Inject
    OrganizacionSocialService service;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener todas las organizaciones registradas.")
    public Response get() {
        return Response.ok(service.listarTodos()).build();
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener la organizacion a partir de un id",
            parameters = @Parameter(name = "organizacion id"))
    public Response get(@PathParam("id") Long id) {
        Optional<OrganizacionSocial> objeto = service.buscarPorId(id);
            if (objeto.isPresent()) {
                OrganizacionSocialDTO dto = new OrganizacionSocialDTO();
                dto.setId(objeto.get().getId());
                dto.setNombre(objeto.get().getNombre());
                dto.setDomicilio(objeto.get().getDomicilio());
                dto.setActividadPrincipal(objeto.get().getActividadPrincipal());
                dto.setReferente_id(objeto.get().getReferente().getId());
                dto.setBarrio_id(objeto.get().getBarrio().getId());
            return Response.ok(dto).build();
        } else {
            throw new EntidadNoEncontradaException("No existe la organización.");
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite crear una organizacion.",
            requestBody = @RequestBody(description = "una nueva organizacion en JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Organizacion",
                                    summary = "Organizacion",
                                    value = """
                            {
                               "nombre": "Organizacion Plato Lleno",
                               "domicilio": "Calle 52 n°420",
                               "actividadPrincipal": "Deportes",
                               "barrio_id": "3",
                               "referente_id": "5"
                            }
                            """
                            )}
                    )
            ))
    public Response post(OrganizacionSocialDTO dto) {
        //TODO buscar en la base por id los otros campos y agregarlos al objeto y actualizarlo con barrioService
        OrganizacionSocial organizacionSocial = service.crear(dto);
        return Response.status(Response.Status.CREATED).entity(organizacionSocial).build();
    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(description = "Este endpoint nos permite actualizar una organizacion.",
            parameters = @Parameter(name = "organizacion id"),
            requestBody = @RequestBody(description = "una organizacion en JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Organizacion",
                                    summary = "Organizacion",
                                    value = """
                            {
                               "nombre": "Organizacion Plato Lleno",
                               "domicilio": "Calle 52 n°420",
                               "actividadPrincipal": "Deportes",
                               "barrio_id": "3",
                               "referente_id": "5"
                            }
                            """
                            )}
                    )
            ))
    public Response put(@PathParam("id") Long id, OrganizacionSocialDTO dto) {
        OrganizacionSocial organizacionSocial = service.actualizar(id, dto);
        return Response.ok().build();
    }


    @DELETE
    @Path("{id}")
    @Operation(description = "Este endpoint nos permite eliminar la organizacion a partir de un id",
            parameters = @Parameter(name = "organizacion id"))
    public Response delete(@PathParam("id") Long id) {
        service.eliminar(id);
        return Response.noContent().build();
    }
}
