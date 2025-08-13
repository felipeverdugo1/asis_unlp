package model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ToString(exclude = {"preguntas", "jornada", "zona", "encuestador"}) // Excluye relaciones
@Table(name = "encuestas")
public class Encuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uuid;

    @Column( nullable = false)
    private LocalDate fecha;

    @Column( nullable = false)
    private String nombreArchivo;

    @Column( nullable = false)
    private String coordenadas;


    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jornada_id", nullable = false)
    private Jornada jornada;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_id", nullable = false)
    private Zona zona;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encuestador_id")
    private Encuestador encuestador;


    @JsonIgnore
    @OneToMany(mappedBy = "encuesta", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<Pregunta> preguntas = new ArrayList<>();



    @Override
    public int hashCode(){
        return this.id.hashCode();
    }




}
