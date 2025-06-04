package tests;

import model.Usuario;
import org.junit.jupiter.api.*;
import service.UsuarioService;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DAOTests {

    private static UsuarioService usuarioService;

    @BeforeAll
    public static void setup() {
        usuarioService = new UsuarioService();
    }

    @Test
    @Order(1)
    public void testCrearUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("TestNombre");
        usuario.setEmail("test@example.com");

        usuarioService.crear(usuario);

        assertNotNull(usuario.getId(), "El ID del usuario no debería ser null luego de persistir.");
    }

    @Test
    @Order(2)
    public void testBuscarUsuario() {
        Usuario usuario = usuarioService.buscarPorCampo("email", "test@example.com"); // ojo: método no está en GenericService, lo tenés que agregar en UsuarioService
        assertNotNull(usuario, "El usuario debería existir.");
        assertEquals("TestNombre", usuario.getNombreUsuario());
    }

    @Test
    @Order(3)
    public void testEliminarUsuario() {
        Usuario usuario = usuarioService.buscarPorCampo("email", "test@example.com");
        assertNotNull(usuario, "Debe existir antes de eliminar.");

        usuarioService.eliminar(usuario.getId());

        Usuario eliminado = usuarioService.buscarPorCampo("email", "test@example.com");
        assertNull(eliminado, "El usuario debería haber sido eliminado.");
    }
}
