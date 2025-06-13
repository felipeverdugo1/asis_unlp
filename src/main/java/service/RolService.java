package service;


import dao.RolDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Rol;

@RequestScoped
public class RolService extends GenericServiceImpl<Rol, Long> {


    @Inject
    public RolService (RolDAO dao) {super(dao);}

    public RolService() {  super(null); }
}
