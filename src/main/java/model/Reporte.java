package model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


@Data
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.NONE)
@Entity
@Table(name = "reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @Column( nullable = false)
    private String nombreUnico;

    @Column( nullable = false)
    private String descripcion;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "creado_por", nullable = false)
    private Usuario creadoPor;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "reporte_compartidos",
            joinColumns = @JoinColumn(name = "reporte_id"),
            inverseJoinColumns = @JoinColumn(name = "compartidoCon")
    )
    private List<Usuario> compartidoCon;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "campaña_id", nullable = false)
    private Campaña campaña;


 
}