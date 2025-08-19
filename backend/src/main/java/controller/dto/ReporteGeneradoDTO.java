package controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReporteGeneradoDTO {
    Map<Integer, Integer> totalesEdad;
    Map<String, Integer> totalesGenero;
    Map<String, Integer> totalesMaterialVivienda;
    Integer totalEncuestados;
    Integer totalCumplidos;
    List<DatosRecolectadosDTO> datosRecolectados;

    public ReporteGeneradoDTO() {

    }
}
