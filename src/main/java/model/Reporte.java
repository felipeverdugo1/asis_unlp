package model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;


@Getter
@Setter
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