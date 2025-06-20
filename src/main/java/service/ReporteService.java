package service;

import controller.dto.CampañaDTO;
import controller.dto.ReporteDTO;
import controller.dto.ZonaDTO;
import dao.CampañaDAO;
import dao.ReporteDAO;
import dao.UsuarioDAO;
import exceptions.EntidadExistenteException;
import exceptions.EntidadNoEncontradaException;
import exceptions.FaltanArgumentosException;
import exceptions.RangoDeFechasInvalidoException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.*;

import java.time.LocalDate;
import java.util.Optional;

@RequestScoped
public class ReporteService extends GenericServiceImpl<Reporte, Long> {

    @Inject
    private ReporteDAO reporteDAO;

    @Inject
    private CampañaDAO campañaDAO;

    @Inject
    private UsuarioDAO usuarioDAO;


    @Inject
    public ReporteService(ReporteDAO reporteDAO) {super(reporteDAO);
    }

    public ReporteService() {
        super(null);
    }

    public Reporte crear(ReporteDTO dto) {

        if (dto.getNombreUnico() == null || dto.getFechaCreacion() == null || dto.getDescripcion() == null) {
            throw new FaltanArgumentosException("El nombre es obligatorio , la fecha de creacion es obligatorio y descripcion");
        }
        if (dto.getFechaCreacion().isAfter(LocalDate.now())) {
            throw new RangoDeFechasInvalidoException("La fecha de inicio no puede ser posterior a la fecha de hoy");
        }

        Optional<Reporte> reporte = reporteDAO.buscarPorCampo("nombreUnico", dto.getNombreUnico());
        // si es duplicado
        if ( reporte.isPresent() ) {
            throw new EntidadExistenteException("Ya existe un reporte con ese nombre");
        }


        Reporte reporte_nuevo = new Reporte();
        // si existe el campaña o usuario
        if (dto.getCampaña_id() != null || dto.getCreadoPor_id() != null) {
            Optional<Campaña> campaña = campañaDAO.buscarPorId(dto.getCampaña_id());
            Optional<Usuario> usuario = usuarioDAO.buscarPorId(dto.getCreadoPor_id());
            if (!campaña.isPresent() ) {
                throw new EntidadNoEncontradaException("El Campaña no existe");
            } else {
                if (!usuario.isPresent()) {
                    throw new EntidadNoEncontradaException("El usuario no existe");
                }
                Campaña campaña_t = campaña.get();
                Usuario usuario_t = usuario.get();
                reporte_nuevo.setFechaCreacion(dto.getFechaCreacion());
                reporte_nuevo.setNombreUnico(dto.getNombreUnico());
                reporte_nuevo.setCreadoPor(usuario_t);
                    reporte_nuevo.setDescripcion(dto.getDescripcion());
                reporte_nuevo.setCampaña(campaña_t);
                reporteDAO.crear(reporte_nuevo);
            }
        } else {
            throw new FaltanArgumentosException("El reporte_id es obligatorio");
        }
        return reporte_nuevo;
    }


    public Reporte actualizar(Long id, ReporteDTO dto) {
        Optional<Reporte> reporte = reporteDAO.buscarPorId(id);
        if ( reporte.isPresent() ) {
            Reporte reporte_actualizado = reporte.get();

            if (reporteDAO.existeOtroConMismoCampo(id,"nombreUnico", dto.getNombreUnico())) {
                throw new EntidadExistenteException("Ya existe otro reporte con ese nombre");
            }

            if (dto.getNombreUnico() != null) {
                reporte_actualizado.setNombreUnico(dto.getNombreUnico());
            }

            if (dto.getDescripcion() != null) {
                reporte_actualizado.setDescripcion(dto.getDescripcion());
            }


            LocalDate fechaCreacionActualizada = dto.getFechaCreacion() != null ? dto.getFechaCreacion() : reporte_actualizado.getFechaCreacion();

            // Validar consistencia
            if (fechaCreacionActualizada.isAfter(LocalDate.now())) {
                throw new RangoDeFechasInvalidoException("La fecha de inicio no puede ser posterior a la fecha de hoy");
            }

            if (dto.getFechaCreacion() != null) {
                reporte_actualizado.setFechaCreacion(dto.getFechaCreacion());
            }

            if (dto.getCreadoPor_id() != null) {
                if (!reporte_actualizado.getCreadoPor().getId().equals(dto.getCreadoPor_id())) {
                    Optional<Usuario> usuario = usuarioDAO.buscarPorId(dto.getCreadoPor_id());
                    if (usuario.isPresent()) {
                        Usuario usuario_r = usuario.get();
                        reporte_actualizado.setCreadoPor(usuario_r);
                    } else {
                        throw new EntidadNoEncontradaException("No existe el usuario " + dto.getCreadoPor_id());
                    }
                }
            }

                if (dto.getCampaña_id() != null) {
                    if (!reporte_actualizado.getCampaña().getId().equals(dto.getCampaña_id())) {
                        Optional<Campaña> campaña = campañaDAO.buscarPorId(dto.getCampaña_id());
                        if (campaña.isPresent()) {
                            reporte_actualizado.setCampaña( campaña.get());
                        } else {
                            throw new EntidadNoEncontradaException("No existe la campaña " + dto.getCampaña_id());
                        }
                    }

            }

            reporteDAO.actualizar(reporte_actualizado);
            return reporte_actualizado;
        } else {
            throw new EntidadNoEncontradaException("El reporte no existe");
        }
    }

    public void eliminar(Long id) {
        Optional<Reporte> reporte = reporteDAO.buscarPorId(id);
        if ( reporte.isPresent() ) {
            //Barrio barrio = zona.get().getBarrio();
            //barrio.getZonas().remove(zona.get());
            //barrioDAO.actualizar(barrio);
            reporteDAO.eliminar(reporte.get());
        } else {
            throw new EntidadNoEncontradaException("El reporte no existe");
        }
    }

    // Additional business logic methods can be added here if needed
}