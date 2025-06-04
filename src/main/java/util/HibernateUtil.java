package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class HibernateUtil {
    private static final EntityManagerFactory emf;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "asis_unlp";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin";

    static {
        try {
            // Try to create the database if it doesn't exist
            try {
                createDatabaseIfNotExists();
                System.out.println("Database '" + DB_NAME + "' is ready for application.");
            } catch (Exception e) {
                System.err.println("WARNING: Could not create or verify the database. " +
                        "Make sure to create it manually with: CREATE DATABASE " + DB_NAME);
                System.err.println("Error details: " + e.getMessage());
            }

            // Create properties to ensure the database schema is updated
            java.util.Map<String, String> properties = new java.util.HashMap<>();
            properties.put("hibernate.hbm2ddl.auto", "update");

            emf = Persistence.createEntityManagerFactory("asis_unlp", properties);
        } catch (Exception ex) {
            System.err.println("Error al crear EntityManagerFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Obtiene un nuevo EntityManager
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Cierra el EntityManager (si está abierto)
    public static void closeEntityManager(EntityManager em) {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    /**
     * Attempts to create the database if it doesn't exist.
     */
    private static void createDatabaseIfNotExists() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            stmt.executeUpdate(sql);
        }
    }

}
