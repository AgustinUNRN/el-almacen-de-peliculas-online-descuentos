package unrn.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import unrn.app.ElAlmacenDePeliculasOnlineDescuentosApplication;
import unrn.dto.CuponDTO;
import unrn.dto.ValidarCuponResponse;
import unrn.infra.persistence.CuponRepository;
import unrn.infra.persistence.CuponEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ElAlmacenDePeliculasOnlineDescuentosApplication.class)
@ActiveProfiles("test")
@Transactional
class CuponServiceTest {

    @Autowired
    private CuponRepository cuponRepository;

    @Autowired
    private CuponService cuponService;

    private CuponEntity cuponVigente;
    private CuponEntity cuponExpirado;
    private CuponEntity cuponFuturo;

    @BeforeEach
    void setUp() {
        // Cupón vigente
        cuponVigente = new CuponEntity();
        cuponVigente.setNombre("VIGENTE_SETUP");
        cuponVigente.setPorcentaje(15.0f);
        cuponVigente.setFechaInicio(LocalDate.now().minusDays(5));
        cuponVigente.setFechaFin(LocalDate.now().plusDays(5));
        cuponRepository.guardar(cuponVigente);

        // Cupón expirado
        cuponExpirado = new CuponEntity();
        cuponExpirado.setNombre("EXPIRADO_SETUP");
        cuponExpirado.setPorcentaje(20.0f);
        cuponExpirado.setFechaInicio(LocalDate.now().minusDays(20));
        cuponExpirado.setFechaFin(LocalDate.now().minusDays(5));
        cuponRepository.guardar(cuponExpirado);

        // Cupón futuro
        cuponFuturo = new CuponEntity();
        cuponFuturo.setNombre("FUTURO_SETUP");
        cuponFuturo.setPorcentaje(25.0f);
        cuponFuturo.setFechaInicio(LocalDate.now().plusDays(5));
        cuponFuturo.setFechaFin(LocalDate.now().plusDays(15));
        cuponRepository.guardar(cuponFuturo);
    }

    @Test
    void testCrearCupon() {
        CuponEntity cupon = new CuponEntity();
        cupon.setNombre("DESCUENTO10");
        cupon.setPorcentaje(10.0f);
        cupon.setFechaInicio(LocalDate.now());
        cupon.setFechaFin(LocalDate.now().plusDays(30));

        CuponEntity saved = cuponRepository.guardar(cupon);

        assertNotNull(saved.getId());
        assertEquals("DESCUENTO10", saved.getNombre());
        assertEquals(10.0f, saved.getPorcentaje());
    }

    @Test
    void testCuponVigente() {
        CuponEntity cupon = new CuponEntity();
        cupon.setNombre("VIGENTE");
        cupon.setPorcentaje(15.0f);
        cupon.setFechaInicio(LocalDate.now().minusDays(5));
        cupon.setFechaFin(LocalDate.now().plusDays(10));

        cuponRepository.guardar(cupon);

        assertTrue(esVigente(cupon));
    }

    @Test
    void testCuponExpirado() {
        CuponEntity cupon = new CuponEntity();
        cupon.setNombre("EXPIRADO");
        cupon.setPorcentaje(20.0f);
        cupon.setFechaInicio(LocalDate.now().minusDays(20));
        cupon.setFechaFin(LocalDate.now().minusDays(5));

        cuponRepository.guardar(cupon);

        assertFalse(esVigente(cupon));
    }

    private boolean esVigente(CuponEntity cupon) {
        LocalDate hoy = LocalDate.now();
        return !hoy.isBefore(cupon.getFechaInicio()) && !hoy.isAfter(cupon.getFechaFin());
    }

    @Test
    void testBuscarPorId() {
        // Act
        CuponEntity encontrado = cuponService.buscarPorId(cuponVigente.getId());

        // Assert
        assertNotNull(encontrado);
        assertEquals("VIGENTE_SETUP", encontrado.getNombre());
    }

    @Test
    void testBuscarPorIdNoExistente() {
        // Act
        CuponEntity resultado = cuponService.buscarPorId(999);

        // Assert
        assertNull(resultado);
    }

    @Test
    void testValidarCuponValido() {
        // Act
        Optional<CuponDTO> resultado = cuponService.validarCupon("VIGENTE_SETUP");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("VIGENTE_SETUP", resultado.get().nombre());
        assertEquals(15.0f, resultado.get().porcentaje());
    }

    @Test
    void testValidarCuponExpiradoService() {
        // Act
        Optional<CuponDTO> resultado = cuponService.validarCupon("EXPIRADO_SETUP");

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    void testValidarCuponFuturo() {
        // Act
        Optional<CuponDTO> resultado = cuponService.validarCupon("FUTURO_SETUP");

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    void testValidarCuponNoExiste() {
        // Act
        Optional<CuponDTO> resultado = cuponService.validarCupon("NOEXISTE");

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    void testListarCupones() {
        // Act
        List<CuponDTO> resultado = cuponService.listarCupones();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.size() >= 3);
    }

    @Test
    void testListarCuponesVigentes() {
        // Act
        List<CuponDTO> resultado = cuponService.listarCuponesVigentes();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.stream().anyMatch(c -> c.nombre().equals("VIGENTE_SETUP")));
        assertFalse(resultado.stream().anyMatch(c -> c.nombre().equals("EXPIRADO_SETUP")));
    }

    @Test
    void testValidarCuponRpcValido() {
        // Act
        ValidarCuponResponse response = cuponService.validarCuponRpc("VIGENTE_SETUP");

        // Assert
        assertNotNull(response);
        assertTrue(response.valido());
        assertEquals(15.0f, response.porcentajeDescuento());
        assertNotNull(response.vigenteDesde());
        assertNotNull(response.vigenteHasta());
        assertNull(response.motivo());
    }

    @Test
    void testValidarCuponRpcNoExiste() {
        // Act
        ValidarCuponResponse response = cuponService.validarCuponRpc("NOEXISTE");

        // Assert
        assertNotNull(response);
        assertFalse(response.valido());
        assertNull(response.porcentajeDescuento());
        assertEquals("NO_EXISTE", response.motivo());
    }

    @Test
    void testValidarCuponRpcNoVigente() {
        // Act
        ValidarCuponResponse response = cuponService.validarCuponRpc("EXPIRADO_SETUP");

        // Assert
        assertNotNull(response);
        assertFalse(response.valido());
        assertNull(response.porcentajeDescuento());
        assertEquals("NO_VIGENTE", response.motivo());
    }

    @Test
    void testCrearCuponCompleto() {
        // Arrange
        CuponDTO cuponDTO = new CuponDTO(
                null,
                "NUEVO2026",
                30.0f,
                LocalDate.now(),
                LocalDate.now().plusDays(10)
        );

        // Act
        CuponDTO resultado = cuponService.crearCupon(cuponDTO);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.id());
        assertEquals("NUEVO2026", resultado.nombre());
        assertEquals(30.0f, resultado.porcentaje());
    }

}


