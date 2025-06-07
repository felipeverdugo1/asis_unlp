package service;

import dao.ZonaDAO;
import model.Zona;

public class ZonaService extends GenericServiceImpl<Zona, Long> {
    public ZonaService() {
        super(new ZonaDAO());
    }

    // Métodos específicos de Usuario si los necesitás
}
