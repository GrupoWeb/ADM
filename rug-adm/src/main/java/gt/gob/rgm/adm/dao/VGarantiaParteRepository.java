package gt.gob.rgm.adm.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.model.VGarantiaParte;

@ApplicationScoped
public class VGarantiaParteRepository {
	@PersistenceContext
    private EntityManager em;
	
    public List<VGarantiaParte> findByTramite(Long idTramite, Long idGarantia) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VGarantiaParte> criteria = cb.createQuery(VGarantiaParte.class);
        Root<VGarantiaParte> partes = criteria.from(VGarantiaParte.class);
        criteria.select(partes)
        	.where(cb.and(
    			cb.equal(partes.get("idTramite"), idTramite),
    			cb.equal(partes.get("idGarantia"), idGarantia)
			));
        return em.createQuery(criteria).getResultList();
    }
}
