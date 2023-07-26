package gt.gob.rgm.adm.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import gt.gob.rgm.adm.model.RugGarantiasPendientes;

public class RugGarantiasPendientesRepository {
	@PersistenceContext
	EntityManager em;
	
	public Long save(RugGarantiasPendientes garantiaPendiente) {
		em.persist(garantiaPendiente);
		return garantiaPendiente.getIdGarantiaPend();
	}
	
	public RugGarantiasPendientes findByTramite(Long idTramite) {
		TypedQuery<RugGarantiasPendientes> query = em.createNamedQuery("RugGarantiasPendientes.findByTramite", RugGarantiasPendientes.class);
		query.setParameter("idUltimoTramite", idTramite);
		return query.getSingleResult();
	}
}
