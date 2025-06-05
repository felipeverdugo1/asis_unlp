package service;

import dao.JornadaDAO;
import model.Jornada;

public class JornadaService extends GenericServiceImpl<Jornada, Long> {
    public JornadaService() {
        super(new JornadaDAO());
    }

    // Métodos específicos de Usuario si los necesitás
}
