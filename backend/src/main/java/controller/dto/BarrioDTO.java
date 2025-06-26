package controller.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BarrioDTO {

    private String nombre;
    private String geolocalizacion;
    private String informacion;

    public boolean validarTodosNull() {
        return nombre == null && geolocalizacion == null && informacion == null;
    }

}
