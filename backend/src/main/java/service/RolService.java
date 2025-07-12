package service;


import controller.dto.RolDTO;
import dao.RolDAO;
import exceptions.EntidadExistenteException;
import exceptions.FaltanArgumentosException;
import exceptions.NoPuedesHacerEsoException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Rol;
import model.Usuario;

import java.util.List;
import java.util.Optional;

@RequestScoped
public class RolService extends GenericServiceImpl<Rol, Long> {

    @Inject
    private RolDAO rolDAO;

    @Inject
    private UsuarioService usuarioService;

    @Inject
    public RolService (RolDAO dao) {super(dao);}

    public RolService() {  super(null); }

    public Rol crear(RolDTO dto) {
        if (dto.getNombre() == null){
            throw new FaltanArgumentosException("El campo nombre es obligatorio.");
        }
        Optional<Rol> rol_t = rolDAO.buscarPorCampo("nombre", dto.getNombre());
        if (rol_t.isPresent()) {
            throw new EntidadExistenteException("Ya existe un rol con ese nombre.");
        }
        Rol rol = new Rol();
        rol.setNombre(dto.getNombre());
        rolDAO.crear(rol);
        return rol;
    }

    public Rol actualizar(Long id, RolDTO dto) {
        Optional<Rol> rol_t = rolDAO.buscarPorId(id);
        if (rol_t.isEmpty()) {
            throw new EntidadExistenteException("No existe un rol con ese id.");
        }
        Rol rol = rol_t.get();
        if (rolDAO.existeOtroConMismoCampo(id,"nombre", dto.getNombre())) {
            throw new EntidadExistenteException("Ya existe otra rol con ese nombre");
        }
        rol.setNombre(dto.getNombre());
        rolDAO.actualizar(rol);
        return rol;
    }

    public void eliminar(Long id) {
        Optional<Rol> rol_t = rolDAO.buscarPorId(id);
        if (rol_t.isEmpty()) {
            throw new EntidadExistenteException("No existe un rol con ese id.");
        }
        List<Usuario> usuarios = usuarioService.getUsuariosByRol(id);
        if (usuarios.isEmpty()) {
            rolDAO.eliminar(rol_t.get());
        } else {
            throw new NoPuedesHacerEsoException("Existen usuarios con ese rol asignado.");
        }
    }
}
