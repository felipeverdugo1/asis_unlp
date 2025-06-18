package service;

import controller.dto.ZonaDTO;
import dao.BarrioDAO;
import dao.ZonaDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import model.Barrio;
import model.Zona;
import exceptions.*;

import java.util.Optional;


@RequestScoped
public class ZonaService extends GenericServiceImpl<Zona, Long> {

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
        Optional<Zona> zona = zonaDAO.buscarPorCampo("nombre", zonaDTO.getNombre());
        // si es duplicado
        if ( zona.isPresent() ) {
            throw new EntidadExistenteException("Ya existe una zona con ese nombre");
        }
        Zona zona_nueva = new Zona();
        // si existe el barrio
        if (zonaDTO.getBarrio_id() != null) {
            Barrio barrio = barrioDAO.buscarPorId(zonaDTO.getBarrio_id());
            if (barrio != null) {
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
}
