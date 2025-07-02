package controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FiltroDTO {

    private String nombre;
    @NotBlank(message = "Geolocalizacion es obligatoria")
    private String criterios;
    @NotNull(message = "El id del barrio es obligatorio")
    private Long propietario_id;
}
