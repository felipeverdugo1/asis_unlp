package service;

import dao.FiltroPersonalizadoDAO;
import jakarta.enterprise.context.ApplicationScoped;
import model.FiltroPersonalizado;
@ApplicationScoped

public class FiltroPersonalizadoService extends GenericServiceImpl<FiltroPersonalizado, Long> {
    public FiltroPersonalizadoService() {
        super(new FiltroPersonalizadoDAO());
    }

    // Métodos específicos de Usuario si los necesitás
}
