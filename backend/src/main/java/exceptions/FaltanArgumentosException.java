package exceptions;

import jakarta.ws.rs.core.Response;

public class FaltanArgumentosException extends ApiException {
    public FaltanArgumentosException(String message) {
        super(message, Response.Status.BAD_REQUEST);
    }
}