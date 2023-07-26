package gt.gob.rgm.adm.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import gt.gob.rgm.adm.model.RugRelGarantiaPartes;

public class RugRelGarantiaPartesRepository {
	@PersistenceContext
	EntityManager em;
	
	public void save(RugRelGarantiaPartes relGarantiaParte) {
		em.persist(relGarantiaParte);
	}
}
