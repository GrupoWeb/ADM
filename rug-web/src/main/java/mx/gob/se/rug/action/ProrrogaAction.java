package mx.gob.se.rug.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.detallegarantia.service.impl.DetalleServiceImpl;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;
import mx.gob.se.rug.garantia.to.GarantiaTO;
import mx.gob.se.rug.juez.service.JuezService;
import mx.gob.se.rug.prorroga.service.ProrrogaService;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

import org.apache.struts2.interceptor.ServletRequestAware;

public class ProrrogaAction extends RugBaseAction implements ServletRequestAware {
	private static final String SUCCESS = "success";
	private Integer vigencia;
	private String fechaFin;
	private Integer meses=0;
	private Integer vigenciaM;
	private String fechaFinM; 
	private String nomAcreedor;
	private Integer dia;
	private Integer mes;
	private Integer anio;
	private String autoridad;
	private String idGarantia;
	private Boolean vigenciaValida;
	
	
	public String getAutoridad() {
		return autoridad;
	}

	public void setAutoridad(String autoridad) {
		this.autoridad = autoridad;
	}

	public Integer getDia() {
		return dia;
	}

	public void setDia(Integer dia) {
		this.dia = dia;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public String getNomAcreedor() {
		return nomAcreedor;
	}

	public void setNomAcreedor(String nomAcreedor) {
		this.nomAcreedor = nomAcreedor;
	}

	public ProrrogaAction(){
		super();		
	}
	
	
	public int getMonths(Calendar g1, Calendar g2) {
		int elapsed = -1; 
		Calendar gc1, gc2;

		if (g2.after(g1)) {
			gc2 = (Calendar) g2.clone();
			gc1 = (Calendar) g1.clone();
		}
		else {
			gc2 = (Calendar) g1.clone();
			gc1 = (Calendar) g2.clone();
		}

		while ( gc1.before(gc2) ) {
			gc1.add(Calendar.MONTH, 1);
			elapsed++;
		}

		if (gc1.get(Calendar.DATE)==(gc2.get(Calendar.DATE))) elapsed++; 
			return elapsed;
	}
	public Date parseStrDate(String strDate){
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		try {
			date = formater.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	public String inicia(){
		
		MyLogger.Logger.log(Level.INFO, "--Inicia Prorroga--");
		String regresa = "failed";
		try{
			ProrrogaService prorrogaService= new ProrrogaService();		
			DetalleServiceImpl detservd = new DetalleServiceImpl();
						
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			Integer idGarantia =(Integer) sessionMap.get(Constants.ID_GARANTIA);	
			setIdGarantia(idGarantia.toString());
			
			MyLogger.Logger.log(Level.INFO, "usuario::"+usuario.getPersona().getIdPersona());
			MyLogger.Logger.log(Level.INFO, "idGarantia::"+idGarantia);		
			MyLogger.Logger.log(Level.INFO, "Prorroga data ::"+prorrogaService.getVigencia(idGarantia));
			SimpleDateFormat formato= new SimpleDateFormat("dd/MM/yyyy");
			
			vigenciaValida= detservd.vigenciaValida(Integer.parseInt(getIdGarantia()));
			
			GarantiaTO garantiaTO = prorrogaService.getVigencia(idGarantia);
			MyLogger.Logger.log(Level.INFO, "fecha de garantiaTO::"+ garantiaTO.getFechaFin());
			setVigencia(garantiaTO.getVigencia());
			setFechaFin((formato.format(garantiaTO.getFechaFin())));
			setVigenciaM(getVigencia());
			setFechaFinM(getFechaFin());
			setMeses(0);
			String cadena= formato.format(parseStrDate(getFechaFin())); 
			MyLogger.Logger.log(Level.INFO, "la fecha en el action :" + cadena);
			setDia(Integer.parseInt(cadena.substring(0,2))); 
			MyLogger.Logger.log(Level.INFO, "el dia en el action :" + getDia());
			setMes(Integer.parseInt(cadena.substring(3,5)));
			MyLogger.Logger.log(Level.INFO, "el mes en el action :" + getMes());
			setAnio(Integer.parseInt(cadena.substring(6,10)));
			MyLogger.Logger.log(Level.INFO, "el anio en el action :" + getAnio());
			setAutoridad("");
			regresa= "success";
		}catch(NoDataFoundException e){
			regresa = "failed";
			MyLogger.Logger.log(Level.INFO, "dio error en el try");
			MyLogger.Logger.log(Level.SEVERE, e.getMessage(),e);
		}catch(Exception e){
			regresa = "failed";
			MyLogger.Logger.log(Level.INFO, "dio error en el try 2");
			MyLogger.Logger.log(Level.SEVERE, e.getMessage(),e);
		}
		
		return regresa;
	}
	
	
	public String prorroga(){
		String regresa = "failed";
		try{
			Integer idGarantia =(Integer) sessionMap.get(mx.gob.se.rug.constants.Constants.ID_GARANTIA);
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			ProrrogaService prorrogaService= new ProrrogaService();
			GarantiaTO garantiaTO = prorrogaService.getVigencia(idGarantia);
			setVigenciaM(garantiaTO.getVigencia()+getMeses());
			MyLogger.Logger.log(Level.INFO, "los años a agregar son "+getVigenciaM());
			Integer idTramiteNuevo = prorrogaService.setVigencia(idGarantia,usuario.getPersona().getIdPersona(), 9, getVigenciaM());
			sessionMap.put(Constants.ID_TRAMITE_NUEVO, idTramiteNuevo);
			MyLogger.Logger.log(Level.INFO, "id tramite nuevo "+idTramiteNuevo);
			MyLogger.Logger.log(Level.INFO, "id Garantia "+idGarantia);
			MyLogger.Logger.log(Level.INFO, "la vigencia "+getVigencia());
			MyLogger.Logger.log(Level.INFO, "la fecha fin "+getFechaFin());
			MyLogger.Logger.log(Level.INFO, "la nueva vigencia es: "+ getVigenciaM());
			if (getAutoridad()!= ""){
				JuezService juezService = new JuezService();
				MyLogger.Logger.log(Level.INFO, "entra al action de prorroga: " + idTramiteNuevo+ "la autoridad"+ getAutoridad());
				juezService.insertJuez(idTramiteNuevo, getAutoridad());
			}
			if (idTramiteNuevo.intValue() != 0){
				regresa = "success";				
			}
		}catch(Exception e){
			regresa = "failed";
		}
		return regresa;
	}

	public Integer getVigencia() {
		return vigencia;
	}

	public void setVigencia(Integer vigencia) {
		this.vigencia = vigencia;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Integer getMeses() {
		return meses;
	}

	public void setMeses(Integer meses) {
		this.meses = meses;
	}

	public Integer getVigenciaM() {
		return vigenciaM;
	}

	public void setVigenciaM(Integer vigenciaM) {
		this.vigenciaM = vigenciaM;
	}

	public String getFechaFinM() {
		return fechaFinM;
	}

	public void setFechaFinM(String fechaFinM) {
		this.fechaFinM = fechaFinM;
	}

	

	@Override
	public void setServletRequest(HttpServletRequest request) {
		HttpSession session=request.getSession();
		session.setAttribute(RugSessionListener.KEY_REQUESTURI, RequestContext.getAttribute(RequestContext.KEY_REQUESTURI));
		session.setAttribute(RugSessionListener.KEY_REMOTEADDR, RequestContext.getAttribute(RequestContext.KEY_REMOTEADDR));
	}

	public String getIdGarantia() {
		return idGarantia;
	}

	public void setIdGarantia(String idGarantia) {
		this.idGarantia = idGarantia;
	}

	public Boolean getVigenciaValida() {
		return vigenciaValida;
	}

	public void setVigenciaValida(Boolean vigenciaValida) {
		this.vigenciaValida = vigenciaValida;
	}

	
}
