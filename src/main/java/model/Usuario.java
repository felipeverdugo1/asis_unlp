package model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;



@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.NONE)
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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




    //Hibernate carga las relaciones Lazy (pero Jackson las serializa al JSON).
    //Al serializar Usuario → incluye filtrosPersonalizados → cada FiltroPersonalizado referencia de nuevo a Usuario → bucle infinito.
    //Por lo tanto Usamos @JsonManagedReference (para relaciones bidireccionales):
    @JsonManagedReference
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<FiltroPersonalizado> filtrosPersonalizados;




}