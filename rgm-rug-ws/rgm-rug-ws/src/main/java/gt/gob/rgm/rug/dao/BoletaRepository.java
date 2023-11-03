package gt.gob.rgm.rug.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import gt.gob.rgm.rug.model.Boleta;

@ApplicationScoped
public class BoletaRepository {
	@PersistenceContext
    private EntityManager em;
	
	public Boleta findById(Long id) {
        return em.find(Boleta.class, id);
    }

	@Transactional(value=TxType.REQUIRED)
    public void save(Boleta boleta) {
    	em.persist(boleta); 
    }
	
	@Transactional(value=TxType.REQUIRED)
	public void update(Boleta boleta) {
		em.merge(boleta);
	}
	
	public Double findSaldo(String pIdUsuario) {
		Query query = em.createNamedQuery("Boleta.getSaldo")
						.setParameter("persona", pIdUsuario)
						.setParameter("usada", 1);
		
		BigDecimal result = (BigDecimal) query.getSingleResult(); 
	    
		if(result==null) return 0.0D;
		else 
			return result.doubleValue(); 
	}
	
    public List<Boleta> findByStatusOrderedByFecha(Integer status) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Boleta> criteria = cb.createQuery(Boleta.class);
        Root<Boleta> boletas = criteria.from(Boleta.class);
        criteria.select(boletas)
        	.where(cb.equal(boletas.get("usada"), status))
        	.orderBy(cb.asc(boletas.get("fechaHora")));
        return em.createQuery(criteria).getResultList();
    }
    
    public Boleta findByIdActivas(Long pId, Integer modo) {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Boleta> criteria = cb.createQuery(Boleta.class);
        Root<Boleta> boletas = criteria.from(Boleta.class);
        
        String campo = "numero";
        if(modo==1) campo = "idTransaccion";
        
        Predicate hasId = cb.equal(boletas.get(campo), pId);
        Predicate has0 = cb.equal(boletas.get("usada"), 0);
        Predicate has1 = cb.equal(boletas.get("usada"), 1);
        Predicate isActive = cb.or(has0, has1);
        
        criteria.select(boletas)
        	.where(cb.and(hasId,isActive));
        try {
        	
        	if(em.createQuery(criteria).getResultList().isEmpty()) {
        		return null;
        	} else {
        		return em.createQuery(criteria).getResultList().get(0);
        	}
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
    
    public Boleta findByBoleta(Boleta pBoleta) {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Boleta> criteria = cb.createQuery(Boleta.class);
        Root<Boleta> boletas = criteria.from(Boleta.class);
        Predicate hasSerie = null;
        Predicate hasTran = cb.equal(boletas.get("idTransaccion"), pBoleta.getIdTransaccion());
        if(pBoleta.getSerie()==null || pBoleta.getSerie().equalsIgnoreCase("")) {
        	hasSerie = cb.isNull(boletas.get("serie"));
        } else {
        	hasSerie = cb.equal(boletas.get("serie"), pBoleta.getSerie());
        }
        Predicate hasNumber = cb.equal(boletas.get("numero"), pBoleta.getNumero());
        Predicate hasMonto = cb.equal(boletas.get("monto"), pBoleta.getMonto());
        Predicate hasActivo = cb.or( 
        			cb.equal(boletas.get("usada"), 0),
        			cb.equal(boletas.get("usada"), 1)
        			);
                
        criteria.select(boletas)
        	.where(cb.and(hasTran,hasSerie,hasNumber,hasMonto,hasActivo));
        try {
        	
        	Boleta boletaFinal = em.createQuery(criteria).getSingleResult(); 
        	
        	if(boletaFinal!=null) {
        		return boletaFinal;
        	} else {
        		return null;
        	}
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
}
