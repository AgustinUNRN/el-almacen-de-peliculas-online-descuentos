package unrn.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidarCuponRequestTest {

    @Test
    void testCrearValidarCuponRequestConNombre() {
        // Arrange
        String nombreCupon = "VERANO2026";

        // Act
        ValidarCuponRequest request = new ValidarCuponRequest(nombreCupon);

        // Assert
        assertNotNull(request);
        assertEquals(nombreCupon, request.nombreCupon());
    }

    @Test
    void testCrearValidarCuponRequestConNombreVacio() {
        // Arrange & Act
        ValidarCuponRequest request = new ValidarCuponRequest("");

        // Assert
        assertNotNull(request);
        assertEquals("", request.nombreCupon());
    }

    @Test
    void testCrearValidarCuponRequestConNombreNulo() {
        // Arrange & Act
        ValidarCuponRequest request = new ValidarCuponRequest(null);

        // Assert
        assertNotNull(request);
        assertNull(request.nombreCupon());
    }

    @Test
    void testValidarCuponRequestEquals() {
        // Arrange
        ValidarCuponRequest request1 = new ValidarCuponRequest("PROMO2026");
        ValidarCuponRequest request2 = new ValidarCuponRequest("PROMO2026");

        // Act & Assert
        assertEquals(request1, request2);
    }

    @Test
    void testValidarCuponRequestHashCode() {
        // Arrange
        ValidarCuponRequest request1 = new ValidarCuponRequest("DESCUENTO10");
        ValidarCuponRequest request2 = new ValidarCuponRequest("DESCUENTO10");

        // Act & Assert
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testValidarCuponRequestToString() {
        // Arrange
        ValidarCuponRequest request = new ValidarCuponRequest("BLACKFRIDAY");

        // Act
        String result = request.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("BLACKFRIDAY"));
    }

    @Test
    void testValidarCuponRequestEsSerializable() {
        // Arrange
        ValidarCuponRequest request = new ValidarCuponRequest("SERIALTEST");

        // Act & Assert
        assertTrue(request instanceof java.io.Serializable);
    }

    @Test
    void testCrearValidarCuponRequestConNombreLargo() {
        // Arrange
        String nombreLargo = "CUPON_SUPER_DESCUENTO_2026";

        // Act
        ValidarCuponRequest request = new ValidarCuponRequest(nombreLargo);

        // Assert
        assertEquals(nombreLargo, request.nombreCupon());
    }
}

