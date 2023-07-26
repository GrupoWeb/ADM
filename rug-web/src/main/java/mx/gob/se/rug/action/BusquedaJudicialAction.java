package mx.gob.se.rug.action;

import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.busqueda.dao.BusquedaDAO;
import mx.gob.se.rug.busqueda.to.BusquedaTO;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

public class BusquedaJudicialAction extends RugBaseAction{

	private static final long serialVersionUID = 1L;
	//muestraBusqueda
	private String folioTramite;
	private String nombreOtorgante;
	private String folioMercantil;
	private String descBienes;
	private String idGarantia;
	private String serial;
	private String idPersona;
	
	
	public String getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}

	public String getIdGarantia() {
		return idGarantia;
	}

	public void setIdGarantia(String idGarantia) {
		this.idGarantia = idGarantia;
	}

	public String getFolioTramite() {
		return folioTramite;
	}

	public void setFolioTramite(String folioTramite) {
		this.folioTramite = folioTramite;
	}

	public String getNombreOtorgante() {
		return nombreOtorgante;
	}

	public void setNombreOtorgante(String nombreOtorgante) {
		this.nombreOtorgante = nombreOtorgante;
	}

	public String getFolioMercantil() {
		return folioMercantil;
	}

	public void setFolioMercantil(String folioMercantil) {
		this.folioMercantil = folioMercantil;
	}


	public String getDescBienes() {
		return descBienes;
	}

	public void setDescBienes(String descBienes) {
		this.descBienes = descBienes;
	}


	private List<BusquedaTO> busquedaTOs;
	
	
	public List<BusquedaTO> getBusquedaTOs() {
		return busquedaTOs;
	}

	public void setBusquedaTOs(List<BusquedaTO> busquedaTOs) {
		this.busquedaTOs = busquedaTOs;
	}

	private static final String SUCCESS = "success";
	
	public BusquedaJudicialAction(){
		super();
	}
	
	public String muestraBusqueda(){
		MyLogger.Logger.log(Level.INFO, "Entro al muestraBusqueda Judicial");
		
		UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
		setIdPersona(new Integer(usuario.getPersona().getIdPersona()).toString());
		
		return SUCCESS;
	}
	
	public String muestraCertifica(){
		MyLogger.Logger.log(Level.INFO, "Entro al muestraCertifica");
		
		UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
		setIdPersona(new Integer(usuario.getPersona().getIdPersona()).toString());
		
		return SUCCESS;
	}
	        
	public String resBusqueda(){
		
		if(getNombreOtorgante().trim().equals("") ||
				getFolioTramite().trim().equals("") ||
				getDescBienes().trim().equals("") ||
				getFolioMercantil().trim().equals("") ||
				getIdGarantia().trim().equals("")){
			return "input";
		}
		MyLogger.Logger.log(Level.INFO, "Entro al resBusqueda");
		BusquedaDAO busquedaDAO =new BusquedaDAO();
		BusquedaTO busquedaInTO = new BusquedaTO();
		
		busquedaInTO.setNombre(getNombreOtorgante());
		busquedaInTO.setIdTramite(getFolioTramite());
		busquedaInTO.setDescGarantia(getDescBienes());
		busquedaInTO.setFolioMercantil(getFolioMercantil());
		busquedaInTO.setIdGarantia(getIdGarantia());
		//setBusquedaTOs(busquedaDAO.busqueda(busquedaInTO));
		return SUCCESS;
	}
	
	public String ayuda(){
		return SUCCESS;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}
}
