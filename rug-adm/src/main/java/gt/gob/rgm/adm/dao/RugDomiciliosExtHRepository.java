package gt.gob.rgm.adm.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import gt.gob.rgm.adm.model.RugDomiciliosExtH;

public class RugDomiciliosExtHRepository {
	@PersistenceContext
	EntityManager em;
	
	public void save(RugDomiciliosExtH domicilioExtH) {
		em.persist(domicilioExtH);
	}
}
