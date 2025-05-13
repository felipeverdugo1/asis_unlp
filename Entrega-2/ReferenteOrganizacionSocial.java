
public class ReferenteOrganizacionSocial extends Usuario{

	public ReferenteOrganizacionSocial(String nombreUsuario, String email, String contraseña) {
		super(nombreUsuario, email, contraseña, RolUsuario.REFORGASOCIAL);
	}
}
