package gt.gob.rgm.adm.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import gt.gob.rgm.adm.model.BitacoraOperaciones;

@ApplicationScoped
public class BitacoraOperacionesRepository {

	@PersistenceContext
    private EntityManager em;

    public void save(BitacoraOperaciones bitacora) {
    	em.persist(bitacora);
    }
}
