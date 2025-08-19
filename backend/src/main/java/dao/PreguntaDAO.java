package dao;

import controller.dto.PreguntaDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
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






    public List<PreguntaDTO> findPreguntasPersonalesByEncuestaId(Long encuestaId) {
        return em.createQuery(
                        "SELECT  new controller.dto.PreguntaDTO(p.id, p.pregunta, p.respuesta,p.personaId,p.encuesta.id) " +
                                "FROM Pregunta p WHERE p.personaId IS NOT NULL  AND p.encuesta.id = :id",
                        PreguntaDTO.class
                )
                .setParameter("id", encuestaId)
                .getResultList();
    }


    public List<PreguntaDTO> findPreguntasViviendaByEncuestaId(Long encuestaId) {
        return em.createQuery(
                        "SELECT new controller.dto.PreguntaDTO(p.id, p.pregunta, p.respuesta, p.personaId, p.encuesta.id) " +
                                "FROM Pregunta p WHERE p.personaId IS NULL AND p.encuesta.id = :id",
                        PreguntaDTO.class
                )
                .setParameter("id", encuestaId)
                .getResultList();
    }

    public long countPersonasDistintasPorEncuesta(Long encuestaId) {
        return em.createQuery(
                        "SELECT COUNT(DISTINCT p.personaId) " +
                                "FROM Pregunta p " +
                                "WHERE p.personaId IS NOT NULL AND p.encuesta.id = :id",
                        Long.class
                )
                .setParameter("id", encuestaId)
                .getSingleResult();
    }



}