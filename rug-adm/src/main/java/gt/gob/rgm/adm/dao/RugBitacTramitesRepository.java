package gt.gob.rgm.adm.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import gt.gob.rgm.adm.model.RugBitacTramites;

public class RugBitacTramitesRepository {
	@PersistenceContext
	EntityManager em;
	
	public void save(RugBitacTramites bitacTramites) {
		em.persist(bitacTramites);
	}
	
	public RugBitacTramites findByTramiteAndStatus(Long idTramiteTemp, Integer idStatus) {
		TypedQuery<RugBitacTramites> query = em.createNamedQuery("RugBitacTramites.findByTramiteAndStatus", RugBitacTramites.class);
		query.setParameter("idTramiteTemp", idTramiteTemp);
		query.setParameter("idStatus", idStatus);
		return query.getSingleResult();
	}
}
