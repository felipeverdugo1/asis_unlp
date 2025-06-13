package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import model.Usuario;

@ApplicationScoped
@Transactional
public class UsuarioDAO extends GenericDAOImpl<Usuario, Long> {

    public UsuarioDAO() {
        super(Usuario.class);  // Pasa la clase de entidad al padre
    }


}