package service;

import dao.ReporteDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import model.Reporte;
@ApplicationScoped

public class ReporteService extends GenericServiceImpl<Reporte, Long> {

     @Inject
     public ReporteService(ReporteDAO reporteDAO) {super(reporteDAO);}

    public ReporteService() {
        super(null);
    }

    // Additional business logic methods can be added here if needed
}