package service;

import dao.EncuestadorDAO;
import jakarta.enterprise.context.ApplicationScoped;
import model.Encuestador;
@ApplicationScoped

public class EncuestadorService extends GenericServiceImpl<Encuestador, Long> {
    public EncuestadorService() {
        super(new EncuestadorDAO());
    }

    // Métodos específicos de Usuario si los necesitás
}
