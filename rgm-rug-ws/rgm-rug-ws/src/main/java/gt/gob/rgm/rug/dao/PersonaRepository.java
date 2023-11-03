package gt.gob.rgm.rug.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gt.gob.rgm.rug.model.RugPersonasFisicas;
import gt.gob.rgm.rug.model.VCodigoPersonas;

@ApplicationScoped
public class PersonaRepository {

	@PersistenceContext
    private EntityManager em;
	
	public VCodigoPersonas findByCodigoRegistro(String pCodigoRegistro) {
				
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VCodigoPersonas> criteria = cb.createQuery(VCodigoPersonas.class);
        Root<VCodigoPersonas> personas = criteria.from(VCodigoPersonas.class);               
        
        criteria.select(personas)
        	.where(cb.equal(personas.get("codigo"), pCodigoRegistro.toUpperCase()));
        try {
        	return em.createQuery(criteria).getSingleResult();
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
		
	public RugPersonasFisicas findById(Long pId) {
		try {
		 return em.find(RugPersonasFisicas.class, pId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
