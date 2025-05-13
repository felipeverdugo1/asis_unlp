import java.util.Date;
import java.util.List;

public class Jornada {
	
	private Date fecha;
	private Campaña campaña;
	private List<Encuesta> encuestas;
	private List<Zona> zona;
	
	public Jornada(Date fecha, Campaña campaña, List<Encuesta> encuestas, List<Zona> zona) {
		this.fecha = fecha;
		this.campaña = campaña;
		this.encuestas = encuestas;
		this.zona = zona;
	}
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Campaña getCampaña() {
		return campaña;
	}
	
	public void setCampaña(Campaña campaña) {
		this.campaña = campaña;
	}
	
	public List<Encuesta> getEncuestas() {
		return encuestas;
	}
	
	public void setEncuestas(List<Encuesta> encuestas) {
		this.encuestas = encuestas;
	}
	
	public List<Zona> getZona() {
		return zona;
	}
	
	public void setZona(List<Zona> zona) {
		this.zona = zona;
	}
	
	
	
}
