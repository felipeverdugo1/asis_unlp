package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import model.FiltroPersonalizado;
import model.Jornada;

import java.util.List;

@RequestScoped
@Transactional
public class FiltroPersonalizadoDAO extends GenericDAOImpl<FiltroPersonalizado, Long> {


    public FiltroPersonalizadoDAO() {super(FiltroPersonalizado.class);}
    // This class extends GenericDAOImpl with Barrio as the entity type and Long as the ID type
    // No additional methods are needed for basic CRUD operations

    public List<FiltroPersonalizado> buscarFiltrosPorUsuarioId(Long usuarioId) {
        String jpql = "SELECT f FROM FiltroPersonalizado f JOIN f.propietario u WHERE u.id = :usuarioId";
        return em.createQuery(jpql, FiltroPersonalizado.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList();
    }
}