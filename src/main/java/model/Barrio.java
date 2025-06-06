package model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
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
    private List<Zona> zonas = new ArrayList<>();

    public void agregarZonas(Zona zona) {
        this.zonas.add(zona);
        zona.setBarrio(this);
    }
}