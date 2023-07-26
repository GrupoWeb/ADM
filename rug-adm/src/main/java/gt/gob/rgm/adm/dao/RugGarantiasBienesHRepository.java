package gt.gob.rgm.adm.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.model.RugGarantiasBienesH;

@ApplicationScoped
public class RugGarantiasBienesHRepository {
	@PersistenceContext
    private EntityManager em;
	
    public List<RugGarantiasBienesH> findByTramite(Long idTramite) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RugGarantiasBienesH> criteria = cb.createQuery(RugGarantiasBienesH.class);
        Root<RugGarantiasBienesH> bienes = criteria.from(RugGarantiasBienesH.class);
        criteria.select(bienes)
        	.where(cb.equal(bienes.get("idTramite"), idTramite));
        return em.createQuery(criteria).getResultList();
    }
}
