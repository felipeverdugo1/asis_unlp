package dao;

import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import model.Barrio;


@RequestScoped
@Transactional
public class BarrioDAO extends GenericDAOImpl<Barrio, Long> {

    public BarrioDAO() {
        super(Barrio.class);
    }

}