package model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "encuestas")
public class Encuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false)
    private LocalDate fecha;

    @Column( nullable = false, unique = true)
    private String nombreUnico;

    @JsonManagedReference
    @OneToMany(mappedBy = "encuesta", cascade = CascadeType.ALL)
    private List<Pregunta> preguntas = new ArrayList<>();

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "jornada_id", nullable = false)
    private Jornada jornada;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "zona_id", nullable = false)
    private Zona zona;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "encuestador_id")
    private Encuestador encuestador;

    public void agregarPregunta(Pregunta pregunta) {
        this.preguntas.add(pregunta);
        pregunta.setEncuesta(this);
    }

}
