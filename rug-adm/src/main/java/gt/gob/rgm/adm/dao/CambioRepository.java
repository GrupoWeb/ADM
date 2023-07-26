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

import gt.gob.rgm.adm.domain.Change;
import gt.gob.rgm.adm.model.Cambio;

@ApplicationScoped
public class CambioRepository {

	@PersistenceContext
	private EntityManager em;
	
	public List<Cambio> findAll(){		
		Query query = em.createNamedQuery("Cambio.findAll");
		return query.getResultList();
	}
	
	public List<Cambio> findWithFilter(Change filtro, Integer page, Integer size, String fechaInicio, String fechaFin) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Cambio> criteria = cb.createQuery(Cambio.class);
		Root<Cambio> cambios = criteria.from(Cambio.class);
		Predicate restrictions = null;
		if(filtro.getTexto() != null) {
    		restrictions = cb.or(cb.like(cambios.get("usuarioSolicitante"), "%" + filtro.getTexto() + "%"),
    							 cb.like(cambios.get("sistema"), "%" + filtro.getTexto() + "%"),
    							 cb.like(cambios.get("descripcion"), "%" + filtro.getTexto() + "%"),
    							 cb.like(cambios.get("observaciones"), "%" + filtro.getTexto() + "%"),
    							 cb.like(cambios.get("version"), "%" + filtro.getTexto() + "%"));
        }
		
		if(filtro.getEstado() != null) {
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.equal(cambios.get("estado"),filtro.getEstado()));
			} else {
				restrictions = cb.equal(cambios.get("estado"),filtro.getEstado());
			}
		}
		
		if(filtro.getImpacto() != null) {
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.equal(cambios.get("impacto"),filtro.getImpacto()));
			} else {
				restrictions = cb.equal(cambios.get("impacto"),filtro.getImpacto());
			}
		}
		
		if(fechaInicio != null) {			
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.between(cambios.get("fechaRegistro"), fechaInicio, fechaFin));
			} else {
				restrictions = cb.between(cambios.get("fechaRegistro"), fechaInicio, fechaFin);
			}
		}
		criteria = criteria.select(cambios);
		if(restrictions != null) {
			criteria = criteria.where(restrictions);
		}
		TypedQuery<Cambio> query = em.createQuery(criteria);
		if(page != null) {
			query.setFirstResult((page - 1) * size);
			query.setMaxResults(size);
		}
		return query.getResultList();
	}
	
	public Long countWithFilter(Change filtro, String fechaInicio, String fechaFin) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<Cambio> cambios = criteria.from(Cambio.class);
		Predicate restrictions = null;
		if(filtro.getTexto() != null) {
    		restrictions = cb.or(cb.like(cambios.get("usuarioSolicitante"), "%" + filtro.getTexto() + "%"),
    							 cb.like(cambios.get("sistema"), "%" + filtro.getTexto() + "%"),
    							 cb.like(cambios.get("descripcion"), "%" + filtro.getTexto() + "%"),
    							 cb.like(cambios.get("observaciones"), "%" + filtro.getTexto() + "%"),
    							 cb.like(cambios.get("version"), "%" + filtro.getTexto() + "%"));
        }
		
		if(filtro.getEstado() != null) {
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.equal(cambios.get("estado"),filtro.getEstado()));
			} else {
				restrictions = cb.equal(cambios.get("estado"),filtro.getEstado());
			}
		}
		
		if(filtro.getImpacto() != null) {
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.equal(cambios.get("impacto"),filtro.getImpacto()));
			} else {
				restrictions = cb.equal(cambios.get("impacto"),filtro.getImpacto());
			}
		}
		
		if(fechaInicio != null) {			
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.between(cambios.get("fechaRegistro"), fechaInicio, fechaFin));
			} else {
				restrictions = cb.between(cambios.get("fechaRegistro"), fechaInicio, fechaFin);
			}
		}
		criteria = criteria.select(cb.construct(Long.class,
	    		cb.count(cambios.get("cambioId"))
			));
		if(restrictions != null) {
			criteria = criteria.where(restrictions);
		}
		
		return em.createQuery(criteria).getSingleResult();
	}
	
	public Cambio findById(Long id) {
		return em.find(Cambio.class, id);
	}
	
	public void save(Cambio cambio) {
		em.persist(cambio);
	}
	
	public void update(Cambio cambio) {
		em.merge(cambio);
	}
}
