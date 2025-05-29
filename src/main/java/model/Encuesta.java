package model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Encuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false)
    private LocalDate fecha;

    @OneToMany(mappedBy = "encuesta", cascade = CascadeType.ALL)
    private List<Pregunta> preguntas;

    @ManyToOne
    @JoinColumn(name = "jornada_id", nullable = false)
    private Jornada jornada;

    @ManyToOne
    @JoinColumn(name = "zona_id")
    private Zona zona;

    @ManyToOne
    @JoinColumn(name = "encuestador_id")
    private Encuestador encuestador;

    public Encuesta(LocalDate fecha, List<Pregunta> preguntas, Jornada jornada, Zona zona, Encuestador encuestador) {
        this.fecha = fecha;
        this.preguntas = preguntas;
        this.jornada = jornada;
        this.zona = zona;
        this.encuestador = encuestador;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public Zona getZona() {
        return zona;
    }

    public Encuestador getEncuestador() {
        return encuestador;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public void setEncuestador(Encuestador encuestador) {
        this.encuestador = encuestador;
    }
}
