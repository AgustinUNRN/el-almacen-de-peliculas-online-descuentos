package unrn.model;

import java.time.LocalDate;

public class Cupon {

    static final String ERROR_NOMBRE = "El nombre del cupon no puede ser vacío";
    static final String ERROR_FECHA_INICIO = "La fecha de inicio no puede ser posterior a la fecha de fin";
    static final String ERROR_FECHA_INICIO_VACIA = "La fecha de inicio no puede ser vacía";
    static final String ERROR_FECHA_FIN = "La fecha de fin no puede ser anterior a la fecha de inicio";
    static final String ERROR_FECHA_FIN_VACIA = "La fecha de fin no puede ser vacía";
    static final String ERROR_MONTO = "El monto del descuento debe ser positivo";
    static final String ERROR_PORCENTAJE = "El porcentaje de descuento debe estar entre 0 y 100";

    private Integer id;

    private String nombre;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private Float monto;

    private Integer porcentaje;

    public Cupon (Integer id, String nombre, LocalDate fechaInicio, LocalDate fechaFin, Float monto, Integer porcentaje) {
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
        if (monto != null && monto <= 0) {
            throw new IllegalArgumentException(ERROR_MONTO);
        }
        if (porcentaje != null && (porcentaje < 0 || porcentaje > 100)) {
            throw new IllegalArgumentException(ERROR_PORCENTAJE);
        }

        this.id = id;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.monto = monto;
        this.porcentaje = porcentaje;
    }

    public int getId() {
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

    public double getMonto() {
        return monto;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

}