package service;

import dao.EncuestaDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Encuesta;

@RequestScoped
public class EncuestaService extends GenericServiceImpl<Encuesta, Long> {

    @Inject
    public EncuestaService(EncuestaDAO encuestaDAO) {
        super(encuestaDAO);
    }

    public EncuestaService() { super(null); }
    // Métodos específicos de Usuario si los necesitás
}
