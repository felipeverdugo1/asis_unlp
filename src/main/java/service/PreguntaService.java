package service;

import controller.dto.PreguntaDTO;
import dao.EncuestaDAO;
import dao.PreguntaDAO;
import exceptions.EntidadExistenteException;
import exceptions.EntidadNoEncontradaException;
import exceptions.FaltanArgumentosException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Pregunta;

import java.util.Optional;

@RequestScoped
public class PreguntaService extends GenericServiceImpl<Pregunta, Long> {

    @Inject
    private PreguntaDAO preguntaDAO;

    @Inject
    private EncuestaDAO encuestaDAO;

    @Inject
    public PreguntaService(PreguntaDAO preguntaDAO) {
        super(preguntaDAO);
    }

    public PreguntaService() {
        super(null);
    }

    public Pregunta crear(PreguntaDTO dto) {
        if (dto.getEncuesta_id() == null) {
            throw new FaltanArgumentosException("El encuesta_id es obligatorio");
        }

        Pregunta nuevaPregunta = new Pregunta();

        encuestaDAO.buscarPorId(dto.getEncuesta_id()).ifPresentOrElse(
                nuevaPregunta::setEncuesta,
                () -> { throw new EntidadNoEncontradaException("La encuesta no existe"); }
        );

        nuevaPregunta.setPregunta(dto.getPregunta());
        nuevaPregunta.setTipo(dto.getTipo());
        nuevaPregunta.setRespuesta(dto.getRespuesta());

        preguntaDAO.crear(nuevaPregunta);
        return nuevaPregunta;
    }

    public Pregunta actualizar(Long id, PreguntaDTO dto) {
        Optional<Pregunta> preguntaOpt = preguntaDAO.buscarPorId(id);
        if (preguntaOpt.isEmpty()) {
            throw new EntidadNoEncontradaException("La pregunta no existe");
        }

        Pregunta pregunta = preguntaOpt.get();

        if (dto.getPregunta() != null) {
            pregunta.setPregunta(dto.getPregunta());
        }

        if (dto.getTipo() != null) {
            pregunta.setTipo(dto.getTipo());
        }

        if (dto.getRespuesta() != null) {
            pregunta.setRespuesta(dto.getRespuesta());
        }


        if (dto.getEncuesta_id() != null) {
            encuestaDAO.buscarPorId(dto.getEncuesta_id()).ifPresentOrElse(
                    pregunta::setEncuesta,
                    () -> { throw new EntidadNoEncontradaException("La encuesta no existe"); }
            );
        }

        preguntaDAO.actualizar(pregunta);
        return pregunta;
    }

    public void eliminar(Long id) {
        preguntaDAO.buscarPorId(id).ifPresentOrElse(
                preguntaDAO::eliminar,
                () -> { throw new EntidadNoEncontradaException("La pregunta no existe"); }
        );
    }
}