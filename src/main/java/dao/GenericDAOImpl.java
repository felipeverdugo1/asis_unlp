package dao;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;


import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class GenericDAOImpl<T, ID> implements GenericDAO<T, ID> {

    @Inject
    protected EntityManager em;
    private final Class<T> tipoEntidad;

    public GenericDAOImpl(Class<T> tipoEntidad) {
        this.tipoEntidad = tipoEntidad;
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
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
        } finally {
        }
    }

    @Override
    public T buscarPorId(ID id) {
        try {
            T t = em.find(tipoEntidad, id);
            return t;
        } catch (Exception e) {
            throw e;
        } finally {
        }
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
        } finally {
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
        } finally {
        }
    }

    @Override
    public List<T> listarTodos() {
        try {
            String jpql = "SELECT e FROM " + tipoEntidad.getSimpleName() + " e";
            List<T> tList = em.createQuery(jpql, tipoEntidad).getResultList();
            return tList;
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }


    @Override
    public T buscarPorCampo(String campo, Object valor) {
        try {
            String jpql = "SELECT e FROM " + tipoEntidad.getSimpleName() + " e WHERE e." + campo + " = :valor";
            return em.createQuery(jpql, tipoEntidad)
                    .setParameter("valor", valor)
                    .getSingleResult();
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    @Override
    public List<T> buscarTodosPorCampoLike(String campo, Object patron) {
        try {
            String jpql = "SELECT e FROM " + tipoEntidad.getSimpleName() + " e WHERE e." + campo + " LIKE :pattern";
            return em.createQuery(jpql, tipoEntidad)
                    .setParameter("pattern", patron)
                    .getResultList();
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    @Transactional
    public void flush() {
        em.flush();
    }
}

