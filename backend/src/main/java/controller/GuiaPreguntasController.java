

package controller;

import controller.dto.EncuestadorDTO;
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
import model.Barrio;
import model.Encuestador;
import model.GuiaPregunta;
import service.EncuestadorService;
import service.GuiaPreguntaService;

import java.io.IOException;
import java.util.Optional;


@Path("/guia-preguntas")
@Tag(
        name = "Guia Preguntas",
        description = "Controller que nos permite cargar preguntas"
)
public class GuiaPreguntasController {

    @Inject
    GuiaPreguntaService service;
    @Inject
    private EncuestadorService encuestadorService;


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Este endpoint  permite cargar las preguntas.")
    public Response post() throws IOException {
        service.crear();
        return Response.status(Response.Status.CREATED).entity(null).build();
    }


}


