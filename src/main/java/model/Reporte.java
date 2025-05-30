package model;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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


    public Reporte() {}

    public Reporte(Date fechaCreacion, String nombre, String descripcion, Usuario creadoPor, Usuario compartidoCon) {
        this.fechaCreacion = fechaCreacion;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creadoPor = creadoPor;
        this.compartidoCon = compartidoCon;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Usuario getCreadoPor() {
        return creadoPor;
    }

    public Usuario getCompartidoCon() {
        return compartidoCon;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCreadoPor(Usuario creadoPor) {
        this.creadoPor = creadoPor;
    }

    public void setCompartidoCon(Usuario compartidoCon) {
        this.compartidoCon = compartidoCon;
    }
}