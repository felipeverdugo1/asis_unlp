package model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
public abstract class Usuario {

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @OneToMany(mappedBy = "propietario", cascade = CascadeType.ALL)
    private List<FiltroPersonalizado> filtrosPersonalizados;



    public Usuario() {}

    // Getters y Setters

    public Usuario(String email, String password, String nombreUsuario, Rol rol) {
        this.email = email;
        this.password = password;
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
        this.habilitado = true;
        this.filtrosPersonalizados = new ArrayList<>();

    }


    public Long getId() {
        return id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public Rol getRol() {
        return rol;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    //PersonalDeSalud
    public void agregarFiltro(FiltroPersonalizado filtro) {
        if (this.rol != Rol.PERSONAL_DE_SALUD) {
            throw new IllegalStateException("Solo el personal de salud puede tener filtros personalizados");
        }
        filtro.setUsuario(this);
        this.filtrosPersonalizados.add(filtro);
    }


}