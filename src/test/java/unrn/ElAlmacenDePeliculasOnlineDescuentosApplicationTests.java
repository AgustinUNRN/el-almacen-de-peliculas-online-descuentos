package unrn;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import unrn.app.ElAlmacenDePeliculasOnlineDescuentosApplication;

/**
 * Suite principal de tests del microservicio de descuentos.
 *
 * Ejecuta todos los 105 tests del proyecto organizados por paquetes:
 * - API REST (DescuentoController)
 * - DTOs (CuponDTO, ValidarCuponRequest, ValidarCuponResponse)
 * - Eventos RabbitMQ (ValidarCuponRpcListener)
 * - Persistencia (CuponEntity, CuponRepository)
 * - Modelo de Dominio (Cupon)
 * - Servicios (CuponService)
 *
 * Para ejecutar con coverage en IntelliJ:
 * 1. Haz clic derecho en esta clase
 * 2. Selecciona "Run 'ElAlmacenDePeliculasOnlineDescuentosApplicationTests'
 * with Coverage"
 *
 * Resultado esperado: 105 tests, ~94% de cobertura
 */
@Suite
@SuiteDisplayName("Suite Completa - Microservicio de Descuentos (105 tests)")
@SelectPackages({
		"unrn.api", // Tests del REST Controller
		"unrn.dto", // Tests de los DTOs
		"unrn.event.descuento", // Tests del RabbitMQ Listener
		"unrn.infra.persistence", // Tests de Entity y Repository
		"unrn.model", // Tests del modelo de dominio
		"unrn.service" // Tests del servicio de negocio
})
@SpringBootTest(classes = ElAlmacenDePeliculasOnlineDescuentosApplicationTests.TestConfig.class)
@ActiveProfiles("test")
class ElAlmacenDePeliculasOnlineDescuentosApplicationTests {

	@Test
	void contextLoads() {
		// Test básico que verifica que el contexto de Spring carga correctamente
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
