package service;

import dao.ReporteDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Reporte;

@RequestScoped
public class ReporteService extends GenericServiceImpl<Reporte, Long> {

    @Inject
    public ReporteService(ReporteDAO reporteDAO) {super(reporteDAO);}

    public ReporteService() {
        super(null);
    }

    // Additional business logic methods can be added here if needed
}