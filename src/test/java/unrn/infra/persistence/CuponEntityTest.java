package unrn.infra.persistence;

import org.junit.jupiter.api.Test;
import unrn.model.Cupon;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CuponEntityTest {

    @Test
    void testConstructorSinArgumentos() {
        // Arrange & Act
        CuponEntity cuponEntity = new CuponEntity();

        // Assert
        assertNotNull(cuponEntity);
        assertNull(cuponEntity.getId());
        assertNull(cuponEntity.getNombre());
        assertNull(cuponEntity.getFechaInicio());
        assertNull(cuponEntity.getFechaFin());
        assertNull(cuponEntity.getPorcentaje());
    }

    @Test
    void testSettersYGetters() {
        // Arrange
        CuponEntity cuponEntity = new CuponEntity();
        Integer expectedId = 1;
        String expectedNombre = "VERANO2026";
        LocalDate expectedFechaInicio = LocalDate.of(2026, 1, 1);
        LocalDate expectedFechaFin = LocalDate.of(2026, 3, 31);
        Float expectedPorcentaje = 20.0f;

        // Act
        cuponEntity.setId(expectedId);
        cuponEntity.setNombre(expectedNombre);
        cuponEntity.setFechaInicio(expectedFechaInicio);
        cuponEntity.setFechaFin(expectedFechaFin);
        cuponEntity.setPorcentaje(expectedPorcentaje);

        // Assert
        assertEquals(expectedId, cuponEntity.getId());
        assertEquals(expectedNombre, cuponEntity.getNombre());
        assertEquals(expectedFechaInicio, cuponEntity.getFechaInicio());
        assertEquals(expectedFechaFin, cuponEntity.getFechaFin());
        assertEquals(expectedPorcentaje, cuponEntity.getPorcentaje());
    }

    @Test
    void testAsDomainConDatosCompletos() {
        // Arrange
        CuponEntity cuponEntity = new CuponEntity();
        cuponEntity.setId(1);
        cuponEntity.setNombre("PROMO2026");
        cuponEntity.setFechaInicio(LocalDate.of(2026, 2, 1));
        cuponEntity.setFechaFin(LocalDate.of(2026, 2, 28));
        cuponEntity.setPorcentaje(15.0f);

        // Act
        Cupon cupon = cuponEntity.asDomain();

        // Assert
        assertNotNull(cupon);
        assertEquals(cuponEntity.getId(), cupon.getId());
        assertEquals(cuponEntity.getNombre(), cupon.getNombre());
        assertEquals(cuponEntity.getFechaInicio(), cupon.getFechaInicio());
        assertEquals(cuponEntity.getFechaFin(), cupon.getFechaFin());
        assertEquals(cuponEntity.getPorcentaje(), cupon.getPorcentaje());
    }

    @Test
    void testAsDomainConPorcentajeCero() {
        // Arrange
        CuponEntity cuponEntity = new CuponEntity();
        cuponEntity.setId(2);
        cuponEntity.setNombre("SINCOSTO");
        cuponEntity.setFechaInicio(LocalDate.of(2026, 3, 1));
        cuponEntity.setFechaFin(LocalDate.of(2026, 3, 31));
        cuponEntity.setPorcentaje(0.0f);

        // Act
        Cupon cupon = cuponEntity.asDomain();

        // Assert
        assertNotNull(cupon);
        assertEquals(0.0f, cupon.getPorcentaje());
    }

    @Test
    void testAsDomainConPorcentaje100() {
        // Arrange
        CuponEntity cuponEntity = new CuponEntity();
        cuponEntity.setId(3);
        cuponEntity.setNombre("GRATIS");
        cuponEntity.setFechaInicio(LocalDate.of(2026, 4, 1));
        cuponEntity.setFechaFin(LocalDate.of(2026, 4, 30));
        cuponEntity.setPorcentaje(100.0f);

        // Act
        Cupon cupon = cuponEntity.asDomain();

        // Assert
        assertNotNull(cupon);
        assertEquals(100.0f, cupon.getPorcentaje());
    }

    @Test
    void testAsDomainConFechasMismaFecha() {
        // Arrange
        CuponEntity cuponEntity = new CuponEntity();
        LocalDate fecha = LocalDate.of(2026, 5, 15);
        cuponEntity.setId(4);
        cuponEntity.setNombre("DIADEPAGO");
        cuponEntity.setFechaInicio(fecha);
        cuponEntity.setFechaFin(fecha);
        cuponEntity.setPorcentaje(10.0f);

        // Act
        Cupon cupon = cuponEntity.asDomain();

        // Assert
        assertNotNull(cupon);
        assertEquals(fecha, cupon.getFechaInicio());
        assertEquals(fecha, cupon.getFechaFin());
    }

    @Test
    void testAsDomainConIdNulo() {
        // Arrange
        CuponEntity cuponEntity = new CuponEntity();
        cuponEntity.setId(null);
        cuponEntity.setNombre("NUEVOCUPON");
        cuponEntity.setFechaInicio(LocalDate.of(2026, 6, 1));
        cuponEntity.setFechaFin(LocalDate.of(2026, 6, 30));
        cuponEntity.setPorcentaje(25.0f);

        // Act
        Cupon cupon = cuponEntity.asDomain();

        // Assert
        assertNotNull(cupon);
        assertNull(cupon.getId());
    }

    @Test
    void testAsDomainConNombreCorto() {
        // Arrange
        CuponEntity cuponEntity = new CuponEntity();
        cuponEntity.setId(5);
        cuponEntity.setNombre("A");
        cuponEntity.setFechaInicio(LocalDate.of(2026, 7, 1));
        cuponEntity.setFechaFin(LocalDate.of(2026, 7, 31));
        cuponEntity.setPorcentaje(5.0f);

        // Act
        Cupon cupon = cuponEntity.asDomain();

        // Assert
        assertNotNull(cupon);
        assertEquals("A", cupon.getNombre());
    }

    @Test
    void testAsDomainConNombreLargo() {
        // Arrange
        CuponEntity cuponEntity = new CuponEntity();
        cuponEntity.setId(6);
        cuponEntity.setNombre("CUPONPROMOLARGO");
        cuponEntity.setFechaInicio(LocalDate.of(2026, 8, 1));
        cuponEntity.setFechaFin(LocalDate.of(2026, 8, 31));
        cuponEntity.setPorcentaje(30.0f);

        // Act
        Cupon cupon = cuponEntity.asDomain();

        // Assert
        assertNotNull(cupon);
        assertEquals("CUPONPROMOLARGO", cupon.getNombre());
    }

    @Test
    void testAsDomainConPorcentajeDecimal() {
        // Arrange
        CuponEntity cuponEntity = new CuponEntity();
        cuponEntity.setId(7);
        cuponEntity.setNombre("DECIMAL");
        cuponEntity.setFechaInicio(LocalDate.of(2026, 9, 1));
        cuponEntity.setFechaFin(LocalDate.of(2026, 9, 30));
        cuponEntity.setPorcentaje(12.5f);

        // Act
        Cupon cupon = cuponEntity.asDomain();

        // Assert
        assertNotNull(cupon);
        assertEquals(12.5f, cupon.getPorcentaje());
    }

    @Test
    void testEqualsYHashCode() {
        // Arrange
        CuponEntity cupon1 = new CuponEntity();
        cupon1.setId(1);
        cupon1.setNombre("PROMO");
        cupon1.setFechaInicio(LocalDate.of(2026, 1, 1));
        cupon1.setFechaFin(LocalDate.of(2026, 1, 31));
        cupon1.setPorcentaje(10.0f);

        CuponEntity cupon2 = new CuponEntity();
        cupon2.setId(1);
        cupon2.setNombre("PROMO");
        cupon2.setFechaInicio(LocalDate.of(2026, 1, 1));
        cupon2.setFechaFin(LocalDate.of(2026, 1, 31));
        cupon2.setPorcentaje(10.0f);

        // Act & Assert
        assertEquals(cupon1, cupon2);
        assertEquals(cupon1.hashCode(), cupon2.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        CuponEntity cuponEntity = new CuponEntity();
        cuponEntity.setId(10);
        cuponEntity.setNombre("TESTCUPON");
        cuponEntity.setFechaInicio(LocalDate.of(2026, 10, 1));
        cuponEntity.setFechaFin(LocalDate.of(2026, 10, 31));
        cuponEntity.setPorcentaje(50.0f);

        // Act
        String resultado = cuponEntity.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("TESTCUPON"));
    }

    @Test
    void testSetIdMultiplesVeces() {
        // Arrange
        CuponEntity cuponEntity = new CuponEntity();

        // Act
        cuponEntity.setId(1);
        cuponEntity.setId(2);
        cuponEntity.setId(3);

        // Assert
        assertEquals(3, cuponEntity.getId());
    }

    @Test
    void testSetNombreMultiplesVeces() {
        // Arrange
        CuponEntity cuponEntity = new CuponEntity();

        // Act
        cuponEntity.setNombre("PRIMERO");
        cuponEntity.setNombre("SEGUNDO");
        cuponEntity.setNombre("TERCERO");

        // Assert
        assertEquals("TERCERO", cuponEntity.getNombre());
    }

    @Test
    void testSetFechasMultiplesVeces() {
        // Arrange
        CuponEntity cuponEntity = new CuponEntity();
        LocalDate fecha1 = LocalDate.of(2026, 1, 1);
        LocalDate fecha2 = LocalDate.of(2026, 2, 1);
        LocalDate fecha3 = LocalDate.of(2026, 3, 1);

        // Act
        cuponEntity.setFechaInicio(fecha1);
        cuponEntity.setFechaInicio(fecha2);
        cuponEntity.setFechaInicio(fecha3);

        // Assert
        assertEquals(fecha3, cuponEntity.getFechaInicio());
    }

    @Test
    void testSetPorcentajeMultiplesVeces() {
        // Arrange
        CuponEntity cuponEntity = new CuponEntity();

        // Act
        cuponEntity.setPorcentaje(10.0f);
        cuponEntity.setPorcentaje(20.0f);
        cuponEntity.setPorcentaje(30.0f);

        // Assert
        assertEquals(30.0f, cuponEntity.getPorcentaje());
    }
}

