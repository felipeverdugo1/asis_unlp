package model;


import jakarta.persistence.*;

@Entity
@Table(name = "zonas")
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "geolocalizacion", nullable = false)
    private String geolocalizacion;

    @ManyToOne
    @JoinColumn(name = "barrio_id", nullable = false)
    private Barrio barrio;

    // Getters y Setters


    public void setGeolocalizacion(String geolocalizacion) {
        this.geolocalizacion = geolocalizacion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



    public void setBarrio(Barrio barrio) {
        this.barrio = barrio;
    }
}