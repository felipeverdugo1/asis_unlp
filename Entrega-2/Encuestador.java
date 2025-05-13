
public class Encuestador {

	private String nombre;
	private String dni;
	private int edad;
	private String genero;
	private String ocupacion;
	
	
	public Encuestador(String nombre, String dni, int edad, String genero, String ocupacion) {
		this.nombre = nombre;
		this.dni = dni;
		this.edad = edad;
		this.genero = genero;
		this.ocupacion = ocupacion;
	}

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getDni() {
		return dni;
	}
	
	public void setDni(String dni) {
		this.dni = dni;
	}
	
	public int getEdad() {
		return edad;
	}
	
	public void setEdad(int edad) {
		this.edad = edad;
	}
	
	public String getGenero() {
		return genero;
	}
	
	public void setGenero(String genero) {
		this.genero = genero;
	}
	
	public String getOcupacion() {
		return ocupacion;
	}
	
	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}
	
	
	
}
