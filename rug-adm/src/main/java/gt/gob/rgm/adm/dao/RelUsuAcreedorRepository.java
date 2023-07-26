package gt.gob.rgm.adm.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import gt.gob.rgm.adm.model.RelUsuAcreedor;

public class RelUsuAcreedorRepository {
	@PersistenceContext
	private EntityManager em;
	
	public void save(RelUsuAcreedor relUsuAcreedor) {
		em.persist(relUsuAcreedor);
	}
}
