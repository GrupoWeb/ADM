package gt.gob.rgm.adm.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import gt.gob.rgm.adm.model.RugPersonasMorales;

public class RugPersonasMoralesRepository {
	@PersistenceContext
	EntityManager em;
	
	public void save(RugPersonasMorales personaMoral) {
		em.persist(personaMoral);
	}
}
