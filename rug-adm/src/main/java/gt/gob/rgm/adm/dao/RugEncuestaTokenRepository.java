package gt.gob.rgm.adm.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import gt.gob.rgm.adm.model.RugEncuestaToken;

@ApplicationScoped
public class RugEncuestaTokenRepository {

	@PersistenceContext
    private EntityManager em;
	
	public void save(RugEncuestaToken encuesta) {
    	em.persist(encuesta);
    }
}
