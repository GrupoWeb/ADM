package gt.gob.rgm.adm.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.model.VDetalleBoletaPartes;

@ApplicationScoped
public class VDetalleBoletaPartesRepository {

	@PersistenceContext
    private EntityManager em;
	
	public List<VDetalleBoletaPartes> findByTramite(Long idTramite, Long idGarantia, Long idParte) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VDetalleBoletaPartes> criteria = cb.createQuery(VDetalleBoletaPartes.class);
        Root<VDetalleBoletaPartes> detalle = criteria.from(VDetalleBoletaPartes.class);
        criteria.select(detalle)
        	.where(cb.and(
    			cb.equal(detalle.get("idTramite"), idTramite),
    			cb.equal(detalle.get("idParte"), idParte)
			));
        List<VDetalleBoletaPartes> listaDetalle = em.createQuery(criteria).getResultList();
        return listaDetalle;
	}
}
