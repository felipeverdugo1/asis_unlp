package service;

import auth.annotation.RequiereRol;
import controller.dto.BarrioDTO;
import controller.dto.ZonaDTO;
import dao.BarrioDAO;
import dao.ZonaDAO;
import exceptions.EntidadExistenteException;
import exceptions.EntidadNoEncontradaException;
import exceptions.FaltanArgumentosException;
import exceptions.NoPuedesHacerEsoException;
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
            Optional<Barrio> barrioExistente = barrioDAO.buscarPorId(barrioId);
            if (!barrioExistente.isPresent()) {
                throw new EntidadNoEncontradaException("Barrio no encontrado");
            }

            // Validar nombre único si se va a actualizar
            if (barrioDTO.getNombre() != null) {
                if (!barrioDTO.getNombre().equals(barrioExistente.get().getNombre())) {
                    // Buscar si existe otro barrio con ese nombre
                    Optional<Barrio> barrioConMismoNombre = barrioDAO.buscarPorCampo("nombre", barrioDTO.getNombre());
                    if (barrioConMismoNombre.isPresent()) {
                        throw new EntidadExistenteException("Nombre de barrio ya existente");
                    }
                    barrioExistente.get().setNombre(barrioDTO.getNombre());
                }
            }

            //Seteo variables nuevas
            if (barrioDTO.getInformacion() != null){
                if (!barrioDTO.getInformacion().equals(barrioExistente.get().getInformacion())) {
                    barrioExistente.get().setInformacion(barrioDTO.getInformacion());
                }
            }

            if (barrioDTO.getGeolocalizacion() != null){
                if (!barrioDTO.getGeolocalizacion().equals(barrioExistente.get().getGeolocalizacion())) {
                    barrioExistente.get().setGeolocalizacion(barrioDTO.getGeolocalizacion());
                }
            }
            barrioDAO.actualizar(barrioExistente.get());
            return barrioExistente.get();

        } else {
            throw new FaltanArgumentosException("Se requiere alguno de los campos nombre, informacion y geolocalizacion");
        }
    }

    public void eliminar(Long id) {
        Optional<Barrio> barrio_t = barrioDAO.buscarPorId(id);
        if ( barrio_t.isEmpty() ) {
            throw new EntidadNoEncontradaException("El Barrio no existe");
        }
        Barrio barrio = barrio_t.get();
        if (!barrio.getCampañas().isEmpty()) {
            throw new NoPuedesHacerEsoException("El Barrio tiene Campañas asignadas, no se puede eliminar.");
        }
        if (!barrio.getZonas().isEmpty()) {
            throw new NoPuedesHacerEsoException("El Barrio tiene Zonas asignadas, no se puede eliminar.");
        }
        if (!barrio.getOrganizacionesSociales().isEmpty()) {
            throw new NoPuedesHacerEsoException("El Barrio tiene Organizaciones Sociales asignadas, no se puede eliminar.");
        }

        barrioDAO.eliminar(barrio);
    }

}