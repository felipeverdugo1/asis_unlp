package service;

import dao.BarrioDAO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Barrio;

@RequestScoped
public class BarrioService extends GenericServiceImpl<Barrio, Long> {

    @Inject
    public BarrioService(BarrioDAO dao) {
        super(dao);
    }

    public BarrioService() { super(null);}
    // Additional business logic methods can be added here if needed
}