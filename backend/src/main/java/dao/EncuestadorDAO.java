package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import model.Encuestador;

@RequestScoped
@Transactional
public class EncuestadorDAO extends GenericDAOImpl<Encuestador, Long> {


    public EncuestadorDAO() {super(Encuestador.class);}
    // This class extends GenericDAOImpl with Barrio as the entity type and Long as the ID type
    // No additional methods are needed for basic CRUD operations
}