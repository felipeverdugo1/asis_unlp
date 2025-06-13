package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import model.Rol;

@RequestScoped
@Transactional
public class RolDAO extends GenericDAOImpl<Rol, Long> {

    public RolDAO() {
        super(Rol.class);
    }
}
