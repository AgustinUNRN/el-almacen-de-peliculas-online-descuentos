package unrn.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "unrn")
@EnableJpaRepositories("unrn.infra.persistence")
@EntityScan("unrn.infra.persistence")
@OpenAPIDefinition(
		info = @Info(title = "API de Descuentos", version = "1.0"),
		security = @SecurityRequirement(name = "bearerAuth") // Aplica seguridad global
)
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT"
)
public class ElAlmacenDePeliculasOnlineDescuentosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElAlmacenDePeliculasOnlineDescuentosApplication.class, args);
	}

}
