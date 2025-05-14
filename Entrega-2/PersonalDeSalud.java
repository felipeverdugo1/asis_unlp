import java.util.ArrayList;
import java.util.List;

public class PersonalDeSalud extends Usuario{
	
	private List<FiltroPersonalizado> filtros;
	
	public PersonalDeSalud(String nombreUsuario, String email, String contraseña) {
		super(nombreUsuario, email, contraseña, RolUsuario.PERSONALDESALUD);
		this.filtros = new ArrayList<FiltroPersonalizado>();
	}

	public List<FiltroPersonalizado> getFiltros() {
		return filtros;
	}

	public void setFiltros(List<FiltroPersonalizado> filtros) {
		this.filtros = filtros;
	}
	
	
}
