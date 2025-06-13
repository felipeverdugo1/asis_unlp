package service;

import dao.ZonaDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import model.Zona;
@ApplicationScoped

public class ZonaService extends GenericServiceImpl<Zona, Long> {

    @Inject
    public ZonaService(ZonaDAO dao) {super(dao);}


    public ZonaService() {
        super(null);
    }

    // Métodos específicos de Usuario si los necesitás
}
