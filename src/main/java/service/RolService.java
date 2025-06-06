package service;


import dao.RolDAO;
import model.Rol;

public class RolService extends GenericServiceImpl<Rol, Long> {

    public RolService() {  super(new RolDAO()); }
}
