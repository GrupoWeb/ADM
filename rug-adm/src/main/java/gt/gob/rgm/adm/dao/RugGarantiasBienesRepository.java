package gt.gob.rgm.adm.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import gt.gob.rgm.adm.model.RugGarantiasBienes;

public class RugGarantiasBienesRepository {
	@PersistenceContext
	private EntityManager em;
	
	public List<RugGarantiasBienes> findByIdentificador(String identificador) {
		TypedQuery<RugGarantiasBienes> query = em.createNamedQuery("RugGarantiasBienes.findByIdentificador", RugGarantiasBienes.class);
		query.setParameter("identificador", "%" + identificador.toUpperCase() + "%");
		return query.getResultList();
	}
}
