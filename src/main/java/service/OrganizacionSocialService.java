package service;

import dao.OrganizacionSocialDAO;
import jakarta.enterprise.context.ApplicationScoped;
import model.OrganizacionSocial;
@ApplicationScoped

public class OrganizacionSocialService extends GenericServiceImpl<OrganizacionSocial, Long> {

    public OrganizacionSocialService() {
        super(new OrganizacionSocialDAO());
    }

    // Additional business logic methods can be added here if needed
}