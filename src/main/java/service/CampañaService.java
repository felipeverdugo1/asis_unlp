package service;

import dao.CampañaDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Campaña;

@RequestScoped
public class CampañaService extends GenericServiceImpl<Campaña, Long> {

    @Inject
    public CampañaService(CampañaDAO dao) {
        super(dao);
    }

    public CampañaService() { super(null); }

    // Métodos específicos de Usuario si los necesitás
}
