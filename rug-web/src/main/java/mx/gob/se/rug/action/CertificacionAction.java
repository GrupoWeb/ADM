package mx.gob.se.rug.action;

import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.boleta.dao.BoletaDAO;
import mx.gob.se.rug.certificacion.dao.CertificacionDAO;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.detallegarantia.service.impl.DetalleServiceImpl;
import mx.gob.se.rug.detallegarantia.to.DetalleTO;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;
import mx.gob.se.rug.inscripcion.service.impl.InscripcionServiceImpl;
import mx.gob.se.rug.pago.dao.PagoDAO;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

public class CertificacionAction extends RugBaseAction {
	private static final long serialVersionUID = 1L;
	private Integer idTramite;
	private Integer idGarantia;
	private Integer idTipoTramite;
	private List<DetalleTO> tramiteTOs;
	private static final String SUCCESS = "success";
	private static final String FAILED = "failed";

	public String inicia() {
		// MyLogger.Logger.log(Level.INFO, "--Inicia Certificacion--");
		String regresa = Constants.FAILED;
		UsuarioTO usuarioTO = (UsuarioTO) sessionMap.get(Constants.USUARIO);
		Integer idTramiteVar = (Integer) sessionMap.get(Constants.ID_TRAMITE);
		Integer idGarantiaVar = (Integer) sessionMap.get(Constants.ID_GARANTIA);
		setIdGarantia(idGarantiaVar);
		setIdTramite(idTramiteVar);
		System.out.println("Inicia " + usuarioTO);

		try {
			DetalleServiceImpl detserv = new DetalleServiceImpl();

			setTramiteTOs(detserv.getTramites(getIdGarantia(), getIdTramite()));

			regresa = Constants.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return regresa;
	}

	public String certificacionTramite() {
		// MyLogger.Logger.log(Level.INFO, "--Inicia Certificacion--");
		String regresa = "failed";
		UsuarioTO usuarioTO = (UsuarioTO) sessionMap.get(Constants.USUARIO);
		Integer idTramiteVar = (Integer) sessionMap.get(Constants.ID_TRAMITE);
		Integer idGarantiaVar = (Integer) sessionMap.get(Constants.ID_GARANTIA);

		InscripcionDAO inscripcionDAO = new InscripcionDAO();
		GarantiasDAO garantiasDAO = new GarantiasDAO();
		BoletaDAO boletaDAO = new BoletaDAO();
		CertificacionDAO certificacionDAO = new CertificacionDAO();
		InscripcionServiceImpl inscripcionService = new InscripcionServiceImpl();


		if (getIdTramite() != null) {
			idTramiteVar = getIdTramite();
		}

		if (getIdGarantia() != null) {
			idGarantiaVar = getIdGarantia();
		}

		if (!inscripcionService.getSaldoByUsuario(new Integer(usuarioTO.getPersona().getIdPersona()).toString(),new Integer(35), 0, idGarantiaVar)) {
			regresa = "nosaldo";
		} else {
			Integer idTramiteNuevo = inscripcionDAO.insert(usuarioTO.getPersona().getIdPersona(), new Integer(35));
			garantiasDAO.altaBitacoraTramite(idTramiteNuevo, new Integer(3), new Integer(0), null, "V");
			try {
				Integer idTramiteCert = boletaDAO.getIdTramitebyIdTramiteNuevo(idTramiteNuevo);
				Integer idTipoTramite = boletaDAO.getTipoTramitebyIdTramiteTerminado(idTramiteVar);
				// en este procedimiento inserta lo cobrado a una tabla de registro de las
				// consultas y su valor
				certificacionDAO.setCertificacion(idTramiteCert, idTramiteVar, idTipoTramite, idGarantiaVar,new Integer(usuarioTO.getPersona().getIdPersona()));
				// Param para boleta
				setIdTramite(idTramiteNuevo);
				setIdTipoTramite(35);
				regresa = SUCCESS;
			} catch (NoDataFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				regresa = ERROR;
			}
		}
		// regresa = SUCCESS;
		return regresa;
	}

	public Integer getIdTramite() {
		return idTramite;
	}

	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}

	public Integer getIdGarantia() {
		return idGarantia;
	}

	public void setIdGarantia(Integer idGarantia) {
		this.idGarantia = idGarantia;
	}

	public Integer getIdTipoTramite() {
		return idTipoTramite;
	}

	public void setIdTipoTramite(Integer idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}

	public List<DetalleTO> getTramiteTOs() {
		return tramiteTOs;
	}

	public void setTramiteTOs(List<DetalleTO> tramiteTOs) {
		this.tramiteTOs = tramiteTOs;
	}

}
