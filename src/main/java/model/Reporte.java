package model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


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
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private Set<Usuario> compartidoCon = new HashSet<>();

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "campaña_id", nullable = false)
    private Campaña campaña;

    public void agregarUsuarioCompartido(Usuario usuario) {
        this.compartidoCon.add(usuario);
    }

    public void quitarUsuarioCompartido(Long id) {
        this.compartidoCon.removeIf(usuario -> usuario.getId().equals(id));
    }



}