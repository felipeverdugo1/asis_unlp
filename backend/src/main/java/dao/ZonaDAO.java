package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import model.Jornada;
import model.Zona;

import java.util.List;

@RequestScoped
@Transactional
public class ZonaDAO extends GenericDAOImpl<Zona, Long> {

    public ZonaDAO() {
        super(Zona.class);
    }

    // Si necesitás métodos específicos, acá los agregás
    public List<Zona> listarZonasByBarrio(Long barrio_id) {
        try {
            String jpql = "SELECT e FROM " + Zona.class.getSimpleName() + " e WHERE e.barrio.id" + " = :valor";
            List<Zona> resultado = em.createQuery(jpql, Zona.class)
                    .setParameter("valor", barrio_id)
                    .getResultList();
            return resultado;
        } catch (Exception e) {
            throw e;
        } finally {

        }
    }
}