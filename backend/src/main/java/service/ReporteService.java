package service;

import controller.dto.ReporteDTO;
import dao.CampañaDAO;
import dao.ReporteDAO;
import dao.UsuarioDAO;
import exceptions.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestScoped
public class ReporteService extends GenericServiceImpl<Reporte, Long> {

    @Inject
    private ReporteDAO reporteDAO;

    @Inject
    private CampañaDAO campañaDAO;

    @Inject
    private UsuarioDAO usuarioDAO;


    @Inject
    public ReporteService(ReporteDAO reporteDAO) {
        super(reporteDAO);
    }

    public ReporteService() {
        super(null);
    }

    public Reporte crear(ReporteDTO dto) {

        if (dto.getNombreUnico() == null || dto.getFechaCreacion() == null || dto.getDescripcion() == null) {
            throw new FaltanArgumentosException("El nombre es obligatorio , la fecha de creacion es obligatorio y descripcion");
        }
        if (dto.getFechaCreacion().isAfter(LocalDate.now())) {
            throw new RangoDeFechasInvalidoException("La fecha de inicio no puede ser posterior a la fecha de hoy");
        }

        Optional<Reporte> reporte = reporteDAO.buscarPorCampo("nombreUnico", dto.getNombreUnico());
        // si es duplicado
        if (reporte.isPresent()) {
            throw new EntidadExistenteException("Ya existe un reporte con ese nombre");
        }


        Reporte reporte_nuevo = new Reporte();
        // si existe el campaña o usuario
        if (dto.getCampaña_id() != null || dto.getCreadoPor_id() != null) {
            Optional<Campaña> campaña = campañaDAO.buscarPorId(dto.getCampaña_id());
            Optional<Usuario> usuario = usuarioDAO.buscarPorId(dto.getCreadoPor_id());
            if (!campaña.isPresent()) {
                throw new EntidadNoEncontradaException("El Campaña no existe");
            } else {
                if (!usuario.isPresent()) {
                    throw new EntidadNoEncontradaException("El usuario no existe");
                }
                Campaña campaña_t = campaña.get();
                Usuario usuario_t = usuario.get();
                reporte_nuevo.setFechaCreacion(dto.getFechaCreacion());
                reporte_nuevo.setNombreUnico(dto.getNombreUnico());
                reporte_nuevo.setCreadoPor(usuario_t);
                reporte_nuevo.setDescripcion(dto.getDescripcion());
                reporte_nuevo.setCampaña(campaña_t);
                reporteDAO.crear(reporte_nuevo);
            }
        } else {
            throw new FaltanArgumentosException("La campaña id y el user id es obligatorio");
        }
        return reporte_nuevo;
    }


    public Reporte actualizar(Long id, ReporteDTO dto) {
        Optional<Reporte> reporte = reporteDAO.buscarPorId(id);
        if (reporte.isPresent()) {
            Reporte reporte_actualizado = reporte.get();

            if (reporteDAO.existeOtroConMismoCampo(id, "nombreUnico", dto.getNombreUnico())) {
                throw new EntidadExistenteException("Ya existe otro reporte con ese nombre");
            }

            if (dto.getNombreUnico() != null) {
                reporte_actualizado.setNombreUnico(dto.getNombreUnico());
            }

            if (dto.getDescripcion() != null) {
                reporte_actualizado.setDescripcion(dto.getDescripcion());
            }


            LocalDate fechaCreacionActualizada = dto.getFechaCreacion() != null ? dto.getFechaCreacion() : reporte_actualizado.getFechaCreacion();

            // Validar consistencia
            if (fechaCreacionActualizada.isAfter(LocalDate.now())) {
                throw new RangoDeFechasInvalidoException("La fecha de inicio no puede ser posterior a la fecha de hoy");
            }

            if (dto.getFechaCreacion() != null) {
                reporte_actualizado.setFechaCreacion(dto.getFechaCreacion());
            }

            if (dto.getCreadoPor_id() != null) {
                if (!reporte_actualizado.getCreadoPor().getId().equals(dto.getCreadoPor_id())) {
                    Optional<Usuario> usuario = usuarioDAO.buscarPorId(dto.getCreadoPor_id());
                    if (usuario.isPresent()) {
                        Usuario usuario_r = usuario.get();
                        reporte_actualizado.setCreadoPor(usuario_r);
                    } else {
                        throw new EntidadNoEncontradaException("No existe el usuario " + dto.getCreadoPor_id());
                    }
                }
            }

            if (dto.getCampaña_id() != null) {
                if (!reporte_actualizado.getCampaña().getId().equals(dto.getCampaña_id())) {
                    Optional<Campaña> campaña = campañaDAO.buscarPorId(dto.getCampaña_id());
                    if (campaña.isPresent()) {
                        reporte_actualizado.setCampaña(campaña.get());
                    } else {
                        throw new EntidadNoEncontradaException("No existe la campaña " + dto.getCampaña_id());
                    }
                }

            }

            reporteDAO.actualizar(reporte_actualizado);
            return reporte_actualizado;
        } else {
            throw new EntidadNoEncontradaException("El reporte no existe");
        }
    }

    public Reporte quitarUsuarioCompartido(Long id, Long compartidoCon_id) {
        Reporte reporte = validarReporte(id, compartidoCon_id);
        Usuario usuario = validarUsuario(id, compartidoCon_id);

        if (!reporte.getCompartidoCon().contains(usuario)) {
            throw new EntidadExistenteException("El reporte no fue compartido con este usuario.");
        }
        reporte.quitarUsuarioCompartido(compartidoCon_id);
        reporteDAO.actualizar(reporte);

        return reporte;
    }


    private Usuario validarUsuario(Long id, Long compartidoCon_id) {
        Optional<Usuario> usuario_t = usuarioDAO.buscarPorId(compartidoCon_id);
        if (usuario_t.isEmpty()) {
            throw new EntidadNoEncontradaException("No existe el usuario.");
        }
        return usuario_t.get();
    }

    private Reporte validarReporte(Long id, Long compartidoCon_id) {
        Optional<Reporte> reporte_t = reporteDAO.buscarPorId(id);
        if (reporte_t.isEmpty()) {
            throw new EntidadNoEncontradaException("No existe el reporte.");
        }
        return reporte_t.get();
    }


    public Reporte agregarUsuarioCompartido(Long id, Long compartidoCon_id) {
        Reporte reporte = validarReporte(id, compartidoCon_id);
        Usuario usuario = validarUsuario(id, compartidoCon_id);

        if (reporte.getCompartidoCon().contains(usuario)) {
            throw new EntidadExistenteException("El reporte ya fue compartido con este usuario.");
        }
        reporte.agregarUsuarioCompartido(usuario);
        reporteDAO.actualizar(reporte);

        return reporte;
    }


    public void eliminar(Long id) {
        Optional<Reporte> reporte = reporteDAO.buscarPorId(id);
        if (reporte.isPresent()) {
            reporte.get().getCompartidoCon().clear();
            reporteDAO.actualizar(reporte.get());
            reporteDAO.eliminar(reporte.get());
        } else {
            throw new EntidadNoEncontradaException("El reporte no existe");
        }
    }

    public List<Reporte> listarReportesByCreador(Long usuario_id){
        Optional<Usuario> usuario_t = usuarioDAO.buscarPorId(usuario_id);
        if (usuario_t.isEmpty()){
            throw new EntidadNoEncontradaException("No existe un usuario con ese id");
        }
        List<Reporte> response = new ArrayList<>();
        response = reporteDAO.listarPedidosPorCreador(usuario_id);
        return response;
    }

    public String getStoragePath() {
        // 1. Intentar leer de variable de entorno
        String envPath = System.getenv("PDF_STORAGE_DIR");
        if (envPath != null && !envPath.isBlank()) {
            return envPath.endsWith("/") ? envPath : envPath + "/";
        }

        // 2. Si no hay variable, usar carpeta relativa (desarrollo)
        // return "C:\\\arreglate\\\\vos\\\\que\\\\usas\\\\\windows
        return "/home/matiasc/reportesPDF/";
    }

    public String persistirPDF(InputStream pdfStream, String filename, Long user_id, Long campaña_id, String descr) {
        // Validaciones básicas
        if (pdfStream == null) {
            throw new IllegalArgumentException("El stream del PDF no puede ser nulo");
        }
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de archivo no puede estar vacío");
        }

        // Obtener el directorio de almacenamiento
        String storagePath = getStoragePath();

        Path filePath;

        try {
            // Crear el directorio si no existe
            Path directory = Paths.get(storagePath);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // Validar que el nombre de archivo sea seguro
            String safeFilename = sanitizeFilename(filename);

            // Crear el path completo del archivo
            filePath = directory.resolve(safeFilename).normalize();

            // Guardar el archivo en disco
            Files.copy(pdfStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error al guardar el PDF en disco: " + e.getMessage());
        }

        LocalDate fechaActual = LocalDate.now();
        // falta pasarle id de la campaña y descripcion
        ReporteDTO dto = new ReporteDTO(fechaActual, filePath.toString(), descr, user_id, campaña_id, null);
        crear(dto);

        return "PDF guardado exitosamente en: " + filePath.toString();
    }

    /**
     * Método para sanitizar el nombre de archivo y evitar path traversal
     */
    private String sanitizeFilename(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de archivo no puede ser nulo o vacío");
        }

        // 1. Obtener solo el nombre del archivo (sin path)
        String safeName = new File(filename.trim()).getName();

        // 2. Eliminar caracteres no permitidos (más restrictivo)
        safeName = safeName.replaceAll("[^a-zA-Z0-9._-]", "_");

        // 3. Eliminar múltiples puntos y guiones consecutivos
        safeName = safeName.replaceAll("[._-]{2,}", "_");

        // 4. Asegurar que comience y termine con caracteres válidos
        safeName = safeName.replaceAll("^[._-]+", "").replaceAll("[._-]+$", "");

        // 5. Asegurar extensión .pdf
        if (!safeName.toLowerCase().endsWith(".pdf")) {
            safeName += ".pdf";
        }

        // 6. Limitar longitud máxima (255 caracteres)
        int maxLength = 255;
        if (safeName.length() > maxLength) {
            int extensionLength = 4; // .pdf
            int baseLength = maxLength - extensionLength;
            safeName = safeName.substring(0, baseLength) + safeName.substring(safeName.length() - extensionLength);
        }

        return safeName.isEmpty() ? "documento.pdf" : safeName;
    }
}



    // Additional business logic methods can be added here if needed
