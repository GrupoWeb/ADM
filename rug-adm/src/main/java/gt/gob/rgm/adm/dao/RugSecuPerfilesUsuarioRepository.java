package gt.gob.rgm.adm.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import gt.gob.rgm.adm.model.RugSecuPerfilesUsuario;

public class RugSecuPerfilesUsuarioRepository {
	@PersistenceContext
	EntityManager em;
	
	public void save(RugSecuPerfilesUsuario secuPerfilesUsuario) {
		em.persist(secuPerfilesUsuario);
	}
}
