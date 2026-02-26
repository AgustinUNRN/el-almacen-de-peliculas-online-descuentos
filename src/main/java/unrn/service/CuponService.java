package unrn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unrn.dto.CuponDTO;
import unrn.dto.ValidarCuponResponse;
import unrn.infra.persistence.CuponEntity;
import unrn.infra.persistence.CuponRepository;
import unrn.model.Cupon;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CuponService {
    @Autowired
    private CuponRepository repository;

    public CuponEntity buscarPorId(int id) {
        return repository.buscarPorId(id);
    }

    public Optional<CuponDTO> validarCupon(String nombre) {
        CuponEntity cuponEntity = repository.buscarPorNombre(nombre);

        if (cuponEntity == null) {
            System.out.println("DEBUG: El repositorio no encontró nada para: " + nombre);
            return Optional.empty();
        }

        boolean valido = esValido(cuponEntity);
        System.out.println("DEBUG: Cupón encontrado: " + cuponEntity.getNombre());
        System.out.println("DEBUG: ¿Es válido por fecha? " + valido);
        System.out.println("DEBUG: Fecha Inicio: " + cuponEntity.getFechaInicio() + " | Fin: " + cuponEntity.getFechaFin());

        if (valido) {
            return Optional.of(convertirADto(cuponEntity));
        }

        return Optional.empty();
    }

    private boolean esValido(CuponEntity cupon) {
        LocalDate hoy = LocalDate.now();
        return !hoy.isBefore(cupon.getFechaInicio()) && !hoy.isAfter(cupon.getFechaFin());
    }

    private CuponDTO convertirADto(CuponEntity entity) {
        return new CuponDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getMonto(),
                entity.getFechaInicio(),
                entity.getFechaFin()
        );
    }

    @Transactional(readOnly = true)
    public List<CuponDTO> listarCupones() {
        return repository.listarCupones().stream()
                .map(this::convertirADto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CuponDTO> listarCuponesVigentes() {
        return repository.listarCuponesVigentes().stream()
                .map(this::convertirADto)
                .toList();
    }

    @Transactional
    public CuponDTO crearCupon(CuponDTO cuponDTO) {
        CuponEntity entity = new CuponEntity();
        entity.setNombre(cuponDTO.nombre());
        entity.setMonto(cuponDTO.monto());
        entity.setFechaInicio(cuponDTO.fechaInicio());
        entity.setFechaFin(cuponDTO.fechaFin());

        CuponEntity guardado = repository.guardar(entity);
        return convertirADto(guardado);
    }

    public ValidarCuponResponse validarCuponRpc(String nombreCupon) {
        Optional<CuponDTO> cuponOpt = validarCupon(nombreCupon);
        if (cuponOpt.isEmpty()) {
            return new ValidarCuponResponse(false, null, "NO_EXISTE_O_NO_VIGENTE");
        }
        CuponDTO cupon = cuponOpt.get();
        return new ValidarCuponResponse(true, cupon.monto(), null); // motivo null cuando es válido
    }
}