package service;

import dao.CampañaDAO;
import model.Campaña;

public class CampañaService extends GenericServiceImpl<Campaña, Long> {
    public CampañaService() {
        super(new CampañaDAO());
    }

    // Métodos específicos de Usuario si los necesitás
}
