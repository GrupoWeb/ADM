package gt.gob.rgm.adm.dao;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import gt.gob.rgm.adm.model.VTramitesMailVigencia;

@ApplicationScoped
public class VTramitesMailVigenciaRepository {

	@PersistenceContext
    private EntityManager em;
	
	public List<VTramitesMailVigencia> getTramitesVigencia() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<VTramitesMailVigencia> criteria = cb.createQuery(VTramitesMailVigencia.class);
        return em.createQuery(criteria).getResultList();
	}
	
	public void callCaducidadTramites() {
		StoredProcedureQuery spQuery = em.createStoredProcedureQuery("SP_CADUCIDAD_TRAMITES");
		spQuery.registerStoredProcedureParameter("psResult", Integer.class, ParameterMode.OUT);
		spQuery.registerStoredProcedureParameter("psTxResult", String.class, ParameterMode.OUT);		
		spQuery.execute();
		
		Integer psResult = (Integer) spQuery.getOutputParameterValue("psResult");
		String psTxResult = (String) spQuery.getOutputParameterValue("psTxResult");
		
		System.out.println("-->"+psResult+":"+psTxResult);
	}
	
	public void callCaducidadTramitesInc() {
		StoredProcedureQuery spQuery = em.createStoredProcedureQuery("SP_CADUCIDAD_TRAMITES_INC");
		spQuery.registerStoredProcedureParameter("psResult", Integer.class, ParameterMode.OUT);
		spQuery.registerStoredProcedureParameter("psTxResult", String.class, ParameterMode.OUT);		
		spQuery.execute();
		
		Integer psResult = (Integer) spQuery.getOutputParameterValue("psResult");
		String psTxResult = (String) spQuery.getOutputParameterValue("psTxResult");
		
		System.out.println("-->"+psResult+":"+psTxResult);
	}
}
