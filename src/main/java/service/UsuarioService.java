package service;

import dao.UsuarioDAO;
import model.Usuario;

public class UsuarioService extends GenericServiceImpl<Usuario, Long> {
    public UsuarioService() {
        super(new UsuarioDAO());
    }

    // Métodos específicos de Usuario si los necesitás
}
