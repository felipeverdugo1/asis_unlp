package model;

import jakarta.persistence.*;

@Entity
@Table(name = "filtros_personalizados")
public class FiltroPersonalizado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String criterios;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario propietario;

    public FiltroPersonalizado(Long id, String nombre, String criterios, Usuario usuario) {
        this.id = id;
        this.nombre = nombre;
        this.criterios = criterios;
        this.propietario = usuario;
    }

    public FiltroPersonalizado() { }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCriterios() {
        return criterios;
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCriterios(String criterios) {
        this.criterios = criterios;
    }

    public void setUsuario(Usuario usuario) {
        this.propietario = usuario;
    }
}

