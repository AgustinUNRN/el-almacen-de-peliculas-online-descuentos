package unrn.event.descuento;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import unrn.dto.ValidarCuponRequest;
import unrn.dto.ValidarCuponResponse;
import unrn.service.CuponService;

@Component
public class ValidarCuponRpcListener {

    private final CuponService cuponService;

    public ValidarCuponRpcListener(CuponService cuponService) {
        this.cuponService = cuponService;
    }

    @RabbitListener(queues = "${rabbitmq.descuentos.cupon.validar.queue}")
    public ValidarCuponResponse validar(ValidarCuponRequest req) {
        if (req == null || req.nombreCupon() == null || req.nombreCupon().isBlank()) {
            return new ValidarCuponResponse(false, null, "CODIGO_VACIO");
        }

        // La lógica concreta depende de cómo sea tu CuponService hoy.
        // Concepto: "existe + vigente" => valido=true y devolver monto/porcentaje
        return cuponService.validarCuponRpc(req.nombreCupon());
    }
}