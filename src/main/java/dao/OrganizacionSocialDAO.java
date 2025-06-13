package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import model.OrganizacionSocial;


@RequestScoped
@Transactional
public class OrganizacionSocialDAO extends GenericDAOImpl<OrganizacionSocial, Long> {


    public OrganizacionSocialDAO() {super(OrganizacionSocial.class);}
    // This class extends GenericDAOImpl with Barrio as the entity type and Long as the ID type
    // No additional methods are needed for basic CRUD operations
}