package gt.gob.rgm.adm.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.QueryHints;

import gt.gob.rgm.adm.model.VDetalleBoletaNuevo;

@ApplicationScoped
public class VDetalleBoletaNuevoRepository {

	@PersistenceContext
    private EntityManager em;
	
	public VDetalleBoletaNuevo findByTramite(Long idTramite, Long idGarantia) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VDetalleBoletaNuevo> criteria = cb.createQuery(VDetalleBoletaNuevo.class);
        Root<VDetalleBoletaNuevo> detalle = criteria.from(VDetalleBoletaNuevo.class);
        criteria.select(detalle)
        	.where(cb.and(
    			cb.equal(detalle.get("idTramite"), idTramite),
    			cb.equal(detalle.get("idGarantia"), idGarantia)
			));
        Query query = em.createQuery(criteria);
        query.setHint(QueryHints.CACHE_RETRIEVE_MODE, CacheRetrieveMode.BYPASS);
        List<VDetalleBoletaNuevo> listaDetalle = query.getResultList();
        return listaDetalle.get(0);
    }
}
