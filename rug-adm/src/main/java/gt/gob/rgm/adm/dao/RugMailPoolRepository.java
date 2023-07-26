package gt.gob.rgm.adm.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.model.RugMailPool;

@ApplicationScoped
public class RugMailPoolRepository {
	@PersistenceContext
    private EntityManager em;
	
	public RugMailPool findById(Long id) {
        return em.find(RugMailPool.class, id);
    }
	
	public void save(RugMailPool mailPool) {
    	em.persist(mailPool);
    }
	
    public List<RugMailPool> findByEmailAndStatus(String emailUsuario, Long status) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RugMailPool> criteria = cb.createQuery(RugMailPool.class);
        Root<RugMailPool> mails = criteria.from(RugMailPool.class);
        criteria.select(mails)
        	.where(cb.and(
    			cb.equal(mails.get("destinatario"), emailUsuario),
    			cb.equal(mails.get("idStatusMail"), status)
			))
        	.orderBy(cb.asc(mails.get("fechaEnvio")));
        return em.createQuery(criteria).getResultList();
    }
    
    public Long countByEmailAndStatus(String emailUsuario, Long status) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<RugMailPool> mails = criteria.from(RugMailPool.class);
        criteria.select(cb.construct(Long.class,
    		cb.count(mails.get("idStatusMail"))
		))
    	.where(cb.and(
			cb.equal(mails.get("destinatario"), emailUsuario),
			cb.equal(mails.get("idStatusMail"), status)
		));
        return em.createQuery(criteria).getSingleResult();
    }
}
