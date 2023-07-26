package gt.gob.rgm.adm.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import gt.gob.rgm.adm.model.RugRelGrupoAcreedor;

public class RugRelGrupoAcreedorRepository {
	@PersistenceContext
	private EntityManager em;
	
	public void save(RugRelGrupoAcreedor relGrupoAcreedor) {
		em.persist(relGrupoAcreedor);
	}
}
