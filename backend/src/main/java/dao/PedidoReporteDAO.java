package dao;

import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import model.PedidoReporte;


@RequestScoped
@Transactional
public class PedidoReporteDAO extends GenericDAOImpl<PedidoReporte, Long> {

    public PedidoReporteDAO() {
        super(PedidoReporte.class);
    }

}