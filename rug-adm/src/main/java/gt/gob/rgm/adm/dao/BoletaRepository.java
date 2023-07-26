package gt.gob.rgm.adm.dao;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.domain.Deposit;
import gt.gob.rgm.adm.model.Boleta;
import gt.gob.rgm.adm.model.BoletaSum;
import gt.gob.rgm.adm.model.GenericCount;

@ApplicationScoped
public class BoletaRepository {
	@PersistenceContext
    private EntityManager em;
	
	public Boleta findById(Long id) {
        return em.find(Boleta.class, id);
    }

    public void save(Boleta boleta) {
    	em.persist(boleta);
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
    
    public List<Boleta> findAllOrderedByFecha() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Boleta> criteria = cb.createQuery(Boleta.class);
        Root<Boleta> boletas = criteria.from(Boleta.class);
        criteria.select(boletas)
        	.orderBy(cb.asc(boletas.get("fechaHora")));
        return em.createQuery(criteria).getResultList();
    }
    
    public List<BoletaSum> sumMontoByTipoAndUsada() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BoletaSum> criteria = cb.createQuery(BoletaSum.class);
        Root<Boleta> boletas = criteria.from(Boleta.class);
        List<Expression<?>> groupByList = new ArrayList<>();
        groupByList.add(boletas.get("tipoPago"));
        groupByList.add(boletas.get("usada"));
        criteria.select(cb.construct(BoletaSum.class,
        	boletas.get("tipoPago"),
        	boletas.get("usada"),
        	cb.sum(boletas.get("monto"))
        	)
		)
        .groupBy(groupByList)
        .orderBy(cb.asc(boletas.get("tipoPago")), cb.asc(boletas.get("usada")));
        
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Boleta> findWithFilter(Deposit filter, Integer page, Integer size) {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Boleta> criteria = cb.createQuery(Boleta.class);
        Root<Boleta> boletas = criteria.from(Boleta.class);
        Predicate restrictions = null;
        if(filter.getUsada() != null) {
        	restrictions = cb.equal(boletas.get("usada"), filter.getUsada());
        }
        if(filter.getAgencia() != null) {
        	if(restrictions != null) {
        		restrictions = cb.and(restrictions,
    				cb.equal(boletas.get("agencia"), filter.getAgencia())
				);
        	} else {
        		restrictions = cb.equal(boletas.get("agencia"), filter.getAgencia());
        	}
        }
        if(filter.getNumero() != null) {
        	if(restrictions != null) {
        		restrictions = cb.and(restrictions,
    				cb.equal(boletas.get("numero"), filter.getNumero())
				);
        	} else {
        		restrictions = cb.equal(boletas.get("numero"), filter.getNumero());
        	}
        }
        if(filter.getExternalUser() != null) {
        	if(restrictions != null) {
        		restrictions = cb.and(restrictions,
    				cb.like(cb.upper(boletas.get("secuUser").get("personaFisica").get("nombrePersona")), "%" + filter.getExternalUser().getName().toUpperCase() + "%")
				);
        	} else {
        		restrictions = cb.like(cb.upper(boletas.get("secuUser").get("personaFisica").get("nombrePersona")), "%" + filter.getExternalUser().getName().toUpperCase() + "%");
        	}
        }
        if(filter.getTipoPago() != null) {
        	if(restrictions != null) {
        		restrictions = cb.and(restrictions,
    				cb.equal(boletas.get("tipoPago"), filter.getTipoPago())
				);
        	} else {
        		restrictions = cb.equal(boletas.get("tipoPago"), filter.getTipoPago());
        	}
        }
        
        criteria = criteria.select(boletas);
        if(restrictions != null) {
        	criteria = criteria.where(restrictions);
        }
        criteria = criteria.orderBy(cb.asc(boletas.get("fechaHora")));
        TypedQuery<Boleta> query = em.createQuery(criteria);
        if(page != null) {
        	query.setFirstResult((page - 1) * size);
        	query.setMaxResults(size);
        }
        return query.getResultList();
    }
    
    public Long countWithFilter(Deposit filter) {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Boleta> boletas = criteria.from(Boleta.class);
        Predicate restrictions = null;
        if(filter.getUsada() != null) {
        	restrictions = cb.equal(boletas.get("usada"), filter.getUsada());
        }
        if(filter.getAgencia() != null) {
        	if(restrictions != null) {
        		restrictions = cb.and(restrictions,
    				cb.equal(boletas.get("agencia"), filter.getAgencia())
				);
        	} else {
        		restrictions = cb.equal(boletas.get("agencia"), filter.getAgencia());
        	}
        }
        if(filter.getNumero() != null) {
        	if(restrictions != null) {
        		restrictions = cb.and(restrictions,
    				cb.equal(boletas.get("numero"), filter.getNumero())
				);
        	} else {
        		restrictions = cb.equal(boletas.get("numero"), filter.getNumero());
        	}
        }
        if(filter.getExternalUser() != null) {
        	if(restrictions != null) {
        		restrictions = cb.and(restrictions,
    				cb.like(cb.upper(boletas.get("secuUser").get("personaFisica").get("nombrePersona")), "%" + filter.getExternalUser().getName().toUpperCase() + "%")
				);
        	} else {
        		restrictions = cb.like(cb.upper(boletas.get("secuUser").get("personaFisica").get("nombrePersona")), "%" + filter.getExternalUser().getName().toUpperCase() + "%");
        	}
        }
        if(filter.getTipoPago() != null) {
        	if(restrictions != null) {
        		restrictions = cb.and(restrictions,
    				cb.equal(boletas.get("tipoPago"), filter.getTipoPago())
				);
        	} else {
        		restrictions = cb.equal(boletas.get("tipoPago"), filter.getTipoPago());
        	}
        }
        
        criteria = criteria.select(cb.construct(Long.class,
    		cb.count(boletas.get("id"))
		));
        if(restrictions != null) {
        	criteria = criteria.where(restrictions);
        }
        return em.createQuery(criteria).getSingleResult();
    }
    
    /**
     * @param sqlString
     * @return
     */
    public List<GenericCount> statsBoletas(String sqlString) {
    	Query query = em.createNativeQuery(sqlString, GenericCount.class);
    	
    	return query.getResultList();
    }
}
