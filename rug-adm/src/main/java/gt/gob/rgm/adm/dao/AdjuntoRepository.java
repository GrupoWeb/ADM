package gt.gob.rgm.adm.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.model.Adjunto;


@ApplicationScoped
public class AdjuntoRepository {

	@PersistenceContext
	private EntityManager em;
	
	public List<Adjunto> findByIncidente(Long incidenteId){		
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Adjunto> criteria = cb.createQuery(Adjunto.class);
        Root<Adjunto> adjuntos = criteria.from(Adjunto.class);
        criteria.select(adjuntos)
        	.where(cb.equal(adjuntos.get("incidenteId"), incidenteId));        	
        return em.createQuery(criteria).getResultList();
	}
	
	public void save(Adjunto adjunto) {
		em.persist(adjunto);
	}
	
	public Adjunto findById(Long id) {
        return em.find(Adjunto.class, id);
    }
}
