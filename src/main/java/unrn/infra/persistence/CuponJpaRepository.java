package unrn.infra.persistence;

import org.springframework.stereotype.Repository;
import unrn.model.Cupon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository
public interface CuponJpaRepository extends JpaRepository<CuponEntity, Integer> {
}