package gt.gob.rgm.adm.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.model.RugRelTramPartes;

@ApplicationScoped
public class RugRelTramPartesRepository {
	@PersistenceContext
	private EntityManager em;
	
	public List<RugRelTramPartes> findWithFilter(RugRelTramPartes filter, Integer page, Integer size) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RugRelTramPartes> criteria = cb.createQuery(RugRelTramPartes.class);
		Root<RugRelTramPartes> partes = criteria.from(RugRelTramPartes.class);
		Predicate restrictions = null;
		if(filter.getId() != null && filter.getId().getIdTramite() != null) {
			restrictions = cb.equal(partes.get("id").get("idTramite"), filter.getId().getIdTramite());
		}
		if(filter.getId() != null && filter.getId().getIdPersona() != null) {
			restrictions = cb.equal(partes.get("id").get("idPersona"), filter.getId().getIdPersona());
		}
		criteria = criteria.select(partes);
		if(restrictions != null) {
			criteria = criteria.where(restrictions);
		}
		criteria.orderBy(cb.desc(partes.get("id").get("idParte")));
		TypedQuery<RugRelTramPartes> query = em.createQuery(criteria);
		if(page != null) {
			query.setFirstResult((page - 1) * size);
			query.setMaxResults(size);
		}


		return query.getResultList();
	}
	
	public void save(RugRelTramPartes relTramParte) {
		em.persist(relTramParte);
	}
}
