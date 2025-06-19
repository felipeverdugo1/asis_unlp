package service;

import controller.dto.UsuarioDTO;
import dao.RolDAO;
import dao.UsuarioDAO;
import exceptions.EntidadExistenteException;
import exceptions.EntidadNoEncontradaException;
import exceptions.FaltanArgumentosException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Rol;
import model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequestScoped
public class UsuarioService extends GenericServiceImpl<Usuario, Long> {

    @Inject
    private UsuarioDAO usuarioDAO;
    @Inject
    private RolDAO rolDAO;

    @Inject
    public UsuarioService(UsuarioDAO usuarioDAO) {
        super(usuarioDAO); // Ahora sí se inyecta correctamente
    }

    // Add no-arg constructor
    public UsuarioService() {
        super(null); // CDI will use the @Inject constructor at runtime
    }

    public Usuario crear(UsuarioDTO usuarioDTO) {
        Optional<Usuario> usuario_t = usuarioDAO.buscarPorCampo("email", usuarioDTO.getEmail());
        if (usuario_t.isPresent()) {
            throw new EntidadExistenteException("Ya existe un usuario con ese email");
        }
        Usuario usuario = new Usuario();
        if (usuarioDTO.getEspecialidad() != null) {
            usuario.setEspecialidad(usuarioDTO.getEspecialidad());
        }
        if (!usuarioDTO.getRoles_id().isEmpty()){
            for (Long id: usuarioDTO.getRoles_id()) {
                Optional<Rol> rol = rolDAO.buscarPorId(id);
                if (rol.isPresent()) {
                    usuario.agregarRol(rol.get());
                } else {
                    throw new EntidadNoEncontradaException("El rol no existe con id: " + id.toString());
                }
            }
            usuario.setEmail(usuarioDTO.getEmail());
            usuario.setPassword(usuarioDTO.getPassword());
            usuario.setNombreUsuario(usuarioDTO.getNombreUsuario());
            usuario.setHabilitado(usuarioDTO.getHabilitado());
            usuarioDAO.crear(usuario);
            return usuario;
        } else {
            throw new FaltanArgumentosException("Se deben especificar al menos un rol.");
        }
    }

    public Usuario habilitacionUsuario(Long id, Boolean bool) {
        Optional<Usuario> usuario_t = usuarioDAO.buscarPorId(id);
        if (usuario_t.isPresent()) {
            Usuario usuario = usuario_t.get();
            usuario.setHabilitado(bool);
            usuarioDAO.actualizar(usuario);
            return usuario;
        } else {
            throw new EntidadNoEncontradaException("No existe el usuario");
        }
    }

    public Usuario quitarRol(Long id, Long rol_id) {
        Optional<Usuario> usuario_t = usuarioDAO.buscarPorId(id);
        if (usuario_t.isPresent()) {
            Optional<Rol> rol_t = rolDAO.buscarPorId(rol_id);
            if (rol_t.isPresent()) {
                Usuario usuario = usuario_t.get();
                Rol rol = rol_t.get();
                usuario.quitarRol(rol.getId());
                usuarioDAO.actualizar(usuario);
                return usuario;
            } else {
                throw new EntidadNoEncontradaException("No existe el rol");
            }
        } else {
            throw new EntidadNoEncontradaException("No existe el usuario.");
        }
    }

    public Usuario agregarRol(Long id, Long rol_id) {
        Optional<Usuario> usuario_t = usuarioDAO.buscarPorId(id);
        if (usuario_t.isPresent()) {
            Optional<Rol> rol_t = rolDAO.buscarPorId(rol_id);
            if (rol_t.isPresent()){
                Usuario usuario = usuario_t.get();
                Rol rol = rol_t.get();
                usuario.agregarRol(rol);
                usuarioDAO.actualizar(usuario);
                return usuario;
            } else {
                throw new EntidadNoEncontradaException("No existe el rol.");
            }
        } else {
            throw new EntidadNoEncontradaException("No existe el usuario.");
        }
    }

    public void eliminar(Long id) {
        Optional<Usuario> usuario_t = usuarioDAO.buscarPorId(id);
        if (usuario_t.isPresent()) {
            Usuario usuario = usuario_t.get();
            usuario.getRoles().clear();
            usuarioDAO.actualizar(usuario);
            usuarioDAO.eliminar(usuario);
        } else {
            throw new EntidadNoEncontradaException("No existe el usuario.");
        }
    }

    public Usuario actualizar(Long id, UsuarioDTO dto) {
        Optional<Usuario> usuario_t = usuarioDAO.buscarPorId(id);
        if (usuario_t.isPresent()) {
            Usuario usuario = usuario_t.get();
            if (dto.getEmail() != null) {
                Optional<Usuario> usuario_existente = usuarioDAO.buscarPorCampo("email", dto.getEmail());
                if (usuario_existente.isPresent()) {
                    throw new EntidadNoEncontradaException("Ya existe un usuario con ese email.");
                }
                usuario.setEmail(dto.getEmail());
            }
            if (dto.getPassword() != null) {
                usuario.setPassword(dto.getPassword());
            }
            if (dto.getNombreUsuario() != null) {
                Optional<Usuario> usuario_existente_2 = usuarioDAO.buscarPorCampo("nombreUsuario", dto.getNombreUsuario());
                if (usuario_existente_2.isPresent()) {
                    throw new EntidadNoEncontradaException("Ya existe un usuario con ese username.");
                }
                usuario.setNombreUsuario(dto.getNombreUsuario());
            }
            if (dto.getHabilitado() != null) {
                usuario.setHabilitado(dto.getHabilitado());
            }
            if (dto.getEspecialidad() != null) {
                usuario.setEspecialidad(dto.getEspecialidad());
            }
            usuarioDAO.actualizar(usuario);
            return usuario;
        } else {
            throw new EntidadNoEncontradaException("No existe el usuario.");
        }
    }

    public List<Usuario> getUsuariosByRol(Long id){
        Optional<Rol> rol_t = rolDAO.buscarPorId(id);
        if (rol_t.isPresent()) {
            return usuarioDAO.getAllUsuariosByRol(rol_t.get());
        } else {
            throw new EntidadNoEncontradaException("No existe el rol.");
        }
    }

}