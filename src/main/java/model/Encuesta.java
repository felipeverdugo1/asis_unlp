package model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "encuestas")
public class Encuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false)
    private LocalDate fecha;

    @JsonManagedReference
    @OneToMany(mappedBy = "encuesta", cascade = CascadeType.ALL)
    private List<Pregunta> preguntas;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "jornada_id", nullable = false)
    private Jornada jornada;


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "zona_id")
    private Zona zona;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "encuestador_id")
    private Encuestador encuestador;



}
