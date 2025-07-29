package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "guia_preguntas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GuiaPregunta {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    // Por ejemplo: "56_44"
    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    // Por ejemplo: "Hay personas embarazadas"
    @Column(name = "etiqueta", nullable = false)
    private String etiqueta;

    // Por ejemplo: "Qu usa princip"
    @Column(name = "row_etiqueta", nullable = false)
    private String row_etiqueta;



    // Otro orden a tener en cuenta, se puede sacar
    @Column(name = "orden")
    private Integer orden;

}
