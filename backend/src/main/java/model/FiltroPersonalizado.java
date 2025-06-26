package model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "filtros_personalizados")
public class FiltroPersonalizado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    // string con formato con el setup del filtro
    @Column(columnDefinition = "TEXT", nullable = false)
    private String criterios;

    @ManyToOne
    @JsonBackReference //Explicacion en model.Usuario
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario propietario;

}


