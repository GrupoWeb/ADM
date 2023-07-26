package gt.gob.rgm.adm.dao;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.eclipse.persistence.config.QueryHints;

import gt.gob.rgm.adm.model.Tramites;

public class TramitesRepository {

	@PersistenceContext
    private EntityManager em;
	
	public Tramites findById(Long id) {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(QueryHints.CACHE_RETRIEVE_MODE, CacheRetrieveMode.BYPASS);
        return em.find(Tramites.class, id);
    }
	
	public Tramites findByIdTemp(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tramites> criteria = cb.createQuery(Tramites.class);
        Root<Tramites> trams = criteria.from(Tramites.class);
        criteria.select(trams)
        	.where(cb.equal(trams.get("idTramiteTemp"), id));
        Tramites encontrado = null;
		try {
			encontrado = em.createQuery(criteria).getSingleResult();
		} catch (Exception e) {
			//e.printStackTrace();
		}
        return encontrado;
    }
	
	public void save(Tramites tramites) {
		em.persist(tramites);
	}
}
