package gt.gob.rgm.adm.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import gt.gob.rgm.adm.model.RugRelTramIncParte;

public class RugRelTramIncParteRepository {
	@PersistenceContext
	EntityManager em;
	
	public void save(RugRelTramIncParte relTramIncParte) {
		em.persist(relTramIncParte);
	}
}
