package service;

import dao.BarrioDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import model.Barrio;


@ApplicationScoped
public class BarrioService extends GenericServiceImpl<Barrio, Long> {


    @Inject
    public BarrioService(BarrioDAO dao) { super(dao); }

    public BarrioService() { super(null); }



}