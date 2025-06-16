package config;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

public class AppConfig extends ResourceConfig {
    public AppConfig() {
        register(JacksonJsonProvider.class);
        packages("controller");
    }
}