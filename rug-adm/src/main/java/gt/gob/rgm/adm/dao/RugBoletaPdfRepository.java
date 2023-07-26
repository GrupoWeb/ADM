package gt.gob.rgm.adm.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.model.RugBoletaPdf;

@ApplicationScoped
public class RugBoletaPdfRepository {
	@PersistenceContext
    private EntityManager em;
	
	public RugBoletaPdf findById(Long id) {
        return em.find(RugBoletaPdf.class, id);
    }

    public List<RugBoletaPdf> findByTramite(Long idTramite) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RugBoletaPdf> criteria = cb.createQuery(RugBoletaPdf.class);
        Root<RugBoletaPdf> boletas = criteria.from(RugBoletaPdf.class);
        criteria.select(boletas)
        	.where(cb.equal(boletas.get("idTramite"), idTramite))
        	.orderBy(cb.asc(boletas.get("fechaReg")));
        return em.createQuery(criteria).getResultList();
    }
    
    public void save(RugBoletaPdf boleta) {    	
    	em.persist(boleta);
    }
}
