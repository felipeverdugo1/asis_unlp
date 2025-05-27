package service;

import dao.BarrioDAO;
import model.Barrio;

public class BarrioService extends GenericServiceImpl<Barrio, Long> {
    
    public BarrioService() {
        super(new BarrioDAO());
    }
    
    // Additional business logic methods can be added here if needed
}