package service;


import controller.dto.EncuestadorDTO;
import dao.EncuestadorDAO;
import exceptions.EntidadExistenteException;
import exceptions.EntidadNoEncontradaException;
import exceptions.FaltanArgumentosException;
import exceptions.NoPuedesHacerEsoException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Encuestador;

import java.util.Optional;

@RequestScoped
public class EncuestadorService extends GenericServiceImpl<Encuestador, Long> {

    @Inject
    private EncuestadorDAO encuestadorDAO;

    @Inject
    public EncuestadorService(EncuestadorDAO dao) {
        super(dao);
    }


    public EncuestadorService() {super(null);}
    // Métodos específicos de Usuario si los necesitás

    public Encuestador crear(EncuestadorDTO encuestadorDTO) {
        // Validación de campos requeridos primero
        if (
                encuestadorDTO.getNombre() == null ||
                        encuestadorDTO.getDni() == null ||
                        encuestadorDTO.getEdad() == null ||
                        encuestadorDTO.getGenero() == null ||
                        encuestadorDTO.getOcupacion() == null
        ) {
            throw new FaltanArgumentosException("Se requieren los campos nombre, dni, edad, genero y ocupacion");
        }

        Optional<Encuestador> encuestador = encuestadorDAO.buscarPorCampo("dni", encuestadorDTO.getDni());
        // Validación de duplicado
        if (encuestador.isPresent()) {
            throw new EntidadExistenteException("Ya existe un encuestador con ese dni");
        }

        Encuestador encuestador_nuevo = new Encuestador();
        encuestador_nuevo.setNombre(encuestadorDTO.getNombre());
        encuestador_nuevo.setDni(encuestadorDTO.getDni());
        encuestador_nuevo.setEdad(encuestadorDTO.getEdad());
        encuestador_nuevo.setGenero(encuestadorDTO.getGenero());
        encuestador_nuevo.setOcupacion(encuestadorDTO.getOcupacion());
        encuestadorDAO.crear(encuestador_nuevo);
        return encuestador_nuevo;
    }


    public Encuestador actualizar(Long encuestadorId, EncuestadorDTO encuestadorDTO) {
        // validar si viene algun argumento
        if(!encuestadorDTO.validarTodosNull()){
            // Validar si existe el barrio
            Optional<Encuestador> encuestadorExistente = encuestadorDAO.buscarPorId(encuestadorId);
            if (encuestadorExistente.isEmpty()) {
                throw new EntidadNoEncontradaException("Encuestador no encontrado");
            }
            // Validar dni único si se va a actualizar
            if (encuestadorDTO.getDni() != null) {
                if (!encuestadorDTO.getDni().equals(encuestadorExistente.get().getDni())) {
                    // Buscar si existe otro encuestador con ese dni
                    Optional<Encuestador> EncuestadorConMismoDni = encuestadorDAO.buscarPorCampo("dni", encuestadorDTO.getDni());
                    if (EncuestadorConMismoDni.isPresent()) {
                        throw new EntidadExistenteException("Dni de encuestador ya existente");
                    }
                    encuestadorExistente.get().setDni(encuestadorDTO.getDni());
                }
            }
            //Seteo variables nuevas
            if (encuestadorDTO.getNombre() != null){
                encuestadorExistente.get().setNombre(encuestadorDTO.getNombre());
            }

            if (encuestadorDTO.getEdad() != null){
                encuestadorExistente.get().setEdad(encuestadorDTO.getEdad());
            }

            if (encuestadorDTO.getOcupacion() != null){
                encuestadorExistente.get().setOcupacion(encuestadorDTO.getOcupacion());
            }

            if (encuestadorDTO.getGenero() != null){
                encuestadorExistente.get().setGenero(encuestadorDTO.getGenero());
            }
            encuestadorDAO.actualizar(encuestadorExistente.get());
            return encuestadorExistente.get();

        } else {
            throw new FaltanArgumentosException("Se requiere alguno de los campos nombre, dni, edad, genero u ocupacion");
        }
    }

    public void eliminar(Long id) {
        Optional<Encuestador> encuestador_t = encuestadorDAO.buscarPorId(id);
        if ( encuestador_t.isEmpty() ) {
            throw new EntidadNoEncontradaException("El encuestador no existe");
        } else {
            Encuestador encuestador = encuestador_t.get();
            if (!encuestador.getEncuestas().isEmpty()) {
                throw new NoPuedesHacerEsoException("El encuestador tiene Encuestas relacionadas, no se puede eliminar.");
            }
            encuestadorDAO.eliminar(encuestador);
        }
    }
}
