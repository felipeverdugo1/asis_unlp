package controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.jvnet.hk2.annotations.Optional;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReporteDTO {

    private LocalDate fechaCreacion;
    private String nombreUnico;
    private String descripcion;
    private Long creadoPor_id;
    private Long campaña_id;
    private Long compartidoCon_id; // Opcional
}