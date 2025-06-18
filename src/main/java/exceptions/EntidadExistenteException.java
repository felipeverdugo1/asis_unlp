package exceptions;

import jakarta.ws.rs.core.Response;

public class EntidadExistenteException extends ApiException {
    public EntidadExistenteException(String mensaje) {
        super(mensaje, Response.Status.CONFLICT);
    }
}