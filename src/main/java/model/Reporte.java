package model;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_creacion", nullable = false)
    private Date fechaCreacion;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @ElementCollection
    @CollectionTable(name = "reporte_filtros", joinColumns = @JoinColumn(name = "reporte_id"))
    @MapKeyColumn(name = "clave")
    @Column(name = "valor")
    private Map<String, String> filtros =  new HashMap<>(); // Inicialización directa; // Ej: {"enfermedad": "diabetes", "acceso_agua": "no"}

    @ManyToOne
    @JoinColumn(name = "personal_salud_id", nullable = false)
    private PersonalDeSalud personalSalud;

    @Lob
    @Column(name = "contenido")
    private byte[] contenido; // Para almacenar la captura de pantalla o datos del reporte


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFiltros(Map<String, String> filtros) {
        this.filtros = filtros;
    }

    public void setPersonalSalud(PersonalDeSalud personalSalud) {
        this.personalSalud = personalSalud;
    }


    public Map<String, String> getFiltros() {
        return filtros;
    }





    // Constructor, Getters y Setters
    public Reporte() {}
}