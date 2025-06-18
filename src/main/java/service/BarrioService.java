package service;

import controller.dto.BarrioDTO;
import controller.dto.ZonaDTO;
import dao.BarrioDAO;
import dao.ZonaDAO;
import exceptions.EntidadExistenteException;
import exceptions.EntidadNoEncontradaException;
import exceptions.FaltanArgumentosException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Barrio;
import model.Zona;

import java.util.Optional;

@RequestScoped
public class BarrioService extends GenericServiceImpl<Barrio, Long> {

    @Inject
    public BarrioService(BarrioDAO dao) {
        super(dao);
    }
    @Inject
    private BarrioDAO barrioDAO;
    @Inject
    private ZonaService zonaService;




    public BarrioService() { super(null);}
    // Additional business logic methods can be added here if needed

    public Barrio crear(BarrioDTO barrioDTO) {
        // Validación de campos requeridos primero
        if (barrioDTO.getNombre() == null || barrioDTO.getInformacion() == null || barrioDTO.getGeolocalizacion() == null) {
            throw new FaltanArgumentosException("Se requieren los campos nombre, informacion y geolocalizacion");
        }

        Optional<Barrio> barrio = barrioDAO.buscarPorCampo("nombre", barrioDTO.getNombre());
        // Validación de duplicado
        if (barrio.isPresent()) {
            throw new EntidadExistenteException("Ya existe un barrio con ese nombre");
        }

        Barrio barrio_nuevo = new Barrio();
        barrio_nuevo.setNombre(barrioDTO.getNombre());
        barrio_nuevo.setInformacion(barrioDTO.getInformacion());
        barrio_nuevo.setGeolocalizacion(barrioDTO.getGeolocalizacion());
        barrioDAO.crear(barrio_nuevo);
        return barrio_nuevo;
    }


    public Barrio actualizar(Long barrioId, BarrioDTO barrioDTO) {

        // validar si viene algun argumento
        if(!barrioDTO.validarTodosNull()){

            // Validar si existe el barrio
            Barrio barrioExistente = barrioDAO.buscarPorId(barrioId);
            if (barrioExistente == null) {
                throw new EntidadNoEncontradaException("Barrio no encontrado");
            }

            // Validar nombre único si se va a actualizar
            if (barrioDTO.getNombre() != null) {
                if (!barrioDTO.getNombre().equals(barrioExistente.getNombre())) {
                    // Buscar si existe otro barrio con ese nombre
                    Optional<Barrio> barrioConMismoNombre = barrioDAO.buscarPorCampo("nombre", barrioDTO.getNombre());
                    if (barrioConMismoNombre.isPresent()) {
                        throw new EntidadExistenteException("Nombre de barrio ya existente");
                    }
                    barrioExistente.setNombre(barrioDTO.getNombre());
                }
            }

            //Seteo variables nuevas
            if (barrioDTO.getInformacion() != null){
                barrioExistente.setInformacion(barrioDTO.getInformacion());
            }

            if (barrioDTO.getGeolocalizacion() != null){
                barrioExistente.setGeolocalizacion(barrioDTO.getGeolocalizacion());
            }
            barrioDAO.actualizar(barrioExistente);
            return barrioExistente;

        } else {
            throw new FaltanArgumentosException("Se requiere alguno de los campos nombre, informacion y geolocalizacion");
        }
    }

}