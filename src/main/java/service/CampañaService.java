package service;

import controller.dto.CampañaDTO;
import dao.BarrioDAO;
import dao.CampañaDAO;
import exceptions.EntidadExistenteException;
import exceptions.EntidadNoEncontradaException;
import exceptions.FaltanArgumentosException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Campaña;

import java.util.Optional;

@RequestScoped
public class CampañaService extends GenericServiceImpl<Campaña, Long> {

    @Inject
    private CampañaDAO campañaDAO;

    @Inject
    private BarrioDAO barrioDAO;

    @Inject
    public CampañaService(CampañaDAO campañaDAO) {
        super(campañaDAO);
    }

    public CampañaService() {
        super(null);
    }

    public Campaña crear(CampañaDTO dto) {
        Optional<Campaña> campañaExistente = campañaDAO.buscarPorCampo("nombre", dto.getNombre());
        if (campañaExistente.isPresent()) {
            throw new EntidadExistenteException("Ya existe una campaña con ese nombre");
        }

        if (dto.getBarrio_id() == null) {
            throw new FaltanArgumentosException("El barrio_id es obligatorio");
        }

        Campaña nuevaCampaña = new Campaña();

        barrioDAO.buscarPorId(dto.getBarrio_id()).ifPresentOrElse(
                nuevaCampaña::setBarrio,
                () -> { throw new EntidadNoEncontradaException("El barrio no existe"); }
        );

        nuevaCampaña.setNombre(dto.getNombre());
        nuevaCampaña.setFechaInicio(dto.getFechaInicio());
        nuevaCampaña.setFechaFin(dto.getFechaFin());

        campañaDAO.crear(nuevaCampaña);
        return nuevaCampaña;
    }

    public Campaña actualizar(Long id, CampañaDTO dto) {
        Optional<Campaña> campañaOpt = campañaDAO.buscarPorId(id);
        if (campañaOpt.isEmpty()) {
            throw new EntidadNoEncontradaException("La campaña no existe");
        }

        Campaña campaña = campañaOpt.get();

        if (dto.getNombre() != null) {
            campaña.setNombre(dto.getNombre());
        }

        if (dto.getFechaInicio() != null) {
            campaña.setFechaInicio(dto.getFechaInicio());
        }

        if (dto.getFechaFin() != null) {
            campaña.setFechaFin(dto.getFechaFin());
        }

        if (dto.getBarrio_id() != null) {
            barrioDAO.buscarPorId(dto.getBarrio_id()).ifPresentOrElse(
                    campaña::setBarrio,
                    () -> { throw new EntidadNoEncontradaException("El barrio no existe"); }
            );
        }

        campañaDAO.actualizar(campaña);
        return campaña;
    }

    public void eliminar(Long id) {
        campañaDAO.buscarPorId(id).ifPresentOrElse(
                campañaDAO::eliminar,
                () -> { throw new EntidadNoEncontradaException("La campaña no existe"); }
        );
    }
}