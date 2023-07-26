package mx.gob.se.rug.cancelacion.serviceimpl;

import mx.gob.se.rug.cancelacion.dao.CancelacionDAO;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;

/**
 * 
 * @author Abraham Stalin
 * 
 */
public class CancelacionServiceImpl {

	private CancelacionDAO cancelacionDAO = new CancelacionDAO();
	private GarantiasDAO garantiasDAO = new GarantiasDAO();

	public Boolean cancelar(Integer idTramite, Integer idGarantia, String observaciones) {
		boolean regresa = false;
		if (cancelacionDAO.altaCancelacion(idGarantia, idTramite, observaciones).getIntPl() == 0 ? true
				: false) {
			if (garantiasDAO.altaBitacoraTramite(idTramite, 5, 0, null, "V")) {
				regresa = true;
			} 
		} 
		return regresa;

	}

	public Integer creaTramiteTem(Integer idPersona, Integer idTipoTramite) {
		int idTramite = new InscripcionDAO().insert(idPersona, idTipoTramite);
		return idTramite;
	}
}
