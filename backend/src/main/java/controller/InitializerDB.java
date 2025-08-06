package controller;

import controller.dto.RolDTO;
import controller.dto.UsuarioDTO;
import exceptions.EntidadExistenteException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import service.RolService;
import service.UsuarioService;
import model.Rol;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Path("/inicializacion")
public class InitializerDB {

    @Inject
    private RolService rolService;

    @Inject
    private UsuarioService usuarioService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Inicializar la base de datos. Usando tomcat puro y sin spring se complica.")
    public Response init() {
        System.out.println("[INIT] Inicializando datos de la base...");
        List<String> roles = new ArrayList<>() {{
            add("admin");
            add("personal_salud");
            add("referente");
        }};
        RolDTO rol = new RolDTO();
        for (int i = 0; i < roles.size(); i++) {
            rol.setNombre(roles.get(i));
            try {
                rolService.crear(rol);
            } catch (EntidadExistenteException e) {
                System.out.println("[INIT] Ya existe el rol: " + roles.get(i));
            } catch (Exception e){
                System.out.println("[INIT] No se pudo crear el rol: " + roles.get(i) + "/n" + e.getMessage());
            }
        }

        Optional<Rol> rol_admin = rolService.buscarPorCampo("nombre", "admin");
        if (rol_admin.isPresent()) {
            UsuarioDTO administrador = new UsuarioDTO();
            administrador.setNombreUsuario("admin");
            administrador.setPassword("supersecreto");
            administrador.setEmail("admin@gmail.com");
            administrador.setHabilitado(true);
            administrador.setRoles_id(new ArrayList<>(){{ add(rol_admin.get().getId()); }});
            try {
                usuarioService.crear(administrador);
            } catch (EntidadExistenteException e) {
                System.out.println("[INIT] Ya existe un usuario con email " + administrador.getEmail());
            } catch (Exception e){
                System.out.println("[INIT] No se pudo crear el usuario: " + e.getMessage());
            }
        } else {
            System.out.println("[INIT] No existe el rol admin.");
        }

        return Response.ok().build();
    }
}
