package model;

import jakarta.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy = "barrio", cascade = CascadeType.ALL)
    private List<Zona> zonas;

    public Barrio(String nombre, String geolocalizacion, String informacion, List<Zona> zonas) {
        this.nombre = nombre;
        this.geolocalizacion = geolocalizacion;
        this.informacion = informacion;
        this.zonas = zonas;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setGeolocalizacion(String geolocalizacion) {
        this.geolocalizacion = geolocalizacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public void setZonas(List<Zona> zonas) {
        this.zonas = zonas;
    }

    public String getNombre() {
        return nombre;
    }

    public String getGeolocalizacion() {
        return geolocalizacion;
    }

    public String getInformacion() {
        return informacion;
    }

    public List<Zona> getZonas() {
        return zonas;
    }

    public void agregarZona(Zona zona) {
        this.zonas.add(zona);
        zona.setBarrio(this);
    }
}