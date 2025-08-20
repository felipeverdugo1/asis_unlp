package service;


import controller.dto.EncuestadorDTO;
import controller.dto.PedidoReporteDTO;
import dao.PedidoReporteDAO;
import dao.ReporteDAO;
import dao.UsuarioDAO;
import exceptions.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.*;


import java.util.*;


@RequestScoped
public class PedidoReporteService extends GenericServiceImpl<PedidoReporte, Long> {

    @Inject
    private PedidoReporteDAO pedidoReporteDAO;


    @Inject
    private UsuarioDAO usuarioDAO;
    @Inject
    private ReporteDAO reporteDAO;


    @Inject
    public PedidoReporteService(PedidoReporteDAO dao) {
        super(dao);
    }


    public PedidoReporteService() {super(null);}
    // Métodos específicos de Usuario si los necesitás




    public PedidoReporte crear(PedidoReporteDTO pedidoReporteDTO) {
        // Validación de campos requeridos primero
        if (pedidoReporteDTO.validarCrearNull()) {
            throw new FaltanArgumentosException("Se requieren los campos nombre,campos,estados , creado por ");
        }

        Optional<PedidoReporte> pedidoReporteOptional = pedidoReporteDAO.buscarPorCampo("nombre", pedidoReporteDTO.getNombre());
        // Validación de duplicado
        if (pedidoReporteOptional.isPresent()) {
            throw new EntidadExistenteException("Ya existe un pedidoreporte con ese nombre");
        }
        Optional<Usuario> usuario = usuarioDAO.buscarPorId(pedidoReporteDTO.getCreadoPor_id());
            if (usuario.isEmpty()) {
                throw new EntidadNoEncontradaException("El usuario no existe");
            }

            PedidoReporte pedidoReporte = new PedidoReporte();

        EstadoPedido estado;
        try {
            estado = EstadoPedido.fromString(pedidoReporteDTO.getEstado());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + pedidoReporteDTO.getEstado()+ " los estados son PENDIENTE TOMADO COMPLETADO");
        }

        pedidoReporte.setEstado(estado);

        if (!EnumSet.of(EstadoPedido.PENDIENTE).contains(pedidoReporte.getEstado())) {
            throw new IllegalStateException("Solo se pueden crear pedidos en estado 'PENDIENTE'.");
        }

        pedidoReporte.setNombre(pedidoReporteDTO.getNombre());
        pedidoReporte.setCamposPedidos(pedidoReporteDTO.getCamposPedidos());
        pedidoReporte.setCreadoPor(usuario.get());
        pedidoReporte.setComentario(pedidoReporteDTO.getComentario());
        pedidoReporteDAO.crear(pedidoReporte);
        return pedidoReporte;
    }


    public PedidoReporte actualizar(Long id, PedidoReporteDTO dto) {
        Optional<PedidoReporte> pedidoReporte = pedidoReporteDAO.buscarPorId(id);

        if (pedidoReporte.isPresent()) {
            PedidoReporte pedidoReporte_actualizado = pedidoReporte.get();

            if (pedidoReporteDAO.existeOtroConMismoCampo(id, "nombre", dto.getNombre())) {
                throw new EntidadExistenteException("Ya existe otro pedido con ese nombre");
            }

            if (dto.getNombre() != null) {
                pedidoReporte_actualizado.setNombre(dto.getNombre());
            }

            if (dto.getCamposPedidos() != null) {
                pedidoReporte_actualizado.setCamposPedidos(dto.getCamposPedidos());
            }

            if (dto.getComentario() != null) {
                pedidoReporte_actualizado.setComentario(dto.getComentario());
            }

            if (dto.getEstado() != null) {
                EstadoPedido estado;
                try {
                    estado = EstadoPedido.fromString(dto.getEstado());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Estado inválido: " + dto.getEstado());
                }

                pedidoReporte_actualizado.setEstado(estado);


        }


            if (dto.getReporte_id() != null) {
                Long idReporteActual = pedidoReporte_actualizado.getReporte() != null
                        ? pedidoReporte_actualizado.getReporte().getId()
                        : null;

                if (!dto.getReporte_id().equals(idReporteActual)) {
                    Optional<Reporte> reporte = reporteDAO.buscarPorId(dto.getReporte_id());
                    if (reporte.isPresent()) {
                        pedidoReporte_actualizado.setReporte(reporte.get());
                    } else {
                        throw new EntidadNoEncontradaException("No existe el reporte " + dto.getReporte_id());
                    }
                }
            }

            if (dto.getReporte_id() != null) {
                Long idReporteActual = pedidoReporte_actualizado.getReporte() != null
                        ? pedidoReporte_actualizado.getReporte().getId()
                        : null;

                if (!dto.getReporte_id().equals(idReporteActual)) {
                    Optional<Reporte> reporte = reporteDAO.buscarPorId(dto.getReporte_id());
                    if (reporte.isPresent()) {
                        pedidoReporte_actualizado.setReporte(reporte.get());
                    } else {
                        throw new EntidadNoEncontradaException("No existe el reporte " + dto.getReporte_id());
                    }
                }
            }


            if (dto.getAsignado_a_id() != null) {
                Long idUsuarioActual = pedidoReporte_actualizado.getAsignado_a() != null
                        ? pedidoReporte_actualizado.getAsignado_a().getId()
                        : null;

                if (!dto.getAsignado_a_id().equals(idUsuarioActual)) {
                    Optional<Usuario> usuario = usuarioDAO.buscarPorId(dto.getAsignado_a_id());
                    if (usuario.isPresent()) {
                        pedidoReporte_actualizado.setAsignado_a(usuario.get());
                    } else {
                        throw new EntidadNoEncontradaException("No existe el usuario " + dto.getAsignado_a_id());
                    }
                }
            }




            if (dto.getCreadoPor_id() != null) {
                if (!pedidoReporte_actualizado.getCreadoPor().getId().equals(dto.getCreadoPor_id())) {
                    Optional<Usuario> usuario = usuarioDAO.buscarPorId(dto.getCreadoPor_id());
                    if (usuario.isPresent()) {
                        Usuario usuario_r = usuario.get();
                        pedidoReporte_actualizado.setCreadoPor(usuario_r);
                    } else {
                        throw new EntidadNoEncontradaException("No existe el usuario " + dto.getCreadoPor_id());
                    }
                }
            }

            pedidoReporteDAO.actualizar(pedidoReporte_actualizado);
            return pedidoReporte_actualizado;
        } else {
            throw new EntidadNoEncontradaException("El pedido no existe");
        }
    }

    public void eliminar(Long id) {
        Optional<PedidoReporte> pedidoReporte = pedidoReporteDAO.buscarPorId(id);
        if (pedidoReporte.isEmpty()){
            throw new EntidadNoEncontradaException("No existe el pedido");
        }
        pedidoReporteDAO.eliminar(pedidoReporte.get());

    }

    public void tomarPedido(Long pedidoId, Long usuarioId) {
        PedidoReporte pedido = pedidoReporteDAO.buscarPorId(pedidoId)
                .orElseThrow(() -> new EntidadNoEncontradaException("No existe el pedido con ID " + pedidoId));

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden tomar pedidos en estado PENDIENTE.");
        }

        Usuario profesional = usuarioDAO.buscarPorId(usuarioId)
                .orElseThrow(() -> new EntidadNoEncontradaException("No existe el usuario con ID " + usuarioId));

        pedido.setAsignado_a(profesional);
        pedido.setEstado(EstadoPedido.TOMADO);

        pedidoReporteDAO.actualizar(pedido);
    }


    public void soltarPedido(Long pedidoId) {
        PedidoReporte pedido = pedidoReporteDAO.buscarPorId(pedidoId)
                .orElseThrow(() -> new EntidadNoEncontradaException("No existe el pedido con ID " + pedidoId));

        if (pedido.getEstado() != EstadoPedido.TOMADO) {
            throw new IllegalStateException("Solo se pueden soltar pedidos en estado TOMADO.");
        }

        if (pedido.getAsignado_a() == null) {
            throw new IllegalStateException("El pedido no está asignado a ningún profesional.");
        }

        pedido.setAsignado_a(null);
        pedido.setEstado(EstadoPedido.PENDIENTE);

        pedidoReporteDAO.actualizar(pedido);
    }


    public void completarPedido(Long pedidoId, PedidoReporteDTO dto) {
        PedidoReporte pedido = pedidoReporteDAO.buscarPorId(pedidoId)
                .orElseThrow(() -> new EntidadNoEncontradaException("No existe el pedido con ID " + pedidoId));

        if (pedido.getEstado() != EstadoPedido.TOMADO) {
            throw new IllegalStateException("Solo se pueden completar pedidos en estado TOMADO.");
        }

        if (dto.getReporte_id() == null) {
            throw new IllegalArgumentException("Debe enviar el ID del reporte y un comentario.");
        }

        if (pedido.getAsignado_a() == null || !pedido.getAsignado_a().getId().equals(dto.getAsignado_a_id())) {
            throw new IllegalStateException("Solo el profesional asignado puede completar el pedido.");
        }

        Reporte reporte = reporteDAO.buscarPorId(dto.getReporte_id())
                .orElseThrow(() -> new EntidadNoEncontradaException("No existe el reporte con ID " + dto.getReporte_id()));

        pedido.setReporte(reporte);
        if (dto.getComentario() != null &&  !(dto.getComentario().trim().isEmpty()) && !dto.getComentario().isBlank()) {
            pedido.setComentario(dto.getComentario());
        }
        pedido.setEstado(EstadoPedido.COMPLETADO);


        Usuario referente = pedido.getCreadoPor();
        if (referente != null) {
            compartirReporteConUsuario(reporte, referente);
        }

        pedidoReporteDAO.actualizar(pedido);
    }


    private void compartirReporteConUsuario(Reporte reporte, Usuario usuario) {
        Set<Usuario> compartidos = reporte.getCompartidoCon();

        if (compartidos == null) {
            compartidos = new HashSet<>();
            reporte.setCompartidoCon(compartidos);
        }

        if (!compartidos.contains(usuario)) {
            compartidos.add(usuario);
            reporteDAO.actualizar(reporte);
        }
    }

    public List<PedidoReporte> buscarPedidosPorEstado(String estado) {
        if (!EstadoPedido.contiene(estado)) {
            throw new IllegalArgumentException("Estado no válido: " + estado);
        }
        return pedidoReporteDAO.listarPedidosPorEstado(estado);
    }

    public List<PedidoReporte> listarPedidosByReferente(Long usuario_id){
        Optional<Usuario> usuario_t = usuarioDAO.buscarPorId(usuario_id);
        // Verificar si el usuario es referente?
        if (usuario_t.isEmpty()){
            throw new EntidadNoEncontradaException("No existe un referente con ese id");
        }
        List<PedidoReporte> response = new ArrayList<>();
        response = pedidoReporteDAO.listarPedidosPorCreador(usuario_id);
        return response;
    }

    public List<PedidoReporte> listarPedidosByAsignado(Long usuario_id){
        Optional<Usuario> usuario_t = usuarioDAO.buscarPorId(usuario_id);
        // Verificar si el usuario es referente?
        if (usuario_t.isEmpty()){
            throw new EntidadNoEncontradaException("No existe un referente con ese id");
        }
        List<PedidoReporte> response = new ArrayList<>();
        response = pedidoReporteDAO.listarPedidosTomadosPor(usuario_id);

        return response;
    }

    public Boolean existePedidoReporte(Long reporte_id) {
        Optional<PedidoReporte> pedido_T = pedidoReporteDAO.buscarPorReporte_Id(reporte_id);
        return pedido_T.isPresent();
    }


}
