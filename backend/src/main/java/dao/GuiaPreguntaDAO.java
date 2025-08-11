package dao;

import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import model.GuiaPregunta;

import java.io.*;
import java.util.List;
import java.util.Map;

@RequestScoped
@Transactional
public class GuiaPreguntaDAO extends GenericDAOImpl<GuiaPregunta, Long> {

    public GuiaPreguntaDAO() {
        super(GuiaPregunta.class);
    }



    public List<String> obtenerTodosLasRowEtiqueta () {
        return  em.createQuery(
                "SELECT g.row_etiqueta FROM GuiaPregunta g", String.class
        ).getResultList();
    }



    private ByteArrayInputStream toByteArrayInputStream(InputStream input) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int nRead;
        while ((nRead = input.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return new ByteArrayInputStream(buffer.toByteArray());
    }




    public void cargarDesdeArchivos(InputStream generalCsv, InputStream branchesCsv) throws IOException {
        em.getTransaction().begin();

        // Convertir InputStream a ByteArrayInputStream
        ByteArrayInputStream generalCsvBytes = toByteArrayInputStream(generalCsv);
        ByteArrayInputStream branchesCsvBytes = toByteArrayInputStream(branchesCsv);

        cargarDesdeHeader(generalCsvBytes, "general");
        cargarDesdeHeader(branchesCsvBytes, "branch");

        em.getTransaction().commit();
    }

    private void cargarDesdeHeader(InputStream inputStream, String origen) throws IOException {
        // Mapa de campos que nos interesan con sus identificadores
        Map<String, String> camposRelevantes = Map.of(
                "8_3_Edad", "Edad",
                "9_4_De_acuerdo_a_la_", "Género",
                "20_14_Recibe_algn_pr", "Acceso a salud",
                "38_26_Tiene_acceso_a", "Acceso a agua",
                "37_25_Con_qu_materia", "Material de vivienda"
        );

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String headerLine = reader.readLine();
        if (headerLine == null) return;

        String[] headers = headerLine.split(",");
        for (int i = 0; i < headers.length; i++) {
            String raw = headers[i].trim();

            // Verificar si el campo es uno de los que nos interesan
            boolean esCampoRelevante = camposRelevantes.keySet().stream()
                    .anyMatch(raw::contains);

            if (esCampoRelevante) {
                // Extraer el identificador completo (ej: "8_3_Edad")
                String rowEtiqueta = raw;

                // Obtener la etiqueta legible del mapa
                String etiquetaLegible = camposRelevantes.entrySet().stream()
                        .filter(entry -> raw.contains(entry.getKey()))
                        .map(Map.Entry::getValue)
                        .findFirst()
                        .orElse("sin definir");

                // Extraer el código (primer número antes del guion bajo)
                String codigo = rowEtiqueta.split("_")[0];

                // Verificar si ya existe
                Long count = em.createQuery(
                                "SELECT COUNT(g) FROM GuiaPregunta g WHERE g.row_etiqueta = :row", Long.class)
                        .setParameter("row", rowEtiqueta)
                        .getSingleResult();

                if (count == 0) {
                    // Crear y persistir solo los campos relevantes
                    GuiaPregunta gp = new GuiaPregunta()
                            .setRow_etiqueta(rowEtiqueta)
                            .setCodigo(codigo)
                            .setEtiqueta(etiquetaLegible);

                    em.persist(gp);
                }
            }
        }
        em.flush();
    }




//    public boolean existeCodigo(String codigo) {
//        String query = "SELECT COUNT(g) FROM GuiaPregunta g WHERE g.codigo = :codigo";
//        Long count = em.createQuery(query, Long.class)
//                .setParameter("codigo", codigo)
//                .getSingleResult();
//        return count > 0;
//    }
//
//    public GuiaPregunta buscarPorCodigo(String codigo) {
//        String query = "SELECT g FROM GuiaPregunta g WHERE g.codigo = :codigo";
//        return em.createQuery(query, GuiaPregunta.class)
//                .setParameter("codigo", codigo)
//                .getSingleResult();
//    }




}

