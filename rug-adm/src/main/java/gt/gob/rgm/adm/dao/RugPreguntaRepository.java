package gt.gob.rgm.adm.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import gt.gob.rgm.adm.model.RugPregunta;

@ApplicationScoped
public class RugPreguntaRepository {

	@PersistenceContext
    private EntityManager em;
	
	 public void save(RugPregunta pregunta) {
		 em.persist(pregunta);
	 }
}
