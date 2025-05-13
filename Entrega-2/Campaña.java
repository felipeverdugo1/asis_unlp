import java.util.Date;
import java.util.List;

public class Campaña {
	
	private String nombre;
	private Date fechaInicio;
	private Date fechaFin;
	private Barrio barrio;
	private List<Jornada> jornadas;
	
	public Campaña(String nombre, Date fechaInicio, Date fechaFin, Barrio barrio, List<Jornada> jornadas) {
		this.nombre = nombre;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.barrio = barrio;
		this.jornadas = jornadas;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Date getFechaInicio() {
		return fechaInicio;
	}
	
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	
	public Date getFechaFin() {
		return fechaFin;
	}
	
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	public Barrio getBarrio() {
		return barrio;
	}
	
	public void setBarrio(Barrio barrio) {
		this.barrio = barrio;
	}
	
	public List<Jornada> getJornadas() {
		return jornadas;
	}
	
	public void setJornadas(List<Jornada> jornadas) {
		this.jornadas = jornadas;
	}
	
}
