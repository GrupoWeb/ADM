package gt.gob.rgm.adm.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import gt.gob.rgm.adm.model.RugFirmaDoctos;

public class RugFirmaDoctosRepository {
	@PersistenceContext
	EntityManager em;
	
	public void save(RugFirmaDoctos firmaDoctos) {
		em.persist(firmaDoctos);
	}
}
