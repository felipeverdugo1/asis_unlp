package util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.*;

@ApplicationScoped
public class HibernateUtil {

    private EntityManagerFactory emf;

    public HibernateUtil() {
        String env = System.getenv("APP_ENV");
        if ("production".equals(env)) {
            this.emf = Persistence.createEntityManagerFactory("asis_unlp");
        } else {
            this.emf = Persistence.createEntityManagerFactory("asis_unlp_localhost");
        }
    }

    @Produces
    public EntityManager produceEntityManager() {
        return emf.createEntityManager();
    }

    public void close(@Disposes EntityManager em) {
        if (em.isOpen()) {
            em.close();
        }
    }
}