package service;

import controller.dto.*;
import dao.*;
import exceptions.EntidadNoEncontradaException;
import exceptions.FaltanArgumentosException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;



@RequestScoped
public class EncuestaService extends GenericServiceImpl<Encuesta, Long> {

    @Inject
    private EncuestaDAO encuestaDAO;

    @Inject
    private EncuestadorDAO encuestadorDAO;

    @Inject
    private PreguntaDAO preguntaDAO;
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

        // Definir las etiquetas específicas que queremos procesar
        Set<String> etiquetasPermitidas = Set.of(
                "8_3_Edad",
                "9_4_De_acuerdo_a_la_",
                "20_14_Recibe_algn_pr",
                "38_26_Tiene_acceso_a",
                "37_25_Con_qu_materia"
        );

        try {
            // === Preparamos los InputStreams ===
            byte[] generalBytes = inputStreamToByteArray(prepararInputStream(cargarEncuestasDTO.getGeneralCsv()));
            byte[] branchesBytes = inputStreamToByteArray(prepararInputStream(cargarEncuestasDTO.getBranchesCsv()));


            //Cargo la guia de preguntas
            guiaPreguntaDAO.cargarDesdeArchivos(
                    new ByteArrayInputStream(generalBytes),
                    new ByteArrayInputStream(branchesBytes)
            );
            // === Definimos el formato del CSV ===
            CSVFormat format = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build();

            try {
                // Lectores y parsers
                BufferedReader readerGeneral = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(generalBytes), StandardCharsets.UTF_8));
                BufferedReader readerBranches = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(branchesBytes), StandardCharsets.UTF_8));

                CSVParser parserGeneral = format.parse(readerGeneral);
                CSVParser parserBranches = format.parse(readerBranches);

                // Cargamos solo las guías de las etiquetas permitidas
                Map<String, GuiaPregunta> mapaGuiaPorEtiqueta = guiaPreguntaDAO.listarTodos().stream()
                        .filter(g -> etiquetasPermitidas.contains(g.getRow_etiqueta()))
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
                            .setNombreArchivo("archivo_importado.csv")
                            .setFecha(fecha)
                            .setUuid(uuid)
                            .setCoordenadas(coordenadas);
                    encuestaDAO.crear(nuevaEncuesta);

                    // Preguntas generales (de la casa) - solo las etiquetas permitidas
                    for (String etiquetaGeneral : mapaGuiaPorEtiqueta.keySet()) {
                        if (recordGeneral.isSet(etiquetaGeneral)) {
                            String respuesta = recordGeneral.get(etiquetaGeneral);
                            GuiaPregunta guia = mapaGuiaPorEtiqueta.get(etiquetaGeneral);

                            if (respuesta != null && !respuesta.isBlank() && respuesta.length() <= 100) {
                                Pregunta pregunta = new Pregunta()
                                        .setPregunta(guiaPreguntaDAO.normalizarTexto(guia.getEtiqueta()))
                                        .setRespuesta(guiaPreguntaDAO.normalizarTexto(respuesta))
                                        .setEncuesta(nuevaEncuesta);
                                preguntaDAO.crear(pregunta);
                            }
                        }
                    }
                    int personaId = 1;
                    // Preguntas de las personas dentro de la casa - solo las etiquetas permitidas
                    List<CSVRecord> branchesOfHouse = branchesByOwner.getOrDefault(uuid, Collections.emptyList());
                    for (CSVRecord branch : branchesOfHouse) {
                        for (String etiquetaBranch : mapaGuiaPorEtiqueta.keySet()) {
                            if (branch.isSet(etiquetaBranch)) {
                                String respuesta = branch.get(etiquetaBranch);
                                GuiaPregunta guia = mapaGuiaPorEtiqueta.get(etiquetaBranch);

                                if (respuesta != null && !respuesta.isBlank() && respuesta.length() <= 100) {
                                    Pregunta pregunta = new Pregunta()
                                            .setPregunta(guiaPreguntaDAO.normalizarTexto(guia.getEtiqueta()))
                                            .setRespuesta(guiaPreguntaDAO.normalizarTexto(respuesta))
                                            .setPersonaId(personaId)
                                            .setEncuesta(nuevaEncuesta);
                                    preguntaDAO.crear(pregunta);
                                }
                            }
                        }
                        personaId++; // paso a la siguiente persona dentro de la casa
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException("Error al procesar los archivos CSV: " + e.getMessage(), e);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error al leer los archivos: " + e.getMessage(), e);
        }

        return "Encuestas cargadas correctamente con preguntas filtradas";
    }


    private byte[] inputStreamToByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int nRead;
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }


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


    public List<DatosRecolectadosDTO> obtenerDatos(ObtenerDatosDTO dto) {


        boolean tieneFiltrosVivienda = dto.getMaterial_vivienda() != null ||
                dto.getAcceso_agua() != null;

        boolean tieneFiltrosPersonales = (dto.getEdad() != null && !dto.getEdad().isEmpty()) ||
                (dto.getGeneros() != null && !dto.getGeneros().isEmpty()) || dto.getAcceso_salud() != null;

        List<Encuesta> encuestas = tieneFiltrosVivienda ?
                encuestaDAO.findByFiltrosVivienda(
                        dto.getMaterial_vivienda(),
                        dto.getAcceso_agua()
                ) :
                encuestaDAO.listarTodos();


        int cant;
        int cantEncuestadas=0;
        List<DatosRecolectadosDTO> datosRecolectados = new ArrayList<>();
        DatosRecolectadosDTO datosRecolectadosDTO;

        for (Encuesta encuesta : encuestas) {
            cant = 0;

            if (tieneFiltrosPersonales) {
                // Traigo preguntas personales de esta encuesta
                List<PreguntaDTO> preguntas = preguntaDAO.findPreguntasPersonalesByEncuestaId(encuesta.getId());


                // Agrupamos por persona dentro de la casa
                Map<String, List<PreguntaDTO>> personas = preguntas.stream()
                        .collect(Collectors.groupingBy(p -> p.getEncuesta_id() + "_" + p.getPersona_id()));


                for (List<PreguntaDTO> preguntasPersona : personas.values()) {
                    boolean cumple = true;

                    // Filtro por edad
                    if (dto.getEdad() != null && !dto.getEdad().isEmpty()) {
                        cumple &= preguntasPersona.stream()
                                .anyMatch(p -> p.getPregunta().equalsIgnoreCase("edad") &&
                                        dto.getEdad().contains(Integer.parseInt(p.getRespuesta())));
                    }

                    // Filtro por género
                    if (dto.getGeneros() != null && !dto.getGeneros().isEmpty()) {
                        cumple &= preguntasPersona.stream()
                                .anyMatch(p -> p.getPregunta().equalsIgnoreCase("genero") &&
                                        dto.getGeneros().contains(p.getRespuesta().toLowerCase()));
                    }

                    // Filtro por acceso_salud
                    if (dto.getAcceso_salud() != null) {
                        cumple &= preguntasPersona.stream()
                                .anyMatch(p -> p.getPregunta().equalsIgnoreCase("acceso_salud") &&
                                        p.getRespuesta().equalsIgnoreCase(dto.getAcceso_salud()));
                    }

                    if (cumple) {
                        cant++;
                    }
                }
                 datosRecolectadosDTO = crearDTO(encuesta,cant);
                if (cant > 0 && datosRecolectadosDTO !=null) {
                    datosRecolectados.add(datosRecolectadosDTO);
                }
            } else {
                cant= (int) preguntaDAO.countPersonasDistintasPorEncuesta(encuesta.getId());
                datosRecolectadosDTO = crearDTO(encuesta,cant);
                if (datosRecolectadosDTO !=null) {
                    datosRecolectados.add(datosRecolectadosDTO);
                }

            }
            cantEncuestadas +=cant;
        }

        Integer totalPersonas = obtenerTotalPersonas();
        System.out.println("Total personas: " + totalPersonas);
        System.out.println("Total de encuestas: " + cantEncuestadas);

        return datosRecolectados;
    }

    private int obtenerTotalPersonas(){
        return encuestaDAO
                .listarTodos()
                .stream()
                .mapToInt(encuesta ->
                        (int) preguntaDAO.countPersonasDistintasPorEncuesta(encuesta.getId())
                )
                .sum();
    }


    private DatosRecolectadosDTO crearDTO(Encuesta encuesta, int cantidad) {
        return parsearCoordenadas(encuesta.getCoordenadas())
                .map(coords -> new DatosRecolectadosDTO(
                        coords.getLatitud(),
                        coords.getLongitud(),
                        cantidad
                ))
                .orElse(null);
    }

    private Optional<Coordenadas> parsearCoordenadas(String coordenadasStr) {
        if (coordenadasStr == null || coordenadasStr.trim().isEmpty()) {
            return Optional.empty();
        }

        // Eliminar espacios y separar por coma
        String[] partes = coordenadasStr.trim().replace(" ", "").split(",");

        if (partes.length != 2) {
            return Optional.empty();
        }

        try {
            double latitud = Double.parseDouble(partes[0]);
            double longitud = Double.parseDouble(partes[1]);
            return Optional.of(new Coordenadas(latitud, longitud));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    // Clase auxiliar para coordenadas
    private static class Coordenadas {
        private final double latitud;
        private final double longitud;

        public Coordenadas(double latitud, double longitud) {
            this.latitud = latitud;
            this.longitud = longitud;
        }

        public double getLatitud() {
            return latitud;
        }

        public double getLongitud() {
            return longitud;
        }
    }
}












