package gt.gob.rgm.adm.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import gt.gob.rgm.adm.model.RugCatNacionalidades;

public class RugCatNacionalidadesRepository {
	@PersistenceContext
	EntityManager em;
	
	public List<RugCatNacionalidades> findByStatus(String status) {
		TypedQuery<RugCatNacionalidades> query = em.createNamedQuery("RugCatNacionalidades.findByStatus", RugCatNacionalidades.class);
		query.setParameter("status", status);
		return query.getResultList();
	}
}
