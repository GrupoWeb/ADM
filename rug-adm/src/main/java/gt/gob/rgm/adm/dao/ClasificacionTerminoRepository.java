package gt.gob.rgm.adm.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import gt.gob.rgm.adm.model.ClasificacionTermino;

public class ClasificacionTerminoRepository {
	@PersistenceContext
	EntityManager em;
	
	public List<ClasificacionTermino> findAll(Integer page, Integer size) {
		TypedQuery<ClasificacionTermino> query = em.createNamedQuery("ClasificacionTermino.findAll", ClasificacionTermino.class);
		if(page != null) {
			query.setFirstResult((page - 1) * size);
			query.setMaxResults(size);
		}
		return query.getResultList();
	}
	
	public Long countAll() {
		TypedQuery<Long> query = em.createNamedQuery("ClasificacionTermino.countAll", Long.class);
		return query.getSingleResult();
	}
	
	public ClasificacionTermino save(ClasificacionTermino termino) {
		em.persist(termino);
		return termino;
	}
	
	public ClasificacionTermino findById(Long clasificacionTerminoId) {
		return em.find(ClasificacionTermino.class, clasificacionTerminoId);
	}
	
	public ClasificacionTermino findByTermino(String termino) {
		TypedQuery<ClasificacionTermino> query = em.createNamedQuery("ClasificacionTermino.findByTermino", ClasificacionTermino.class);
		query.setParameter("termino", termino);
		return query.getSingleResult();
	}
	
	public List<ClasificacionTermino> findNotApplied() {
		TypedQuery<ClasificacionTermino> query = em.createNamedQuery("ClasificacionTermino.findNotApplied", ClasificacionTermino.class);
		return query.getResultList();
	}
	
	public List<ClasificacionTermino> findNotClassified(Integer page, Integer size) {
		TypedQuery<ClasificacionTermino> query = em.createNamedQuery("ClasificacionTermino.findNotClassified", ClasificacionTermino.class);
		if(page != null) {
			query.setFirstResult((page - 1) * size);
			query.setMaxResults(size);
		}
		return query.getResultList();
	}
	
	public Long countNotClassified() {
		TypedQuery<Long> query = em.createNamedQuery("ClasificacionTermino.countNotClassified", Long.class);
		return query.getSingleResult();
	}
}
