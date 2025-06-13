package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import model.FiltroPersonalizado;

@RequestScoped
@Transactional
public class FiltroPersonalizadoDAO extends GenericDAOImpl<FiltroPersonalizado, Long> {


    public FiltroPersonalizadoDAO() {super(FiltroPersonalizado.class);}
    // This class extends GenericDAOImpl with Barrio as the entity type and Long as the ID type
    // No additional methods are needed for basic CRUD operations
}