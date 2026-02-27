package unrn.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValidarCuponResponseTest {

    @Test
    void testCrearValidarCuponResponseValido() {
        // Arrange
        boolean valido = true;
        Float porcentaje = 20.0f;
        LocalDate desde = LocalDate.of(2026, 1, 1);
        LocalDate hasta = LocalDate.of(2026, 3, 31);
        String motivo = null;

        // Act
        ValidarCuponResponse response = new ValidarCuponResponse(
                valido, porcentaje, desde, hasta, motivo
        );

        // Assert
        assertTrue(response.valido());
        assertEquals(porcentaje, response.porcentajeDescuento());
        assertEquals(desde, response.vigenteDesde());
        assertEquals(hasta, response.vigenteHasta());
        assertNull(response.motivo());
    }

    @Test
    void testCrearValidarCuponResponseNoExiste() {
        // Arrange & Act
        ValidarCuponResponse response = new ValidarCuponResponse(
                false, null, null, null, "NO_EXISTE"
        );

        // Assert
        assertFalse(response.valido());
        assertNull(response.porcentajeDescuento());
        assertNull(response.vigenteDesde());
        assertNull(response.vigenteHasta());
        assertEquals("NO_EXISTE", response.motivo());
    }

    @Test
    void testCrearValidarCuponResponseNoVigente() {
        // Arrange & Act
        ValidarCuponResponse response = new ValidarCuponResponse(
                false, null, null, null, "NO_VIGENTE"
        );

        // Assert
        assertFalse(response.valido());
        assertEquals("NO_VIGENTE", response.motivo());
    }

    @Test
    void testCrearValidarCuponResponseCodigoVacio() {
        // Arrange & Act
        ValidarCuponResponse response = new ValidarCuponResponse(
                false, null, null, null, "CODIGO_VACIO"
        );

        // Assert
        assertFalse(response.valido());
        assertEquals("CODIGO_VACIO", response.motivo());
    }

    @Test
    void testValidarCuponResponseConPorcentajeCero() {
        // Arrange & Act
        ValidarCuponResponse response = new ValidarCuponResponse(
                true,
                0.0f,
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 28),
                null
        );

        // Assert
        assertTrue(response.valido());
        assertEquals(0.0f, response.porcentajeDescuento());
    }

    @Test
    void testValidarCuponResponseConPorcentaje100() {
        // Arrange & Act
        ValidarCuponResponse response = new ValidarCuponResponse(
                true,
                100.0f,
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 31),
                null
        );

        // Assert
        assertTrue(response.valido());
        assertEquals(100.0f, response.porcentajeDescuento());
    }

    @Test
    void testValidarCuponResponseEquals() {
        // Arrange
        ValidarCuponResponse response1 = new ValidarCuponResponse(
                true, 15.0f,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 30),
                null
        );

        ValidarCuponResponse response2 = new ValidarCuponResponse(
                true, 15.0f,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 30),
                null
        );

        // Act & Assert
        assertEquals(response1, response2);
    }

    @Test
    void testValidarCuponResponseHashCode() {
        // Arrange
        ValidarCuponResponse response1 = new ValidarCuponResponse(
                false, null, null, null, "NO_EXISTE"
        );

        ValidarCuponResponse response2 = new ValidarCuponResponse(
                false, null, null, null, "NO_EXISTE"
        );

        // Act & Assert
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testValidarCuponResponseToString() {
        // Arrange
        ValidarCuponResponse response = new ValidarCuponResponse(
                true,
                25.0f,
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 31),
                null
        );

        // Act
        String result = response.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("true"));
        assertTrue(result.contains("25.0"));
    }

    @Test
    void testValidarCuponResponseEsSerializable() {
        // Arrange
        ValidarCuponResponse response = new ValidarCuponResponse(
                true, 10.0f,
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 30),
                null
        );

        // Act & Assert
        assertTrue(response instanceof java.io.Serializable);
    }

    @Test
    void testValidarCuponResponseConFechasMismaFecha() {
        // Arrange
        LocalDate fecha = LocalDate.of(2026, 7, 15);

        // Act
        ValidarCuponResponse response = new ValidarCuponResponse(
                true, 30.0f, fecha, fecha, null
        );

        // Assert
        assertEquals(response.vigenteDesde(), response.vigenteHasta());
    }

    @Test
    void testValidarCuponResponseConPorcentajeDecimal() {
        // Arrange & Act
        ValidarCuponResponse response = new ValidarCuponResponse(
                true,
                12.5f,
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 31),
                null
        );

        // Assert
        assertEquals(12.5f, response.porcentajeDescuento());
    }
}

