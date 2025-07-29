package model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "preguntas")
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pregunta;

    @Column(nullable = false)
    private String uuid_padre;

    @Column()
    private Boolean esPersonal;

    @Column(nullable = false)
    private String respuesta;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "encuesta_id", nullable = false)
    private Encuesta encuesta;


}
