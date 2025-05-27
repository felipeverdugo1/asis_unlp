package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import java.util.List;


@Entity
@Table(name = "personal_de_salud")
public class PersonalDeSalud extends Usuario {

    @Column(name = "especialidad", nullable = false)
    private String especialidad;

    @OneToMany(mappedBy = "personalSalud", cascade = CascadeType.ALL)
    private List<Reporte> reportesGenerados;

    // Constructor, Getters y Setters
    public PersonalDeSalud() {
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public List<Reporte> getReportesGenerados() {
        return reportesGenerados;
    }

    public void setReportesGenerados(List<Reporte> reportesGenerados) {
        this.reportesGenerados = reportesGenerados;
    }

}

