package controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JornadaDTO {
    private LocalDate fechaFin;
    private LocalDate fechaInicio;
    private Long campaña_id;
}
