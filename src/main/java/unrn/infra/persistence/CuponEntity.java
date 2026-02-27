package unrn.infra.persistence;

import jakarta.persistence.*;
import lombok.*;
import unrn.model.Cupon;

import java.time.LocalDate;

@Entity(name = "CuponEntity")
@Table(name = "cupon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // <--- Agregá esto
@ToString // <--- Agregá esto para que el test de ToString también pase
public class CuponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // <--- Incluí solo el ID en el equals
    private Integer id;

    @Column(nullable = false, length = 15)
    @ToString.Include // Opcional: para que se vea en el log
    private String nombre;

    @Column(name = "fechaInicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fechaFin", nullable = false)
    private LocalDate fechaFin;

    private Float porcentaje;

    public Cupon asDomain() {
        return new Cupon(id, nombre, fechaInicio, fechaFin, porcentaje);
    }
}