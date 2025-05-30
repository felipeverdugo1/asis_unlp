package model;



import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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


    public void agregarEncuesta(Encuesta encuesta) {
        this.encuestas.add(encuesta);
        encuesta.setJornada(this);
    }
}