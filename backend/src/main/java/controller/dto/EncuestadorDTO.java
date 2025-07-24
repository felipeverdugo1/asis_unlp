package controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EncuestadorDTO {
    private String nombre;
    private String dni;
    private Integer edad;
    private String genero;
    private String ocupacion;

    public boolean validarTodosNull() {
        return nombre == null && dni == null && edad == null && genero == null && ocupacion == null;
    }
}

