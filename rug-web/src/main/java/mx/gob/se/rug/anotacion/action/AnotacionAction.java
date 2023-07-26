package mx.gob.se.rug.anotacion.action;

import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mx.gob.se.rug.anotacio.service.AnotacionSinGarantiaService;
import mx.gob.se.rug.anotacion.serviceimpl.AnotacionSinGarantiaSerImpl;
import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;
import mx.gob.se.rug.to.AnotacionSinGarantiaTO;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

import org.apache.struts2.interceptor.ServletRequestAware;

public class AnotacionAction extends RugBaseAction implements ServletRequestAware{
	
	private static final String SUCCESS = "success";

	private String autoridad;
	private String anotacion;
	private Integer idTramite;
	private Integer idUsuario;
	private AnotacionSinGarantiaTO sinGarantia;
	private Integer meses;
	private String contenidoHTML;
	private String tipoPersona;
	private String folioMercantil;
	private String RazonSocial;
	private String Nombre;
	private String aPaterno;
	private String aMaterno;
	private String Nomnbre;
	private String descripcionTram;
	private String fechaStatus;
	private String HIdGarantia;
	private String fechaInscripcion;
	
	//-----------Detalle Anotacion Sn Garantia -----------------------	
		public String detalle(){
			AnotacionSinGarantiaSerImpl anotacion= new AnotacionSinGarantiaSerImpl();
			String regresa = "failed";
			try {			
				sinGarantia=anotacion.getDatosAnotacionSinGarantia(idTramite);
				sessionMap.put(Constants.ID_TRAMITE_ANOTACION_SG_PADRE, sinGarantia.getIdTramite());
				setAutoridad(sinGarantia.getAutoridad());
				setTipoPersona(sinGarantia.getTipoPersona());
				setAnotacion(sinGarantia.getAnotacion());
				setIdTramite(sinGarantia.getIdTramite());
				setIdUsuario(sinGarantia.getIdUsuario());
				setMeses(sinGarantia.getMeses());
				setFolioMercantil(sinGarantia.getFolioMercantil());
				setRazonSocial(sinGarantia.getRazonSocial());
				setNombre(sinGarantia.getNombre());
				setaPaterno(sinGarantia.getaPaterno());
				setaMaterno(sinGarantia.getaMaterno());
				setNomnbre(sinGarantia.getNomnbre());
				setDescripcionTram(sinGarantia.getDescripcionTram());
				setFechaStatus(sinGarantia.getFechaStatus());
				setFechaInscripcion(sinGarantia.getFechaInscripcion());
	//--------------------------Historico------------------------------------------------
				setHIdGarantia("N/A");
				regresa = "success";
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(Exception e){
				e.printStackTrace();
			}
			return regresa;
		}
		

	public String getNomnbre() {
		return Nomnbre;
	}


	public void setNomnbre(String nomnbre) {
		Nomnbre = nomnbre;
	}


	public String getRazonSocial() {
		return RazonSocial;
	}


	public void setRazonSocial(String razonSocial) {
		RazonSocial = razonSocial;
	}


	public Integer getMeses() {
		return meses;
	}

	public void setMeses(Integer meses) {
		this.meses = meses;
	}

	public AnotacionSinGarantiaTO getSinGarantia() {
		return sinGarantia;
	}

	public void setSinGarantia(AnotacionSinGarantiaTO sinGarantia) {
		this.sinGarantia = sinGarantia;
	}



	public String getAutoridad() {
		return autoridad;
	}

	public void setAutoridad(String autoridad) {
		this.autoridad = autoridad;
	}

	public String getAnotacion() {
		return anotacion;
	}

	public void setAnotacion(String anotacion) {
		this.anotacion = anotacion;
	}

	public void setIdTramite(int idTramite) {
		this.idTramite = idTramite;
	}

	public int getIdTramite() {
		return idTramite;
	}

	public static String getSUCCESS() {
		return SUCCESS;
	}

	public AnotacionAction(){
		super();		
	}
	
	public String sinGarantia(){
		String regresa= "error";
	try{
		AnotacionSinGarantiaService anotacionService= new AnotacionSinGarantiaService();
		UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
		
		setIdUsuario(usuario.getPersona().getIdPersona());
		setAutoridad("");
		setAnotacion("");
		Integer idTramiteNuevo= anotacionService.altaTramite(usuario.getPersona().getIdPersona(),10);	
		sessionMap.put(Constants.ID_TRAMITE_NUEVO,idTramiteNuevo);
		MyLogger.Logger.log(Level.INFO, "el ide tramiote incom anotacion  " +idTramiteNuevo);
		regresa= "success";
	}catch(Exception e){
		e.printStackTrace();
	}
	return regresa;
}
	
	
	
	public String  agregarSinGarantia () {
		String regresa="failed";
		try{
			UsuarioTO usuarioTO= (UsuarioTO) sessionMap.get(Constants.USUARIO);
			Integer idTramiteNuevo=(Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO);
			setIdUsuario(usuarioTO.getPersona().getIdPersona());
			setIdTramite(idTramiteNuevo);
			regresa = "success";
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return regresa;
	}
	
	public String guardaAnotacion(){
		String regresa = "error";
		try{
			AnotacionSinGarantiaService anotacionService= new AnotacionSinGarantiaService();
			UsuarioTO usuarioTO= (UsuarioTO) sessionMap.get(Constants.USUARIO);
			Integer idTramiteNuevo=(Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO);
			Boolean res=anotacionService.anotacionSinGarantia(usuarioTO.getPersona().getIdPersona(), getAutoridad(),getAnotacion(), idTramiteNuevo, getMeses());
			
			regresa = "success";
		}catch(Exception e){
			e.printStackTrace();
		}
		MyLogger.Logger.log(Level.INFO, regresa);
		MyLogger.Logger.log(Level.INFO, "Regresa con el id de Tramite Guardar Todo");
		return regresa;
	}

	public void setServletRequest(HttpServletRequest request) {
		HttpSession session=request.getSession();
		session.setAttribute(RugSessionListener.KEY_REQUESTURI, RequestContext.getAttribute(RequestContext.KEY_REQUESTURI));
		session.setAttribute(RugSessionListener.KEY_REMOTEADDR, RequestContext.getAttribute(RequestContext.KEY_REMOTEADDR));
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}


	public String getFolioMercantil() {
		return folioMercantil;
	}


	public void setFolioMercantil(String folioMercantil) {
		this.folioMercantil = folioMercantil;
	}

	public void setContenidoHTML(String contenidoHTML) {
		this.contenidoHTML = contenidoHTML;
		
	}


	public String getTipoPersona() {
		return tipoPersona;
	}


	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}


	public String getNombre() {
		return Nombre;
	}


	public void setNombre(String nombre) {
		Nombre = nombre;
	}


	public String getaPaterno() {
		return aPaterno;
	}


	public void setaPaterno(String aPaterno) {
		this.aPaterno = aPaterno;
	}


	public String getaMaterno() {
		return aMaterno;
	}

	public String getContenidoHTML() {
		return contenidoHTML;
	}


	public void setaMaterno(String aMaterno) {
		this.aMaterno = aMaterno;
	}


	public String getDescripcionTram() {
		return descripcionTram;
	}


	public void setDescripcionTram(String descripcionTram) {
		this.descripcionTram = descripcionTram;
	}


	public String getFechaStatus() {
		return fechaStatus;
	}


	public void setFechaStatus(String fechaStatus) {
		this.fechaStatus = fechaStatus;
	}


	public String getHIdGarantia() {
		return HIdGarantia;
	}


	public void setHIdGarantia(String hIdGarantia) {
		HIdGarantia = hIdGarantia;
	}


	public String getFechaInscripcion() {
		return fechaInscripcion;
	}


	public void setFechaInscripcion(String fechaInscripcion) {
		this.fechaInscripcion = fechaInscripcion;
	}


}
