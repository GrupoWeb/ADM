package gt.gob.rgm.adm.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import gt.gob.rgm.adm.model.RugPersonasH;
import gt.gob.rgm.adm.solr.PersonaSolr;

@ApplicationScoped
public class RugPersonasHRepository {
	@PersistenceContext
	private EntityManager em;
	
	public List<PersonaSolr> findNative(String sqlString, Integer page, Integer size) {
		Query query = em.createNativeQuery(sqlString, PersonaSolr.class);
		if(page != null) {
			query.setFirstResult((page - 1) * size);
        	query.setMaxResults(size);
        }
    	
    	return query.getResultList();
	}
	
	public void save(RugPersonasH personaH) {
		em.persist(personaH);
	}
}
