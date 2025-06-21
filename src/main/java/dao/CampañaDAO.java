package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import model.Campaña;


@RequestScoped
@Transactional
public class CampañaDAO extends GenericDAOImpl<Campaña, Long> {
    public CampañaDAO() {
        super(Campaña.class);
    }




}