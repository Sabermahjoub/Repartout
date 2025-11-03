package config;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
public class ApplicationConfig extends ResourceConfig {
    public ApplicationConfig() {
        // Packages à scanner pour les ressources REST
        packages("api", "dao", "models");
        
        // Active le support JSON
        register(org.glassfish.jersey.jackson.JacksonFeature.class);
    }
}