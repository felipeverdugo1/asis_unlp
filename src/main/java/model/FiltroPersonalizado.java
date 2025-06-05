package model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.NONE)
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
    @JsonBackReference //Explicacion en model.Usuario
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

}


