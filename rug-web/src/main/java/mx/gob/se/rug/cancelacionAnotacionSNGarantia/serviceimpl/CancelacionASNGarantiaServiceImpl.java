package mx.gob.se.rug.cancelacionAnotacionSNGarantia.serviceimpl;

import mx.gob.se.rug.cancelacion.dao.CancelacionDAO;
import mx.gob.se.rug.cancelacionAnotacionSNGarantia.dao.CancelacionAnotacionSNGarantiaDAO;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;

/**
 * 
 * @author 
 * 
 */
public class CancelacionASNGarantiaServiceImpl {

	private CancelacionAnotacionSNGarantiaDAO cancelacionASGarantiaDAO = new CancelacionAnotacionSNGarantiaDAO();
	
	private GarantiasDAO garantiasDAO = new GarantiasDAO();
	
	public Boolean cancelar(Integer IdUsuario, Integer idTipoTram, Integer IdAnotacion, String autoInst,String Anotacion, Integer VigenMes) {
		boolean regresa = false;
		
		if (cancelacionASGarantiaDAO.altaCancelacion(IdUsuario, idTipoTram, IdAnotacion, autoInst, Anotacion, VigenMes).getIntPl() == 0 ? true : false) {
			System.out.println("pasa if de cancelacionASGarantiaDAO.altaCancelacion... ");
			if (garantiasDAO.altaBitacoraTramite(IdAnotacion, 5, 0, null, "V")) {
				System.out.println("pasa segundo IF de cancelacionASGarantiaDAO.altaCancelacion... ");	
				regresa = true;
			} 
		} 
		
		System.out.println("regresa WWWWWWW"+regresa);
		return regresa;

	}

//	public Integer creaTramiteTem(Integer IdUsuario, Integer idTramiteTemporal, Integer IdAnotacion, String Anotacion, Integer VigenciaAnotacion) {
//		int idTramite = new InscripcionDAO().insert(IdUsuario, idTramiteTemporal, IdAnotacion, Anotacion, VigenciaAnotacion);
//		return idTramite;
//	}

	public CancelacionAnotacionSNGarantiaDAO getCancelacionASGarantiaDAO() {
		return cancelacionASGarantiaDAO;
	}

	public void setCancelacionASGarantiaDAO(CancelacionAnotacionSNGarantiaDAO cancelacionASGarantiaDAO) {
		this.cancelacionASGarantiaDAO = cancelacionASGarantiaDAO;
	}
}
