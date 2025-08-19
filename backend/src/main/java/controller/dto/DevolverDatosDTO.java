package controller.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DevolverDatosDTO {
    private List<DatosRecolectadosDTO> encuestasFiltradas;
    private Map<String, Integer> total_generos;
    private Map<String, Integer> total_materiales;
    private Map<Integer, Integer> total_edades;
    private Integer total_personas;
    private Integer total_casas;
    private Integer cantEncuestadas;
    private Integer cantCasas;
}




