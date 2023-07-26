package gt.gob.rgm.adm.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import gt.gob.rgm.adm.model.RugGarantiasH;

public class RugGarantiasHRepository {
	@PersistenceContext
	EntityManager em;
	
	public void save(RugGarantiasH garantiaH) {
		em.persist(garantiaH);
	}
	
	public RugGarantiasH findByTramite(Long idTramite) {
		TypedQuery<RugGarantiasH> query = em.createNamedQuery("RugGarantiasH.findByTramite", RugGarantiasH.class);
		query.setParameter("idUltimoTramite", idTramite);
		return query.getSingleResult();
	}
}
