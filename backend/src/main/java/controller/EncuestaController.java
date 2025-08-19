package controller;

import controller.dto.CargaEncuestasDTO;
import controller.dto.DatosRecolectadosDTO;
import controller.dto.EncuestaDTO;
import controller.dto.ObtenerDatosDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Encuesta;
import model.Encuestador;
import org.glassfish.jersey.media.multipart.FormDataParam;
import service.EncuestaService;

import java.io.InputStream;
import java.util.Map;
import java.util.List;
import java.util.Optional;


@Path("/encuesta")
@Tag(
        name = "Encuesta",
        description = "Controller que nos permite hacer operaciones sobre las encuestas"
)
public class EncuestaController {

    @Inject
    EncuestaService service;
    @Inject
    private EncuestaService encuestaService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener todas las encuestas registradas.")
    public Response get() {
        return Response.ok(service.listarTodos()).build();
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener la encuesta a partir de un id",
            parameters = @Parameter(name = "encuesta id"))
    public Response get(@PathParam("id") Long id) {
        Optional<Encuesta> objeto = service.buscarPorId(id);
        if (objeto.isPresent()) {
            return Response.ok(objeto.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }



    @POST
    @Path("/obtener-datos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite obtener los datos de las encuestas para realizar un reporte",
            requestBody = @RequestBody(description = "unos campos en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "ObtenerDatos",
                                    summary = "campos de prueba",
                                    value = """
                                                {
                                                  "edad": [
                                                    5
                                                  ],
                                                  "genero": [
                                                    "varón cis"
                                                  ],
                                                  "barrio": "2",
                                                  "acceso_salud": "no",
                                                  "acceso_agua": "si",
                                                  "material_vivienda": [
                                                    "ladrillo"
                                                  ]
                                                }
                                            """
                            )}
                    )
            )
            , responses = {
            @ApiResponse(responseCode = "200", description = "Creacion exitosa"),
            @ApiResponse(responseCode = "400", description = "Error de validacion."),
            @ApiResponse(responseCode = "500", description = "Error interno.")
    }
    )
    public Response obtenerDatos(ObtenerDatosDTO dto) {
        List<DatosRecolectadosDTO> resultado = service.obtenerDatos(dto);
        return Response.ok(resultado).build();


    }

//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Operation(description = "Este endpoint nos permite crear una encuesta, teniendo antes creados encuestador, zona y jornada.",
//            requestBody = @RequestBody(description = "una nueva encuesta en formato JSON",
//                    required = true,
//                    content = @Content(
//                            mediaType = "application/json",
//                            examples = {@ExampleObject(
//                                    name = "Encuesta 1",
//                                    summary = "Encuesta 1",
//                                    value = """
//                                            {
//                                               "nombreUnico": "/path/a/encuesta.xlsx",
//                                               "fecha": "2025-06-13",
//                                               "encuestador_id": 3,
//                                               "zona_id": 1,
//                                               "jornada_id": 1
//                                            }
//                                            """
//                            )}
//                    )
//            ),
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Actualizacion exitosa"),
//                    @ApiResponse(responseCode = "400", description = "Error de validacion."),
//                    @ApiResponse(responseCode = "500", description = "Error interno.")
//            }
//    )
//    public Response post(EncuestaDTO encuestaDTO) {
//        Encuesta encuesta = service.crear(encuestaDTO);
//        return Response.status(Response.Status.CREATED).entity(encuesta).build();
//    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint nos permite actualizar una encuesta",
            parameters = @Parameter(name = "encuesta id"),
            requestBody = @RequestBody(description = "una encuesta en formato JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "Encuesta 1",
                                    summary = "Encuesta 1",
                                    value = """
                                            {
                                               "nombreUnico": "/path/a/encuesta.xlsx",
                                               "fecha": "2025-06-13",
                                               "encuestador_id": 3,
                                               "zona_id": 1,
                                               "jornada_id": 1
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
    public Response put(@PathParam("id") Long id, EncuestaDTO encuestaDTO) {
        Encuesta encuesta = encuestaService.actualizar(id, encuestaDTO);
        return Response.status(Response.Status.OK).entity(encuesta).build();
    }


    @DELETE
    @Path("{id}")
    @Operation(description = "Este endpoint nos permite eliminar la encuesta a partir de un id",
            parameters = @Parameter(name = "encuesta id"))
    public Response delete(@PathParam("id") Long id) {
        encuestaService.eliminar(id);
        return Response.noContent().build();
    }


    @POST
    @Path("/importar-encuestas")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importarEncuestas(
            @FormDataParam("generalCsv") InputStream generalCsv,
            @FormDataParam("branchesCsv") InputStream branchesCsv,
            @FormDataParam("encuestador_id") Long encuestador_id,
            @FormDataParam("zona_id") Long zona_id,
            @FormDataParam("jornada_id") Long jornada_id
    ) {
        // Procesar archivos directamente
        String resultado = encuestaService.cargarEncuestas(new CargaEncuestasDTO(generalCsv, branchesCsv, encuestador_id, zona_id, jornada_id));
        return Response.ok(Map.of("mensaje", resultado)).build();
    }
}


