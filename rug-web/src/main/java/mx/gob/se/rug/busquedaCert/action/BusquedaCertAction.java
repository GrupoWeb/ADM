package mx.gob.se.rug.busquedaCert.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mx.gob.se.rug.BusquedaCertificacion.to.BusquedaCertTO;
import mx.gob.se.rug.anotacion.tramites.dao.TramitesDAO;
import mx.gob.se.rug.anotacion.tramites.to.AnotacionSnGarantiaTO;
import mx.gob.se.rug.boleta.dao.BoletaDAO;
import mx.gob.se.rug.boleta.to.BoletaAnotacionSnGarantia;
import mx.gob.se.rug.boleta.to.BoletaAvisoPreventivoTO;
import mx.gob.se.rug.boleta.to.BoletaCertificacionTO;
import mx.gob.se.rug.busquedaCert.service.BusquedaCertServ;
import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
import mx.gob.se.rug.util.MyLogger;

import org.apache.struts2.interceptor.ServletRequestAware;

public class BusquedaCertAction extends RugBaseAction implements
		ServletRequestAware {
	private Boolean hayDatos;
	private Boolean pasoInicio;
	private Integer idTramite;
	private String numGarantia;
	private String tipoOperacion;
	private List<OtorganteTO> otorgantes;
	private Date fechaInsc;
	private String fechaStr;
	private String numOperacion;
	private Double precio;
	private Integer idTipoTramite;

	public String inicia() {
		String regresa = "error";
		setNumOperacion("");
		regresa = "success";
		setHayDatos(false);
		setPasoInicio(false);
		MyLogger.Logger.log(Level.INFO, "entra a inicia:" + getNumOperacion());
		return regresa;
	}

	public String busquedaCert() {
		 DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String regresa = "error";
		MyLogger.Logger.log(Level.INFO, "entra al de busqueda cert");
		BusquedaCertTO busquedaCertTO = new BusquedaCertTO();
		BusquedaCertServ busquedaCertServ = new BusquedaCertServ();
		BoletaDAO boletaDAO= new BoletaDAO();
		numOperacion = numOperacion.replace(" ","");
		Integer idTramiteTerminado = new Integer(numOperacion);
		Integer idTramiteNuevo=null;
		
		Integer idTipoTramite=boletaDAO.getTipoTramitebyIdTramiteTerminado(idTramiteTerminado);
		setIdTipoTramite(idTipoTramite);//setear para validar cancelación por fin de vigencia
		if (idTipoTramite==0){
			setHayDatos(false);
			setPasoInicio(true);
		}
		if(idTipoTramite.intValue()==3){//AvisoPreventivo
			try {
				BoletaAvisoPreventivoTO avisoPreventivoTO=  boletaDAO.getAvisoPreventivo(idTramiteTerminado);
				setIdTramite(avisoPreventivoTO.getIdTramite());
				setNumGarantia("N/A");
				setTipoOperacion("Aviso Preventivo");
				setOtorgantes(busquedaCertTO.getOtorgantes());
				 idTramiteNuevo=boletaDAO.getIdTramiteTempByIdTramite(idTramiteTerminado);
				 MyLogger.Logger.log(Level.INFO, "la fecha q trae del objeto ******" + avisoPreventivoTO.getFechaCreacion());
				 setFechaInsc(format.parse(avisoPreventivoTO.getFechaCreacion()));
				setPrecio(new Double(0));
				setHayDatos(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(idTipoTramite.intValue()==10||idTipoTramite.intValue()==26||idTipoTramite.intValue()==27
				||idTipoTramite.intValue()==28||idTipoTramite.intValue()==29){//Anotacion sn Garantia 10
			try {
				TramitesDAO anotacionSnGarantiaDao = new TramitesDAO();
				idTramiteNuevo=	boletaDAO.getIdTramiteTempByIdTramite(idTramiteTerminado);
				AnotacionSnGarantiaTO anotacionSnGarantia = anotacionSnGarantiaDao.getAnotacionSnGarantiaFinal(idTramiteTerminado);
				
				setIdTramite(idTramiteTerminado);
				setNumGarantia("N/A");
				setTipoOperacion(anotacionSnGarantia.getTipoTramiteStr());
				List<OtorganteTO> otorganteTOs =  new ArrayList<OtorganteTO>();
				OtorganteTO otorganteTO =  new OtorganteTO();
				if(anotacionSnGarantia.getOtorganteDenominacion() != null ){
				otorganteTO.setNombreCompleto(anotacionSnGarantia.getOtorganteDenominacion() );
				}else{
					otorganteTO.setNombreCompleto(anotacionSnGarantia.getOtorganteNombre()+ " " + anotacionSnGarantia.getOtorganteAPaterno());
					
				}
				otorganteTO.setFolioMercantil(anotacionSnGarantia.getOtorganteFolioElectronico());
				otorganteTOs.add(otorganteTO);
				setOtorgantes(otorganteTOs);
				MyLogger.Logger.log(Level.INFO,"la fecha q trae del objeto ******" + anotacionSnGarantia.getFechaFirma());
				setFechaStr(anotacionSnGarantia.getFechaFirma());
				setHayDatos(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(idTipoTramite.intValue()==5){//Certificacion 
			try {
				BoletaCertificacionTO  certificacion  =	boletaDAO.getcertificacion(idTramiteTerminado);
				setTipoOperacion("Certificación");
				setNumGarantia(certificacion.getIdGarantia().toString());
				setFechaInsc(certificacion.getFechaCert());
				OtorganteTO oto = new OtorganteTO();
				oto.setNombre("N/A");
				oto.setNombreCompleto("N/A");
				oto.setFolioMercantil("N/A");
				List<OtorganteTO> otorganteaux = new ArrayList<OtorganteTO>();
				otorganteaux.add(oto);
				setOtorgantes(otorganteaux);
				setIdTramite(certificacion.getIdTramiteCert());
				idTramiteNuevo=	boletaDAO.getIdTramiteTempByIdTramite(idTramiteTerminado);
				setHayDatos(true);
			} catch (NoDataFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{//tramite con garantia

		// garantia
		try {
			busquedaCertTO = busquedaCertServ.busquedaNum(idTramiteTerminado.toString());
		} catch (InfrastructureException e) {
			// TODO Auto-generated catch block
			regresa=Constants.FAILED;
			e.printStackTrace();
		}

		if (busquedaCertTO.getIdTramTem() != null) {
			setIdTramite(busquedaCertTO.getIdTramTem());
			setNumGarantia(busquedaCertTO.getNumGarantia().toString());
			MyLogger.Logger.log(Level.INFO, "numgarantia " + busquedaCertTO.getNumGarantia());
			setTipoOperacion(busquedaCertTO.getTipoOp());
			MyLogger.Logger.log(Level.INFO, "tipoop " + busquedaCertTO.getTipoOp());
			setOtorgantes(busquedaCertTO.getOtorgantes());
			setFechaInsc(busquedaCertTO.getFechaInsc());
			setPrecio(busquedaCertTO.getPrecio());
			 idTramiteNuevo=busquedaCertTO.getIdTramTem();
			setHayDatos(true);
		} else {
			MyLogger.Logger.log(Level.INFO, "objeto en q es nulo" + busquedaCertTO);
			setHayDatos(false);
			setPasoInicio(true);
		}
		}
		
	
		sessionMap.put(Constants.ID_TRAMITE_NUEVO, idTramiteNuevo);
		MyLogger.Logger.log(Level.INFO, "el id tramite en sesion : "	+ sessionMap.get(Constants.ID_TRAMITE_NUEVO));
		setIdTramite(null);
		regresa = "success";
		return regresa;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Boolean getHayDatos() {
		return hayDatos;
	}

	public void setHayDatos(Boolean hayDatos) {
		this.hayDatos = hayDatos;
	}

	public Integer getIdTramite() {
		return idTramite;
	}

	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}

	

	public String getNumGarantia() {
		return numGarantia;
	}

	public void setNumGarantia(String numGarantia) {
		this.numGarantia = numGarantia;
	}

	public String getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}


	public Date getFechaInsc() {
		return fechaInsc;
	}

	public void setFechaInsc(Date fechaInsc) {
		this.fechaInsc = fechaInsc;
	}

	public String getNumOperacion() {
		return numOperacion;
	}

	public void setNumOperacion(String numOperacion) {
		this.numOperacion = numOperacion;
	}

	public Boolean getPasoInicio() {
		return pasoInicio;
	}

	public void setPasoInicio(Boolean pasoInicio) {
		this.pasoInicio = pasoInicio;
	}
	
	public void setIdTipoTramite(Integer idTipoTramite){
		this.idTipoTramite=idTipoTramite;
	}
	
	public Integer getIdTipoTramite(){
		return idTipoTramite;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute(RugSessionListener.KEY_REQUESTURI, RequestContext
				.getAttribute(RequestContext.KEY_REQUESTURI));
		session.setAttribute(RugSessionListener.KEY_REMOTEADDR, RequestContext
				.getAttribute(RequestContext.KEY_REMOTEADDR));
	}

	public void setOtorgantes(List<OtorganteTO> otorgantes) {
		this.otorgantes = otorgantes;
	}

	public List<OtorganteTO> getOtorgantes() {
		return otorgantes;
	}

	public String getFechaStr() {
		return fechaStr;
	}

	public void setFechaStr(String fechaStr) {
		this.fechaStr = fechaStr;
	}

	
}
