package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import model.Encuesta;
import model.Pregunta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequestScoped
@Transactional
public class PreguntaDAO extends GenericDAOImpl<Pregunta, Long> {


    public PreguntaDAO() {super(Pregunta.class);}
    // This class extends GenericDAOImpl with Barrio as the entity type and Long as the ID type
    // No additional methods are needed for basic CRUD operations



    public List<Pregunta> findByEncuestaAndEsPersonal(Encuesta encuesta, boolean esPersonal) {
        String jpql = "SELECT p FROM Pregunta p WHERE p.encuesta = :encuesta AND p.esPersonal = :esPersonal";
        return em.createQuery(jpql, Pregunta.class)
                .setParameter("encuesta", encuesta)
                .setParameter("esPersonal", esPersonal)
                .getResultList();
    }





    public List<Pregunta> findByEncuesta(Long encuestaId, Boolean esPersonal) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Pregunta> cq = cb.createQuery(Pregunta.class);
        Root<Pregunta> p = cq.from(Pregunta.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(p.get("encuesta").get("id"), encuestaId));

        if (esPersonal != null) {
            predicates.add(cb.equal(p.get("esPersonal"), esPersonal));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(cq).getResultList();
    }




    public List<Pregunta> findByEncuestaAndPalabrasClave(Long encuestaId, List<String> palabras, boolean esPersonal) {
        String jpql = """
        SELECT p FROM Pregunta p
        WHERE p.encuesta.id = :encuestaId
        AND p.esPersonal = :esPersonal
        AND (
    """ +
                palabras.stream()
                        .map(p -> "LOWER(p.pregunta) LIKE LOWER(CONCAT('%', :palabra" + palabras.indexOf(p) + ", '%'))")
                        .collect(Collectors.joining(" OR ")) + ")";

        TypedQuery<Pregunta> query = em.createQuery(jpql, Pregunta.class)
                .setParameter("encuestaId", encuestaId)
                .setParameter("esPersonal", esPersonal);

        for (int i = 0; i < palabras.size(); i++) {
            query.setParameter("palabra" + i, palabras.get(i));
        }

        return query.getResultList();
    }

}