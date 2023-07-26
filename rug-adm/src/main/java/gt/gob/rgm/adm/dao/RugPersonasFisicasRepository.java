package gt.gob.rgm.adm.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import gt.gob.rgm.adm.model.RugPersonasFisicas;

public class RugPersonasFisicasRepository {
	@PersistenceContext
	private EntityManager em;
	
	public void save(RugPersonasFisicas personaFisica) {
		em.persist(personaFisica);
	}
}
