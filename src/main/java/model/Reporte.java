package model;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_creacion", nullable = false)
    private Date fechaCreacion;

    @Column( nullable = false)
    private String nombre;

    @Column( nullable = false)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "creado_por")
    private Usuario creadoPor;

    @ManyToOne
    @JoinColumn(name = "compartido_con")
    private Usuario compartidoCon;


 
}