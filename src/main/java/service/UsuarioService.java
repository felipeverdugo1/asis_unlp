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
        if (!usuarioDTO.getRoles_id().isEmpty()){
            for (Long id: usuarioDTO.getRoles_id()) {
                Optional<Rol> rol = rolDAO.buscarPorId(id);
                if (rol.isPresent()) {
                    if (rol.get().getNombre().contains("referente") && usuarioDTO.getEspecialidad() != null) {
                        usuario.agregarRol(rol.get());
                        usuario.setEspecialidad(usuarioDTO.getEspecialidad());
                    } else {
                        throw new FaltanArgumentosException("Si se asigna rol referente debe tener el campo especialidad.");
                    }
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

    public Usuario quitarRol(Long id, Rol rol) {
        Optional<Usuario> usuario_t = usuarioDAO.buscarPorId(id);
        if (usuario_t.isPresent()) {
            Usuario usuario = usuario_t.get();
            usuario.quitarRol(rol);
            usuarioDAO.actualizar(usuario);
            return usuario;
        } else {
            throw new EntidadNoEncontradaException("No existe el usuario.");
        }
    }

    public Usuario agregarRol(Long id, Rol rol) {
        Optional<Usuario> usuario_t = usuarioDAO.buscarPorId(id);
        if (usuario_t.isPresent()) {
            Usuario usuario = usuario_t.get();
            usuario.agregarRol(rol);
            usuarioDAO.actualizar(usuario);
            return usuario;
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

}