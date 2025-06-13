package service;

import dao.JornadaDAO;
import jakarta.enterprise.context.ApplicationScoped;
import model.Jornada;
@ApplicationScoped

public class JornadaService extends GenericServiceImpl<Jornada, Long> {
    public JornadaService() {
        super(new JornadaDAO());
    }

    // Métodos específicos de Usuario si los necesitás
}
