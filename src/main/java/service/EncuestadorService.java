package service;

import dao.EncuestadorDAO;
import model.Encuestador;

public class EncuestadorService extends GenericServiceImpl<Encuestador, Long> {
    public EncuestadorService() {
        super(new EncuestadorDAO());
    }

    // Métodos específicos de Usuario si los necesitás
}
