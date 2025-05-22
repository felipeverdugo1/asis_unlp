package dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import until.HibernateUtil;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class GenericDAOImpl<T, ID> implements GenericDAO<T, ID> {

    protected EntityManager em;
    private final Class<T> tipoEntidad;

    @SuppressWarnings("unchecked")
    public GenericDAOImpl() {
        this.tipoEntidad = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.em = HibernateUtil.getEntityManagerFactory().createEntityManager();
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
        em.close();
    }

    @Override
    public T buscarPorId(ID id) {
        T t = em.find(tipoEntidad, id);
        em.close();
        return t;
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
        em.close();
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
        em.close();
    }

    @Override
    public List<T> listarTodos() {
        String jpql = "SELECT e FROM " + tipoEntidad.getSimpleName() + " e";
        List<T> tList = em.createQuery(jpql, tipoEntidad).getResultList();
        em.close();
        return tList;
    }



}