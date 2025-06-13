package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import model.Encuesta;

@RequestScoped
@Transactional
public class EncuestaDAO extends GenericDAOImpl<Encuesta, Long> {
    public EncuestaDAO() {
        super(Encuesta.class);
    }

    // Si necesitás métodos específicos, acá los agregás
}