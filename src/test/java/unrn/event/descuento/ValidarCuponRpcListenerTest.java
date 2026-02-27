package unrn.event.descuento;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import unrn.dto.ValidarCuponRequest;
import unrn.dto.ValidarCuponResponse;
import unrn.service.CuponService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidarCuponRpcListenerTest {

    @Mock
    private CuponService cuponService;

    @InjectMocks
    private ValidarCuponRpcListener listener;

    private ValidarCuponResponse responseValido;
    private ValidarCuponResponse responseNoExiste;
    private ValidarCuponResponse responseNoVigente;

    @BeforeEach
    void setUp() {
        responseValido = new ValidarCuponResponse(
                true,
                20.0f,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 3, 31),
                null
        );

        responseNoExiste = new ValidarCuponResponse(
                false, null, null, null, "NO_EXISTE"
        );

        responseNoVigente = new ValidarCuponResponse(
                false, null, null, null, "NO_VIGENTE"
        );
    }

    @Test
    void testValidarCuponConCodigoValido() {
        // Arrange
        ValidarCuponRequest request = new ValidarCuponRequest("VERANO2026");
        when(cuponService.validarCuponRpc("VERANO2026")).thenReturn(responseValido);

        // Act
        ValidarCuponResponse response = listener.validar(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.valido());
        assertEquals(20.0f, response.porcentajeDescuento());
        assertNull(response.motivo());
        verify(cuponService, times(1)).validarCuponRpc("VERANO2026");
    }

    @Test
    void testValidarCuponConCodigoNoExiste() {
        // Arrange
        ValidarCuponRequest request = new ValidarCuponRequest("NOEXISTE");
        when(cuponService.validarCuponRpc("NOEXISTE")).thenReturn(responseNoExiste);

        // Act
        ValidarCuponResponse response = listener.validar(request);

        // Assert
        assertNotNull(response);
        assertFalse(response.valido());
        assertEquals("NO_EXISTE", response.motivo());
        verify(cuponService, times(1)).validarCuponRpc("NOEXISTE");
    }

    @Test
    void testValidarCuponConCodigoExpirado() {
        // Arrange
        ValidarCuponRequest request = new ValidarCuponRequest("EXPIRADO");
        when(cuponService.validarCuponRpc("EXPIRADO")).thenReturn(responseNoVigente);

        // Act
        ValidarCuponResponse response = listener.validar(request);

        // Assert
        assertNotNull(response);
        assertFalse(response.valido());
        assertEquals("NO_VIGENTE", response.motivo());
        verify(cuponService, times(1)).validarCuponRpc("EXPIRADO");
    }

    @Test
    void testValidarCuponConRequestNulo() {
        // Act
        ValidarCuponResponse response = listener.validar(null);

        // Assert
        assertNotNull(response);
        assertFalse(response.valido());
        assertEquals("CODIGO_VACIO", response.motivo());
        verify(cuponService, never()).validarCuponRpc(anyString());
    }

    @Test
    void testValidarCuponConNombreCuponNulo() {
        // Arrange
        ValidarCuponRequest request = new ValidarCuponRequest(null);

        // Act
        ValidarCuponResponse response = listener.validar(request);

        // Assert
        assertNotNull(response);
        assertFalse(response.valido());
        assertEquals("CODIGO_VACIO", response.motivo());
        verify(cuponService, never()).validarCuponRpc(anyString());
    }

    @Test
    void testValidarCuponConNombreCuponVacio() {
        // Arrange
        ValidarCuponRequest request = new ValidarCuponRequest("");

        // Act
        ValidarCuponResponse response = listener.validar(request);

        // Assert
        assertNotNull(response);
        assertFalse(response.valido());
        assertEquals("CODIGO_VACIO", response.motivo());
        verify(cuponService, never()).validarCuponRpc(anyString());
    }

    @Test
    void testValidarCuponConNombreCuponBlanco() {
        // Arrange
        ValidarCuponRequest request = new ValidarCuponRequest("   ");

        // Act
        ValidarCuponResponse response = listener.validar(request);

        // Assert
        assertNotNull(response);
        assertFalse(response.valido());
        assertEquals("CODIGO_VACIO", response.motivo());
        verify(cuponService, never()).validarCuponRpc(anyString());
    }

    @Test
    void testValidarCuponConCodigoConEspacios() {
        // Arrange
        ValidarCuponRequest request = new ValidarCuponRequest(" VERANO2026 ");
        when(cuponService.validarCuponRpc(" VERANO2026 ")).thenReturn(responseValido);

        // Act
        ValidarCuponResponse response = listener.validar(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.valido());
        verify(cuponService, times(1)).validarCuponRpc(" VERANO2026 ");
    }

    @Test
    void testValidarCuponConPorcentajeCero() {
        // Arrange
        ValidarCuponRequest request = new ValidarCuponRequest("GRATIS");
        ValidarCuponResponse responseCero = new ValidarCuponResponse(
                true, 0.0f,
                LocalDate.now(), LocalDate.now().plusDays(10),
                null
        );
        when(cuponService.validarCuponRpc("GRATIS")).thenReturn(responseCero);

        // Act
        ValidarCuponResponse response = listener.validar(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.valido());
        assertEquals(0.0f, response.porcentajeDescuento());
    }

    @Test
    void testValidarCuponConPorcentaje100() {
        // Arrange
        ValidarCuponRequest request = new ValidarCuponRequest("COMPLETO");
        ValidarCuponResponse response100 = new ValidarCuponResponse(
                true, 100.0f,
                LocalDate.now(), LocalDate.now().plusDays(10),
                null
        );
        when(cuponService.validarCuponRpc("COMPLETO")).thenReturn(response100);

        // Act
        ValidarCuponResponse response = listener.validar(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.valido());
        assertEquals(100.0f, response.porcentajeDescuento());
    }

    @Test
    void testValidarCuponConCodigoLargo() {
        // Arrange
        String codigoLargo = "SUPER_MEGA_DESCUENTO_2026_ESPECIAL";
        ValidarCuponRequest request = new ValidarCuponRequest(codigoLargo);
        when(cuponService.validarCuponRpc(codigoLargo)).thenReturn(responseValido);

        // Act
        ValidarCuponResponse response = listener.validar(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.valido());
        verify(cuponService, times(1)).validarCuponRpc(codigoLargo);
    }

    @Test
    void testValidarCuponLlamadaMultiple() {
        // Arrange
        ValidarCuponRequest request1 = new ValidarCuponRequest("CUPON1");
        ValidarCuponRequest request2 = new ValidarCuponRequest("CUPON2");

        when(cuponService.validarCuponRpc("CUPON1")).thenReturn(responseValido);
        when(cuponService.validarCuponRpc("CUPON2")).thenReturn(responseNoExiste);

        // Act
        ValidarCuponResponse response1 = listener.validar(request1);
        ValidarCuponResponse response2 = listener.validar(request2);

        // Assert
        assertTrue(response1.valido());
        assertFalse(response2.valido());
        verify(cuponService, times(1)).validarCuponRpc("CUPON1");
        verify(cuponService, times(1)).validarCuponRpc("CUPON2");
    }

    @Test
    void testConstructorConCuponService() {
        // Arrange & Act
        ValidarCuponRpcListener nuevoListener = new ValidarCuponRpcListener(cuponService);

        // Assert
        assertNotNull(nuevoListener);
    }

    @Test
    void testValidarCuponRetornaFechasCorrectas() {
        // Arrange
        LocalDate fechaInicio = LocalDate.of(2026, 2, 1);
        LocalDate fechaFin = LocalDate.of(2026, 2, 28);
        ValidarCuponRequest request = new ValidarCuponRequest("FEBRERO");
        ValidarCuponResponse responseConFechas = new ValidarCuponResponse(
                true, 15.0f, fechaInicio, fechaFin, null
        );
        when(cuponService.validarCuponRpc("FEBRERO")).thenReturn(responseConFechas);

        // Act
        ValidarCuponResponse response = listener.validar(request);

        // Assert
        assertNotNull(response);
        assertEquals(fechaInicio, response.vigenteDesde());
        assertEquals(fechaFin, response.vigenteHasta());
    }
}

