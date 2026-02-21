package unrn.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unrn.dto.CuponDTO;
import unrn.model.Cupon;
import unrn.service.CuponService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/descuentos")
public class DescuentoController {

    @Autowired
    private CuponService cuponService;

    @GetMapping("/test")
    public Map<String, String> test() {
        return Map.of(
                "status", "OK",
                "message", "Servicio de Descuentos operando en el puerto 8084"
        );
    }

    @GetMapping("/validar")
    public ResponseEntity<CuponDTO> validarCupon(@RequestParam String codigo) {
        return cuponService.validarCupon(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/listar")
    public ResponseEntity<List<CuponDTO>> listarTodos() {
        List<CuponDTO> cupones = cuponService.listarCupones();

        if (cupones.isEmpty()) {
            return ResponseEntity.noContent().build(); // Devuelve 204 si no hay datos
        }

        return ResponseEntity.ok(cupones); // Devuelve 200 con la lista de DTOs
    }

    @PostMapping("/crear")
    public ResponseEntity<CuponDTO> crearCupon(@RequestBody CuponDTO cuponDTO) {
        try {
            CuponDTO nuevoCupon = cuponService.crearCupon(cuponDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCupon);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Devuelve 400 con mensaje de error
        }
    }
}