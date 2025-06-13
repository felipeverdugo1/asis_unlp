package service;

import dao.EncuestaDAO;
import jakarta.enterprise.context.ApplicationScoped;
import model.Encuesta;
@ApplicationScoped

public class EncuestaService extends GenericServiceImpl<Encuesta, Long> {
    public EncuestaService() {
        super(new EncuestaDAO());
    }

    // Métodos específicos de Usuario si los necesitás
}
