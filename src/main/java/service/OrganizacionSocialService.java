package service;

import dao.OrganizacionSocialDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.OrganizacionSocial;

@RequestScoped
public class OrganizacionSocialService extends GenericServiceImpl<OrganizacionSocial, Long> {

    @Inject
    public OrganizacionSocialService(OrganizacionSocialDAO dao) {
        super(dao);
    }

    public OrganizacionSocialService() {
        super(null);
    }

    // Additional business logic methods can be added here if needed
}