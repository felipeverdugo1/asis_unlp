package service;

import dao.FiltroPersonalizadoDAO;
import model.FiltroPersonalizado;

public class FiltroPersonalizadoService extends GenericServiceImpl<FiltroPersonalizado, Long> {
    public FiltroPersonalizadoService() {
        super(new FiltroPersonalizadoDAO());
    }

    // Métodos específicos de Usuario si los necesitás
}
