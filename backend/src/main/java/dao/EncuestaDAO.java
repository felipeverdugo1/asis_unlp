package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import model.Encuesta;
import model.Pregunta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Transactional
public class EncuestaDAO extends GenericDAOImpl<Encuesta, Long> {
    public EncuestaDAO() {
        super(Encuesta.class);
    }

    // Si necesitás métodos específicos, acá los agregás

    public List<Encuesta> findByFiltrosVivienda(List<String> materiales, String agua) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Encuesta> cq = cb.createQuery(Encuesta.class);
        Root<Encuesta> e = cq.from(Encuesta.class);
        Join<Encuesta, Pregunta> p = e.join("preguntas");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(p.get("esPersonal"), false));

        if (materiales != null && !materiales.isEmpty()) {
            predicates.add(cb.and(
                    cb.like(cb.lower(p.get("pregunta")), "%material%"),
                    p.get("respuesta").in(materiales)
            ));
        }

        if (agua != null) {
            predicates.add(cb.and(
                    cb.like(cb.lower(p.get("pregunta")), "%agua%"),
                    cb.equal(cb.lower(p.get("respuesta")), agua.toLowerCase())
            ));
        }

        cq.select(e).distinct(true).where(predicates.toArray(new Predicate[0]));
        return em.createQuery(cq).getResultList();
    }

    public boolean cumpleFiltrosVivienda(Long encuestaId, List<String> materiales, String agua) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Pregunta> p = cq.from(Pregunta.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(p.get("encuesta").get("id"), encuestaId));
        predicates.add(cb.equal(p.get("esPersonal"), false));

        List<Predicate> condicionesMaterial = new ArrayList<>();
        if (materiales != null && !materiales.isEmpty()) {
            condicionesMaterial.add(cb.and(
                    cb.like(cb.lower(p.get("pregunta")), "%material%"),
                    p.get("respuesta").in(materiales)
            ));
        }

        List<Predicate> condicionesAgua = new ArrayList<>();
        if (agua != null) {
            condicionesAgua.add(cb.and(
                    cb.like(cb.lower(p.get("pregunta")), "%agua%"),
                    cb.equal(cb.lower(p.get("respuesta")), agua.toLowerCase())
            ));
        }



        // Construir la condición final
        Predicate condicionFinal = cb.and(
                materiales == null ? cb.conjunction() : cb.or(condicionesMaterial.toArray(new Predicate[0])),
                agua == null ? cb.conjunction() : cb.or(condicionesAgua.toArray(new Predicate[0]))
        );

        cq.select(cb.count(p)).where(cb.and(predicates.toArray(new Predicate[0])), condicionFinal);
        Long count = em.createQuery(cq).getSingleResult();
        return count > 0;
    }



}