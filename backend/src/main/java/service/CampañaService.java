package service;

import controller.dto.CampañaDTO;
import dao.BarrioDAO;
import dao.CampañaDAO;
import exceptions.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Barrio;
import model.Campaña;
import model.Jornada;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

        if (dto.getNombre() != null) {
            if (!dto.getNombre().equals(campaña.getNombre())) {
                if (campañaDAO.existeOtroConMismoCampo(id,"nombre", dto.getNombre())) {
                    throw new EntidadExistenteException("Ya existe otra campaña con ese nombre");
                }
                campaña.setNombre(dto.getNombre());
            }
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

            if (dto.getBarrio_id() != null) {
                if (!dto.getBarrio_id().equals(campaña.getBarrio().getId())) {
                    barrioDAO.buscarPorId(dto.getBarrio_id()).ifPresentOrElse(
                            campaña::setBarrio,
                            () -> { throw new EntidadNoEncontradaException("El barrio no existe"); }
                    );
                }
            }

        campañaDAO.actualizar(campaña);

        return campaña;
    }

    public void eliminar(Long id) {
        campañaDAO.buscarPorId(id).ifPresentOrElse(
                campañaExistente -> {
                    if (!campañaExistente.getJornadas().isEmpty()) {
                        throw new NoPuedesHacerEsoException("La Campaña tiene Jornadas asignadas, no se puede eliminar.");
                    }
                    if (!campañaExistente.getReportes().isEmpty()) {
                        throw new NoPuedesHacerEsoException("La Campaña tiene Reportes relacionados, no se puede eliminar.");
                    }
                    campañaDAO.eliminar(campañaExistente);
                },
                () -> { throw new EntidadNoEncontradaException("La campaña no existe"); }
        );
    }

    public List<Campaña> listarCampaniasByBarrio(Long barrio_id){
        Optional<Barrio> barrio_t = barrioDAO.buscarPorId(barrio_id);
        if (barrio_t.isEmpty()){
            throw new EntidadNoEncontradaException("No existe una campaña con ese id");
        }
        List<Campaña> response = campañaDAO.listarCampaniasByBarrio(barrio_id);
        return response;
    }

}