package gt.gob.rgm.adm.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import gt.gob.rgm.adm.model.RugCatTipoTramite;

public class RugCatTipoTramiteRepository {
	@PersistenceContext
	EntityManager em;
	
	public RugCatTipoTramite getById(Integer idTipoTramite) {
		return em.find(RugCatTipoTramite.class, idTipoTramite);
	}
}
