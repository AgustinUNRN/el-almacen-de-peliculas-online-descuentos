package unrn.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import unrn.infra.persistence.CuponEntity;

import java.time.LocalDate;

public record CuponDTO (
        Integer id,
        String nombre,
        Float porcentaje,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate fechaInicio,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate fechaFin){

    public static Object from(CuponEntity cuponEntity) {
        return new CuponDTO(cuponEntity.getId(), cuponEntity.getNombre(), cuponEntity.getPorcenaje(), cuponEntity.getFechaInicio(), cuponEntity.getFechaFin());
    }
}
