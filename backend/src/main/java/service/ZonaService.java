package service;

import controller.dto.ZonaDTO;
import dao.BarrioDAO;
import dao.EncuestaDAO;
import dao.ZonaDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import model.Barrio;
import model.Encuesta;
import model.Zona;
import exceptions.*;

import java.util.List;
import java.util.Optional;


@RequestScoped
public class ZonaService extends GenericServiceImpl<Zona, Long> {

    @Inject
    private EncuestaDAO encuestaDAO;

    @Inject
    public ZonaService(ZonaDAO dao) {super(dao);}

    @Inject
    private ZonaDAO zonaDAO;
    @Inject
    private BarrioDAO barrioDAO;


    public ZonaService() {
        super(null);
    }

    public Zona crear(ZonaDTO zonaDTO) {
        if (zonaDTO.getNombre() == null){
            throw new FaltanArgumentosException("El campo nombre es obligatorio.");
        }
        if (zonaDTO.getGeolocalizacion() == null){
            throw new FaltanArgumentosException("El campo geolocalizacion es obligatorio.");
        }
        Optional<Zona> zona = zonaDAO.buscarPorCampo("nombre", zonaDTO.getNombre());
        // si es duplicado
        if ( zona.isPresent() ) {
            throw new EntidadExistenteException("Ya existe una zona con ese nombre");
        }
        Zona zona_nueva = new Zona();
        // si existe el barrio
        if (zonaDTO.getBarrio_id() != null) {
            Optional<Barrio> barrio_t = barrioDAO.buscarPorId(zonaDTO.getBarrio_id());
            if (barrio_t.isPresent()) {
                Barrio barrio = barrio_t.get();
                zona_nueva.setBarrio(barrio);
                zona_nueva.setNombre(zonaDTO.getNombre());
                zona_nueva.setGeolocalizacion(zonaDTO.getGeolocalizacion());
                zonaDAO.crear(zona_nueva);
            } else {
                throw new EntidadNoEncontradaException("El Barrio no existe");
            }
        } else {
            throw new FaltanArgumentosException("El barrio_id es obligatorio");
        }
        return zona_nueva;
    }

    public Zona actualizar(Long id, ZonaDTO zonaDTO) {
        Optional<Zona> zona = zonaDAO.buscarPorId(id);
        if ( zona.isPresent() ) {
            Zona zona_actualizada = zona.get();
            if (zonaDTO.getNombre() != null) {
                zona_actualizada.setNombre(zonaDTO.getNombre());
            }
            if (zonaDTO.getGeolocalizacion() != null) {
                zona_actualizada.setGeolocalizacion(zonaDTO.getGeolocalizacion());
            }
            if (zonaDTO.getBarrio_id() != null) {
                if (!zona_actualizada.getBarrio().getId().equals(zonaDTO.getBarrio_id())) {
                    Optional<Barrio> barrio_t = barrioDAO.buscarPorId(zonaDTO.getBarrio_id());
                    if (barrio_t.isPresent()) {
                        Barrio barrio = barrio_t.get();
                        zona_actualizada.setBarrio(barrio);
                    } else {
                        throw new EntidadNoEncontradaException("No existe el barrio " + zonaDTO.getBarrio_id());
                    }
                }
            }
            zonaDAO.actualizar(zona_actualizada);
            return zona_actualizada;
        } else {
            throw new EntidadNoEncontradaException("La zona no existe");
        }
    }

    public void eliminar(Long id) {
        Optional<Zona> zona = zonaDAO.buscarPorId(id);
        if ( zona.isPresent() ) {
            //Barrio barrio = zona.get().getBarrio();
            //barrio.getZonas().remove(zona.get());
            //barrioDAO.actualizar(barrio);
            zonaDAO.eliminar(zona.get());
        } else {
            throw new EntidadNoEncontradaException("El Zona no existe");
        }
    }
}
