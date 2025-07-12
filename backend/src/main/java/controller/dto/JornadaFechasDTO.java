package controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import model.Zona;

import java.time.LocalDate;
import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JornadaFechasDTO {
    private Long id;
    private String fechaFin;
    private String fechaInicio;
    private Long campaña_id;
    private Set<Zona> zonas;
}
