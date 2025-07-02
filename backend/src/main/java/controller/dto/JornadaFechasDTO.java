package controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JornadaFechasDTO {
    private Long id;
    private String fechaFin;
    private String fechaInicio;
    private Long campaña_id;
}
