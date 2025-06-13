package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import model.Jornada;
@ApplicationScoped
@Transactional
public class JornadaDAO extends GenericDAOImpl<Jornada, Long> {
    public JornadaDAO() {
        super(Jornada.class);
    }

    // Si necesitás métodos específicos, acá los agregás
}