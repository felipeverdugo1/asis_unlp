package service;

import controller.dto.OrganizacionSocialDTO;
import dao.BarrioDAO;
import dao.OrganizacionSocialDAO;
import dao.UsuarioDAO;
import exceptions.EntidadExistenteException;
import exceptions.EntidadNoEncontradaException;
import exceptions.FaltanArgumentosException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.OrganizacionSocial;
import model.Usuario;

import java.util.Optional;

@RequestScoped
public class OrganizacionSocialService extends GenericServiceImpl<OrganizacionSocial, Long> {

    @Inject
    private OrganizacionSocialDAO orgSocialDAO;

    @Inject
    private BarrioDAO barrioDAO;

    @Inject
    private UsuarioDAO usuarioDAO;

    @Inject
    public OrganizacionSocialService(OrganizacionSocialDAO orgSocialDAO) {
        super(orgSocialDAO);
    }

    public OrganizacionSocialService() {
        super(null);
    }

    public OrganizacionSocial crear(OrganizacionSocialDTO dto) {
        Optional<OrganizacionSocial> orgSocialExistente = orgSocialDAO.buscarPorCampo("nombre", dto.getNombre());
        if (orgSocialExistente.isPresent()) {
            throw new EntidadExistenteException("Ya existe una organización social con ese nombre");
        }

        if (dto.getBarrio_id() == null || dto.getReferente_id() == null) {
            throw new FaltanArgumentosException("El barrio_id y referente_id son obligatorios");
        }

        OrganizacionSocial nuevaOrgSocial = new OrganizacionSocial();

        barrioDAO.buscarPorId(dto.getBarrio_id()).ifPresentOrElse(
                nuevaOrgSocial::setBarrio,
                () -> { throw new EntidadNoEncontradaException("El barrio no existe"); }
        );

        usuarioDAO.buscarPorId(dto.getReferente_id()).ifPresentOrElse(
                nuevaOrgSocial::setReferente,
                () -> { throw new EntidadNoEncontradaException("El referente no existe"); }
        );

        nuevaOrgSocial.setNombre(dto.getNombre());
        nuevaOrgSocial.setDomicilio(dto.getDomicilio());
        nuevaOrgSocial.setActividadPrincipal(dto.getActividadPrincipal());

        orgSocialDAO.crear(nuevaOrgSocial);
        return nuevaOrgSocial;
    }

    public OrganizacionSocial actualizar(Long id, OrganizacionSocialDTO dto) {
        Optional<OrganizacionSocial> orgSocialOpt = orgSocialDAO.buscarPorId(id);
        if (orgSocialOpt.isEmpty()) {
            throw new EntidadNoEncontradaException("La organización social no existe");
        }

        OrganizacionSocial orgSocial = orgSocialOpt.get();

        if (dto.getNombre() != null) {
            Optional<OrganizacionSocial> orga_existente = orgSocialDAO.buscarPorCampo("nombre", dto.getNombre());

            if (orga_existente.isPresent() && !orga_existente.get().getId().equals(id)) {
                throw new EntidadExistenteException("Ya existe una organizacion social con ese nombre.");
            }
            orgSocial.setNombre(dto.getNombre());
        }


        if (dto.getDomicilio() != null) {
            orgSocial.setDomicilio(dto.getDomicilio());
        }

        if (dto.getActividadPrincipal() != null) {
            orgSocial.setActividadPrincipal(dto.getActividadPrincipal());
        }

        if (dto.getBarrio_id() != null) {
            barrioDAO.buscarPorId(dto.getBarrio_id()).ifPresentOrElse(
                    orgSocial::setBarrio,
                    () -> { throw new EntidadNoEncontradaException("El barrio no existe"); }
            );
        }

        if (dto.getReferente_id() != null) {
            usuarioDAO.buscarPorId(dto.getReferente_id()).ifPresentOrElse(
                    orgSocial::setReferente,
                    () -> { throw new EntidadNoEncontradaException("El referente no existe"); }
            );
        }

        orgSocialDAO.actualizar(orgSocial);
        return orgSocial;
    }

    public void eliminar(Long id) {
        orgSocialDAO.buscarPorId(id).ifPresentOrElse(
                orgSocialDAO::eliminar,
                () -> { throw new EntidadNoEncontradaException("La organización social no existe"); }
        );
    }
}