package gt.gob.rgm.adm.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import gt.gob.rgm.adm.model.RugPersonas;

import java.math.BigDecimal;

@ApplicationScoped
public class RugPersonasRepository {

	@PersistenceContext
    private EntityManager em;

	public RugPersonas findById(Long id) {
        return em.find(RugPersonas.class, id);
    }
	
	public void save(RugPersonas persona) {
		em.persist(persona);
	}
	
	public long getNextRegistry(String perJuridica) {
		String sequenceName = perJuridica.equals("PF") ? "SEQ_REG_PERSONAS_INDIVIDUALES" : "SEQ_REG_PERSONAS_JURIDICAS";
		Query q = em.createNativeQuery("SELECT " + sequenceName + ".nextval from DUAL");
		BigDecimal result=(BigDecimal)q.getSingleResult();   
		return result.longValue();
	}
}
