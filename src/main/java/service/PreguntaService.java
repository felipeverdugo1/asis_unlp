package service;

import dao.PreguntaDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Pregunta;


@RequestScoped
public class PreguntaService extends GenericServiceImpl<Pregunta, Long> {

    @Inject
    public PreguntaService(PreguntaDAO preguntaDAO) {
        super(preguntaDAO);
    }

    public PreguntaService(){
        super(null);
    }

    // Additional business logic methods can be added here if needed
}