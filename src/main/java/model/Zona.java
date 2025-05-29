package model;


import jakarta.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "barrio_id", nullable = false)
    private Barrio barrio;

    public Zona(String nombre, String geolocalizacion, Barrio barrio) {
        this.nombre = nombre;
        this.geolocalizacion = geolocalizacion;
        this.barrio = barrio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getGeolocalizacion() {
        return geolocalizacion;
    }

    public Barrio getBarrio() {
        return barrio;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setGeolocalizacion(String geolocalizacion) {
        this.geolocalizacion = geolocalizacion;
    }

    public void setBarrio(Barrio barrio) {
        this.barrio = barrio;
    }
}