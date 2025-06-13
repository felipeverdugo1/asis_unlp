package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import model.Usuario;

@RequestScoped
@Transactional
public class UsuarioDAO extends GenericDAOImpl<Usuario, Long> {

    public UsuarioDAO() {
        super(Usuario.class);  // Pasa la clase de entidad al padre
    }

}