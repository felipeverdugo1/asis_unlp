package service;

import dao.ReporteDAO;
import model.Reporte;

public class ReporteService extends GenericServiceImpl<Reporte, Long> {

    public ReporteService() {
        super(new ReporteDAO());
    }

    // Additional business logic methods can be added here if needed
}