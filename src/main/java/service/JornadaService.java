package service;

import controller.dto.JornadaDTO;
import dao.CampañaDAO;
import dao.JornadaDAO;
import dao.ZonaDAO;
import exceptions.EntidadNoEncontradaException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Campaña;
import model.Jornada;
import model.Zona;

import java.util.Optional;

@RequestScoped
public class JornadaService extends GenericServiceImpl<Jornada, Long> {

    @Inject
    private JornadaDAO jornadaDAO;
    @Inject
    private CampañaDAO campañaDAO;
    @Inject
    private ZonaDAO zonaDAO;

    @Inject
    public JornadaService(JornadaDAO dao, JornadaDAO jornadaDAO) {
        super(dao);
        this.jornadaDAO = jornadaDAO;
    }

    public JornadaService(){
        super(null);
    }
    // Métodos específicos de Usuario si los necesitás

    public Jornada crear(JornadaDTO jornadaDTO) {
        Jornada jornada = new Jornada();
        Optional<Campaña> campaña = campañaDAO.buscarPorId(jornadaDTO.getCampaña_id());
        if (campaña.isEmpty()) {
            throw new EntidadNoEncontradaException("No existe una campaña con ese id");
        }
        jornada.setFechaFin(jornadaDTO.getFechaFin());
        jornada.setFechaInicio(jornadaDTO.getFechaFin());
        jornada.setCampaña(campaña.get());
        jornadaDAO.crear(jornada);
        return jornada;
    }

    public Jornada actualizar(Long id, JornadaDTO dto){
        Optional<Jornada> jornada_t = jornadaDAO.buscarPorId(id);
        if(jornada_t.isEmpty()){
            throw new EntidadNoEncontradaException("No existe una jornada con ese id");
        }
        Jornada jornada = jornada_t.get();
        if(dto.getCampaña_id() != null){
            Optional<Campaña> campaña_t = campañaDAO.buscarPorId(dto.getCampaña_id());
            if (campaña_t.isEmpty()){
                throw new EntidadNoEncontradaException("No existe una campaña con ese id");
            }
            jornada.setCampaña(campaña_t.get());
        }
        if (dto.getFechaFin() != null){
            jornada.setFechaFin(dto.getFechaFin());
        }
        if (dto.getFechaInicio() != null){
            jornada.setFechaInicio(dto.getFechaInicio());
        }
        jornadaDAO.actualizar(jornada);
        return jornada;
    }

    public Jornada agregarZona(Long id, Long zona_id){
        Optional<Jornada> jornada_t = jornadaDAO.buscarPorId(id);
        if(jornada_t.isEmpty()){
            throw new EntidadNoEncontradaException("No existe una jornada con ese id");
        }
        Optional<Zona> zona_t = zonaDAO.buscarPorId(zona_id);
        if(zona_t.isEmpty()){
            throw new EntidadNoEncontradaException("No existe una zona con ese id");
        }
        Jornada jornada = jornada_t.get();
        jornada.agregarZona(zona_t.get());
        jornadaDAO.actualizar(jornada);
        return jornada;
    }

    public Jornada quitarZona(Long id, Long zona_id){
        Optional<Jornada> jornada_t = jornadaDAO.buscarPorId(id);
        if(jornada_t.isEmpty()){
            throw new EntidadNoEncontradaException("No existe una jornada con ese id");
        }
        Optional<Zona> zona_t = zonaDAO.buscarPorId(zona_id);
        if(zona_t.isEmpty()){
            throw new EntidadNoEncontradaException("No existe una zona con ese id");
        }
        Zona zona = zona_t.get();
        Jornada jornada = jornada_t.get();
        jornada.quitarZona(zona.getId());
        jornadaDAO.actualizar(jornada);
        return jornada;
    }
}
