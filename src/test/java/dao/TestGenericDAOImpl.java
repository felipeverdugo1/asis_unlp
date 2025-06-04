package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import util.TestHibernateUtil;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Test implementation of GenericDAO that uses the test database.
 * This class is similar to GenericDAOImpl but uses TestHibernateUtil instead.
 */
public abstract class TestGenericDAOImpl<T, ID> implements GenericDAO<T, ID> {

    protected EntityManager em;
    private final Class<T> tipoEntidad;

    @SuppressWarnings("unchecked")
    public TestGenericDAOImpl() {
        this.tipoEntidad = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.em = TestHibernateUtil.getEntityManager();
    }

    @Override
    public void crear(T entidad) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(entidad);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public T buscarPorId(ID id) {
        return em.find(tipoEntidad, id);
    }

    @Override
    public void actualizar(T entidad) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(entidad);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public void eliminar(ID id) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T entidad = buscarPorId(id);
            if (entidad != null) {
                em.remove(entidad);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<T> listarTodos() {
        String jpql = "SELECT e FROM " + tipoEntidad.getSimpleName() + " e";
        return em.createQuery(jpql, tipoEntidad).getResultList();
    }

    @Override
    public T buscarPorCampo(String campo, Object valor) {
        try {
            String jpql = "SELECT e FROM " + tipoEntidad.getSimpleName() + " e WHERE e." + campo + " = :valor";
            return em.createQuery(jpql, tipoEntidad)
                    .setParameter("valor", valor)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Close the EntityManager when done with this DAO.
     */
    public void close() {
        TestHibernateUtil.closeEntityManager(em);
    }
}