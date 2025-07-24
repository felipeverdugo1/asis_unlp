package config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;

@OpenAPIDefinition(
        info = @Info(
                title = "ASIS Grupo 5 Swagger",
                version = "1.0.0",
                description = "Documentacion sobre los endpoints de nuestra API REST.",
                contact = @Contact(
                        name = "Matias Cipriano",
                        email = "emailfalso@jejemail.com",
                        url = "https://www.instagram.com/brucelee/"
                )
        ),
        servers = {
                @Server(
                        description = "Servidor local de desarrollo",
                        url = "http://localhost:8080/rest/",
                        variables = {
                                @ServerVariable(
                                        name = "basePath",
                                        defaultValue = "v1",
                                        description = "Version del path base"
                                )
                        }
                ),
                @Server(
                        description = "Servidor De La Catedra LINTI",
                        url = "https://grupo5.jyaa-ci.linti.unlp.edu.ar/rest/",
                        variables = {
                                @ServerVariable(
                                        name = "basePath",
                                        defaultValue = "v1",
                                        description = "Version del path base"
                                )
                        }
                )
        }
)
public class OpenApiConfig {
}
