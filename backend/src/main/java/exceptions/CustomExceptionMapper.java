package exceptions;

import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import exceptions.ApiException;
import java.util.Map;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception ex) {
        if (ex instanceof ApiException apiEx) {
            // Maneja tus excepciones personalizadas
            return Response
                    .status(apiEx.getStatus())
                    .entity(Map.of(
                            "error", apiEx.getMessage(),
                            "code", apiEx.getStatus().getStatusCode()
                    ))
                    .build();
        } else if (ex instanceof ForbiddenException) {
            // Maneja excepciones de autorización
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity(Map.of(
                            "error", ex.getMessage(),
                            "code", Response.Status.FORBIDDEN.getStatusCode()
                    ))
                    .build();
        }

        // Para otras excepciones no controladas
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of(
                        "error", "Error interno del servidor",
                        "code", 500,
                        "details", ex.getMessage()
                ))
                .build();
    }
}
