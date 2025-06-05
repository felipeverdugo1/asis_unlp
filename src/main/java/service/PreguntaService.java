package service;

import dao.PreguntaDAO;
import model.Pregunta;

public class PreguntaService extends GenericServiceImpl<Pregunta, Long> {

    public PreguntaService() {
        super(new PreguntaDAO());
    }

    // Additional business logic methods can be added here if needed
}