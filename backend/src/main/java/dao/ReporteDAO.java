package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import model.PedidoReporte;
import model.Reporte;

import java.util.List;


@RequestScoped
@Transactional
public class ReporteDAO extends GenericDAOImpl<Reporte, Long> {


    public ReporteDAO() {super(Reporte.class);}
    // This class extends GenericDAOImpl with Barrio as the entity type and Long as the ID type
    // No additional methods are needed for basic CRUD operations

    public List<Reporte> listarPedidosPorCreador(Long usuario_id) {
        try {
            String jpql = "SELECT e FROM " + Reporte.class.getSimpleName() + " e WHERE e.creadoPor.id" + " = :valor";
            List<Reporte> resultado = em.createQuery(jpql, Reporte.class)
                    .setParameter("valor", usuario_id)
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