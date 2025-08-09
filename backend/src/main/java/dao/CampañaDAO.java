package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import model.Campaña;
import model.Jornada;

import java.util.List;


@RequestScoped
@Transactional
public class CampañaDAO extends GenericDAOImpl<Campaña, Long> {
    public CampañaDAO() {
        super(Campaña.class);
    }

    public List<Campaña> listarCampaniasByBarrio(Long barrio_id) {
        try {
            String jpql = "SELECT e FROM " + Campaña.class.getSimpleName() + " e WHERE e.barrio.id" + " = :valor";
            List<Campaña> resultado = em.createQuery(jpql, Campaña.class)
                    .setParameter("valor", barrio_id)
                    .getResultList();
            return resultado;
        } catch (NoResultException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }
}