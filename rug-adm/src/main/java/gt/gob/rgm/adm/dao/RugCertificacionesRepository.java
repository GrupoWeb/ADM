package gt.gob.rgm.adm.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.model.RugCertificaciones;

@ApplicationScoped
public class RugCertificacionesRepository {
	@PersistenceContext
    private EntityManager em;
	
    public RugCertificaciones findByTramite(Long idTramite) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RugCertificaciones> criteria = cb.createQuery(RugCertificaciones.class);
        Root<RugCertificaciones> certificaciones = criteria.from(RugCertificaciones.class);
        criteria.select(certificaciones)
        	.where(cb.equal(certificaciones.get("idTramiteCert"), idTramite));
        return em.createQuery(criteria).getSingleResult();
    }
}
