package controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZonaDTO {

    @NotBlank(message = "Nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "Geolocalizacion es obligatoria")
    private String geolocalizacion;
    @NotNull(message = "El id del barrio es obligatorio")
    private Long barrio_id;
}
