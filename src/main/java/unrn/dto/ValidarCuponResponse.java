package unrn.dto;

import java.io.Serializable;

public record ValidarCuponResponse(
        boolean valido,
        Float monto,
        String motivo
) implements Serializable {}