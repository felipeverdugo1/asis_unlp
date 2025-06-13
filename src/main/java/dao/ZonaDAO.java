package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import model.Zona;

@ApplicationScoped
@Transactional
public class ZonaDAO extends GenericDAOImpl<Zona, Long> {
    public ZonaDAO() {
        super(Zona.class);
    }

    // Si necesitás métodos específicos, acá los agregás
}