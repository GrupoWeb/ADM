package gt.gob.rgm.adm.dao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.eclipse.persistence.config.QueryHints;

import gt.gob.rgm.adm.domain.Transaction;
import gt.gob.rgm.adm.model.GenericCount;
import gt.gob.rgm.adm.model.RugCatTipoTramite;
import gt.gob.rgm.adm.model.RugCertificaciones;
import gt.gob.rgm.adm.model.RugGarantias;
import gt.gob.rgm.adm.model.RugRelTramGaran;
import gt.gob.rgm.adm.model.RugRelTramPartes;
import gt.gob.rgm.adm.model.Tramites;
import javax.persistence.NoResultException;

@ApplicationScoped
public class RugGarantiasRepository {
	@PersistenceContext
	private EntityManager em;
	
	public RugGarantias findById(Long id) {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(QueryHints.CACHE_RETRIEVE_MODE, CacheRetrieveMode.BYPASS);
        return em.find(RugGarantias.class, id, props);
    }
	
	public RugGarantias findByIdRefresh(Long id) {
		Map<String, Object> props = new HashMap<>();
		props.put("javax.persistence.cache.storeMode", "REFRESH");
		return em.find(RugGarantias.class, id, props);
	}
	
	public List<GenericCount> countGarantiasByTramite(String fechaInicio, String fechaFin, Boolean migracion) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<GenericCount> criteria = cb.createQuery(GenericCount.class);
		Root<RugGarantias> garantias = criteria.from(RugGarantias.class);
		Predicate restrictions = null;
		
		List<Expression<?>> groupByList = new ArrayList<>();
		Path<Tramites> tramite = garantias.get("tramite");
		Path<RugCatTipoTramite> tipoTramite = tramite.get("tipoTramite");
		groupByList.add(tipoTramite.get("descripcion"));
		restrictions = cb.between(tramite.get("fechaCreacion"), fechaInicio, fechaFin);
		if(migracion == null || !migracion) {
			restrictions = cb.and(restrictions,
				cb.notEqual(tramite.get("bCargaMasiva"), -1)
			);
		}
		criteria.select(cb.construct(GenericCount.class,
			tipoTramite.get("descripcion"),
			cb.count(garantias.get("idGarantia"))
		))
		.where(restrictions)
		.groupBy(groupByList)
		.orderBy(cb.asc(tipoTramite.get("descripcion")));
		
		return em.createQuery(criteria).getResultList();
	}
	
	public List<GenericCount> countGarantiasByTramiteAndPerJuridica(String fechaInicio, String fechaFin) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<GenericCount> criteria = cb.createQuery(GenericCount.class);
		Root<RugGarantias> garantias = criteria.from(RugGarantias.class);

		List<Expression<?>> groupByList = new ArrayList<>();
		Path<Tramites> tramite = garantias.get("tramite");
		Path<RugCatTipoTramite> tipoTramite = tramite.get("tipoTramite");
		Path<RugRelTramPartes> partes = tramite.get("partes");
		groupByList.add(tipoTramite.get("descripcion"));
		groupByList.add(partes.get("perJuridica"));
		criteria.select(cb.construct(GenericCount.class,
			tipoTramite.get("descripcion"),
			partes.get("perJuridica"),
			cb.count(garantias.get("idGarantia"))
		))
		.where(cb.and(
			cb.notEqual(tramite.get("bCargaMasiva"), -1),
			cb.equal(partes.get("id").get("idParte"), 2)
		))
		.groupBy(groupByList)
		.orderBy(cb.asc(tipoTramite.get("descripcion")), cb.asc(partes.get("perJuridica")));
		
		return em.createQuery(criteria).getResultList();
	}
	
	public List<GenericCount> statsGarantias(String sqlString) {
		
		Query query = em.createNativeQuery(sqlString, GenericCount.class);
		
		return query.getResultList();
	}
        
        public String original(String sql){
            
            try{
                Query query = em.createNativeQuery(sql);
                String r  = query.getSingleResult().toString();
                return r;
            }
            catch(NoResultException e) {
                return "";
             }           
                         
        } 
	
	public List<Tramites> findWithFilter(Transaction filter, Integer page, Integer size, String fechaInicio, String fechaFin) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tramites> criteria = cb.createQuery(Tramites.class);
		Root<Tramites> tramites = criteria.from(Tramites.class);
		Predicate restrictions = null;
                
                System.out.println("Filter " + filter.getSolicitante() + " garante: " + filter.getGuarantee() + " restrictions: " + restrictions + " fechaInicio: " + fechaInicio + " fechaFin: " + fechaFin);
		if(filter.getSolicitante() != null) {
    		restrictions = cb.like(cb.upper(tramites.get("usuario").get("personaFisica").get("nombrePersona")), "%" + filter.getSolicitante().getName().toUpperCase() + "%");
        }
		if(filter.getGuarantee() != null) {
			ListJoin<Tramites, RugRelTramGaran> relGaran = tramites.joinList("relGarantia", JoinType.LEFT);
			ListJoin<Tramites, RugCertificaciones> certificaciones = tramites.joinList("certificacion", JoinType.LEFT);
        	if(restrictions != null) {
        		restrictions = cb.and(restrictions,
    				cb.or(
						cb.equal(cb.upper(relGaran.get("id").get("idGarantia")), filter.getGuarantee().getIdGarantia()),
						cb.equal(cb.upper(certificaciones.get("idGarantia")), filter.getGuarantee().getIdGarantia())
					)    				
				);
        	} else {
        		restrictions = cb.or(
					cb.equal(cb.upper(relGaran.get("id").get("idGarantia")), filter.getGuarantee().getIdGarantia()),
					cb.equal(cb.upper(certificaciones.get("idGarantia")), filter.getGuarantee().getIdGarantia())
				);
        	}
        }
		if(fechaInicio != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate dateFechaFin = LocalDate.parse(fechaFin, formatter);
			dateFechaFin = dateFechaFin.plusDays(1);
                        
                        LocalDate dateFechaInicio = LocalDate.parse(fechaInicio, formatter);
			dateFechaInicio = dateFechaInicio.plusDays(1);
                        
                        System.out.println("inicio " + formatter.format(dateFechaInicio));
                        System.out.println("fin " + formatter.format(dateFechaFin));
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.between(tramites.get("fechaCreacion"), fechaInicio, formatter.format(dateFechaFin)));
			} else {
				restrictions = cb.between(tramites.get("fechaCreacion"), fechaInicio, formatter.format(dateFechaFin));
			}
		}
                
		criteria = criteria.select(tramites);
		if(restrictions != null) {
			criteria = criteria.where(restrictions);
		}
		TypedQuery<Tramites> query = em.createQuery(criteria);
		if(page != null) {
			query.setFirstResult((page - 1) * size);
			query.setMaxResults(size);
		}
                
		query.setHint(QueryHints.CACHE_RETRIEVE_MODE, CacheRetrieveMode.BYPASS);
		return query.getResultList();
	}
	
	public Long countWithFilter(Transaction filter, String fechaInicio, String fechaFin) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Tramites> tramites = criteria.from(Tramites.class);
        Predicate restrictions = null;
		if(filter.getSolicitante() != null) {
    		restrictions = cb.like(cb.upper(tramites.get("usuario").get("personaFisica").get("nombrePersona")), "%" + filter.getSolicitante().getName().toUpperCase() + "%");
        }
		if(filter.getGuarantee() != null) {
			ListJoin<Tramites, RugRelTramGaran> relGaran = tramites.joinList("relGarantia", JoinType.LEFT);
			ListJoin<Tramites, RugCertificaciones> certificaciones = tramites.joinList("certificacion", JoinType.LEFT);
        	if(restrictions != null) {
        		restrictions = cb.and(restrictions,
    				cb.or(
						cb.equal(cb.upper(relGaran.get("id").get("idGarantia")), filter.getGuarantee().getIdGarantia()),
						cb.equal(cb.upper(certificaciones.get("idGarantia")), filter.getGuarantee().getIdGarantia())
					)    				
				);
        	} else {
        		restrictions = cb.or(
					cb.equal(cb.upper(relGaran.get("id").get("idGarantia")), filter.getGuarantee().getIdGarantia()),
					cb.equal(cb.upper(certificaciones.get("idGarantia")), filter.getGuarantee().getIdGarantia())
				);
        	}
        }
		if(fechaInicio != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate dateFechaFin = LocalDate.parse(fechaFin, formatter);
			dateFechaFin = dateFechaFin.plusDays(1);
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.between(tramites.get("fechaCreacion"), fechaInicio, formatter.format(dateFechaFin)));
			} else {
				restrictions = cb.between(tramites.get("fechaCreacion"), fechaInicio, formatter.format(dateFechaFin));
			}
		}
        criteria = criteria.select(cb.construct(Long.class,
    		cb.count(tramites.get("idTramite"))
		));
        if(restrictions != null) {
        	criteria = criteria.where(restrictions);
        }
        return em.createQuery(criteria).getSingleResult();
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
	
	public void save(RugGarantias garantia) {
		em.persist(garantia);
	}

	public List<RugGarantias> findByDescripcionAndStatus(String descripcion, String status) {
		TypedQuery<RugGarantias> query = em.createNamedQuery("RugGarantias.findByDescripcionAndNotStatus", RugGarantias.class);
		query.setParameter("descripcion", "%" + descripcion.toUpperCase() + "%");
		query.setParameter("status", status);
		return query.getResultList();
	}
}
