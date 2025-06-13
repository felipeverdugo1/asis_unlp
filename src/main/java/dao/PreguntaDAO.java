package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import model.Pregunta;


@RequestScoped
@Transactional
public class PreguntaDAO extends GenericDAOImpl<Pregunta, Long> {


    public PreguntaDAO() {super(Pregunta.class);}
    // This class extends GenericDAOImpl with Barrio as the entity type and Long as the ID type
    // No additional methods are needed for basic CRUD operations
}