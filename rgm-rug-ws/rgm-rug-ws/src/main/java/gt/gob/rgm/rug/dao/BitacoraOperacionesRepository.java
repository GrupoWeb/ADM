package gt.gob.rgm.rug.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import gt.gob.rgm.rug.model.BitacoraOperaciones;

@ApplicationScoped
public class BitacoraOperacionesRepository {

	@PersistenceContext
    private EntityManager em;

	@Transactional(value=TxType.REQUIRED)
    public void save(BitacoraOperaciones bitacora) {
    	em.persist(bitacora);
    }
}
