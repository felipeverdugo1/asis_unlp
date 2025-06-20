package dao;


import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

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
    public Optional<T> buscarPorId(ID id) {
        try {
            T t = em.find(tipoEntidad, id);
            return Optional.ofNullable(t);
        } catch (NoResultException e) {
            return Optional.empty();
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
    public void eliminar(T entidad) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(entidad);
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
    public Optional<T> buscarPorCampo(String campo, Object valor) {
        try {
            String jpql = "SELECT e FROM " + tipoEntidad.getSimpleName() + " e WHERE e." + campo + " = :valor";
            T resultado = em.createQuery(jpql, tipoEntidad)
                    .setParameter("valor", valor)
                    .getSingleResult();
            return Optional.ofNullable(resultado);
        } catch (NoResultException e) {
            return Optional.empty();
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

    public List<Object> buscarValoresPorCampo(String campo) {
        String jpql = "SELECT e." + campo + " FROM " + tipoEntidad.getSimpleName() + " e";
        return em.createQuery(jpql, Object.class).getResultList();
    }

    public boolean existeOtroConMismoCampo(Long id, String campo, Object valor) {
        String jpql = "SELECT COUNT(e) FROM " + tipoEntidad.getSimpleName() + " e " +
                "WHERE e." + campo + " = :valor AND e.id <> :id";

        Long count = em.createQuery(jpql, Long.class)
                .setParameter("valor", valor)
                .setParameter("id", id)
                .getSingleResult();

        return count > 0;
    }

    @Transactional
    public void flush() {
        em.flush();
    }
}

