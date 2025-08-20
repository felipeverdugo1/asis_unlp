package dao;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import model.EstadoPedido;
import model.Jornada;
import model.PedidoReporte;
import java.util.List;
import java.util.Optional;


@RequestScoped
@Transactional
public class PedidoReporteDAO extends GenericDAOImpl<PedidoReporte, Long> {

    public PedidoReporteDAO() {
        super(PedidoReporte.class);
    }

    public List<PedidoReporte> listarPedidosPorEstado(String estado) {
        String jpql = "SELECT e FROM " + PedidoReporte.class.getSimpleName() + " e WHERE e.estado = :valor";
        EstadoPedido estadoPedido = EstadoPedido.fromString(estado);
        return em.createQuery(jpql, PedidoReporte.class)
                .setParameter("valor", estadoPedido)
                .getResultList();
    }

    public List<PedidoReporte> listarPedidosPorCreador(Long usuario_id) {
        try {
            String jpql = "SELECT e FROM " + PedidoReporte.class.getSimpleName() + " e WHERE e.creadoPor.id" + " = :valor";
            List<PedidoReporte> resultado = em.createQuery(jpql, PedidoReporte.class)
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

    public List<PedidoReporte> listarPedidosTomadosPor(Long usuario_id) {
        try {
            String jpql = "SELECT e FROM " + PedidoReporte.class.getSimpleName() + " e WHERE e.asignado_a.id" + " = :valor";
            List<PedidoReporte> resultado = em.createQuery(jpql, PedidoReporte.class)
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

    public Optional<PedidoReporte> buscarPorReporte_Id(Long reporte_id) {
        try {
            String jpql = "SELECT e FROM " + PedidoReporte.class.getSimpleName() + " e WHERE e.reporte.id" + " = :valor";
            List<PedidoReporte> resultados = em.createQuery(jpql, PedidoReporte.class)
                    .setParameter("valor", reporte_id)
                    .getResultList();
            return resultados.stream().findFirst();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}