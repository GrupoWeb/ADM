package gt.gob.rgm.adm.dao;

import java.util.List;

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

import gt.gob.rgm.adm.model.VDetalleGarantia;

@ApplicationScoped
public class VDetalleGarantiaRepository {
	@PersistenceContext
    private EntityManager em;
	
    public VDetalleGarantia findByTramite(Long idTramite, Long idGarantia) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VDetalleGarantia> criteria = cb.createQuery(VDetalleGarantia.class);
        Root<VDetalleGarantia> detalle = criteria.from(VDetalleGarantia.class);
        criteria.select(detalle)
        	.where(cb.and(
    			cb.equal(detalle.get("idTramite"), idTramite),
    			cb.equal(detalle.get("idGarantia"), idGarantia)
			));
        TypedQuery<VDetalleGarantia> query = em.createQuery(criteria);
        query.setHint(QueryHints.CACHE_RETRIEVE_MODE, CacheRetrieveMode.BYPASS);
        List<VDetalleGarantia> listaDetalle = query.getResultList();
        return listaDetalle.get(0);
    }
}
