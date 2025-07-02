package controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class ZonaDTO {
    private Long id;
    @NotBlank(message = "Nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "Geolocalizacion es obligatoria")
    private String geolocalizacion;
    @NotNull(message = "El id del barrio es obligatorio")
    private Long barrio_id;
}
