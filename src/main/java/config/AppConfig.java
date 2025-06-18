package config;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import exceptions.CustomExceptionMapper;
import org.glassfish.jersey.server.ResourceConfig;

public class AppConfig extends ResourceConfig {
    public AppConfig() {
        register(CustomExceptionMapper.class);
        packages("controller");
    }
}