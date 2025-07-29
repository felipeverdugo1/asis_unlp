package controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CargaEncuestasDTO {
    private InputStream generalCsv;
    private InputStream branchesCsv;
    private Long encuestador_id;
    private Long zona_id;
    private Long jornada_id;


    public boolean validarTodosNull(){
        return generalCsv == null && branchesCsv == null&& encuestador_id ==  null && zona_id == null && jornada_id == null;
    }





}
