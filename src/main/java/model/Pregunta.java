package model;

import jakarta.persistence.*;

@Entity
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String pregunta;

    @Column(nullable = false)
    private String respuesta;

    @ManyToOne
    @JoinColumn(name = "encuesta_id", nullable = false)
    private Encuesta encuesta;




    public Pregunta(String tipo, String pregunta, String respuesta, Encuesta encuesta) {
        this.tipo = tipo;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.encuesta = encuesta;
    }

    public String getTipo() {
        return tipo;
    }

    public String getPregunta() {
        return pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public Encuesta getEncuesta() {
        return encuesta;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public void setEncuesta(Encuesta encuesta) {
        this.encuesta = encuesta;
    }
}