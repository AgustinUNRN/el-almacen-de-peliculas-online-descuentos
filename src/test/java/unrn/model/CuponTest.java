package unrn.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CuponTest {

    @Test
    void testCrearCuponValido() {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fin = LocalDate.of(2026, 12, 31);

        Cupon cupon = new Cupon(1, "DESCUENTO10", inicio, fin, 10.0f);

        assertEquals(1, cupon.getId());
        assertEquals("DESCUENTO10", cupon.getNombre());
        assertEquals(inicio, cupon.getFechaInicio());
        assertEquals(fin, cupon.getFechaFin());
        assertEquals(10.0f, cupon.getPorcentaje());
    }

    @Test
    void testCuponConNombreVacio() {
        LocalDate inicio = LocalDate.now();
        LocalDate fin = LocalDate.now().plusDays(10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cupon(null, "", inicio, fin, 10.0f);
        });

        assertEquals(Cupon.ERROR_NOMBRE, exception.getMessage());
    }

    @Test
    void testCuponConNombreNulo() {
        LocalDate inicio = LocalDate.now();
        LocalDate fin = LocalDate.now().plusDays(10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cupon(null, null, inicio, fin, 10.0f);
        });

        assertEquals(Cupon.ERROR_NOMBRE, exception.getMessage());
    }

    @Test
    void testCuponConNombreSoloEspacios() {
        LocalDate inicio = LocalDate.now();
        LocalDate fin = LocalDate.now().plusDays(10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cupon(null, "   ", inicio, fin, 10.0f);
        });

        assertEquals(Cupon.ERROR_NOMBRE, exception.getMessage());
    }

    @Test
    void testCuponConFechaInicioNula() {
        LocalDate fin = LocalDate.now().plusDays(10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cupon(null, "VALIDO", null, fin, 10.0f);
        });

        assertEquals(Cupon.ERROR_FECHA_INICIO_VACIA, exception.getMessage());
    }

    @Test
    void testCuponConFechaFinNula() {
        LocalDate inicio = LocalDate.now();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cupon(null, "VALIDO", inicio, null, 10.0f);
        });

        assertEquals(Cupon.ERROR_FECHA_FIN_VACIA, exception.getMessage());
    }

    @Test
    void testCuponConFechaInicioPosteriorAFin() {
        LocalDate inicio = LocalDate.of(2026, 12, 31);
        LocalDate fin = LocalDate.of(2026, 1, 1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cupon(null, "VALIDO", inicio, fin, 10.0f);
        });

        assertEquals(Cupon.ERROR_FECHA_INICIO, exception.getMessage());
    }

    @Test
    void testCuponConFechaFinAnteriorAInicio() {
        LocalDate inicio = LocalDate.of(2026, 12, 1);
        LocalDate fin = LocalDate.of(2026, 1, 1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cupon(null, "VALIDO", inicio, fin, 10.0f);
        });

        assertEquals(Cupon.ERROR_FECHA_INICIO, exception.getMessage());
    }

    @Test
    void testCuponConPorcentajeMayorA100() {
        LocalDate inicio = LocalDate.now();
        LocalDate fin = LocalDate.now().plusDays(10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cupon(null, "VALIDO", inicio, fin, 150.0f);
        });

        assertEquals(Cupon.ERROR_PORCENTAJE, exception.getMessage());
    }

    @Test
    void testCuponConPorcentajeNegativo() {
        LocalDate inicio = LocalDate.now();
        LocalDate fin = LocalDate.now().plusDays(10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cupon(null, "VALIDO", inicio, fin, -10.0f);
        });

        assertEquals(Cupon.ERROR_PORCENTAJE, exception.getMessage());
    }

    @Test
    void testCuponConPorcentajeCero() {
        LocalDate inicio = LocalDate.now();
        LocalDate fin = LocalDate.now().plusDays(10);

        Cupon cupon = new Cupon(null, "VALIDO", inicio, fin, 0.0f);

        assertEquals(0.0f, cupon.getPorcentaje());
    }

    @Test
    void testCuponConPorcentaje100() {
        LocalDate inicio = LocalDate.now();
        LocalDate fin = LocalDate.now().plusDays(10);

        Cupon cupon = new Cupon(null, "VALIDO", inicio, fin, 100.0f);

        assertEquals(100.0f, cupon.getPorcentaje());
    }

    @Test
    void testCuponConFechasIguales() {
        LocalDate fecha = LocalDate.now();
        Cupon cupon = new Cupon(null, "VALIDO", fecha, fecha, 50.0f);

        assertEquals(fecha, cupon.getFechaInicio());
        assertEquals(fecha, cupon.getFechaFin());
    }
}
