package controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PreguntaDTO {

    @NotBlank(message = "Nombre es obligatorio")
    private String tipo;
    @NotBlank(message = "Geolocalizacion es obligatoria")
    private String pregunta;
    private String respuesta;
    @NotNull(message = "El id del barrio es obligatorio")
    private Long encuesta_id;
}
