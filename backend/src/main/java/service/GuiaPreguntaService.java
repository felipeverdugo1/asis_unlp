


package service;


import controller.dto.EncuestadorDTO;
import dao.EncuestadorDAO;
import dao.GuiaPreguntaDAO;
import exceptions.EntidadExistenteException;
import exceptions.EntidadNoEncontradaException;
import exceptions.FaltanArgumentosException;
import exceptions.NoPuedesHacerEsoException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Encuestador;
import model.GuiaPregunta;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@RequestScoped
public class GuiaPreguntaService extends GenericServiceImpl<GuiaPregunta, Long> {

    @Inject
    private GuiaPreguntaDAO guiaPreguntaDAO;


    @Inject
    public GuiaPreguntaService(GuiaPreguntaDAO guiaPreguntaDAO) {
        super(guiaPreguntaDAO);
    }


    public GuiaPreguntaService() {super(null);}



    public void crear() throws IOException {

        InputStream generalCsv = getClass().getClassLoader().getResourceAsStream("csv/general.csv");
        InputStream branchesCsv = getClass().getClassLoader().getResourceAsStream("csv/branches.csv");

        guiaPreguntaDAO.cargarDesdeArchivos(generalCsv,branchesCsv);

    }
}
