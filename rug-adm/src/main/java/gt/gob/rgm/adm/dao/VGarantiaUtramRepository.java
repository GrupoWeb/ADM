package gt.gob.rgm.adm.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.eclipse.persistence.config.QueryHints;

import gt.gob.rgm.adm.model.VGarantiaUtram;


@ApplicationScoped
public class VGarantiaUtramRepository {
	@PersistenceContext
    private EntityManager em;
	
    public VGarantiaUtram findByTramite(Long idTramite, Long idGarantia) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VGarantiaUtram> criteria = cb.createQuery(VGarantiaUtram.class);
        Root<VGarantiaUtram> garantias = criteria.from(VGarantiaUtram.class);
        criteria.select(garantias)
        	.where(cb.and(
    			cb.equal(garantias.get("idTramite"), idTramite),
    			cb.equal(garantias.get("idGarantia"), idGarantia)
			));
        TypedQuery<VGarantiaUtram> query = em.createQuery(criteria);
        query.setHint(QueryHints.CACHE_RETRIEVE_MODE, CacheRetrieveMode.BYPASS);
        return query.getSingleResult();
    }
}
