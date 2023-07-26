package gt.gob.rgm.adm.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import gt.gob.rgm.adm.model.RechazoCuenta;

@ApplicationScoped
public class RechazoCuentaRepository {

	@PersistenceContext
    private EntityManager em;

    public void save(RechazoCuenta rechazo) {
    	em.persist(rechazo);
    }
}
