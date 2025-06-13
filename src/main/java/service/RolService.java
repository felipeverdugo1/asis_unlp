package service;


import dao.RolDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import model.Rol;
@ApplicationScoped

public class RolService extends GenericServiceImpl<Rol, Long> {


    @Inject
    public RolService (RolDAO dao) {super(dao);}

    public RolService() {  super(null); }
}
