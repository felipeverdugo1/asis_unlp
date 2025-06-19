package controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CampañaDTO {

    @NotBlank(message = "Nombre es obligatorio")
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    @NotNull(message = "El id del barrio es obligatorio")
    private Long barrio_id;

}
