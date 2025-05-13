
public class PersonalDeSalud extends Usuario{

	public PersonalDeSalud(String nombreUsuario, String email, String contraseña) {
		super(nombreUsuario, email, contraseña, RolUsuario.PERSONALDESALUD);
	}
}
