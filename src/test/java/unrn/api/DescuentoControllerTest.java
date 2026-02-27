package unrn.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import unrn.app.ElAlmacenDePeliculasOnlineDescuentosApplication;
import unrn.dto.CuponDTO;
import unrn.service.CuponService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ElAlmacenDePeliculasOnlineDescuentosApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class DescuentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CuponService cuponService;

    private CuponDTO cuponDTOValido;
    private List<CuponDTO> listaCupones;

    @BeforeEach
    void setUp() {
        cuponDTOValido = new CuponDTO(
                1,
                "VERANO2026",
                20.0f,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 3, 31)
        );

        listaCupones = Arrays.asList(
                cuponDTOValido,
                new CuponDTO(2, "INVIERNO2026", 15.0f,
                        LocalDate.of(2026, 6, 1), LocalDate.of(2026, 8, 31)),
                new CuponDTO(3, "PRIMAVERA2026", 10.0f,
                        LocalDate.of(2026, 9, 1), LocalDate.of(2026, 11, 30))
        );
    }

    @Test
    void testEndpointTest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/descuentos/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testValidarCuponExistente() throws Exception {
        // Arrange
        when(cuponService.validarCupon("VERANO2026"))
                .thenReturn(Optional.of(cuponDTOValido));

        // Act & Assert
        mockMvc.perform(get("/descuentos/validar")
                        .param("codigo", "VERANO2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("VERANO2026"))
                .andExpect(jsonPath("$.porcentaje").value(20.0))
                .andExpect(jsonPath("$.fechaInicio").value("2026-01-01"))
                .andExpect(jsonPath("$.fechaFin").value("2026-03-31"));
    }

    @Test
    void testValidarCuponNoExistente() throws Exception {
        // Arrange
        when(cuponService.validarCupon("NOEXISTE"))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/descuentos/validar")
                        .param("codigo", "NOEXISTE"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testValidarCuponExpirado() throws Exception {
        // Arrange
        when(cuponService.validarCupon("EXPIRADO"))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/descuentos/validar")
                        .param("codigo", "EXPIRADO"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListarTodosCuponesConDatos() throws Exception {
        // Arrange
        when(cuponService.listarCupones()).thenReturn(listaCupones);

        // Act & Assert
        mockMvc.perform(get("/descuentos/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].nombre").value("VERANO2026"))
                .andExpect(jsonPath("$[1].nombre").value("INVIERNO2026"))
                .andExpect(jsonPath("$[2].nombre").value("PRIMAVERA2026"));
    }

    @Test
    void testListarTodosCuponesSinDatos() throws Exception {
        // Arrange
        when(cuponService.listarCupones()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/descuentos/listar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testListarVigentesConDatos() throws Exception {
        // Arrange
        List<CuponDTO> cuponesVigentes = Arrays.asList(
                cuponDTOValido,
                new CuponDTO(2, "ACTUAL", 25.0f,
                        LocalDate.now().minusDays(5), LocalDate.now().plusDays(10))
        );
        when(cuponService.listarCuponesVigentes()).thenReturn(cuponesVigentes);

        // Act & Assert
        mockMvc.perform(get("/descuentos/listar-vigentes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("VERANO2026"))
                .andExpect(jsonPath("$[1].nombre").value("ACTUAL"));
    }

    @Test
    void testListarVigentesSinDatos() throws Exception {
        // Arrange
        when(cuponService.listarCuponesVigentes()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/descuentos/listar-vigentes"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCrearCuponExitoso() throws Exception {
        // Arrange
        CuponDTO nuevoCupon = new CuponDTO(
                null,
                "NUEVO2026",
                30.0f,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 30)
        );

        CuponDTO cuponCreado = new CuponDTO(
                4,
                "NUEVO2026",
                30.0f,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 30)
        );

        when(cuponService.crearCupon(any(CuponDTO.class))).thenReturn(cuponCreado);

        // Act & Assert
        mockMvc.perform(post("/descuentos/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre": "NUEVO2026",
                                    "porcentaje": 30.0,
                                    "fechaInicio": "2026-04-01",
                                    "fechaFin": "2026-04-30"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.nombre").value("NUEVO2026"))
                .andExpect(jsonPath("$.porcentaje").value(30.0));
    }

    @Test
    void testCrearCuponConDatosInvalidos() throws Exception {
        // Arrange
        when(cuponService.crearCupon(any(CuponDTO.class)))
                .thenThrow(new IllegalArgumentException("Datos inválidos"));

        // Act & Assert
        mockMvc.perform(post("/descuentos/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre": "",
                                    "porcentaje": -5.0,
                                    "fechaInicio": "2026-05-01",
                                    "fechaFin": "2026-04-01"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCrearCuponConNombreDuplicado() throws Exception {
        // Arrange
        when(cuponService.crearCupon(any(CuponDTO.class)))
                .thenThrow(new IllegalArgumentException("El cupón ya existe"));

        // Act & Assert
        mockMvc.perform(post("/descuentos/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre": "VERANO2026",
                                    "porcentaje": 20.0,
                                    "fechaInicio": "2026-01-01",
                                    "fechaFin": "2026-03-31"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testValidarCuponConCodigoVacio() throws Exception {
        // Arrange
        when(cuponService.validarCupon("")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/descuentos/validar")
                        .param("codigo", ""))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListarCuponesUnSoloElemento() throws Exception {
        // Arrange
        List<CuponDTO> unCupon = Collections.singletonList(cuponDTOValido);
        when(cuponService.listarCupones()).thenReturn(unCupon);

        // Act & Assert
        mockMvc.perform(get("/descuentos/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testCrearCuponConPorcentajeCero() throws Exception {
        // Arrange
        CuponDTO cuponCero = new CuponDTO(
                5,
                "GRATIS",
                0.0f,
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 31)
        );

        when(cuponService.crearCupon(any(CuponDTO.class))).thenReturn(cuponCero);

        // Act & Assert
        mockMvc.perform(post("/descuentos/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre": "GRATIS",
                                    "porcentaje": 0.0,
                                    "fechaInicio": "2026-05-01",
                                    "fechaFin": "2026-05-31"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.porcentaje").value(0.0));
    }

    @Test
    void testCrearCuponConPorcentaje100() throws Exception {
        // Arrange
        CuponDTO cupon100 = new CuponDTO(
                6,
                "COMPLETO",
                100.0f,
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 30)
        );

        when(cuponService.crearCupon(any(CuponDTO.class))).thenReturn(cupon100);

        // Act & Assert
        mockMvc.perform(post("/descuentos/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre": "COMPLETO",
                                    "porcentaje": 100.0,
                                    "fechaInicio": "2026-06-01",
                                    "fechaFin": "2026-06-30"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.porcentaje").value(100.0));
    }
}

