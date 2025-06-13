package config;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;



public class AppConfig extends ResourceConfig {
    public AppConfig() {
//        register(CorsFilter.class); // Ejemplo: Filtro CORS personalizado por ahi en un futuro se use
        register(JacksonJsonProvider.class);
        packages("controller"); // Escanea el paquete donde están tus recursos REST
//        register(EntityManagerFilter.class); // Registra el filtro
    }
}