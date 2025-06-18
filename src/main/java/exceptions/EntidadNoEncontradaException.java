package exceptions;

import jakarta.ws.rs.core.Response;

public class EntidadNoEncontradaException extends ApiException {
    public EntidadNoEncontradaException(String mensaje) {
        super(mensaje, Response.Status.NOT_FOUND);
    }
}