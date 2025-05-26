import java.util.List;


public class Administrador extends Usuario {
	
	public Administrador(String nombreUsuario, String email, String contraseña) {
		super(nombreUsuario, email, contraseña, RolUsuario.ADMIN);
	}
}
