package unrn.dto;

import java.io.Serializable;

public record ValidarCuponResponse(
        boolean valido,
        Float porcentaje,
        String motivo
) implements Serializable {}