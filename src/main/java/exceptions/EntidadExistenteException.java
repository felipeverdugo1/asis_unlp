package exceptions;

public class EntidadExistenteException extends RuntimeException {
    public EntidadExistenteException(String mensaje) {
        super(mensaje);
    }
}