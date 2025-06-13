package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import model.Zona;

@RequestScoped
@Transactional
public class ZonaDAO extends GenericDAOImpl<Zona, Long> {

    public ZonaDAO() {
        super(Zona.class);
    }

    // Si necesitás métodos específicos, acá los agregás
}