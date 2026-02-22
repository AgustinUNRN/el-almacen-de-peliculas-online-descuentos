package unrn.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "unrn")
@EnableJpaRepositories("unrn.infra.persistence")
@EntityScan("unrn.infra.persistence")
public class ElAlmacenDePeliculasOnlineDescuentosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElAlmacenDePeliculasOnlineDescuentosApplication.class, args);
	}

}
