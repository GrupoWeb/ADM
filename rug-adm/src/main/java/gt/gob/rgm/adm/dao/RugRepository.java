package gt.gob.rgm.adm.dao;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class RugRepository {
	@PersistenceContext
	EntityManager em;
	
	public long getNextRegistry(String sequenceName) {
		Query q = em.createNativeQuery("SELECT " + sequenceName + ".nextval from DUAL");
		BigDecimal result=(BigDecimal)q.getSingleResult();   
		return result.longValue();
	}
}
