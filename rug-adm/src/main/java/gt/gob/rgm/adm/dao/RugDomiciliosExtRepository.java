package gt.gob.rgm.adm.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import gt.gob.rgm.adm.model.RugDomiciliosExt;

public class RugDomiciliosExtRepository {
	@PersistenceContext
	EntityManager em;
	
	public Long save(RugDomiciliosExt domicilio) {
		em.persist(domicilio);
		return domicilio.getIdDomicilio();
	}
	
	public RugDomiciliosExt getDomicilio(Long idDomicilio) {
		return em.find(RugDomiciliosExt.class, idDomicilio);
	}
}
