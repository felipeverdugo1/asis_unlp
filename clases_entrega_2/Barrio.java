import java.util.List;

public class Barrio {
	
	private String nombre;
	private String geolocalizacion;
	private String infoDeInteres;
	private List<Zona> zonas;
	
	
	public Barrio(String nombre, String geolocalizacion, String infoDeInteres, List<Zona> zonas) {
		this.nombre = nombre;
		this.geolocalizacion = geolocalizacion;
		this.infoDeInteres = infoDeInteres;
		this.zonas = zonas;
	}

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getGeolocalizacion() {
		return geolocalizacion;
	}
	
	public void setGeolocalizacion(String geolocalizacion) {
		this.geolocalizacion = geolocalizacion;
	}
	
	public String getInfoDeInteres() {
		return infoDeInteres;
	}
	
	public void setInfoDeInteres(String infoDeInteres) {
		this.infoDeInteres = infoDeInteres;
	}

	public List<Zona> getZonas() {
		return zonas;
	}

	public void setZonas(List<Zona> zonas) {
		this.zonas = zonas;
	}
	
	
}
