package mx.gob.se.rug.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mx.gob.se.rug.anotacionGarantia.service.AnotacionGarantiaService;
import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.detallegarantia.service.impl.DetalleServiceImpl;
import mx.gob.se.rug.detallegarantia.to.DetalleTO;
import mx.gob.se.rug.detallegarantia.to.PartesTO;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;
import mx.gob.se.rug.inscripcion.to.TipoBienTO;
import mx.gob.se.rug.modificacion.service.impl.ModificacionServiceImp;
import mx.gob.se.rug.to.UsuarioTO;

import org.apache.struts2.interceptor.ServletRequestAware;

public class AnotacionGarantiaAction extends RugBaseAction implements ServletRequestAware {
	
	private static final long serialVersionUID = 1L;
	
	private String autoridad;
	private String anotacion;
	private String nomAcreedor;
	private List<PartesTO> OtorganteTOs;
	private List<PartesTO> AcreedorTOs;
	private List<PartesTO> DeudorTOs;
	private DetalleTO DetalleTO;
	private List<DetalleTO> tramiteTOs;
	private boolean hayAcreedores;
	private List<TipoBienTO> bienesTOs;
	private String titulofecha;
	private boolean habilitaobligacion;
	private String meses;
	
	public String  creaTramite () {
		String regresa="failed";
		try{
			regresa = "success";
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return regresa;
	}
	
	public String guardaAnotacionGarantia(){
		String regresa = "failed";
		UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
		Integer idGarantia= (Integer) sessionMap.get(Constants.ID_GARANTIA);
		
		AnotacionGarantiaService anotacionService = new AnotacionGarantiaService();
		Integer idTramiteNuevo= anotacionService.anotacionGarantia(usuario.getPersona().getIdPersona(),2, getAutoridad(), getAnotacion(), idGarantia,getMeses());		
		sessionMap.put(Constants.ID_TRAMITE_NUEVO, idTramiteNuevo);
		if (idTramiteNuevo.intValue() != 0){
			regresa = "success";
		}		
		return regresa;
	}
	
	public String inicia(){//genera tram incompleto
		String regresa = "error";
		Integer idTramite=(Integer) sessionMap.get(Constants.ID_TRAMITE);
		Integer idGarantia=(Integer) sessionMap.get(Constants.ID_GARANTIA);
		UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
		try{
			DetalleServiceImpl detserv = new DetalleServiceImpl();
			ModificacionServiceImp detservmod = new ModificacionServiceImp();
			
			setNomAcreedor(usuario.getNombre());
			setAcreedorTOs(detserv.getAcreedor(idGarantia,idTramite));
			if (getAcreedorTOs().size()>0){
				setHayAcreedores(true);
			}else{
				setHayAcreedores(false);
			}
			setOtorganteTOs(detserv.getOtorgante(idGarantia,idTramite));
			setDeudorTOs(detserv.getDeudor(idGarantia,idTramite));
			setDetalleTO(detserv.getDetalle(idGarantia,idTramite));			
			setTramiteTOs(detserv.getTramites(idGarantia,idTramite));
			setBienesTOs(detservmod.getBienes(idGarantia,idTramite));
			habilitaobligacion=true;			
			int valor = getDetalleTO().getIdtipogarantia();	
			//1-7 el mismo
			//8,9 dif fecha
			switch(valor){					
					case 8:
						setTitulofecha("Fecha de surgimiento del derecho de retención :");
						habilitaobligacion=false;
						break;
					case 9:
						setTitulofecha("Fecha de surgimiento del derecho de retención :");
						habilitaobligacion=false;						
						break;
					default :
						setTitulofecha("Fecha de celebracion del acto o contrato :");
						habilitaobligacion=true;						
						break;
								
				}
			setAutoridad("");
			setAnotacion("");
			regresa = "success";
		}catch(Exception e){
			e.printStackTrace();
		}
		return regresa;
	}
	
	
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		HttpSession session=request.getSession();
		session.setAttribute(RugSessionListener.KEY_REQUESTURI, RequestContext.getAttribute(RequestContext.KEY_REQUESTURI));
		session.setAttribute(RugSessionListener.KEY_REMOTEADDR, RequestContext.getAttribute(RequestContext.KEY_REMOTEADDR));
	}
	
	public String getMeses() {
		return meses;
	}

	public void setMeses(String meses) {
		this.meses = meses;
	}
	public List<PartesTO> getOtorganteTOs() {
		return OtorganteTOs;
	}

	public void setOtorganteTOs(List<PartesTO> otorganteTOs) {
		OtorganteTOs = otorganteTOs;
	}

	public List<PartesTO> getAcreedorTOs() {
		return AcreedorTOs;
	}

	public void setAcreedorTOs(List<PartesTO> acreedorTOs) {
		AcreedorTOs = acreedorTOs;
	}

	public List<PartesTO> getDeudorTOs() {
		return DeudorTOs;
	}

	public void setDeudorTOs(List<PartesTO> deudorTOs) {
		DeudorTOs = deudorTOs;
	}

	public DetalleTO getDetalleTO() {
		return DetalleTO;
	}

	public void setDetalleTO(DetalleTO detalleTO) {
		DetalleTO = detalleTO;
	}


	public List<DetalleTO> getTramiteTOs() {
		return tramiteTOs;
	}

	public void setTramiteTOs(List<DetalleTO> tramiteTOs) {
		this.tramiteTOs = tramiteTOs;
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
	
	public boolean isHayAcreedores() {
		return hayAcreedores;
	}

	public void setHayAcreedores(boolean hayAcreedores) {
		this.hayAcreedores = hayAcreedores;
	}

	public List<TipoBienTO> getBienesTOs() {
		return bienesTOs;
	}

	public void setBienesTOs(List<TipoBienTO> bienesTOs) {
		this.bienesTOs = bienesTOs;
	}

	public String getTitulofecha() {
		return titulofecha;
	}

	public void setTitulofecha(String titulofecha) {
		this.titulofecha = titulofecha;
	}

	public boolean isHabilitaobligacion() {
		return habilitaobligacion;
	}

	public void setHabilitaobligacion(boolean habilitaobligacion) {
		this.habilitaobligacion = habilitaobligacion;
	}

	public String getNomAcreedor() {
		return nomAcreedor;
	}

	public void setNomAcreedor(String nomAcreedor) {
		this.nomAcreedor = nomAcreedor;
	}

}
