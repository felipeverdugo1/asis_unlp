package model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "barrios")
public class Barrio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "geolocalizacion", nullable = false)
    private String geolocalizacion; // Puede ser una coordenada o dirección

    @OneToMany(mappedBy = "barrio", cascade = CascadeType.ALL)
    private List<Zona> zonas;

    @OneToMany(mappedBy = "barrio")
    private List<Campaña> campanas;

    // Constructor, Getters y Setters
    public Barrio() {}

    public Long getId() { return id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCampanas(List<Campaña> campanas) {
        this.campanas = campanas;
    }

    public void setGeolocalizacion(String geolocalizacion) {
        this.geolocalizacion = geolocalizacion;
    }


    public void setZonas(List<Zona> zonas) {
        this.zonas = zonas;
    }

    public void agregarZona(Zona zona) {
        this.zonas.add(zona);
        zona.setBarrio(this);
    }
}