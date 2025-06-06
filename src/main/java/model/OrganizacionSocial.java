package model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.NONE)
@Entity
@Table(name = "organizaciones_sociales")
public class OrganizacionSocial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "domicilio", nullable = false)
    private String domicilio;

    @Column(name = "actividad_principal", nullable = false)
    private String actividadPrincipal;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "barrio_id")
    private Barrio barrio;


}
