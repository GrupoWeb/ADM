package gt.gob.rgm.adm.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.domain.SurveyStatsMulti;
import gt.gob.rgm.adm.model.RugRespuesta;

@ApplicationScoped
public class RugRespuestaRepository {

	@PersistenceContext
    private EntityManager em;
	
	public Long findTotalByParams(Long pIdPregunta, String pRespuesta, Date pFechaDesde, Date pFechaHasta) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<RugRespuesta> respuestas = criteria.from(RugRespuesta.class);
		Predicate restrictions = null;
		restrictions = cb.equal(respuestas.get("idPregunta"), pIdPregunta);
		restrictions = cb.and(restrictions,
				cb.equal(respuestas.get("respuesta"), pRespuesta));
		restrictions = cb.and(restrictions,
				cb.between(respuestas.get("fecha"), pFechaDesde, pFechaHasta));
		
		criteria = criteria.select(cb.construct(Long.class,
    		cb.count(respuestas.get("idRespuesta"))
		));
        if(restrictions != null) {
        	criteria = criteria.where(restrictions);
        }
        return em.createQuery(criteria).getSingleResult();
	}
	
	public List<SurveyStatsMulti> findGroupByParam(Long pIdPregunta, Date pFechaDesde, Date pFechaHasta) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SurveyStatsMulti> criteria = cb.createQuery(SurveyStatsMulti.class);
        
        Root<RugRespuesta> respuestas = criteria.from(RugRespuesta.class);
        List<Expression<?>> groupByList = new ArrayList<>();
        groupByList.add(respuestas.get("respuesta"));
        
        Predicate restrictions = null;
        restrictions = cb.equal(respuestas.get("idPregunta"), pIdPregunta);
        restrictions = cb.and(restrictions,
				cb.between(respuestas.get("fecha"), pFechaDesde, pFechaHasta));
		        
        criteria.select(cb.construct(SurveyStatsMulti.class,
        		respuestas.get("respuesta"),        
        		cb.count(respuestas.get("idRespuesta"))
        	)
		)
        .groupBy(groupByList);
        
        if(restrictions != null) {
        	criteria = criteria.where(restrictions);
        }
        
        return em.createQuery(criteria).getResultList();
	}
	
	public List<String> findRespuestaByParam(Long pIdPregunta, Date pFechaDesde, Date pFechaHasta) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<String> criteria = cb.createQuery(String.class);
        
        Root<RugRespuesta> respuestas = criteria.from(RugRespuesta.class);
        
        Predicate restrictions = null;
        restrictions = cb.equal(respuestas.get("idPregunta"), pIdPregunta);
        restrictions = cb.and(restrictions,
				cb.between(respuestas.get("fecha"), pFechaDesde, pFechaHasta));
		        
        criteria.select(cb.construct(String.class,
        		respuestas.get("respuesta")        		
        	)
		);
        
        if(restrictions != null) {
        	criteria = criteria.where(restrictions);
        }
        
        return em.createQuery(criteria).getResultList();
	}
}
