package exceptions;

import jakarta.ws.rs.core.Response;

public class RangoDeFechasInvalidoException extends RuntimeException {
    public RangoDeFechasInvalidoException(String mensaje) {
        super(mensaje);
    }
}
