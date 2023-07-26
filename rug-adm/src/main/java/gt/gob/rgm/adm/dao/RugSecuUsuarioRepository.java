package gt.gob.rgm.adm.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.domain.ExternalUser;
import gt.gob.rgm.adm.model.GenericCount;
import gt.gob.rgm.adm.model.RugPersonas;
import gt.gob.rgm.adm.model.RugSecuUsuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class RugSecuUsuarioRepository {

	@PersistenceContext
    private EntityManager em;

    public RugSecuUsuario findById(Long id) {
        return em.find(RugSecuUsuario.class, id);
    }
    
    public RugSecuUsuario findByIdRefresh(Long id) {
    	Map<String, Object> props = new HashMap<>();
		props.put("javax.persistence.cache.storeMode", "REFRESH");
		return em.find(RugSecuUsuario.class, id);
    }
    
    public void save(RugSecuUsuario usuario) {
    	em.persist(usuario);
    }

    public List<RugSecuUsuario> findByStatusOrderedByFecha(String status, Integer page, Integer size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RugSecuUsuario> criteria = cb.createQuery(RugSecuUsuario.class);
        Root<RugSecuUsuario> usuarios = criteria.from(RugSecuUsuario.class);
        criteria.select(usuarios)
        	.where(cb.equal(usuarios.get("sitUsuario"), status))
        	.orderBy(cb.asc(usuarios.get("fhRegistro")));
        TypedQuery<RugSecuUsuario> query = em.createQuery(criteria);
        if(page != null) {
        	query.setFirstResult((page - 1) * size);
        	query.setMaxResults(size);
        }
        return query.getResultList();
    }
    
    public List<RugSecuUsuario> findAll(Integer page, Integer size) {
    	TypedQuery<RugSecuUsuario> query = em.createNamedQuery("RugSecuUsuario.findAll", RugSecuUsuario.class);
    	if(page != null) {
        	query.setFirstResult((page - 1) * size);
        	query.setMaxResults(size);
        }
    	return query.getResultList();
    }
    
    public List<RugSecuUsuario> findNotMigracion(Integer page, Integer size) {
    	TypedQuery<RugSecuUsuario> query = em.createNamedQuery("RugSecuUsuario.findNotMigracion", RugSecuUsuario.class);
    	query.setParameter("email", "%@correo%");
    	query.setParameter("institucion", "RGM");
    	if(page != null) {
        	query.setFirstResult((page - 1) * size);
        	query.setMaxResults(size);
        }
    	return query.getResultList();
    }
    
    public List<GenericCount> summaryUsuariosByStatus(String fechaInicio, String fechaFin, Boolean migracion) {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<GenericCount> criteria = cb.createQuery(GenericCount.class);
    	Root<RugSecuUsuario> usuarios = criteria.from(RugSecuUsuario.class);
    	
    	List<Expression<?>> groupByList = new ArrayList<>();
    	groupByList.add(usuarios.get("sitUsuario"));
    	criteria.select(cb.construct(GenericCount.class,
			usuarios.get("sitUsuario"),
			cb.count(usuarios.get("idPersona"))
		))
    	.where(cb.and(
			cb.notLike(usuarios.get("cveUsuario"), "%@correo%"),
			cb.notEqual(usuarios.get("cveInstitucion"), "RGM")
		))
    	.groupBy(groupByList)
    	.orderBy(cb.asc(usuarios.get("sitUsuario")));
    	
    	return em.createQuery(criteria).getResultList();
    }
    
    public Long countByStatus(String status) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<RugSecuUsuario> usuarios = criteria.from(RugSecuUsuario.class);
        criteria.select(cb.construct(Long.class,
    		cb.count(usuarios.get("idPersona"))
		))
    	.where(cb.equal(usuarios.get("sitUsuario"), status))
    	.orderBy(cb.asc(usuarios.get("fhRegistro")));
        return em.createQuery(criteria).getSingleResult();
    }
    
    public Long countAll() {
    	TypedQuery<Long> query = em.createNamedQuery("RugSecuUsuario.countAll", Long.class);
    	return query.getSingleResult();
    }
    
    public Long countNotMigracion() {
    	TypedQuery<Long> query = em.createNamedQuery("RugSecuUsuario.countNotMigracion", Long.class);
    	query.setParameter("email", "%@correo%");
    	query.setParameter("institucion", "RGM");
    	return query.getSingleResult();
    }
    
    public List<RugSecuUsuario> findWithFilter(ExternalUser filter, Integer page, Integer size) {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RugSecuUsuario> criteria = cb.createQuery(RugSecuUsuario.class);
        Root<RugSecuUsuario> usuarios = criteria.from(RugSecuUsuario.class);
        Predicate restrictions = null;
        if(filter.getStatus() != null) {
        	restrictions = cb.equal(usuarios.get("sitUsuario"), filter.getStatus());
        }
        if(filter.getName() != null) {
        	if(restrictions != null) {
        		restrictions = cb.and(restrictions,
					cb.like(cb.upper(usuarios.get("personaFisica").get("nombrePersona")), "%" + filter.getName().toUpperCase() + "%")
				);
        	} else {
        		restrictions = cb.like(cb.upper(usuarios.get("personaFisica").get("nombrePersona")), "%" + filter.getName().toUpperCase() + "%");
        	}
        }
        if(filter.getEmail() != null) {
        	if(restrictions != null) {
        		restrictions = cb.and(restrictions,
					cb.like(cb.lower(usuarios.get("cveUsuario")), "%" + filter.getEmail().toLowerCase() + "%")
				);
        	} else {
        		restrictions = cb.like(cb.lower(usuarios.get("cveUsuario")), "%" + filter.getEmail().toLowerCase() + "%");
        	}
        }
        Path<RugPersonas> persona = null;
        if(filter.getDocId() != null) {
    		persona = usuarios.get("persona");
        	if(restrictions != null) {
        		restrictions = cb.and(restrictions,
					cb.like(persona.get("curpDoc"), "%" + filter.getDocId() + "%")
				);
        	} else {
        		restrictions = cb.like(persona.get("curpDoc"), "%" + filter.getDocId() + "%");
        	}
        }
        if(filter.getNit() != null) {
        	if(persona == null) {
        		persona = usuarios.get("persona");
        	}
        	if(restrictions != null) {
        		restrictions = cb.and(restrictions,
    				cb.like(persona.get("rfc"), "%" + filter.getNit() + "%")
				);
        	} else {
        		restrictions = cb.like(persona.get("rfc"), "%" + filter.getNit() + "%");
        	}
        }
        if(filter.getMigration() != null) {
        	if(!filter.getMigration()) {
        		
        	}
        }
        
        criteria = criteria.select(usuarios);
        if(restrictions != null) {
        	criteria = criteria.where(restrictions);
        }
    	criteria = criteria.orderBy(cb.asc(usuarios.get("fhRegistro")));
        TypedQuery<RugSecuUsuario> query = em.createQuery(criteria);
        if(page != null) {
        	query.setFirstResult((page - 1) * size);
        	query.setMaxResults(size);
        }
        return query.getResultList();
    }
    
    public Long countWithFilter(ExternalUser filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<RugSecuUsuario> usuarios = criteria.from(RugSecuUsuario.class);
        Predicate restrictions = null;
        
        if(filter.getStatus() != null) {
        	restrictions = cb.equal(usuarios.get("sitUsuario"), filter.getStatus());
        }
        if(filter.getName() != null) {
        	if(restrictions != null) {
        		restrictions = cb.and(restrictions,
					cb.like(cb.upper(usuarios.get("personaFisica").get("nombrePersona")), "%" + filter.getName().toUpperCase() + "%")
				);
        	} else {
        		restrictions = cb.like(cb.upper(usuarios.get("personaFisica").get("nombrePersona")), "%" + filter.getName().toUpperCase() + "%");
        	}
        }
        if(filter.getEmail() != null) {
        	if(restrictions != null) {
        		restrictions = cb.and(restrictions,
					cb.like(cb.lower(usuarios.get("cveUsuario")), "%" + filter.getEmail().toLowerCase() + "%")
				);
        	} else {
        		restrictions = cb.like(cb.lower(usuarios.get("cveUsuario")), "%" + filter.getEmail().toLowerCase() + "%");
        	}
        }
        Path<RugPersonas> persona = null;
        if(filter.getDocId() != null) {
    		persona = usuarios.get("persona");
        	if(restrictions != null) {
        		restrictions = cb.and(restrictions,
					cb.like(persona.get("curpDoc"), "%" + filter.getDocId() + "%")
				);
        	} else {
        		restrictions = cb.like(persona.get("curpDoc"), "%" + filter.getDocId() + "%");
        	}
        }
        if(filter.getNit() != null) {
        	if(persona == null) {
        		persona = usuarios.get("persona");
        	}
        	if(restrictions != null) {
        		restrictions = cb.and(restrictions,
    				cb.like(persona.get("rfc"), "%" + filter.getNit() + "%")
				);
        	} else {
        		restrictions = cb.like(persona.get("rfc"), "%" + filter.getNit() + "%");
        	}
        }
        if(filter.getCorreosError().longValue() == 1) {
        	if(restrictions != null) {
        		restrictions = cb.and(restrictions,
        			cb.or(
    					cb.equal(cb.lower(usuarios.get("correos").get("idStatusMail")), 1),
    					cb.equal(cb.lower(usuarios.get("correos").get("idStatusMail")), 3)
					)				
				);
        	} else {
        		restrictions = cb.or(
    				cb.equal(cb.lower(usuarios.get("correos").get("idStatusMail")), 1),
    				cb.equal(cb.lower(usuarios.get("correos").get("idStatusMail")), 3)
				);
        	}
        }
        
        criteria = criteria.select(cb.construct(Long.class,
        		cb.count(usuarios.get("idPersona"))
		));
        if(restrictions != null) {
        	criteria = criteria.where(restrictions);
        }
        
        return em.createQuery(criteria).getSingleResult();
    }
    
    public List<GenericCount> statsUsuarios(String sqlString) {
    	Query query = em.createNativeQuery(sqlString, GenericCount.class);
    	
    	return query.getResultList();
    }
    
    public List findNative(String sqlString, Integer page, Integer size, Class resultingClass) {
    	Query query;
    	if(resultingClass != null) {
    		query = em.createNativeQuery(sqlString, resultingClass);
    	} else {
    		query = em.createNativeQuery(sqlString);
    	}
    	
    	if(page != null) {
        	query.setFirstResult((page - 1) * size);
        	query.setMaxResults(size);
        }
    	
    	return query.getResultList();
    }
    
    public GenericCount countNative(String sqlString) {
    	Query query = em.createNativeQuery(sqlString, GenericCount.class);
    	
    	return (GenericCount) query.getSingleResult();
    }
    
    public void callProcedure(String spName, Map<Integer, Object> params) {
		StoredProcedureQuery spQuery = em.createStoredProcedureQuery(spName);
		if(params != null) {
			for(Integer key : params.keySet()) {
				spQuery.registerStoredProcedureParameter(key, params.get(key).getClass(), ParameterMode.IN);
				spQuery.setParameter(key, params.get(key));
			}
		}
		spQuery.execute();
	}
}
