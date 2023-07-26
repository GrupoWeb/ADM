package gt.gob.rgm.adm.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.model.RugParametroConf;

@ApplicationScoped
public class RugParametroConfRepository {

	@PersistenceContext
    private EntityManager em;

    public RugParametroConf findByKey(String key) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RugParametroConf> criteria = cb.createQuery(RugParametroConf.class);
        Root<RugParametroConf> parametros = criteria.from(RugParametroConf.class);
        criteria.select(parametros)
        	.where(cb.equal(parametros.get("cveParametro"), key));
        return em.createQuery(criteria).getResultList().get(0);
    }
}
