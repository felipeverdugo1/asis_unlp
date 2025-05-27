package dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import until.HibernateUtil;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class GenericDAOImpl<T, ID> implements GenericDAO<T, ID> {

    private final Class<T> tipoEntidad;

    @SuppressWarnings("unchecked")
    public GenericDAOImpl() {
        this.tipoEntidad = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected EntityManager getEntityManager() {
        return HibernateUtil.getEntityManagerFactory().createEntityManager();
    }

    @Override
    public void crear(T entidad) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
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
        } finally {
            em.close();
        }
    }

    @Override
    public T buscarPorId(ID id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(tipoEntidad, id);
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizar(T entidad) {
        EntityManager em = getEntityManager();
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
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminar(ID id) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T entidad = em.find(tipoEntidad, id);
            if (entidad != null) {
                em.remove(entidad);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> listarTodos() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT e FROM " + tipoEntidad.getSimpleName() + " e";
            return em.createQuery(jpql, tipoEntidad).getResultList();
        } finally {
            em.close();
        }
    }



}
