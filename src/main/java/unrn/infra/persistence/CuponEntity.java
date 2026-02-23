package unrn.infra.persistence;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.authentication.ott.InMemoryOneTimeTokenService;
import unrn.model.Cupon;

import java.time.LocalDate;

@Entity(name = "CuponEntity")
@Table(name = "cupon")
@Data // Genera Getters y Setters con Lombok
public class CuponEntity {

    public CuponEntity() {
    } // JPA requiere un constructor sin argumentos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 15)
    private String nombre;

    @Column(name = "fechaInicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fechaFin", nullable = false)
    private LocalDate fechaFin;

    private Float monto;

    public Cupon asDomain() {
        return new Cupon(id, nombre, fechaInicio, fechaFin, monto);
    }
}