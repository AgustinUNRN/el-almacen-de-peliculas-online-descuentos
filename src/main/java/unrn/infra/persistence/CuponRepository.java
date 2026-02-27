package unrn.infra.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CuponRepository {

    @PersistenceContext
    private EntityManager em;

    public CuponEntity guardar(CuponEntity cupon) {
        try {
            CuponEntity existente = buscarPorNombre(cupon.getNombre());
            if (existente != null) {
                throw new IllegalArgumentException("El cupón con nombre '" + cupon.getNombre() + "' ya existe");
            } else {
                em.persist(cupon);
                return cupon;
            }
        } catch (Exception e) {
            System.err.println("DEBUG REPO: Error al guardar cupón '" + cupon.getNombre() + "': " + e.getMessage());
            throw e;
        }
    }

    public CuponEntity buscarPorNombre(String nombre) {
        try {
            return em.createQuery("SELECT c FROM CuponEntity c WHERE c.nombre = :nombre", CuponEntity.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();
        } catch (Exception e) {
            System.err.println("DEBUG REPO: Error buscando cupón '" + nombre + "': " + e.getMessage());
            return null;
        }
    }

    public CuponEntity buscarPorId(Integer id) {
        try {
            return em.find(CuponEntity.class, id);
        } catch (Exception e) {
            return null; // No se encontró el cupón
        }
    }

    public List<CuponEntity> listarCupones() {
        return em.createQuery("SELECT c FROM CuponEntity c", CuponEntity.class).getResultList();
    }

    public List<CuponEntity> listarCuponesVigentes() {
        return em.createQuery("SELECT c FROM CuponEntity c WHERE c.fechaInicio <= CURRENT_DATE AND c.fechaFin >= CURRENT_DATE", CuponEntity.class).getResultList();
    }

}
