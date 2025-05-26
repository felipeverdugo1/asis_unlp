
public class FiltroPersonalizado {

	private String nombre;
	private String criterios;
	private PersonalDeSalud propietario;
	
	
	
	public FiltroPersonalizado(String nombre, String criterios, PersonalDeSalud propietario) {
		this.nombre = nombre;
		this.criterios = criterios;
		this.propietario = propietario;
	}

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getCriterios() {
		return criterios;
	}
	
	public void setCriterios(String criterios) {
		this.criterios = criterios;
	}
	
	public PersonalDeSalud getPropietario() {
		return propietario;
	}
	
	public void setPropietario(PersonalDeSalud propietario) {
		this.propietario = propietario;
	}
	
	
}
