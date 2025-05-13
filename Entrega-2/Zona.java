
public class Zona {
	
	private String nombre;
	private Barrio barrio;
	
	
	
	public Zona(String nombre, Barrio barrio) {
		this.nombre = nombre;
		this.barrio = barrio;
	}

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Barrio getBarrio() {
		return barrio;
	}
	
	public void setBarrio(Barrio barrio) {
		this.barrio = barrio;
	}
	
	
}
