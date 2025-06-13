package service;

import dao.EncuestadorDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Encuestador;

@RequestScoped
public class EncuestadorService extends GenericServiceImpl<Encuestador, Long> {

    @Inject
    public EncuestadorService(EncuestadorDAO encuestadorDAO) {
        super(encuestadorDAO);
    }

    public EncuestadorService() {super(null);}
    // Métodos específicos de Usuario si los necesitás
}
