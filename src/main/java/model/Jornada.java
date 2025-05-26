package model;



import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "jornadas")
public class Jornada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "campana_id", nullable = false)
    private Campaña campana;

    // Getters y Setters


    public void setCampana(Campaña campana) {
        this.campana = campana;
    }
}