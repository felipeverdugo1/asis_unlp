package controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrganizacionSocialDTO {
    private Long id;
    @NotBlank(message = "Nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "Geolocalizacion es obligatoria")
    private String domicilio;
    private String actividadPrincipal;
    @NotNull(message = "El id del barrio es obligatorio")
    private Long barrio_id;
    private Long referente_id;


}
