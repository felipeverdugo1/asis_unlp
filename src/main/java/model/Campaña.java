package model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "campanas")
public class Campaña {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @ManyToOne
    @JoinColumn(name = "barrio_id", nullable = false)
    private Barrio barrio;

    @OneToMany(mappedBy = "campana", cascade = CascadeType.ALL)
    private List<Jornada> jornadas;

    // Constructor, Getters y Setters
    public Campaña() {}

    // Métodos adicionales
    public void agregarJornada(Jornada jornada) {
        this.jornadas.add(jornada);
        jornada.setCampana(this);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setBarrio(Barrio barrio) {
        this.barrio = barrio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setJornadas(List<Jornada> jornadas) {
        this.jornadas = jornadas;
    }


}