
public class ReferenteOrganizacionSocial extends Usuario{
	
	private OrganizacionSocial organizacionSocial;
	
	public ReferenteOrganizacionSocial(String nombreUsuario, String email, String contraseña, OrganizacionSocial organizacionSocial) {
		super(nombreUsuario, email, contraseña, RolUsuario.REFORGASOCIAL);
		this.organizacionSocial = organizacionSocial;
	}

	public OrganizacionSocial getOrganizacionSocial() {
		return organizacionSocial;
	}

	public void setOrganizacionSocial(OrganizacionSocial organizacionSocial) {
		this.organizacionSocial = organizacionSocial;
	}
		
}
