
public class OrganizacionSocial {
	
	private String nombre;
	private String domicilio;
	private String actividadPrincipal;
	private Barrio barrio;
	
	
	public OrganizacionSocial(String nombre, String domicilio, String actividadPrincipal, Barrio barrio) {
		super();
		this.nombre = nombre;
		this.domicilio = domicilio;
		this.actividadPrincipal = actividadPrincipal;
		this.barrio = barrio;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getDomicilio() {
		return domicilio;
	}
	
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	
	public String getActividadPrincipal() {
		return actividadPrincipal;
	}
	
	public void setActividadPrincipal(String actividadPrincipal) {
		this.actividadPrincipal = actividadPrincipal;
	}
	
	public Barrio getBarrio() {
		return barrio;
	}
	
	public void setBarrio(Barrio barrio) {
		this.barrio = barrio;
	}
	
}
