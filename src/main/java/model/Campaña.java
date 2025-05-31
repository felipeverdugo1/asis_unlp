package model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "campanias")
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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "barrio_id", nullable = false)
    private Barrio barrio;

    @JsonManagedReference
    @OneToMany(mappedBy = "campaña", cascade = CascadeType.ALL)
    private List<Jornada> jornadas;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "campaña_id") // Esta es la columna que se creará en la tabla reporte
    private List<Reporte> reportes = new ArrayList<>();



    // Métodos adicionales
    public void agregarJornada(Jornada jornada) {
        this.jornadas.add(jornada);
        jornada.setCampaña(this);
    }

    public void agregarReporte(Reporte reporte) {
        this.reportes.add(reporte);
    }


}