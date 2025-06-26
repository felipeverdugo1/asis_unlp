package service;

import controller.dto.CampañaDTO;
import dao.BarrioDAO;
import dao.CampañaDAO;
import exceptions.EntidadExistenteException;
import exceptions.EntidadNoEncontradaException;
import exceptions.FaltanArgumentosException;
import exceptions.RangoDeFechasInvalidoException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Campaña;

import java.time.LocalDate;
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

        if (dto.getNombre() == null || dto.getFechaInicio() == null || dto.getFechaFin() == null) {
            throw new FaltanArgumentosException("El nombre es obligatorio , la fecha de inicio es obligatorio y la fecha de fin es obligatorio");
        }
        if (dto.getFechaInicio().isAfter(dto.getFechaFin())) {
            throw new RangoDeFechasInvalidoException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }

        if (dto.getBarrio_id() == null) {
            throw new FaltanArgumentosException("El barrio_id es obligatorio");
        }

        Optional<Campaña> campañaExistente = campañaDAO.buscarPorCampo("nombre", dto.getNombre());
        if (campañaExistente.isPresent()) {
            throw new EntidadExistenteException("Ya existe una campaña con ese nombre");
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

        Campaña campaña;
        Optional<Campaña> campañaOpt = campañaDAO.buscarPorId(id);
        if (campañaOpt.isEmpty()) {
            throw new EntidadNoEncontradaException("La campaña no existe");
        }
            campaña = campañaOpt.get();
            if (campañaDAO.existeOtroConMismoCampo(id,"nombre", dto.getNombre())) {
                throw new EntidadExistenteException("Ya existe otra campaña con ese nombre");
            }



            // Preparar fechas para validación
            LocalDate fechaInicioActualizada = dto.getFechaInicio() != null ? dto.getFechaInicio() : campaña.getFechaInicio();
            LocalDate fechaFinActualizada = dto.getFechaFin() != null ? dto.getFechaFin() : campaña.getFechaFin();

            // Validar consistencia
            if (fechaInicioActualizada.isAfter(fechaFinActualizada)) {
                throw new RangoDeFechasInvalidoException("La fecha de inicio no puede ser posterior a la fecha de fin");
            }

            if (dto.getFechaInicio() != null) {
                campaña.setFechaInicio(dto.getFechaInicio());
            }
            if (dto.getFechaFin() != null) {
                campaña.setFechaFin(dto.getFechaFin());
            }

            if (dto.getNombre() != null) {
                campaña.setNombre(dto.getNombre());
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