import java.io.File;
import java.util.Date;
import java.util.List;

public class Reporte {

	private Date fecha;
	private String descripcion;
	private File archivo;
	private Usuario generadoPor;
	private List<Usuario> compartidoCon;
	private FiltroPersonalizado filtroAplicado;
	
	
	public Reporte(Date fecha, String descripcion, File archivo, Usuario generadoPor, List<Usuario> compartidoCon, FiltroPersonalizado filtroAplicado) {
		this.fecha = fecha;
		this.descripcion = descripcion;
		this.archivo = archivo;
		this.generadoPor = generadoPor;
		this.compartidoCon = compartidoCon;
		this.filtroAplicado = filtroAplicado;
	}
	
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public File getArchivo() {
		return archivo;
	}
	
	public void setArchivo(File archivo) {
		this.archivo = archivo;
	}
	
	public Usuario getGeneradoPor() {
		return generadoPor;
	}
	
	public void setGeneradoPor(Usuario generadoPor) {
		this.generadoPor = generadoPor;
	}
	
	public List<Usuario> getCompartidoCon() {
		return compartidoCon;
	}
	
	public void setCompartidoCon(List<Usuario> compartidoCon) {
		this.compartidoCon = compartidoCon;
	}


	public FiltroPersonalizado getFiltroAplicado() {
		return filtroAplicado;
	}


	public void setFiltroAplicado(FiltroPersonalizado filtroAplicado) {
		this.filtroAplicado = filtroAplicado;
	}
	
	
	
}
