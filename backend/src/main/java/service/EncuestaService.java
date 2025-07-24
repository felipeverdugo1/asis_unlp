package service;

import controller.dto.CargaEncuestasDTO;
import controller.dto.EncuestaDTO;
import dao.*;
import exceptions.EntidadExistenteException;
import exceptions.EntidadNoEncontradaException;
import exceptions.FaltanArgumentosException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import static java.lang.Long.parseLong;


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
    private GuiaPreguntaDAO guiaPreguntaDAO;

    @Inject
    public EncuestaService(EncuestaDAO encuestaDAO) {
        super(encuestaDAO);
    }

    public EncuestaService() {
        super(null);
    }







private Encuesta validarCampos(CargaEncuestasDTO cargaEncuestasDTO) {
    //validamos campos obligatorios
    Encuesta encuesta = new Encuesta();

    Optional<Encuestador> encuestador_t = encuestadorDAO.buscarPorId(cargaEncuestasDTO.getEncuestador_id());
    if (encuestador_t.isEmpty()) {
        throw new EntidadNoEncontradaException("El Encuestador no existe");
    }

    Optional<Jornada> jornada_t = jornadaDAO.buscarPorId(cargaEncuestasDTO.getJornada_id());
    if (jornada_t.isEmpty()) {
        throw new EntidadNoEncontradaException("La Jornada no existe");
    }

    Optional<Zona> zona_t = zonaDAO.buscarPorId(cargaEncuestasDTO.getZona_id());
    if (zona_t.isEmpty()) {
        throw new EntidadNoEncontradaException("La Zona no existe");
    }

    if (jornada_t.get().getZonas().stream().noneMatch(z -> z.getId().equals(zona_t.get().getId()))) {
        throw new EntidadNoEncontradaException("La zona no corresponde a la jornada.");
    }

    encuesta.setEncuestador(encuestador_t.get());
    encuesta.setJornada(jornada_t.get());
    encuesta.setZona(zona_t.get());

    return encuesta;
}



    private InputStream prepararInputStream(InputStream original) throws IOException {
        PushbackInputStream pushbackStream = new PushbackInputStream(original, 3);
        byte[] bom = new byte[3];
        if (pushbackStream.read(bom) != 3 ||
                bom[0] != (byte) 0xEF || bom[1] != (byte) 0xBB || bom[2] != (byte) 0xBF) {
            pushbackStream.unread(bom); // No era BOM, lo devolvemos
        }
        return pushbackStream;
    }




    public String cargarEncuestas(CargaEncuestasDTO cargarEncuestasDTO) {
        Encuesta encuestaBase = validarCampos(cargarEncuestasDTO);

        try {
            // === Preparamos los InputStreams para remover el BOM si existe ===
            InputStream inputStreamGeneral = prepararInputStream(cargarEncuestasDTO.getGeneralCsv());
            InputStream inputStreamBranches = prepararInputStream(cargarEncuestasDTO.getBranchesCsv());

            // === Definimos el formato del CSV ===
            CSVFormat format = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build();

            try {
                // Lectores y parsers
                BufferedReader readerGeneral = new BufferedReader(new InputStreamReader(inputStreamGeneral, StandardCharsets.UTF_8));
                BufferedReader readerBranches = new BufferedReader(new InputStreamReader(inputStreamBranches, StandardCharsets.UTF_8));

                CSVParser parserGeneral = format.parse(readerGeneral);
                CSVParser parserBranches = format.parse(readerBranches);

                // Cargamos guías y etiquetas válidas
                List<String> etiquetasValidas = guiaPreguntaDAO.obtenerTodosLasRowEtiqueta();
                Map<String, GuiaPregunta> mapaGuiaPorEtiqueta = guiaPreguntaDAO.listarTodos().stream()
                        .collect(Collectors.toMap(GuiaPregunta::getRow_etiqueta, g -> g));

                // Referencias a entidades relacionadas
                Encuestador encuestador = encuestaBase.getEncuestador();
                Jornada jornada = encuestaBase.getJornada();
                Zona zona = encuestaBase.getZona();

                // Agrupamos las personas (branches) por UUID
                Map<String, List<CSVRecord>> branchesByOwner = new HashMap<>();
                for (CSVRecord branch : parserBranches) {
                    String ownerUuid = branch.get("ec5_branch_owner_uuid");
                    branchesByOwner.computeIfAbsent(ownerUuid, k -> new ArrayList<>()).add(branch);
                }

                // Procesamos encuestas generales (una por casa)
                for (CSVRecord recordGeneral : parserGeneral) {
                    String uuid = recordGeneral.get("ec5_uuid");
                    String fechaStr = recordGeneral.get("created_at").substring(0, 10);
                    LocalDate fecha = LocalDate.parse(fechaStr);
                    String coordenadas = recordGeneral.get("lat_1_Presione_actualiza") + ", " +
                            recordGeneral.get("long_1_Presione_actualiza");

                    Encuesta nuevaEncuesta = new Encuesta()
                            .setEncuestador(encuestador)
                            .setJornada(jornada)
                            .setZona(zona)
                            .setNombreArchivo("archivo_importado.csv")  // Mejor usar un campo nombre real si está disponible
                            .setFecha(fecha)
                            .setUuid(uuid)
                            .setCoordenadas(coordenadas);

                    // Preguntas generales (de la casa)
                    for (String etiquetaGeneral : recordGeneral.toMap().keySet()) {
                        if (etiquetasValidas.contains(etiquetaGeneral)) {
                            String etiqueta = mapaGuiaPorEtiqueta.get(etiquetaGeneral).getEtiqueta();
                            String respuesta = recordGeneral.get(etiquetaGeneral);

                            if (etiqueta != null && respuesta != null && !respuesta.isBlank() && respuesta.length() <= 100) {
                                Pregunta pregunta = new Pregunta()
                                        .setPregunta(etiqueta)
                                        .setRespuesta(respuesta)
                                        .setEsPersonal(false)
                                        .setUuid_padre(uuid)
                                        .setEncuesta(nuevaEncuesta);
                                nuevaEncuesta.agregarPregunta(pregunta);
                            }
                        }
                    }

                    // Preguntas de las personas dentro de la casa
                    List<CSVRecord> branchesOfHouse = branchesByOwner.getOrDefault(uuid, Collections.emptyList());
                    for (CSVRecord branch : branchesOfHouse) {
                        for (String etiquetaBranch : branch.toMap().keySet()) {
                            if (etiquetasValidas.contains(etiquetaBranch)) {
                                String etiqueta = mapaGuiaPorEtiqueta.get(etiquetaBranch).getEtiqueta();
                                String respuesta = branch.get(etiquetaBranch);

                                if (etiqueta != null && respuesta != null && !respuesta.isBlank() && respuesta.length() <= 100) {
                                    Pregunta pregunta = new Pregunta()
                                            .setPregunta(etiqueta)
                                            .setRespuesta(respuesta)
                                            .setEsPersonal(true)
                                            .setUuid_padre(uuid)
                                            .setEncuesta(nuevaEncuesta);
                                    nuevaEncuesta.agregarPregunta(pregunta);
                                }
                            }
                        }
                    }

                    // Guardamos la encuesta completa
                    encuestaDAO.crear(nuevaEncuesta);
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error al procesar los archivos CSV: " + e.getMessage(), e);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error al leer los archivos: " + e.getMessage(), e);
        }

        return "Encuestas cargadas correctamente";
    }



//    public Encuesta crear(EncuestaDTO encuestaDTO) {
//
//        //validamos campos obligatorios
//        if (
//                encuestaDTO.getNombreArchivo() == null ||
//                        encuestaDTO.getFecha() == null ||
//                        encuestaDTO.getEncuestador_id() == null ||
//                        encuestaDTO.getJornada_id() == null ||
//                        encuestaDTO.getZona_id() == null
//        ) {
//            throw new FaltanArgumentosException("Se requieren los campos nombre unico, fecha, encuestador_id, jornada_id y zona_id");
//        }
//
//        Optional<Encuesta> encuesta = encuestaDAO.buscarPorCampo("nombreUnico", encuestaDTO.getNombreArchivo());
//        // si es duplicado
//        if ( encuesta.isPresent() ) {
//            throw new EntidadExistenteException("Ya existe una encuesta con ese nombre unico");
//        }
//
//        Optional<Encuestador> encuestador_t = encuestadorDAO.buscarPorId(encuestaDTO.getEncuestador_id());
//        if (encuestador_t.isEmpty()) {
//            throw new EntidadNoEncontradaException("El Encuestador no existe");
//        }
//
//        Optional<Jornada> jornada_t = jornadaDAO.buscarPorId(encuestaDTO.getJornada_id());
//        if (jornada_t.isEmpty()) {
//            throw new EntidadNoEncontradaException("La Jornada no existe");
//        }
//
//        Optional<Zona> zona_t = zonaDAO.buscarPorId(encuestaDTO.getZona_id());
//        if (zona_t.isEmpty()) {
//            throw new EntidadNoEncontradaException("La Zona no existe");
//        }
//
//        if (jornada_t.get().getZonas().stream().noneMatch(z -> z.getId().equals(zona_t.get().getId()))) {
//            throw new EntidadNoEncontradaException("La zona no corresponde a la jornada.");
//        }
//
//        Encuesta encuesta_nueva = new Encuesta();
//        encuesta_nueva.setNombreArchivo(encuestaDTO.getNombreArchivo());
//        encuesta_nueva.setFecha(encuestaDTO.getFecha());
//        encuesta_nueva.setEncuestador(encuestador_t.get());
//        encuesta_nueva.setJornada(jornada_t.get());
//        encuesta_nueva.setZona(zona_t.get());
//        encuestaDAO.crear(encuesta_nueva);
//        return encuesta_nueva;
//    }



    public Encuesta actualizar(Long encuestaId, EncuestaDTO encuestaDTO) {
        // validar si viene algun argumento
        if (!encuestaDTO.validarTodosNull()) {
            // Validar si existe la encuesta
            Optional<Encuesta> encuestaExistente = encuestaDAO.buscarPorId(encuestaId);
            if (encuestaExistente.isEmpty()) {
                throw new EntidadNoEncontradaException("Encuesta no encontrado");
            }


            if (encuestaDTO.getNombreArchivo() != null) {
                encuestaExistente.get().setNombreArchivo(encuestaDTO.getNombreArchivo());
            }

            if (encuestaDTO.getCoordenadas() != null) {
                encuestaExistente.get().setCoordenadas(encuestaDTO.getCoordenadas());
            }

            //Seteo variables nuevas
            if (encuestaDTO.getFecha() != null) {
                if (!encuestaDTO.getFecha().equals(encuestaExistente.get().getFecha())) {
                    encuestaExistente.get().setFecha(encuestaDTO.getFecha());
                }
            }

            if (encuestaDTO.getEncuestador_id() != null) {
                if (!encuestaDTO.getEncuestador_id().equals(encuestaExistente.get().getId())) {
                    Optional<Encuestador> encuestador_t = encuestadorDAO.buscarPorId(encuestaDTO.getEncuestador_id());
                    if (encuestador_t.isPresent()) {
                        encuestaExistente.get().setEncuestador(encuestador_t.get());
                    } else {
                        throw new EntidadNoEncontradaException("El encuestador no existe");
                    }
                }
            }
            // si se cambian jornada y zona, la zona debe pertenecer al mismo barrio
            if (encuestaDTO.getJornada_id() != null && encuestaDTO.getZona_id() != null) {
                Optional<Jornada> jornada_t = jornadaDAO.buscarPorId(encuestaDTO.getJornada_id());
                Optional<Zona> zona_t = zonaDAO.buscarPorId(encuestaDTO.getZona_id());
                if (jornada_t.isPresent()) {
                    if (zona_t.isPresent()) {
                        boolean zonaExiste = jornada_t.get().getZonas().stream()
                                .anyMatch(z -> z.getId().equals(zona_t.get().getId()));
                        if (!zonaExiste) {
                            throw new EntidadNoEncontradaException("La zona no corresponde a las zonas de la jornada.");
                        }
                        encuestaExistente.get().setJornada(jornada_t.get());
                        encuestaExistente.get().setZona(zona_t.get());
                    } else {
                        throw new EntidadNoEncontradaException("La zona indicada no existe");
                    }
                } else {
                    throw new EntidadNoEncontradaException("La jornada indicada no existe");
                }
            } else {
                // si se cambia solo la jornada, la zona actual debe pertenecer a la jornada nueva
                if (encuestaDTO.getJornada_id() != null) {
                    Optional<Jornada> jornada_t = jornadaDAO.buscarPorId(encuestaDTO.getJornada_id());
                    if (jornada_t.isPresent()) {
                        Optional<Zona> zona_t = zonaDAO.buscarPorId(encuestaExistente.get().getZona().getId());
                        if (zona_t.isPresent()) {
                            boolean zonaExiste = jornada_t.get().getZonas().stream()
                                    .anyMatch(z -> z.getId().equals(zona_t.get().getId()));
                            if (!zonaExiste) {
                                throw new EntidadNoEncontradaException("La jornada indicada no tiene asignada la zona actual.");
                            }
                        } else {
                            throw new EntidadNoEncontradaException("La zona asignada a la encuesta no existe.");
                        }
                        encuestaExistente.get().setJornada(jornada_t.get());
                    } else {
                        throw new EntidadNoEncontradaException("La jornada no existe");
                    }
                }

                // si se cambia solo la zona, tiene que pertenecer a la jornada actual
                if (encuestaDTO.getZona_id() != null) {
                    Optional<Zona> zona_t = zonaDAO.buscarPorId(encuestaDTO.getZona_id());
                    if (zona_t.isPresent()) {
                        Optional<Jornada> jornada_t = jornadaDAO.buscarPorId(encuestaExistente.get().getJornada().getId());
                        if (jornada_t.isPresent()) {
                            boolean zonaExiste = jornada_t.get().getZonas().stream()
                                    .anyMatch(z -> z.getId().equals(zona_t.get().getId()));
                            if (!zonaExiste) {
                                throw new EntidadNoEncontradaException("La zona indicada no esta asignada a la jornada actual");
                            }
                        } else {
                            throw new EntidadNoEncontradaException("La jornada actual de la encuesta no existe en la bd.");
                        }
                        encuestaExistente.get().setZona(zona_t.get());
                    } else {
                        throw new EntidadNoEncontradaException("La zona no existe");
                    }
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
        if (encuesta.isPresent()) {
            encuestaDAO.eliminar(encuesta.get());
        } else {
            throw new EntidadNoEncontradaException("El encuesta no existe");
        }
    }


}
