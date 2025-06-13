package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import model.Campaña;
@ApplicationScoped
@Transactional
public class CampañaDAO extends GenericDAOImpl<Campaña, Long> {
    public CampañaDAO() {
        super(Campaña.class);
    }

    // Si necesitás métodos específicos, acá los agregás
}