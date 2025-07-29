package exceptions;

import jakarta.ws.rs.core.Response;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String mensaje) {
        super(mensaje, Response.Status.UNAUTHORIZED);
    }
}
