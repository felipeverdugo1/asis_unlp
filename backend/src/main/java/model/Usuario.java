package model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.*;


@Data
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_usuario", nullable = false)
    private String nombreUsuario;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean habilitado;

    @Column(nullable = true)
    private String especialidad;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();

    //Hibernate carga las relaciones Lazy (pero Jackson las serializa al JSON).
    //Al serializar Usuario → incluye filtrosPersonalizados → cada FiltroPersonalizado referencia de nuevo a Usuario → bucle infinito.
    //Por lo tanto Usamos @JsonManagedReference (para relaciones bidireccionales):
    @JsonManagedReference
    @OneToMany(mappedBy = "propietario", cascade = CascadeType.REMOVE)
    private List<FiltroPersonalizado> filtrosGuardados = new ArrayList<>();

    public void agregarRol(Rol rol) {
        this.roles.add(rol);
    }

    public void quitarRol(Long id) {
        this.roles.removeIf(rol -> rol.getId().equals(id));
    }

    public void agregarFiltroPersonalizado(FiltroPersonalizado filtroPersonalizado) {
        this.filtrosGuardados.add(filtroPersonalizado);
        filtroPersonalizado.setPropietario(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}