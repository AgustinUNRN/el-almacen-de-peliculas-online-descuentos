package unrn.dto;

import org.junit.jupiter.api.Test;
import unrn.infra.persistence.CuponEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CuponDTOTest {

    @Test
    void testCrearCuponDTOCompleto() {
        // Arrange
        Integer id = 1;
        String nombre = "VERANO2026";
        Float porcentaje = 20.0f;
        LocalDate fechaInicio = LocalDate.of(2026, 1, 1);
        LocalDate fechaFin = LocalDate.of(2026, 3, 31);

        // Act
        CuponDTO cuponDTO = new CuponDTO(id, nombre, porcentaje, fechaInicio, fechaFin);

        // Assert
        assertNotNull(cuponDTO);
        assertEquals(id, cuponDTO.id());
        assertEquals(nombre, cuponDTO.nombre());
        assertEquals(porcentaje, cuponDTO.porcentaje());
        assertEquals(fechaInicio, cuponDTO.fechaInicio());
        assertEquals(fechaFin, cuponDTO.fechaFin());
    }

    @Test
    void testCrearCuponDTOConIdNulo() {
        // Arrange & Act
        CuponDTO cuponDTO = new CuponDTO(
                null,
                "NUEVO",
                15.0f,
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 28)
        );

        // Assert
        assertNull(cuponDTO.id());
        assertEquals("NUEVO", cuponDTO.nombre());
    }

    @Test
    void testCrearCuponDTOConPorcentajeCero() {
        // Arrange & Act
        CuponDTO cuponDTO = new CuponDTO(
                1,
                "GRATIS",
                0.0f,
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 31)
        );

        // Assert
        assertEquals(0.0f, cuponDTO.porcentaje());
    }

    @Test
    void testCrearCuponDTOConPorcentaje100() {
        // Arrange & Act
        CuponDTO cuponDTO = new CuponDTO(
                2,
                "DESCUENTO100",
                100.0f,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 30)
        );

        // Assert
        assertEquals(100.0f, cuponDTO.porcentaje());
    }

    @Test
    void testFromCuponEntity() {
        // Arrange
        CuponEntity entity = new CuponEntity();
        entity.setId(10);
        entity.setNombre("TESTCUPON");
        entity.setPorcentaje(25.0f);
        entity.setFechaInicio(LocalDate.of(2026, 5, 1));
        entity.setFechaFin(LocalDate.of(2026, 5, 31));

        // Act
        Object result = CuponDTO.from(entity);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof CuponDTO);
        CuponDTO dto = (CuponDTO) result;
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getNombre(), dto.nombre());
        assertEquals(entity.getPorcentaje(), dto.porcentaje());
        assertEquals(entity.getFechaInicio(), dto.fechaInicio());
        assertEquals(entity.getFechaFin(), dto.fechaFin());
    }

    @Test
    void testCuponDTOEquals() {
        // Arrange
        CuponDTO cupon1 = new CuponDTO(
                1,
                "PROMO",
                10.0f,
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 30)
        );

        CuponDTO cupon2 = new CuponDTO(
                1,
                "PROMO",
                10.0f,
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 30)
        );

        // Act & Assert
        assertEquals(cupon1, cupon2);
    }

    @Test
    void testCuponDTOHashCode() {
        // Arrange
        CuponDTO cupon1 = new CuponDTO(
                1,
                "HASH",
                5.0f,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 31)
        );

        CuponDTO cupon2 = new CuponDTO(
                1,
                "HASH",
                5.0f,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 31)
        );

        // Act & Assert
        assertEquals(cupon1.hashCode(), cupon2.hashCode());
    }

    @Test
    void testCuponDTOToString() {
        // Arrange
        CuponDTO cuponDTO = new CuponDTO(
                5,
                "TOSTRING",
                30.0f,
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 31)
        );

        // Act
        String result = cuponDTO.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("TOSTRING"));
        assertTrue(result.contains("30.0"));
    }

    @Test
    void testCuponDTOConFechasMismaFecha() {
        // Arrange
        LocalDate fecha = LocalDate.of(2026, 9, 15);

        // Act
        CuponDTO cuponDTO = new CuponDTO(
                6,
                "UNDIA",
                50.0f,
                fecha,
                fecha
        );

        // Assert
        assertEquals(cuponDTO.fechaInicio(), cuponDTO.fechaFin());
    }

    @Test
    void testCuponDTOConPorcentajeDecimal() {
        // Arrange & Act
        CuponDTO cuponDTO = new CuponDTO(
                7,
                "DECIMAL",
                12.5f,
                LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 10, 31)
        );

        // Assert
        assertEquals(12.5f, cuponDTO.porcentaje());
    }
}

