package service;

import controller.dto.FiltroDTO;
import dao.*;
import exceptions.EntidadExistenteException;
import exceptions.EntidadNoEncontradaException;
import exceptions.FaltanArgumentosException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.FiltroPersonalizado;

import java.util.Optional;

@RequestScoped
public class FiltroPersonalizadoService extends GenericServiceImpl<FiltroPersonalizado, Long> {

    @Inject
    private FiltroPersonalizadoDAO filtroPersonalizadoDAO;


    @Inject
    private UsuarioDAO usuarioDAO;

    @Inject
    public FiltroPersonalizadoService(FiltroPersonalizadoDAO filtroPersonalizadoDAO) {
        super(filtroPersonalizadoDAO);
    }

    public FiltroPersonalizadoService() {
        super(null);
    }

    public FiltroPersonalizado crear(FiltroDTO dto) {
        Optional<FiltroPersonalizado> filtroExistente = filtroPersonalizadoDAO.buscarPorCampo("nombre", dto.getNombre());
        if (filtroExistente.isPresent()) {
            throw new EntidadExistenteException("Ya existe un filtro con ese tipo");
        }

        if (dto.getNombre() == null || dto.getCriterios() == null) {
            throw new FaltanArgumentosException("El tipo y criterio son obligatorios");
        }

        FiltroPersonalizado nuevoFiltro = new FiltroPersonalizado();


        usuarioDAO.buscarPorId(dto.getPropietario_id()).ifPresentOrElse(
                nuevoFiltro::setPropietario,
                () -> { throw new EntidadNoEncontradaException("El referente no existe"); }
        );

        nuevoFiltro.setNombre(dto.getNombre());
        nuevoFiltro.setCriterios(dto.getCriterios());

        filtroPersonalizadoDAO.crear(nuevoFiltro);
        return nuevoFiltro;
    }

    public FiltroPersonalizado actualizar(Long id, FiltroDTO dto) {
        Optional<FiltroPersonalizado> filtroPersonalizadoOptional = filtroPersonalizadoDAO.buscarPorId(id);
        if (filtroPersonalizadoOptional.isEmpty()) {
            throw new EntidadNoEncontradaException("El filtro no existe");
        }

        FiltroPersonalizado filtroPersonalizado = filtroPersonalizadoOptional.get();

        if (dto.getNombre() != null) {
            filtroPersonalizado.setNombre(dto.getNombre());
        }

        if (dto.getCriterios() != null) {
            filtroPersonalizado.setCriterios(dto.getCriterios());
        }

        if (dto.getPropietario_id() != null) {
            usuarioDAO.buscarPorId(dto.getPropietario_id()).ifPresentOrElse(
                    filtroPersonalizado::setPropietario,
                    () -> { throw new EntidadNoEncontradaException("El propietario no existe"); }
            );
        }

        filtroPersonalizadoDAO.actualizar(filtroPersonalizado);
        return filtroPersonalizado;
    }

    public void eliminar(Long id) {
        filtroPersonalizadoDAO.buscarPorId(id).ifPresentOrElse(
                filtroPersonalizadoDAO::eliminar,
                () -> { throw new EntidadNoEncontradaException("El filtro no existe"); }
        );
    }
}