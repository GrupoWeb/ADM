package gt.gob.rgm.adm.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import gt.gob.rgm.adm.model.TramitesRugIncomp;

public class TramitesRugIncompRepository {
	@PersistenceContext
	EntityManager em;
	
	public void save(TramitesRugIncomp tramitesRugIncomp) {
		em.persist(tramitesRugIncomp);
	}
	
	public TramitesRugIncomp findById(Long idTramiteTemp) {
		return em.find(TramitesRugIncomp.class, idTramiteTemp);
	}
}
