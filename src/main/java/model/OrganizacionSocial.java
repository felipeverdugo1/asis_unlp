package model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @ManyToOne
    @JoinColumn(name = "barrio_id")
    private Barrio barrio;


}
