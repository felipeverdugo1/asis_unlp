
public class Visitante extends Usuario {

	
	public Visitante(String nombreUsuario, String email, String contraseña) {
		super(nombreUsuario, email, contraseña, RolUsuario.VISITANTE);
	}
}
