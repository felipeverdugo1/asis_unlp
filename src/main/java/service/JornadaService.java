package service;

import dao.JornadaDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Jornada;

@RequestScoped
public class JornadaService extends GenericServiceImpl<Jornada, Long> {

    @Inject
    public JornadaService(JornadaDAO dao) {
        super(dao);
    }

    public JornadaService(){
        super(null);
    }
    // Métodos específicos de Usuario si los necesitás
}
