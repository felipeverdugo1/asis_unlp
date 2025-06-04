package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for managing EntityManager instances for tests.
 * This class uses the "asis_unlp_test" persistence unit to connect to the test database.
 * 
 * IMPORTANT: Before running tests, make sure the "asis_unlp_test" database exists.
 * You can create it with the SQL command: CREATE DATABASE asis_unlp_test;
 */
public class TestHibernateUtil {
    private static final EntityManagerFactory emf;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "asis_unlp_test";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin";

    static {
        try {
            // Try to create the database if it doesn't exist
            try {
                createDatabaseIfNotExists();
                System.out.println("Database '" + DB_NAME + "' is ready for tests.");
            } catch (Exception e) {
                System.err.println("WARNING: Could not create or verify the test database. " +
                        "Make sure to create it manually with: CREATE DATABASE " + DB_NAME);
                System.err.println("Error details: " + e.getMessage());
            }

            // Create properties to ensure the database is created
            java.util.Map<String, String> properties = new java.util.HashMap<>();
            properties.put("hibernate.hbm2ddl.auto", "update");

            // Use the test persistence unit with the properties
            emf = Persistence.createEntityManagerFactory("asis_unlp_test", properties);

            System.out.println("Test database connection established successfully.");
        } catch (Exception ex) {
            System.err.println("Error al crear EntityManagerFactory para tests: " + ex);
            System.err.println("\nIMPORTANT: Make sure you have created the test database with:");
            System.err.println("CREATE DATABASE " + DB_NAME + ";\n");
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Attempts to create the test database if it doesn't exist.
     */
    private static void createDatabaseIfNotExists() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            stmt.executeUpdate(sql);
        }
    }

    /**
     * Get a new EntityManager connected to the test database.
     * @return EntityManager instance
     */
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Close the EntityManager if it's open.
     * @param em EntityManager to close
     */
    public static void closeEntityManager(EntityManager em) {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    /**
     * Close the EntityManagerFactory when tests are complete.
     * This should be called in a @AfterAll method.
     */
    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
