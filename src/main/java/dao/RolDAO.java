package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import model.Rol;
@ApplicationScoped
@Transactional
public class RolDAO extends GenericDAOImpl<Rol, Long> {

    public RolDAO() {
        super(Rol.class);
    }
}
