package model;

public class Token {

    public long usuarioId;
    public String token;

    public Token(long usuID, String token2) {
        this.usuarioId = usuID;
        this.token=token2;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
