package unrn.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication(scanBasePackages = "unrn")
@EntityScan("unrn.infra.persistence")
public class ElAlmacenDePeliculasOnlineDescuentosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElAlmacenDePeliculasOnlineDescuentosApplication.class, args);
	}

}
