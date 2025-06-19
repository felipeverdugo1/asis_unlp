package exceptions;

import jakarta.ws.rs.core.Response;

public class NoPuedesHacerEsoException extends ApiException {
    public NoPuedesHacerEsoException(String message) {
        super(message, Response.Status.NOT_ACCEPTABLE);
    }
}
