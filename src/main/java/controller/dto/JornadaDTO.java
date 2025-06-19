package controller.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class JornadaDTO {
    private LocalDate fechaFin;
    private LocalDate fechaInicio;
    private Long campaña_id;
}
