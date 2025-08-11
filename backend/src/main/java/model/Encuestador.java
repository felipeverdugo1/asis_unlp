package model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import java.util.List;


@Data
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "encuestadores")
public class Encuestador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String dni;

    @Column(nullable = false)
    private int edad;

    @Column(nullable = false)
    private String genero;

    @Column(nullable = false)
    private String ocupacion;

    @JsonManagedReference
    @OneToMany(mappedBy = "encuestador" , fetch = FetchType.LAZY)
    private List<Encuesta> encuestas;

}
