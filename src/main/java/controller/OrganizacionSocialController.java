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
import model.OrganizacionSocial;
import service.OrganizacionSocialService;


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
        OrganizacionSocial objeto = service.buscarPorId(id);
        if (objeto != null) {
            return Response.ok(objeto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
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
    public Response post(OrganizacionSocial OrganizacionSocial) {
        //TODO buscar en la base por id los otros campos y agregarlos al objeto y actualizarlo con barrioService
        service.crear(OrganizacionSocial);
        return Response.status(Response.Status.CREATED).entity(OrganizacionSocial).build();
    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
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
    public Response put(@PathParam("id") Long id, OrganizacionSocial OrganizacionSocial) {
        //TODO buscar en la base por id los otros campos y agregarlos al objeto y actualizarlo con barrioService
        if ( service.buscarPorId(id) != null) {
            service.actualizar(OrganizacionSocial);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @DELETE
    @Path("{id}")
    @Operation(description = "Este endpoint nos permite eliminar la organizacion a partir de un id",
            parameters = @Parameter(name = "organizacion id"))
    public Response delete(@PathParam("id") Long id) {
        OrganizacionSocial objeto = service.buscarPorId(id);
        if (objeto != null) {
            service.eliminar(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}


