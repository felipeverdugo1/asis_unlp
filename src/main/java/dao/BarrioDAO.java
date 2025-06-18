package dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import model.Barrio;
import model.Zona;
import java.util.List;
import util.HibernateUtil;
import jakarta.persistence.*;


@RequestScoped
@Transactional
public class BarrioDAO extends GenericDAOImpl<Barrio, Long> {



    public BarrioDAO() {
        super(Barrio.class);
    }


    //    public void agregarZonas(Barrio barrio, List<Zona> zonas) {
//        EntityManager em = HibernateUtil.getEntityManager();
//        EntityTransaction tx = em.getTransaction();
//        try {
//            tx.begin();
//            Barrio barrio_actualizado = em.find(Barrio.class, barrio.getId());
//            for (Zona zona : zonas) {
//                barrio_actualizado.agregarZona(zona);
//            }
//
//            // em.merge(barrio); // NO es necesario si ya fue obtenido con find()
//            tx.commit();
//        } catch (Exception e) {
//            if (tx.isActive()) {
//                tx.rollback();
//            }
//            throw e;
//        } finally {
//            em.close();
//        }
//    }
}