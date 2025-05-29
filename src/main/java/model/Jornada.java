package model;



import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "jornadas")
public class Jornada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @ManyToOne
    @JoinColumn(name = "campana_id", nullable = false)
    private Campaña campaña;

    @OneToMany(mappedBy = "jornada", cascade = CascadeType.ALL)
    private List<Encuesta> encuestas;

    public Jornada(LocalDate fechaInicio, LocalDate fechaFin, Campaña campaña, List<Encuesta> encuestas) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.campaña = campaña;
        this.encuestas = encuestas;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public Campaña getCampaña() {
        return campaña;
    }

    public List<Encuesta> getEncuestas() {
        return encuestas;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setCampaña(Campaña campaña) {
        this.campaña = campaña;
    }

    public void setEncuestas(List<Encuesta> encuestas) {
        this.encuestas = encuestas;
    }

    public void agregarEncuesta(Encuesta encuesta) {
        this.encuestas.add(encuesta);
        encuesta.setJornada(this);
    }
}