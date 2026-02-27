package unrn;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

@SpringBootTest(classes = ElAlmacenDePeliculasOnlineDescuentosApplicationTests.TestConfig.class)
class ElAlmacenDePeliculasOnlineDescuentosApplicationTests {

	@Test
	void contextLoads() {
	}

	@Configuration
	@EnableAutoConfiguration(exclude = {
			org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
			org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
			org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration.class,
			org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration.class
	})
	public static class TestConfig {
	}

}
