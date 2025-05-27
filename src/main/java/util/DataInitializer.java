package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.time.LocalDateTime;


public class DataInitializer {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("asis_unlp");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // 1. Crear Barrios
            Barrio villaArgüello = new Barrio();
            villaArgüello.setNombre("Villa Argüello");
            villaArgüello.setGeolocalizacion("-34.8735, -57.9117");
            em.persist(villaArgüello);

            // 2. Crear Zonas
            Zona zona1 = new Zona();
            zona1.setNombre("Zona 1");
            zona1.setGeolocalizacion("-34.8735, -57.9117");
            zona1.setBarrio(villaArgüello);
            em.persist(zona1);

            // 3. Crear Personal de Salud
            PersonalDeSalud drPerez = new PersonalDeSalud();
            drPerez.setNombre("Dr. Juan Pérez");
            drPerez.setEmail("juan.perez@unlp.edu.ar");
            drPerez.setPassword("123456");
            drPerez.setEspecialidad("Cardiología");
            em.persist(drPerez);

            // 4. Crear Campañas
            Campaña campañaDiabetes = new Campaña();
            campañaDiabetes.setNombre("Campaña de Diabetes");
            campañaDiabetes.setFechaInicio(LocalDate.of(2025, 6, 1));
            campañaDiabetes.setFechaFin(LocalDate.of(2025, 6, 30));
            campañaDiabetes.setBarrio(villaArgüello);
            em.persist(campañaDiabetes);

            // 5. Crear Reportes
            Reporte reporteDiabetes = new Reporte();
            reporteDiabetes.setNombre("Reporte Diabetes Zona 1");
            reporteDiabetes.setFechaCreacion(Date.from(Instant.now()));
            reporteDiabetes.setPersonalSalud(drPerez);
            reporteDiabetes.getFiltros().put("enfermedad", "diabetes");
            em.persist(reporteDiabetes);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}