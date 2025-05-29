package model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "campanas")
public class Campaña {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @ManyToOne
    @JoinColumn(name = "barrio_id", nullable = false)
    private Barrio barrio;

    @OneToMany(mappedBy = "campaña", cascade = CascadeType.ALL)
    private List<Jornada> jornadas;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "campaña_id") // Esta es la columna que se creará en la tabla reporte
    private List<Reporte> reportes = new ArrayList<>();

    public Campaña(String nombre, LocalDate fechaInicio, LocalDate fechaFin, Barrio barrio, List<Jornada> jornadas, List<Reporte> reportes) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.barrio = barrio;
        this.jornadas = jornadas;
        this.reportes = reportes;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public Barrio getBarrio() {
        return barrio;
    }

    public List<Jornada> getJornadas() {
        return jornadas;
    }

    public List<Reporte> getReportes() {
        return reportes;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setBarrio(Barrio barrio) {
        this.barrio = barrio;
    }

    public void setJornadas(List<Jornada> jornadas) {
        this.jornadas = jornadas;
    }

    public void setReportes(List<Reporte> reportes) {
        this.reportes = reportes;
    }

    // Métodos adicionales
    public void agregarJornada(Jornada jornada) {
        this.jornadas.add(jornada);
        jornada.setCampaña(this);
    }

    public void agregarReporte(Reporte reporte) {
        this.reportes.add(reporte);
    }


}