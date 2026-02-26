package unrn.model;

import java.time.LocalDate;

public class Cupon {

    static final String ERROR_NOMBRE = "El nombre del cupon no puede ser vacío";
    static final String ERROR_FECHA_INICIO = "La fecha de inicio no puede ser posterior a la fecha de fin";
    static final String ERROR_FECHA_INICIO_VACIA = "La fecha de inicio no puede ser vacía";
    static final String ERROR_FECHA_FIN = "La fecha de fin no puede ser anterior a la fecha de inicio";
    static final String ERROR_FECHA_FIN_VACIA = "La fecha de fin no puede ser vacía";
    static final String ERROR_PORCENTAJE = "El porcentaje del descuento debe ser de 0  a 100";
    static final String ERROR_PORCENTAJE_NULO = "El porcentaje del descuento no puede ser nulo";

    private Integer id;

    private String nombre;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private Float porcentaje;

    public Cupon (Integer id, String nombre, LocalDate fechaInicio, LocalDate fechaFin, Float porcentaje) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(ERROR_NOMBRE);
        }
        if (fechaInicio == null) {
            throw new IllegalArgumentException(ERROR_FECHA_INICIO_VACIA);
        }
        if (fechaFin == null) {
            throw new IllegalArgumentException(ERROR_FECHA_FIN_VACIA);
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException(ERROR_FECHA_INICIO);
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException(ERROR_FECHA_FIN);
        }
        if (porcentaje > 100 || porcentaje < 0) {
            throw new IllegalArgumentException(ERROR_PORCENTAJE);
        }
        if (porcentaje == null) {
            throw new IllegalArgumentException(ERROR_PORCENTAJE_NULO);
        }

        this.id = id;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.porcentaje = porcentaje;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public Float getPorcentaje() {
        return porcentaje;
    }

}