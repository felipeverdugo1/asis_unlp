package model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import java.util.List;


@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.NONE)
@Entity
@Table(name = "zonas")
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column( nullable = false)
    private String geolocalizacion;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "barrio_id", nullable = false)
    private Barrio barrio;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "zona_id")
    private List<Encuesta> encuestas;

    @Override
    public int hashCode(){
        return this.id.hashCode();
    }

}