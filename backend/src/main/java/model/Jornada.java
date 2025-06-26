package model;



import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "campana_id", nullable = false)
    private Campaña campaña;

    @JsonManagedReference
    @OneToMany(mappedBy = "jornada", cascade = CascadeType.ALL)
    private List<Encuesta> encuestas = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="jornada_zona",
            joinColumns = @JoinColumn(name="jornada_id"),
            inverseJoinColumns = @JoinColumn(name="zona_id"))
    private Set<Zona> zonas = new HashSet<>();

    public void agregarEncuesta(Encuesta encuesta) {
        this.encuestas.add(encuesta);
        encuesta.setJornada(this);
    }

    public void agregarZona(Zona zona) {
        this.zonas.add(zona);
    }

    public void quitarZona(Long id) {
        this.zonas.removeIf(zona -> zona.getId().equals(id));
    }

}