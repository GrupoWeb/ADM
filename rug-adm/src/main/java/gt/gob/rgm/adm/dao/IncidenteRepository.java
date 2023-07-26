package gt.gob.rgm.adm.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.domain.Incident;
import gt.gob.rgm.adm.model.Incidente;

@ApplicationScoped
public class IncidenteRepository {

	@PersistenceContext
	private EntityManager em;
	
	public List<Incidente> findAll(){		
		Query query = em.createNamedQuery("Incidente.findAll");
		return query.getResultList();
	}
	
	public List<Incidente> findWithFilter(Incident filtro, Integer page, Integer size, String fechaInicio, String fechaFin) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Incidente> criteria = cb.createQuery(Incidente.class);
		Root<Incidente> incidentes = criteria.from(Incidente.class);
		Predicate restrictions = null;
		if(filtro.getTexto() != null) {
    		restrictions = cb.or(cb.like(incidentes.get("usuarioSolicitante"), "%" + filtro.getTexto() + "%"),
    							 cb.like(incidentes.get("asunto"), "%" + filtro.getTexto() + "%"),
    							 cb.like(incidentes.get("descripcion"), "%" + filtro.getTexto() + "%"),
    							 cb.like(incidentes.get("resolucion"), "%" + filtro.getTexto() + "%"));
        }
		
		if(filtro.getEstado() != null) {
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.equal(incidentes.get("estado"),filtro.getEstado()));
			} else {
				restrictions = cb.equal(incidentes.get("estado"),filtro.getEstado());
			}
		}
		
		if(filtro.getImpacto() != null) {
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.equal(incidentes.get("impacto"),filtro.getImpacto()));
			} else {
				restrictions = cb.equal(incidentes.get("impacto"),filtro.getImpacto());
			}
		}
		
		////
		if(filtro.getTipoSolicitud() != null) {
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.equal(incidentes.get("tipoSolicitud"),filtro.getTipoSolicitud()));
			} else {
				restrictions = cb.equal(incidentes.get("tipoSolicitud"),filtro.getTipoSolicitud());
			}
		}
		
		if(filtro.getCategoria() != null) {
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.equal(incidentes.get("categoria"),filtro.getCategoria()));
			} else {
				restrictions = cb.equal(incidentes.get("categoria"),filtro.getCategoria());
			}
		}
		
		if(filtro.getModoIngreso() != null) {
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.equal(incidentes.get("modoIngreso"),filtro.getModoIngreso()));
			} else {
				restrictions = cb.equal(incidentes.get("modoIngreso"),filtro.getModoIngreso());
			}
		}
		
		if(filtro.getPrioridad() != null) {
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.equal(incidentes.get("prioridad"),filtro.getPrioridad()));
			} else {
				restrictions = cb.equal(incidentes.get("prioridad"),filtro.getPrioridad());
			}
		}		
		///
		
		if(fechaInicio != null) {			
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.between(incidentes.get("fechaCreacion"), fechaInicio, fechaFin));
			} else {
				restrictions = cb.between(incidentes.get("fechaCreacion"), fechaInicio, fechaFin);
			}
		}
		
		criteria = criteria.select(incidentes);
		if(restrictions != null) {
			criteria = criteria.where(restrictions);
		}
		TypedQuery<Incidente> query = em.createQuery(criteria);
		if(page != null) {
			query.setFirstResult((page - 1) * size);
			query.setMaxResults(size);
		}
		return query.getResultList();
	}
	
	public Long countWithFilter(Incident filtro, String fechaInicio, String fechaFin) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<Incidente> incidentes = criteria.from(Incidente.class);
		Predicate restrictions = null;
		if(filtro.getTexto() != null) {
    		restrictions = cb.or(cb.like(incidentes.get("usuarioSolicitante"), "%" + filtro.getTexto() + "%"),
    							 cb.like(incidentes.get("asunto"), "%" + filtro.getTexto() + "%"),
    							 cb.like(incidentes.get("descripcion"), "%" + filtro.getTexto() + "%"),
    							 cb.like(incidentes.get("resolucion"), "%" + filtro.getTexto() + "%"));
        }
		
		if(filtro.getEstado() != null) {
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.equal(incidentes.get("estado"),filtro.getEstado()));
			} else {
				restrictions = cb.equal(incidentes.get("estado"),filtro.getEstado());
			}
		}
		
		if(filtro.getImpacto() != null) {
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.equal(incidentes.get("impacto"),filtro.getImpacto()));
			} else {
				restrictions = cb.equal(incidentes.get("impacto"),filtro.getImpacto());
			}
		}
		
		////
		if(filtro.getTipoSolicitud() != null) {
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.equal(incidentes.get("tipoSolicitud"),filtro.getTipoSolicitud()));
			} else {
				restrictions = cb.equal(incidentes.get("tipoSolicitud"),filtro.getTipoSolicitud());
			}
		}
		
		if(filtro.getCategoria() != null) {
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.equal(incidentes.get("categoria"),filtro.getCategoria()));
			} else {
				restrictions = cb.equal(incidentes.get("categoria"),filtro.getCategoria());
			}
		}
		
		if(filtro.getModoIngreso() != null) {
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.equal(incidentes.get("modoIngreso"),filtro.getModoIngreso()));
			} else {
				restrictions = cb.equal(incidentes.get("modoIngreso"),filtro.getModoIngreso());
			}
		}
		
		if(filtro.getPrioridad() != null) {
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.equal(incidentes.get("prioridad"),filtro.getPrioridad()));
			} else {
				restrictions = cb.equal(incidentes.get("prioridad"),filtro.getPrioridad());
			}
		}		
		///
		
		if(fechaInicio != null) {			
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.between(incidentes.get("fechaCreacion"), fechaInicio, fechaFin));
			} else {
				restrictions = cb.between(incidentes.get("fechaCreacion"), fechaInicio, fechaFin);
			}
		}
		
		criteria = criteria.select(cb.construct(Long.class,
	    		cb.count(incidentes.get("incidenteId"))
			));
		if(restrictions != null) {
			criteria = criteria.where(restrictions);
		}
		
		return em.createQuery(criteria).getSingleResult();
	}
	
	public Incidente findById(Long id) {
		return em.find(Incidente.class, id);
	}
	
	public void save(Incidente incidente) {
		em.persist(incidente);
	}
	
	public void update(Incidente incidente) {
		em.merge(incidente);
	}
}
