package service;

import dao.UsuarioDAO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Usuario;

@RequestScoped
public class UsuarioService extends GenericServiceImpl<Usuario, Long> {


    @Inject
    public UsuarioService(UsuarioDAO usuarioDAO) {
        super(usuarioDAO); // Ahora sí se inyecta correctamente
    }

    // Add no-arg constructor
    public UsuarioService() {
        super(null); // CDI will use the @Inject constructor at runtime
    }


}