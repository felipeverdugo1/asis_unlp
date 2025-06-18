package service;

import controller.dto.EncuestaDTO;
import controller.dto.ZonaDTO;
import dao.EncuestaDAO;
import dao.EncuestadorDAO;
import dao.JornadaDAO;
import dao.ZonaDAO;
import exceptions.EntidadExistenteException;
import exceptions.EntidadNoEncontradaException;
import exceptions.FaltanArgumentosException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.*;

import java.util.Optional;

@RequestScoped
public class EncuestaService extends GenericServiceImpl<Encuesta, Long> {

    @Inject
    private EncuestaDAO encuestaDAO;

    @Inject
    private EncuestadorDAO encuestadorDAO;
    @Inject
    private JornadaDAO jornadaDAO;
    @Inject
    private ZonaDAO zonaDAO;

    @Inject
    public EncuestaService(EncuestaDAO encuestaDAO) {
        super(encuestaDAO);
    }

    public EncuestaService() {
        super(null);
    }


    public Encuesta crear(EncuestaDTO encuestaDTO) {

        //validamos campos obligatorios
        if (
                encuestaDTO.getNombreUnico() == null ||
                        encuestaDTO.getFecha() == null ||
                        encuestaDTO.getEncuestador_id() == null ||
                        encuestaDTO.getJornada_id() == null ||
                        encuestaDTO.getZona_id() == null
        ) {
            throw new FaltanArgumentosException("Se requieren los campos nombre unico, fecha, encuestador_id, jornada_id y zona_id");
        }

        Optional<Encuesta> encuesta = encuestaDAO.buscarPorCampo("nombreUnico", encuestaDTO.getNombreUnico());
        // si es duplicado
        if ( encuesta.isPresent() ) {
            throw new EntidadExistenteException("Ya existe una encuesta con ese nombre unico");
        }

        Optional<Encuestador> encuestador_t = encuestadorDAO.buscarPorId(encuestaDTO.getEncuestador_id());
        if (encuestador_t.isEmpty()) {
            throw new EntidadNoEncontradaException("El Encuestador no existe");
        }

        Optional<Jornada> jornada_t = jornadaDAO.buscarPorId(encuestaDTO.getJornada_id());
        if (jornada_t.isEmpty()) {
            throw new EntidadNoEncontradaException("La Jornada no existe");
        }

        Optional<Zona> zona_t = zonaDAO.buscarPorId(encuestaDTO.getZona_id());
        if (zona_t.isEmpty()) {
            throw new EntidadNoEncontradaException("La Zona no existe");
        }

        Encuesta encuesta_nueva = new Encuesta();
        encuesta_nueva.setNombreUnico(encuestaDTO.getNombreUnico());
        encuesta_nueva.setFecha(encuestaDTO.getFecha());
        encuesta_nueva.setEncuestador(encuestador_t.get());
        encuesta_nueva.setJornada(jornada_t.get());
        encuesta_nueva.setZona(zona_t.get());
        encuestaDAO.crear(encuesta_nueva);
        return encuesta_nueva;
    }

    public Encuesta actualizar(Long encuestaId, EncuestaDTO encuestaDTO) {
        // validar si viene algun argumento
        if(!encuestaDTO.validarTodosNull()){
            // Validar si existe la encuesta
            Optional<Encuesta> encuestaExistente = encuestaDAO.buscarPorId(encuestaId);
            if (encuestaExistente.isEmpty()) {
                throw new EntidadNoEncontradaException("Encuesta no encontrado");
            }
            // Validar nombre único si se va a actualizar
            if (encuestaDTO.getNombreUnico() != null) {
                if (!encuestaDTO.getNombreUnico().equals(encuestaExistente.get().getNombreUnico())) {
                    // Buscar si existe otra encuesta con ese nombre
                    Optional<Encuesta> EncuestaConMismoNombre = encuestaDAO.buscarPorCampo("nombreUnico", encuestaDTO.getNombreUnico());
                    if (EncuestaConMismoNombre.isPresent()) {
                        throw new EntidadExistenteException("Nombre de encuesta ya existente");
                    }
                    encuestaExistente.get().setNombreUnico( encuestaDTO.getNombreUnico());
                }
            }
            //Seteo variables nuevas
            if (encuestaDTO.getFecha() != null){
                encuestaExistente.get().setFecha(encuestaDTO.getFecha());
            }

            if (encuestaDTO.getEncuestador_id() != null){
                Optional<Encuestador> encuestador_t = encuestadorDAO.buscarPorId(encuestaDTO.getEncuestador_id());
                if (encuestador_t.isPresent()) {
                    encuestaExistente.get().setEncuestador(encuestador_t.get());
                }else {
                    throw new EntidadNoEncontradaException("El encuestador no existe");
                }
            }

            if (encuestaDTO.getJornada_id() != null){
                Optional<Jornada> jornada_t = jornadaDAO.buscarPorId(encuestaDTO.getJornada_id());
                if (jornada_t.isPresent()) {
                    encuestaExistente.get().setJornada(jornada_t.get());
                }else {
                    throw new EntidadNoEncontradaException("La jornada no existe");
                }
            }

            if (encuestaDTO.getZona_id() != null){
                Optional<Zona> zona_t = zonaDAO.buscarPorId(encuestaDTO.getZona_id());
                if (zona_t.isPresent()) {
                    encuestaExistente.get().setZona(zona_t.get());
                }else {
                    throw new EntidadNoEncontradaException("La zona no existe");
                }
            }

            encuestaDAO.actualizar(encuestaExistente.get());
            return encuestaExistente.get();
        } else {
            throw new FaltanArgumentosException("Se requiere alguno de los campos nombreUnico, fecha, encuestador_id, jornada_id u zona_id");
        }
    }

    public void eliminar(Long id) {
        Optional<Encuesta> encuesta = encuestaDAO.buscarPorId(id);
        if ( encuesta.isPresent() ) {
            encuestaDAO.actualizar(encuesta.get());
        } else {
            throw new EntidadNoEncontradaException("El encuesta no existe");
        }
    }
}
