package controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioDTO {

    @NotBlank(message = "Nombre es obligatorio")
    private String nombreUsuario;
    @NotBlank(message = "Geolocalizacion es obligatoria")
    private String email;
    @NotNull(message = "El id del barrio es obligatorio")
    private String password;
    @NotNull(message = "El estado de habilitacion es obligatorio")
    private Boolean habilitado;
    @NotNull(message = "Al menos un rol debe ser elegido")
    private List<Long> roles_id;
    private String especialidad;
}