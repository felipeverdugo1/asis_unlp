package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import model.Jornada;

import java.util.List;
import java.util.Optional;


@RequestScoped
@Transactional
public class JornadaDAO extends GenericDAOImpl<Jornada, Long> {
    public JornadaDAO() {
        super(Jornada.class);
    }

    // Si necesitás métodos específicos, acá los agregás
    public List<Jornada> listarJornadasByCampania(Long campania_id) {
        try {
            String jpql = "SELECT e FROM " + Jornada.class.getSimpleName() + " e WHERE e.campaña.id" + " = :valor";
            List<Jornada> resultado = em.createQuery(jpql, Jornada.class)
                    .setParameter("valor", campania_id)
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