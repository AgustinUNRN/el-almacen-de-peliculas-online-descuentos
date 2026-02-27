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
        System.out.println("RPC validar cupon req=" + req);

        if (req == null || req.nombreCupon() == null || req.nombreCupon().isBlank()) {
            System.out.println("RPC validar cupon -> CODIGO_VACIO");

            return new ValidarCuponResponse(false, null,null,null, "CODIGO_VACIO");
        }
        ValidarCuponResponse resp = cuponService.validarCuponRpc(req.nombreCupon());
        System.out.println("RPC validar cupon resp=" + resp);
        return resp;
    }
}