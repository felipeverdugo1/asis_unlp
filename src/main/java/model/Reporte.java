package model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Date;
import java.time.LocalDate;


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
    private String nombre;

    @Column( nullable = false)
    private String descripcion;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "creado_por")
    private Usuario creadoPor;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "compartido_con")
    private Usuario compartidoCon;


 
}