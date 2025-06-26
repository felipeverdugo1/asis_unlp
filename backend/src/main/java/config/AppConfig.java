package config;

import exceptions.CustomExceptionMapper;
import org.glassfish.jersey.server.ResourceConfig;

public class AppConfig extends ResourceConfig {
    public AppConfig() {
        register(CustomExceptionMapper.class);
        packages("controller");
    }
}