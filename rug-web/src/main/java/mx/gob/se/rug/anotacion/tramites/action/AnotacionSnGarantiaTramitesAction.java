package mx.gob.se.rug.anotacion.tramites.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.SimpleFormatter;

import mx.gob.se.rug.anotacion.tramites.dao.TramitesDAO;
import mx.gob.se.rug.anotacion.tramites.to.AnotacionSnGarantiaTO;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.exception.RugException;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.to.UsuarioTO;

public class AnotacionSnGarantiaTramitesAction extends RugBaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AnotacionSnGarantiaTO anotacionSnGarantia;

	private Boolean haySiguiente;
	private Boolean hayAnterior;
	private Boolean noHayCancel;
	private Integer idTramite;

	private ArrayList<AnotacionSnGarantiaTO> anotacionSnGarantiaTOs;

	private Integer idTramiteNuevo;
	private Integer idUsuario;
	private Integer idActual;
	private String dia;
	private String mes;
	private String anio;
	private String fechaFin;
	private String msgError;
	private static final String SUCCESS = "success";
	private static final String FAILED = "failed";
	private static final Integer ID_TIPO_TRAMITE_RECTIFICACION = 29;
	private static final Integer ID_TIPO_TRAMITE_MODIFICACION = 28;
	private static final Integer ID_TIPO_TRAMITE_RENOVACION = 26;
	private static final Integer ID_TIPO_TRAMITE_CANCELACION = 27;
	private static final String ID_ACTUAL = "idActual";

	public String detalle() {
		String regresa = "failed";
		TramitesDAO tramitesDAO = new TramitesDAO();
		Integer idTramitePadre;
		try {

			idTramitePadre = tramitesDAO.getTramitePadreByIdTramite(idTramite);
			sessionMap.put(Constants.ID_TRAMITE_ANOTACION_SG_PADRE,
					idTramitePadre);

			anotacionSnGarantiaTOs = tramitesDAO
					.getTramitesHByIdTramite(idTramitePadre);

			noHayCancel = !tramitesDAO.haveCancelacion(idTramitePadre);

			if (anotacionSnGarantiaTOs.size() == 1) {
				hayAnterior = false;

			} else {
				hayAnterior = true;
			}

			haySiguiente = false;

			// La ultima
			idActual = anotacionSnGarantiaTOs.size() - 1;
			sessionMap.put(ID_ACTUAL, idActual);
			anotacionSnGarantia = anotacionSnGarantiaTOs.get(idActual);

			regresa = "success";
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return regresa;
	}

	public String detalleSiguiente() {
		String regresa = "failed";
		TramitesDAO tramitesDAO = new TramitesDAO();
		Integer idTramitePadre;
		try {

			idTramitePadre = (Integer) sessionMap
					.get(Constants.ID_TRAMITE_ANOTACION_SG_PADRE);

			anotacionSnGarantiaTOs = tramitesDAO
					.getTramitesHByIdTramite(idTramitePadre);

			noHayCancel = !tramitesDAO.haveCancelacion(idTramitePadre);

			idActual = ((Integer) sessionMap.get(ID_ACTUAL)) + 1;
			sessionMap.put(ID_ACTUAL, idActual);

			if (idActual <= anotacionSnGarantiaTOs.size() - 1) {
				hayAnterior = true;
				if (idActual >= anotacionSnGarantiaTOs.size() - 1) {
					haySiguiente = false;
				} else {
					haySiguiente = true;
				}
			} else {
				hayAnterior = false;
			}

			if (idActual == 0) {
				hayAnterior = false;
			}

			// La ultima
			anotacionSnGarantia = anotacionSnGarantiaTOs.get(idActual);

			System.out.println("ANT idActual::" + idActual);

			regresa = "success";
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return regresa;
	}

	public String detalleAnterior() {
		String regresa = "failed";
		TramitesDAO tramitesDAO = new TramitesDAO();
		Integer idTramitePadre;
		try {

			idTramitePadre = (Integer) sessionMap
					.get(Constants.ID_TRAMITE_ANOTACION_SG_PADRE);

			anotacionSnGarantiaTOs = tramitesDAO
					.getTramitesHByIdTramite(idTramitePadre);

			noHayCancel = !tramitesDAO.haveCancelacion(idTramitePadre);

			idActual = ((Integer) sessionMap.get(ID_ACTUAL)) - 1;
			sessionMap.put(ID_ACTUAL, idActual);

			if (idActual <= anotacionSnGarantiaTOs.size() - 1) {
				hayAnterior = true;
				if (idActual >= anotacionSnGarantiaTOs.size() - 1) {
					haySiguiente = false;
				} else {
					haySiguiente = true;
				}
			} else {
				hayAnterior = false;
			}

			if (idActual == 0) {
				hayAnterior = false;
			}

			// La ultima
			anotacionSnGarantia = anotacionSnGarantiaTOs.get(idActual);

			System.out.println("ANT idActual::" + idActual);

			regresa = "success";
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return regresa;
	}

	public String initRectificacion() {
		try {
			TramitesDAO tramitesDAO = new TramitesDAO();
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			Integer idTramitePadre = (Integer) sessionMap
					.get(Constants.ID_TRAMITE_ANOTACION_SG_PADRE);

			idUsuario = usuario.getPersona().getIdPersona();

			idTramiteNuevo = tramitesDAO.initTramite(idUsuario,
					ID_TIPO_TRAMITE_RECTIFICACION, idTramitePadre);
			sessionMap.put(Constants.ID_TRAMITE_NUEVO, idTramiteNuevo);
			anotacionSnGarantia = tramitesDAO
					.getAnotacionSnGarantia_H(idTramitePadre);
			anotacionSnGarantia.setIdTramiteNuevo(idTramiteNuevo);

		} catch (RugException e) {
			e.printStackTrace();
			return FAILED;
		} catch (NoDataFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return FAILED;
		}
		return SUCCESS;
	}

	public String saveRectificacion() {
		try {
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			Integer idTramitePadre = (Integer) sessionMap
					.get(Constants.ID_TRAMITE_ANOTACION_SG_PADRE);
			Integer idTramiteNuevo = (Integer) sessionMap
					.get(Constants.ID_TRAMITE_NUEVO);
			anotacionSnGarantia.setIdTramiteNuevo(idTramiteNuevo);
			anotacionSnGarantia.setIdUsuario(usuario.getPersona()
					.getIdPersona());
			anotacionSnGarantia.setIdTramitePadre(idTramitePadre);

			TramitesDAO tramitesDAO = new TramitesDAO();
			tramitesDAO.saveRectificacionSnGarantia(anotacionSnGarantia);

			return SUCCESS;

		} catch (Exception e) {
			msgError = e.getMessage();
			return FAILED;
		}
	}

	public String initModificacion() {
		try {
			TramitesDAO tramitesDAO = new TramitesDAO();
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			Integer idTramitePadre = (Integer) sessionMap
					.get(Constants.ID_TRAMITE_ANOTACION_SG_PADRE);

			idUsuario = usuario.getPersona().getIdPersona();

			idTramiteNuevo = tramitesDAO.initTramite(idUsuario,
					ID_TIPO_TRAMITE_MODIFICACION, idTramitePadre);
			sessionMap.put(Constants.ID_TRAMITE_NUEVO, idTramiteNuevo);
			anotacionSnGarantia = tramitesDAO
					.getAnotacionSnGarantia_H(idTramitePadre);
			anotacionSnGarantia.setIdTramiteNuevo(idTramiteNuevo);

		} catch (RugException e) {
			e.printStackTrace();
			return FAILED;
		} catch (NoDataFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return FAILED;
		}
		return SUCCESS;
	}

	public String saveModificacion() {
		try {
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			Integer idTramitePadre = (Integer) sessionMap
					.get(Constants.ID_TRAMITE_ANOTACION_SG_PADRE);
			Integer idTramiteNuevo = (Integer) sessionMap
					.get(Constants.ID_TRAMITE_NUEVO);
			anotacionSnGarantia.setIdTramiteNuevo(idTramiteNuevo);
			anotacionSnGarantia.setIdUsuario(usuario.getPersona()
					.getIdPersona());
			anotacionSnGarantia.setIdTramitePadre(idTramitePadre);

			TramitesDAO tramitesDAO = new TramitesDAO();
			tramitesDAO.saveModificacionSnGarantia(anotacionSnGarantia);

			return SUCCESS;
		} catch (Exception e) {
			msgError = e.getMessage();
			return FAILED;
		}
	}

	public String initCancelacion() {
		try {
			TramitesDAO tramitesDAO = new TramitesDAO();
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			Integer idTramitePadre = (Integer) sessionMap
					.get(Constants.ID_TRAMITE_ANOTACION_SG_PADRE);

			idUsuario = usuario.getPersona().getIdPersona();

			idTramiteNuevo = tramitesDAO.initTramite(idUsuario,
					ID_TIPO_TRAMITE_CANCELACION, idTramitePadre);
			sessionMap.put(Constants.ID_TRAMITE_NUEVO, idTramiteNuevo);
			anotacionSnGarantia = tramitesDAO
					.getAnotacionSnGarantia_H(idTramitePadre);
			anotacionSnGarantia.setIdTramiteNuevo(idTramiteNuevo);

		} catch (RugException e) {
			e.printStackTrace();
			return FAILED;
		} catch (NoDataFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return FAILED;
		}
		return SUCCESS;
	}

	public String saveCancelacion() {
		try {

			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			Integer idTramitePadre = (Integer) sessionMap
					.get(Constants.ID_TRAMITE_ANOTACION_SG_PADRE);
			Integer idTramiteNuevo = (Integer) sessionMap
					.get(Constants.ID_TRAMITE_NUEVO);
			anotacionSnGarantia.setIdTramiteNuevo(idTramiteNuevo);
			anotacionSnGarantia.setIdUsuario(usuario.getPersona()
					.getIdPersona());
			anotacionSnGarantia.setIdTramitePadre(idTramitePadre);

			TramitesDAO tramitesDAO = new TramitesDAO();
			tramitesDAO.saveCancelacionSnGarantia(anotacionSnGarantia);

			return SUCCESS;
		} catch (Exception e) {
			msgError = e.getMessage();
			return FAILED;
		}
	}

	public java.util.Date addMonths(Integer year, Integer month, Integer dayOfMonth, int number) {
		java.util.Calendar aCalendar = new GregorianCalendar(year, month, dayOfMonth);
		aCalendar.add(java.util.Calendar.MONTH, number);
		return aCalendar.getTime();
	}

	public String initRenovacion() {
		try {
			TramitesDAO tramitesDAO = new TramitesDAO();
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			Integer idTramitePadre = (Integer) sessionMap
					.get(Constants.ID_TRAMITE_ANOTACION_SG_PADRE);

			idUsuario = usuario.getPersona().getIdPersona();

			idTramiteNuevo = tramitesDAO.initTramite(idUsuario,
					ID_TIPO_TRAMITE_RENOVACION, idTramitePadre);
			sessionMap.put(Constants.ID_TRAMITE_NUEVO, idTramiteNuevo);
			anotacionSnGarantia = tramitesDAO.getAnotacionSnGarantia_H(tramitesDAO.getLastTramiteByIdTramitePadre(idTramitePadre));
			anotacionSnGarantia.setIdTramiteNuevo(idTramiteNuevo);

			dia = anotacionSnGarantia.getFechaFirma().split(" ")[0].split("/")[0];
			mes = anotacionSnGarantia.getFechaFirma().split(" ")[0].split("/")[1];
			anio = anotacionSnGarantia.getFechaFirma().split(" ")[0].split("/")[2];

			java.util.Date fechaVigencia = 	addMonths(Integer.valueOf(anio), Integer.valueOf(mes)-1,Integer.valueOf(dia),anotacionSnGarantia.getVigencia());
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sdfd = new SimpleDateFormat("dd");
			SimpleDateFormat sdfm = new SimpleDateFormat("MM");
			SimpleDateFormat sdfy = new SimpleDateFormat("yyyy");
		
			fechaFin = sdf.format(fechaVigencia.getTime());
			
			dia = sdfd.format(fechaVigencia.getTime());
			mes = sdfm.format(fechaVigencia.getTime());
			anio = sdfy.format(fechaVigencia.getTime());
			
			

		} catch (RugException e) {
			e.printStackTrace();
			return FAILED;
		} catch (NoDataFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return FAILED;
		}
		return SUCCESS;
	}

	public String saveRenovacion() {
		try {

			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			Integer idTramitePadre = (Integer) sessionMap
					.get(Constants.ID_TRAMITE_ANOTACION_SG_PADRE);
			Integer idTramiteNuevo = (Integer) sessionMap
					.get(Constants.ID_TRAMITE_NUEVO);
			anotacionSnGarantia.setIdTramiteNuevo(idTramiteNuevo);
			anotacionSnGarantia.setIdUsuario(usuario.getPersona()
					.getIdPersona());
			anotacionSnGarantia.setIdTramitePadre(idTramitePadre);

			TramitesDAO tramitesDAO = new TramitesDAO();
			tramitesDAO.saveRenovacionSnGarantia(anotacionSnGarantia);

			return SUCCESS;
		} catch (Exception e) {
			msgError = e.getMessage();
			return FAILED;
		}
	}

	public AnotacionSnGarantiaTO getAnotacionSnGarantia() {
		return anotacionSnGarantia;
	}

	public void setAnotacionSnGarantia(AnotacionSnGarantiaTO anotacionSnGarantia) {
		this.anotacionSnGarantia = anotacionSnGarantia;
	}

	public Integer getIdTramiteNuevo() {
		return idTramiteNuevo;
	}

	public void setIdTramiteNuevo(Integer idTramiteNuevo) {
		this.idTramiteNuevo = idTramiteNuevo;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public ArrayList<AnotacionSnGarantiaTO> getAnotacionSnGarantiaTOs() {
		return anotacionSnGarantiaTOs;
	}

	public void setAnotacionSnGarantiaTOs(
			ArrayList<AnotacionSnGarantiaTO> anotacionSnGarantiaTOs) {
		this.anotacionSnGarantiaTOs = anotacionSnGarantiaTOs;
	}

	public Boolean getHaySiguiente() {
		return haySiguiente;
	}

	public void setHaySiguiente(Boolean haySiguiente) {
		this.haySiguiente = haySiguiente;
	}

	public Boolean getHayAnterior() {
		return hayAnterior;
	}

	public void setHayAnterior(Boolean hayAnterior) {
		this.hayAnterior = hayAnterior;
	}

	public Integer getIdTramite() {
		return idTramite;
	}

	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}

	public Boolean getNoHayCancel() {
		return noHayCancel;
	}

	public void setNoHayCancel(Boolean noHayCancel) {
		this.noHayCancel = noHayCancel;
	}

	public String getMsgError() {
		return msgError;
	}

	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}

	
}
