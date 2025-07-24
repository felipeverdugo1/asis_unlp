package dao;

import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import model.GuiaPregunta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

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

    public void cargarDesdeArchivos(InputStream generalCsv, InputStream branchesCsv) throws IOException {
        em.getTransaction().begin();

        cargarDesdeHeader(generalCsv, "general");
        cargarDesdeHeader(branchesCsv, "branch");

        em.getTransaction().commit();
        em.close();
    }

    private void cargarDesdeHeader(InputStream inputStream, String origen) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String headerLine = reader.readLine();
        if (headerLine == null) return;

        String[] headers = headerLine.split(",");
        for (int i = 0; i < headers.length; i++) {
            String raw = headers[i].trim();

            if (raw.matches("^\\d+(_\\d+)?(_.*)?")) {
                // Guardar todo el header como row_etiqueta
                String rowEtiqueta = raw;

                // Generar etiqueta legible (el texto después del último guion bajo)
                String[] partes = raw.split("_", 3);
                String codigo = partes[0];
                String etiquetaLegible = (partes.length == 3) ? partes[2].replace("_", " ").trim() : "sin definir";

                // Verificar si ya existe
                Long count = em.createQuery("SELECT COUNT(g) FROM GuiaPregunta g WHERE g.row_etiqueta = :row", Long.class)
                        .setParameter("row", rowEtiqueta)
                        .getSingleResult();
                if (count > 0) continue;

                // Crear y persistir
                GuiaPregunta gp = new GuiaPregunta()
                        .setRow_etiqueta(rowEtiqueta)
                        .setCodigo(codigo)
                        .setEtiqueta(etiquetaLegible)
                        .setOrden(i);

                em.persist(gp);
            }
        }
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

