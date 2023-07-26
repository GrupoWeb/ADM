package gt.gob.rgm.adm.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import gt.gob.rgm.adm.model.RugContrato;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.eclipse.persistence.config.QueryHints;

public class RugContratoRepository {
	@PersistenceContext
	EntityManager em;
	
        
        public RugContrato findById(Long id){
            Map<String, Object> props = new HashMap<String, Object>();
            props.put(QueryHints.CACHE_RETRIEVE_MODE, CacheRetrieveMode.BYPASS);
            return em.find(RugContrato.class, id);
        }
        
        public RugContrato findByIdTemp(long id){
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<RugContrato> criteria = cb.createQuery(RugContrato.class);
            Root<RugContrato> trams = criteria.from(RugContrato.class);
            criteria.select(trams)
                    .where(cb.and(
    			cb.equal(trams.get("idTramiteTemp"), id),
    			cb.equal(trams.get("clasifContrato"), "OB")
			));
//                    .where(cb.equal(trams.get("idTramiteTemp"), id))
//                    .where(cb.equal(trams.get("clasifContrato"), "OB"));
            RugContrato encontrado = null;
                    try {
                            encontrado = em.createQuery(criteria).getSingleResult();
                    } catch (Exception e) {
                            //e.printStackTrace();
                    }
            return encontrado;
        }
        
	public void save(RugContrato contrato) {
		em.persist(contrato);
	}
        
        
}
