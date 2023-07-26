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

import gt.gob.rgm.adm.model.RugConsultaRegistro;

@ApplicationScoped
public class RugConsultaRegistroRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public List<RugConsultaRegistro> findConsultas(String solicitante, String fechaInicio, String fechaFin, Integer page, Integer size) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RugConsultaRegistro> criteria = cb.createQuery(RugConsultaRegistro.class);
		Root<RugConsultaRegistro> consultas = criteria.from(RugConsultaRegistro.class);
		Predicate restrictions = null;
		if(solicitante != null) {
    		restrictions = cb.like(cb.upper(consultas.get("usuario").get("personaFisica").get("nombrePersona")), "%" + solicitante.toUpperCase() + "%");
        } else {
        	restrictions = cb.equal(consultas.get("usuario").get("personaFisica").get("idPersona"), consultas.get("idPersona"));
        }
		if(fechaInicio != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate dateFechaFin = LocalDate.parse(fechaFin, formatter);
			dateFechaFin = dateFechaFin.plusDays(1);
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.between(consultas.get("fechahora"), fechaInicio, formatter.format(dateFechaFin)));
			} else {
				restrictions = cb.between(consultas.get("fechahora"), fechaInicio, formatter.format(dateFechaFin));
			}
		}
		criteria = criteria.select(consultas);
		if(restrictions != null) {
			criteria = criteria.where(restrictions);
		}
		TypedQuery<RugConsultaRegistro> query = em.createQuery(criteria);
		if(page != null) {
			query.setFirstResult((page - 1) * size);
			query.setMaxResults(size);
		}
		return query.getResultList();
	}
	
	public Long countConsultas(String solicitante, String fechaInicio, String fechaFin) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<RugConsultaRegistro> consultas = criteria.from(RugConsultaRegistro.class);
		Predicate restrictions = null;
		if(solicitante != null) {
    		restrictions = cb.like(cb.upper(consultas.get("usuario").get("personaFisica").get("nombrePersona")), "%" + solicitante.toUpperCase() + "%");
		} else {
        	restrictions = cb.equal(consultas.get("usuario").get("personaFisica").get("idPersona"), consultas.get("idPersona"));
    	}
		if(fechaInicio != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate dateFechaFin = LocalDate.parse(fechaFin, formatter);
			dateFechaFin = dateFechaFin.plusDays(1);
			if(restrictions != null) {
				restrictions = cb.and(restrictions,
					cb.between(consultas.get("fechahora"), fechaInicio, formatter.format(dateFechaFin)));
			} else {
				restrictions = cb.between(consultas.get("fechahora"), fechaInicio, formatter.format(dateFechaFin));
			}
		}
		criteria = criteria.select(cb.construct(Long.class,
    		cb.count(consultas.get("idConsultaReg"))
		));
		if(restrictions != null) {
        	criteria = criteria.where(restrictions);
        }
		return em.createQuery(criteria).getSingleResult();
	}
}
