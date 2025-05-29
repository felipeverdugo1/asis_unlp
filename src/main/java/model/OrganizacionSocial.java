package model;

import jakarta.persistence.*;

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

    public OrganizacionSocial(String nombre, String domicilio, String actividadPrincipal, Barrio barrio) {
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.actividadPrincipal = actividadPrincipal;
        this.barrio = barrio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public String getActividadPrincipal() {
        return actividadPrincipal;
    }

    public Barrio getBarrio() {
        return barrio;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public void setActividadPrincipal(String actividadPrincipal) {
        this.actividadPrincipal = actividadPrincipal;
    }

    public void setBarrio(Barrio barrio) {
        this.barrio = barrio;
    }
}
