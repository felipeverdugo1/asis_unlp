package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import model.Reporte;
@ApplicationScoped
@Transactional
public class ReporteDAO extends GenericDAOImpl<Reporte, Long> {


    public ReporteDAO() {super(Reporte.class);}
    // This class extends GenericDAOImpl with Barrio as the entity type and Long as the ID type
    // No additional methods are needed for basic CRUD operations
}