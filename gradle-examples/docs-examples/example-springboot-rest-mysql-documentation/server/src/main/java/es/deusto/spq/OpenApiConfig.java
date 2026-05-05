package es.deusto.spq;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI spqOpenApi() {
        return new OpenAPI().info(new Info()
            .title("SPQ Messages API")
            .description("REST API for user registration, message publishing, and message lookup.")
            .version("v1")
            .contact(new Contact()
                .name("SPQ Team"))
            .license(new License()
                .name("Educational example")));
    }
}
