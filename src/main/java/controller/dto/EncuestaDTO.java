package controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EncuestaDTO {
    private String nombreUnico;
    private LocalDate fecha;
    private Long encuestador_id;
    private Long zona_id;
    private Long jornada_id;

    public boolean validarTodosNull(){
        return nombreUnico == null && fecha == null && encuestador_id == null && zona_id == null && jornada_id == null;
    }
}
