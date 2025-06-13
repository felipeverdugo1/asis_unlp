package service;

import dao.FiltroPersonalizadoDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.FiltroPersonalizado;

@RequestScoped
public class FiltroPersonalizadoService extends GenericServiceImpl<FiltroPersonalizado, Long> {

    @Inject
    public FiltroPersonalizadoService(FiltroPersonalizadoDAO dao) {
        super(dao);
    }

    public FiltroPersonalizadoService(){super(null);}

    // Métodos específicos de Usuario si los necesitás
}
