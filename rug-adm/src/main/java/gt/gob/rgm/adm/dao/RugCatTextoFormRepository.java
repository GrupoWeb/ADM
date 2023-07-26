package gt.gob.rgm.adm.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.model.RugCatTextoForm;

@ApplicationScoped
public class RugCatTextoFormRepository {

	@PersistenceContext
    private EntityManager em;
	
	public List<String> findByIdTipoGarantia(Long idTipoGarantia) {
		 CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<String> criteria = cb.createQuery(String.class);
	        Root<RugCatTextoForm> textos = criteria.from(RugCatTextoForm.class);
	        criteria.select(textos.get("texto"))
	        	.where(cb.equal(textos.get("rugCatTextoFormId").get("idTipoGarantia"), idTipoGarantia))
	        	.orderBy(cb.asc(textos.get("rugCatTextoFormId").get("idParte")));
	        return em.createQuery(criteria).getResultList();
    }
}
