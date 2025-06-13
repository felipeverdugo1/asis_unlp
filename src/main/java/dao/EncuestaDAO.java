package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import model.Encuesta;
@ApplicationScoped
@Transactional
public class EncuestaDAO extends GenericDAOImpl<Encuesta, Long> {
    public EncuestaDAO() {
        super(Encuesta.class);
    }

    // Si necesitás métodos específicos, acá los agregás
}