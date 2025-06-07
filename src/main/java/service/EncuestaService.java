package service;

import dao.EncuestaDAO;
import model.Encuesta;

public class EncuestaService extends GenericServiceImpl<Encuesta, Long> {
    public EncuestaService() {
        super(new EncuestaDAO());
    }

    // Métodos específicos de Usuario si los necesitás
}
