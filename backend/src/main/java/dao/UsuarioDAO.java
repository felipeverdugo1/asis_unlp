package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import model.Rol;
import model.Usuario;

import java.util.List;
import java.util.Optional;

@RequestScoped
@Transactional
public class UsuarioDAO extends GenericDAOImpl<Usuario, Long> {

    public UsuarioDAO() {
        super(Usuario.class);  // Pasa la clase de entidad al padre
    }

    public List<Usuario> getAllUsuariosByRol(Rol rol) {
        String jpql = "SELECT u FROM " + Usuario.class.getSimpleName() + " u WHERE :rol MEMBER OF u.roles";
        List<Usuario> response = em.createQuery(jpql, Usuario.class).setParameter("rol", rol).getResultList();
        return response;
    }
}