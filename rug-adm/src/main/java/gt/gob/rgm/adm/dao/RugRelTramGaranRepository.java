package gt.gob.rgm.adm.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.model.RugRelTramGaran;

@ApplicationScoped
public class RugRelTramGaranRepository {
	@PersistenceContext
    private EntityManager em;
	
    public RugRelTramGaran findByTramite(Long idTramite) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RugRelTramGaran> criteria = cb.createQuery(RugRelTramGaran.class);
        Root<RugRelTramGaran> garantias = criteria.from(RugRelTramGaran.class);
        criteria.select(garantias)
        	.where(cb.equal(garantias.get("id").get("idTramite"), idTramite));
        return em.createQuery(criteria).getSingleResult();
    }
    
    public List<RugRelTramGaran> findByGarantia(Long idGarantia) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RugRelTramGaran> criteria = cb.createQuery(RugRelTramGaran.class);
        Root<RugRelTramGaran> garantias = criteria.from(RugRelTramGaran.class);
        criteria.select(garantias)
        	.where(cb.equal(garantias.get("id").get("idGarantia"), idGarantia));
        
        return em.createQuery(criteria).getResultList();
    }
    
    public void save(RugRelTramGaran relTramGaran) {
    	em.persist(relTramGaran);
    }
}
