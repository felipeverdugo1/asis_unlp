package controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObtenerDatosDTO {
    private List<Integer> edad; // [min, max] o null si no aplica
    private List<String> generos; // Lista de géneros aceptados
    private String barrio;
    private String acceso_agua;
    private String acceso_salud;
    private List<String> material_vivienda;

}
