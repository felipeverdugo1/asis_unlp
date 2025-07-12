package controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CampañaFechasDTO{
    private Long id;
    private String nombre;
    private String fechaInicio;
    private String fechaFin;
    private Long barrio_id;
}
