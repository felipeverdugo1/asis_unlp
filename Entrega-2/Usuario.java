

public abstract class Usuario {

	private String nombreUsuario;
	private String email;
	private String contraseña;
	private boolean habilitado;
	private RolUsuario rol;
	
	
	
	public Usuario(String nombreUsuario, String email, String contraseña, RolUsuario rol){
		this.setNombreUsuario(nombreUsuario);
		this.setEmail(email);
		this.setContraseña(contraseña);
		this.setRol(rol);
		this.setHabilitado(true);
	}
	
	public boolean hasRole(RolUsuario role) {
		return rol.equals(role);
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public boolean isHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}

	public RolUsuario getRol() {
		return rol;
	}

	public void setRol(RolUsuario rol) {
		this.rol = rol;
	}
}
