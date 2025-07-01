package controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganizacionSocialDTO {

    @NotBlank(message = "Nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "Geolocalizacion es obligatoria")
    private String domicilio;
    private String actividadPrincipal;
    @NotNull(message = "El id del barrio es obligatorio")
    private Long barrio_id;
    private Long referente_id;


}
