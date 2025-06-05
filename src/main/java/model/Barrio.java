package model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.NONE)
@Entity
@Table(name = "barrios")
public class Barrio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private String geolocalizacion; // Puede ser una coordenada o dirección

    @Column(nullable = false)
    private String informacion;

    @JsonManagedReference
    @OneToMany(mappedBy = "barrio", cascade = CascadeType.ALL)
    private List<Zona> zonas;

}