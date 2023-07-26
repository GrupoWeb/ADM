package gt.gob.rgm.adm.dao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.model.Homologado;

@ApplicationScoped
public class HomologadoRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public List<Homologado> findVinculaciones(String solicitante, String fechaInicio, String fechaFin, Integer page, Integer size,String garantia) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Homologado> criteria = cb.createQuery(Homologado.class);
		Root<Homologado> vinculaciones = criteria.from(Homologado.class);
		Predicate restrictions = null;
		/*if(solicitante != null) {
    		restrictions = cb.like(cb.upper(consultas.get("usuario").get("personaFisica").get("nombrePersona")), "%" + solicitante.toUpperCase() + "%");
        } else {
        	restrictions = cb.equal(consultas.get("usuario").get("personaFisica").get("idPersona"), consultas.get("idPersona"));
        }*/
		restrictions = cb.isNotNull(vinculaciones.get("idGarantia"));
		if(fechaInicio != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate dateFechaFin = LocalDate.parse(fechaFin, formatter);
			dateFechaFin = dateFechaFin.plusDays(1);
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.between(vinculaciones.get("fecha"), fechaInicio, formatter.format(dateFechaFin)));
			} else {
				restrictions = cb.between(vinculaciones.get("fecha"), fechaInicio, formatter.format(dateFechaFin));
			}
		}
                
                if (garantia!=null)
                {
                    if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.equal(vinculaciones.get("idGarantia"), garantia));
			} else {
				restrictions = cb.equal(vinculaciones.get("idGarantia"), garantia);
			}
                }
                
		criteria = criteria.select(vinculaciones);
		if(restrictions != null) {
			criteria = criteria.where(restrictions);
		}
		criteria.orderBy(cb.asc(vinculaciones.get("fecha")));
		TypedQuery<Homologado> query = em.createQuery(criteria);
		if(page != null) {
			query.setFirstResult((page - 1) * size);
			query.setMaxResults(size);
		}
		return query.getResultList();
	}
	
	public Long countVinculaciones(String solicitante, String fechaInicio, String fechaFin,String garantia) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<Homologado> vinculaciones = criteria.from(Homologado.class);
		Predicate restrictions = null;
		/*if(solicitante != null) {
    		restrictions = cb.like(cb.upper(consultas.get("usuario").get("personaFisica").get("nombrePersona")), "%" + solicitante.toUpperCase() + "%");
		} else {
        	restrictions = cb.equal(consultas.get("usuario").get("personaFisica").get("idPersona"), consultas.get("idPersona"));
    	}*/
		restrictions = cb.isNotNull(vinculaciones.get("idGarantia"));
		if(fechaInicio != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate dateFechaFin = LocalDate.parse(fechaFin, formatter);
			dateFechaFin = dateFechaFin.plusDays(1);
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.between(vinculaciones.get("fecha"), fechaInicio, formatter.format(dateFechaFin)));
			} else {
				restrictions = cb.between(vinculaciones.get("fecha"), fechaInicio, formatter.format(dateFechaFin));
			}
		}
                 if (garantia!=null)
                {
                    if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.equal(vinculaciones.get("idGarantia"), garantia));
			} else {
				restrictions = cb.equal(vinculaciones.get("idGarantia"), garantia);
			}
                }
		criteria = criteria.select(cb.construct(Long.class,
    		cb.count(vinculaciones.get("homologadoId"))
		));
		if(restrictions != null) {
        	criteria = criteria.where(restrictions);
        }
		return em.createQuery(criteria).getSingleResult();
	}
}
