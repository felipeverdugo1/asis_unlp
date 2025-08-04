package util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.*;

@ApplicationScoped
public class HibernateUtil {

    private EntityManagerFactory emf;

    public HibernateUtil() {
        this.emf = Persistence.createEntityManagerFactory("asis_unlp");
        initializeDatabase();
    }

    private void initializeDatabase() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // Ejecutar consulta de prueba
            em.createNativeQuery("SELECT 1").getResultList();
            em.getTransaction().commit();
            System.out.println("Base de datos inicializada correctamente");
        } catch (Exception e) {
            System.out.println("Error durante inicialización de BD: " + e.getMessage());
            throw e;
        } finally {
            em.close();
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