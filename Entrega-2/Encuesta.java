
public class Encuesta {

	private String datos;
	private Jornada jornada;
	private Encuestador encuestador;
	private Zona zona;
	
	
	
	public Encuesta(String datos, Jornada jornada, Encuestador encuestador, Zona zona) {
		this.datos = datos;
		this.jornada = jornada;
		this.encuestador = encuestador;
		this.zona = zona;
	}
	
	public String getDatos() {
		return datos;
	}
	
	public void setDatos(String datos) {
		this.datos = datos;
	}
	
	public Jornada getJornada() {
		return jornada;
	}
	
	public void setJornada(Jornada jornada) {
		this.jornada = jornada;
	}
	
	public Encuestador getEncuestador() {
		return encuestador;
	}
	
	public void setEncuestador(Encuestador encuestador) {
		this.encuestador = encuestador;
	}
	
	public Zona getZona() {
		return zona;
	}
	
	public void setZona(Zona zona) {
		this.zona = zona;
	}
	
}
