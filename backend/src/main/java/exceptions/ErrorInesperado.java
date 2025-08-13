package exceptions;

import jakarta.ws.rs.core.Response;

public class ErrorInesperado extends ApiException {
    public ErrorInesperado(String message) {
        super(message, Response.Status.INTERNAL_SERVER_ERROR);
    }
}