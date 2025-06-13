package filter;

//import jakarta.ws.rs.container.ContainerRequestContext;
//import jakarta.ws.rs.container.ContainerRequestFilter;
//import jakarta.ws.rs.container.ContainerResponseContext;
//import jakarta.ws.rs.container.ContainerResponseFilter;
//import jakarta.ws.rs.ext.Provider;
//import util.HibernateUtil;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityTransaction;
//
//@Provider
//public class EntityManagerFilter implements ContainerRequestFilter, ContainerResponseFilter {
//
//    @Override
//    public void filter(ContainerRequestContext requestContext) {
//        // Inicia el EntityManager y transacción al comienzo de la petición
//        EntityManager em = HibernateUtil.getEntityManager();
//        em.getTransaction().begin();
//    }
//
//    @Override
//    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
//        EntityManager em = HibernateUtil.getEntityManager();
//        try {
//            if (em != null && em.isOpen()) {
//                EntityTransaction transaction = em.getTransaction();
//                // Commit solo si la transacción está activa y no hubo errores
//                if (transaction.isActive()) {
//                    if (responseContext.getStatus() >= 400) {
//                        transaction.rollback();
//                    } else {
//                        transaction.commit();
//                    }
//                }
//            }
//        } finally {
//            HibernateUtil.closeEntityManager(em);
//        }
//    }
//}