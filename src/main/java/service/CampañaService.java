package service;

import dao.CampañaDAO;
import jakarta.enterprise.context.ApplicationScoped;
import model.Campaña;
@ApplicationScoped

public class CampañaService extends GenericServiceImpl<Campaña, Long> {
    public CampañaService() {
        super(new CampañaDAO());
    }

    // Métodos específicos de Usuario si los necesitás
}
