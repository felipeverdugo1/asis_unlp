package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import model.Jornada;


@RequestScoped
@Transactional
public class JornadaDAO extends GenericDAOImpl<Jornada, Long> {
    public JornadaDAO() {
        super(Jornada.class);
    }

    // Si necesitás métodos específicos, acá los agregás
}