package mx.gob.se.rug.detallegarantia.action;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.detallegarantia.service.impl.DetalleServiceImpl;
import mx.gob.se.rug.detallegarantia.to.DatosAnotacion;
import mx.gob.se.rug.detallegarantia.to.DatosGaranTO;
import mx.gob.se.rug.detallegarantia.to.DetalleTO;
import mx.gob.se.rug.detallegarantia.to.PartesTO;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.to.BienEspecialTO;
import mx.gob.se.rug.inscripcion.to.TipoBienTO;
import mx.gob.se.rug.juez.service.JuezService;
import mx.gob.se.rug.modificacion.service.impl.ModificacionServiceImp;

import org.apache.struts2.interceptor.ServletRequestAware;

public class DetalleAction extends RugBaseAction implements ServletRequestAware {
	
	private static final long serialVersionUID = 1L;
	
	private List<PartesTO> OtorganteTOs;
	private List<PartesTO> AcreedorTOs;
	private List<PartesTO> DeudorTOs;
	private List<BienEspecialTO> bienesEspTOs;
	private DetalleTO detalleTO;
	private String idGarantia;
	private String idTramite;	
	private String bvalida;
	private List<DetalleTO> tramiteTOs;
	private String nomAcreedor;
	private boolean hayAcreedores;
	private boolean hayOtorgantes;
	private boolean hayBienes;
	private List<TipoBienTO> bienesTOs;
	private String titulofecha;
	private boolean habilitaobligacion;
	private DatosAnotacion datosAnotacion;
	private InscripcionService inscripcionService;
	
	private String telefono;
	private String ext;
	private String email;
	private String autoridad;
	private boolean hayautoridad;
	private boolean aBoolean;
	private boolean aBooleanNoGaraOt;
	private Boolean noHayCancel;
	private Boolean vigenciaValida;
	private DetalleTO detalleBasaTO;
	private String tituloacto;
	private DatosGaranTO datosGaranTO;
	private Integer longitud = 0;
	private DetalleTO elem;
	private Integer idGarantiaAnt;
	private Integer idTramiteAnt;
	private Boolean hayAnterior=true;
        
        //corellana
        private Boolean esLeasing=false;
        
	private Boolean haySiguiente=true;
	private Boolean vieneFirma;
	private String fechaInscripcion;
	private String fechaAsiento;
	private String fechaUltAsiento;
	
	private List<String> textosFormulario;
	
	private boolean aPrioridad;
	private boolean aRegistro;
	
	public Integer posTramite(Integer idGarantia, Integer idTramite){
		DetalleServiceImpl detserv = new DetalleServiceImpl();
		setTramiteTOs(detserv.getTramites(idGarantia,idTramite));
		setLongitud(tramiteTOs.size());
		Integer pos= 0;
		Integer esta= 0;
		for (DetalleTO elem : getTramiteTOs() ) {
		    if( elem.getIdTramite().equals(idTramite)){
		    	esta= pos;
		    }
		    pos++;
		}
		if (esta==0){
			setHaySiguiente(false);
		}
		if (esta == (getLongitud()-1)){
			setHayAnterior(false);
		}
		return esta;
	}
	
	public String siguiente(){
		DetalleTO elem = new DetalleTO();
		String regresa = "failed";
		Integer idTramiteAct=(Integer) sessionMap.get(Constants.ID_TRAMITE);
		Integer idGarantiaAct=(Integer) sessionMap.get(Constants.ID_GARANTIA);
		setIdGarantia(idGarantiaAct.toString());
		setIdTramite(idTramiteAct.toString());
		Integer idTramiteAnt=(Integer) sessionMap.get(Constants.ID_TRAMITE_ACTUAL);
		System.out.println("El SIGUIENTE id del tramite = "+idTramiteAnt);
		setIdTramiteAnt(idTramiteAnt);
		Integer idGarantiaAnt=(Integer) sessionMap.get(Constants.ID_GARANTIA);
		setIdGarantiaAnt(idGarantiaAnt);
		Integer posicion = posTramite(getIdGarantiaAnt(),getIdTramiteAnt());
		if (posicion >= 1){
			if ((posicion - 1)==0){
				setHaySiguiente(false);
			}
			else{
				setHaySiguiente(true);
			}
			setHayAnterior(true);
			elem= getTramiteTOs().get(posicion-1);
			setIdTramiteAnt(elem.getIdTramite());
			sessionMap.put(Constants.ID_TRAMITE_ACTUAL,getIdTramiteAnt());
			try{
				
				String DATE_FORMAT = "dd/MM/yyyy";
			    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				DetalleServiceImpl detserv = new DetalleServiceImpl();
				setDatosGaranTO(detserv.datosGaran(getIdGarantiaAnt(), getIdTramiteAnt()));
				setFechaAsiento(sdf.format(datosGaranTO.getFechaAsiento()));
				setFechaInscripcion(sdf.format(datosGaranTO.getFechaInsc()));
				setFechaUltAsiento(sdf.format(datosGaranTO.getFechUltAsiento()));
				ModificacionServiceImp detservmod = new ModificacionServiceImp();
				JuezService jserv = new JuezService();
				noHayCancel= detserv.noHayCancel(getIdGarantiaAnt());
				vigenciaValida= detserv.vigenciaValida(getIdGarantiaAnt());
				setNomAcreedor(detserv.showAcreedorRepresentado(getIdTramiteAnt()));
				setAcreedorTOs(detserv.getAcreedor(getIdGarantiaAnt(),getIdTramiteAnt()));
				if (getAcreedorTOs().size()>0){
					setHayAcreedores(true);
				}else{
					setHayAcreedores(false);
				}
				setBienesEspTOs(detserv.getListaBienes(getIdTramiteAnt(), 3));
				if (getBienesEspTOs().size()>0){
					setHayBienes(true);
				}else{
					setHayBienes(false);
				}
				setOtorganteTOs(detserv.getOtorgante(getIdGarantiaAnt(),getIdTramiteAnt()));
				if (getOtorganteTOs().size()>0){
					setHayOtorgantes(true);
				}else{
					setHayOtorgantes(false);
				}
				setDeudorTOs(detserv.getDeudor(getIdGarantiaAnt(),getIdTramiteAnt()));
				setDetalleTO(detserv.getDetalle(getIdGarantiaAnt(),getIdTramiteAnt()));	
				if(getDetalleTO() == null || getDetalleTO().getRelacionbien() == null){
					setDetalleTO(detserv.getTramiteCancelacion(getIdTramiteAnt()));
					setDetalleTO(detserv.getTramiteActualizado(getDetalleTO().getIdTramite()));
					setDetalleTO(detserv.getDetalle(getDetalleTO().getIdgarantia(),getDetalleTO().getIdTramite()));
				}
				if(getDetalleTO() != null && getDetalleTO().getRelacionbien() != null){
					setBienesTOs(detservmod.getBienes(getIdGarantiaAnt(),detalleTO.getRelacionbien()));
				}
				setTramiteTOs(detserv.getTramites(getIdGarantiaAnt(),getIdTramiteAnt()));
				
				setTextosFormulario(inscripcionService.getTextosFormularioByIdGarantia(getDetalleTO().getIdtipogarantia()));
				
				if(getDetalleTO().getCambio().equalsIgnoreCase("V")){
					setaBoolean(true);
				}else{setaBoolean(false);}
				
				if(getDetalleTO().getNoGarantiaPreviaOt().equalsIgnoreCase("V")){
					setaBooleanNoGaraOt(true);
				}else{setaBooleanNoGaraOt(false);}
				
				if(getDetalleTO().getEsPrioritaria().equalsIgnoreCase("V")){
					setaPrioridad(true);
				}else{setaPrioridad(false);}
				
				if(getDetalleTO().getEnRegistro().equalsIgnoreCase("V")){
					setaRegistro(true);
				}else{setaRegistro(false);}
				
				habilitaobligacion=true;			
				int valor = getDetalleTO().getIdtipogarantia();	
				switch(valor){					
						case 8:
							setTitulofecha("Fecha de surgimiento del Derecho de Retenci�n ");
							habilitaobligacion=false;
							break;
						case 9:
							setTitulofecha("Fecha de surgimiento del Derecho de Retenci�n ");
							habilitaobligacion=false;						
							break;
						case 12:
							setTitulofecha("Fecha de surgimiento del del Privilegio Especial ");
							setTituloacto(" Acto, Contrato o Fundamento Legal del cual surge el Privilegio Especial");
							habilitaobligacion=true;						
							break;	
						default :
							setTitulofecha("Fecha de celebraci�n del Acto o Contrato :");
							habilitaobligacion=true;						
							break;
									
					}
				setAutoridad(jserv.showJuezTram(getIdTramiteAnt()));
			    setDetalleBasaTO(detserv.getDatosBasa(getIdGarantiaAnt(),getIdTramiteAnt()));
			    
			    if(datosGaranTO.getIdTipoAsiento().intValue()==2){
			    	datosAnotacion=detserv.getDatosAnotacion(getIdTramiteAnt());	    	
			    }
			    
			}catch(Exception e){
				e.printStackTrace();
			}
		} 
		else{
			setHaySiguiente(false);
		}
		regresa =Constants.SUCCESS;	
		return regresa;
		
	}
	
	public String anterior(){
		DetalleTO elem = new DetalleTO();
		String regresa = "failed";
		Integer idTramiteAct=(Integer) sessionMap.get(Constants.ID_TRAMITE);
		Integer idGarantiaAct=(Integer) sessionMap.get(Constants.ID_GARANTIA);
		setIdGarantia(idGarantiaAct.toString());
		setIdTramite(idTramiteAct.toString());
		Integer idTramiteAnt=(Integer) sessionMap.get(Constants.ID_TRAMITE_ACTUAL);
		setIdTramiteAnt(idTramiteAnt);
		Integer idGarantiaAnt=(Integer) sessionMap.get(Constants.ID_GARANTIA);
		setIdGarantiaAnt(idGarantiaAnt);
		Integer posicion = posTramite(getIdGarantiaAnt(),getIdTramiteAnt());
		if(posicion < (getLongitud()-1)){
			if ((posicion + 1)==(getLongitud()-1)){
				setHayAnterior(false);
			}
			else{
				setHayAnterior(true);
			}
			if ((posicion + 1)>0){
				setHaySiguiente(true);
			}
			elem= getTramiteTOs().get(posicion+1);
			setIdTramiteAnt(elem.getIdTramite());
			sessionMap.put(Constants.ID_TRAMITE_ACTUAL,getIdTramiteAnt());
			try{
				
				String DATE_FORMAT = "dd/MM/yyyy";
			    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				DetalleServiceImpl detserv = new DetalleServiceImpl();
				setDatosGaranTO(detserv.datosGaran(getIdGarantiaAnt(), getIdTramiteAnt()));
				setFechaAsiento(sdf.format(datosGaranTO.getFechaAsiento()));
				setFechaInscripcion(sdf.format(datosGaranTO.getFechaInsc()));
				setFechaUltAsiento(sdf.format(datosGaranTO.getFechUltAsiento()));
				
				ModificacionServiceImp detservmod = new ModificacionServiceImp();
				JuezService jserv = new JuezService();
				noHayCancel= detserv.noHayCancel(getIdGarantiaAnt());
				vigenciaValida= detserv.vigenciaValida(getIdGarantiaAnt());
				setNomAcreedor(detserv.showAcreedorRepresentado(getIdTramiteAnt()));
				setAcreedorTOs(detserv.getAcreedor(getIdGarantiaAnt(),getIdTramiteAnt()));
				if (getAcreedorTOs().size()>0){
					setHayAcreedores(true);
				}else{
					setHayAcreedores(false);
				}
				setBienesEspTOs(detserv.getListaBienes(getIdTramiteAnt(), 3));
				if (getBienesEspTOs().size()>0){
					setHayBienes(true);
				}else{
					setHayBienes(false);
				}
				setOtorganteTOs(detserv.getOtorgante(getIdGarantiaAnt(),getIdTramiteAnt()));
				if (getOtorganteTOs().size()>0){
					setHayOtorgantes(true);
				}else{
					setHayOtorgantes(false);
				}
				setDeudorTOs(detserv.getDeudor(getIdGarantiaAnt(),getIdTramiteAnt()));
				setDetalleTO(detserv.getDetalle(getIdGarantiaAnt(),getIdTramiteAnt()));	
				if(getDetalleTO() == null || getDetalleTO().getRelacionbien() == null){
					setDetalleTO(detserv.getTramiteCancelacion(getIdTramiteAnt()));
					setDetalleTO(detserv.getTramiteActualizado(getDetalleTO().getIdTramite()));
					setDetalleTO(detserv.getDetalle(getDetalleTO().getIdgarantia(),getDetalleTO().getIdTramite()));
				}
				if(getDetalleTO() != null && getDetalleTO().getRelacionbien() != null){
					setBienesTOs(detservmod.getBienes(getIdGarantiaAnt(),detalleTO.getRelacionbien()));
				}
				setTramiteTOs(detserv.getTramites(getIdGarantiaAnt(),getIdTramiteAnt()));
				
				setTextosFormulario(inscripcionService.getTextosFormularioByIdGarantia(getDetalleTO().getIdtipogarantia()));
								
				if(getDetalleTO().getCambio().equalsIgnoreCase("V")){
					setaBoolean(true);
				}else{setaBoolean(false);}
				
				if(getDetalleTO().getNoGarantiaPreviaOt().equalsIgnoreCase("V")){
					setaBooleanNoGaraOt(true);
				}else{setaBooleanNoGaraOt(false);}
				
				if(getDetalleTO().getEsPrioritaria().equalsIgnoreCase("V")){
					setaPrioridad(true);
				}else{setaPrioridad(false);}
				
				if(getDetalleTO().getEnRegistro().equalsIgnoreCase("V")){
					setaRegistro(true);
				}else{setaRegistro(false);}
				
				habilitaobligacion=true;			
				int valor = getDetalleTO().getIdtipogarantia();	
				switch(valor){					
						case 8:
							setTitulofecha("Fecha de surgimiento del Derecho de Retenci�n ");
							habilitaobligacion=false;
							break;
						case 9:
							setTitulofecha("Fecha de surgimiento del Derecho de Retenci�n ");
							habilitaobligacion=false;						
							break;
						case 12:
							setTitulofecha("Fecha de surgimiento del del Privilegio Especial ");
							setTituloacto(" Acto, Contrato o Fundamento Legal del cual surge el Privilegio Especial");
							habilitaobligacion=true;						
							break;	
						default :
							setTitulofecha("Fecha de celebraci�n del Acto o Contrato :");
							habilitaobligacion=true;						
							break;
									
					}
				setAutoridad(jserv.showJuezTram(getIdTramiteAnt()));
			    setDetalleBasaTO(detserv.getDatosBasa(getIdGarantiaAnt(),getIdTramiteAnt()));
			    
			    if(datosGaranTO.getIdTipoAsiento().intValue()==2){
			    	datosAnotacion=detserv.getDatosAnotacion(getIdTramiteAnt());	    	
			    }
			    
			}catch(Exception e){
				e.printStackTrace();
			}
		}    
		else{
			setHayAnterior(false);
		}
		regresa =Constants.SUCCESS;	
		return regresa;
	}
	
	public String  crearDetalle () {
		String regresa="failed";
		try{
			if (getBvalida()==null){
				setBvalida("0");
			}else{
				if (!getBvalida().equals("1")){
					setBvalida("0");
				}
			}
			sessionMap.put(Constants.ID_TRAMITE_ACTUAL, Integer.parseInt(getIdTramite()));
			sessionMap.put(Constants.ID_TRAMITE, Integer.parseInt(getIdTramite()));
			sessionMap.put(Constants.ID_GARANTIA, Integer.parseInt(getIdGarantia()));
			regresa = Constants.SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("DetalleAction.crearDetalle " + regresa);
		return regresa;
	}
	
	public String inicia(){
	    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_DDMMYY);
		String regresa = Constants.FAILED;
		Integer idTramiteAct=(Integer) sessionMap.get(Constants.ID_TRAMITE);
		Integer idGarantiaAct=(Integer) sessionMap.get(Constants.ID_GARANTIA);
		setIdGarantia(idGarantiaAct.toString());
		setIdTramite(idTramiteAct.toString());
                
 
		if (getBvalida().equals("1")){
			setVieneFirma(true);
		}else{
			setVieneFirma(false);
		}
		
		try{
			Integer tramite = new Integer(getIdTramite());
			posTramite(Integer.parseInt(getIdGarantia()),Integer.parseInt(getIdTramite()));
			DetalleServiceImpl detserv = new DetalleServiceImpl();
			setDatosGaranTO(detserv.datosGaran(Integer.parseInt(getIdGarantia()), Integer.parseInt(getIdTramite())));
			
			setFechaAsiento(sdf.format(datosGaranTO.getFechaAsiento()));
			setFechaInscripcion(sdf.format(datosGaranTO.getFechaInsc()));
			setFechaUltAsiento(sdf.format(datosGaranTO.getFechUltAsiento()));
			ModificacionServiceImp detservmod = new ModificacionServiceImp();
			JuezService jserv = new JuezService();
//----------------------vigenciaValida--------------------------------------------------------------------------------------------------------		
			vigenciaValida= detserv.vigenciaValida(Integer.parseInt(getIdGarantia()));
			//if(vigenciaValida){
			noHayCancel= detserv.noHayCancel(Integer.parseInt(getIdGarantia()));	
			/*}else{
				noHayCancel=false;
			}*/
//----------------------vigenciaValida--------------------------------------------------------------------------------------------------------		
			//setNomAcreedor(detserv.showAcreedorRepresentado(tramite));
			setNomAcreedor(detserv.showAcreedorRepresentadoNew(tramite));
			
			
			Integer idGarantia = new Integer(getIdGarantia()); 
			Integer idAcreedor= 	detserv.getIdAcreedorByIdTramite(tramite);
			sessionMap.put(Constants.ID_ACREEDOR_REPRESENTADO, idAcreedor);
			setAcreedorTOs(detserv.getAcreedor(idGarantia,tramite));
			if (getAcreedorTOs().size()>0){
				setHayAcreedores(true);
			}else{
				setHayAcreedores(false);
			}
			setBienesEspTOs(detserv.getListaBienes(tramite, 3));
			if (getBienesEspTOs().size()>0){
				setHayBienes(true);
			}else{
				setHayBienes(false);
			}
			setOtorganteTOs(detserv.getOtorgante(idGarantia,tramite));
			if (getOtorganteTOs().size()>0){
				setHayOtorgantes(true);
			}else{
				setHayOtorgantes(false);
			}
			setDeudorTOs(detserv.getDeudor(idGarantia,tramite));
			setDetalleTO(detserv.getDetalle(idGarantia,tramite));	
			
			if(getDetalleTO() == null || getDetalleTO().getRelacionbien() == null){
				setDetalleTO(detserv.getTramiteCancelacion(tramite));
				setDetalleTO(detserv.getTramiteActualizado(getDetalleTO().getIdTramite()));
				setDetalleTO(detserv.getDetalle(getDetalleTO().getIdgarantia(),getDetalleTO().getIdTramite()));
			}
			if(getDetalleTO() != null && getDetalleTO().getRelacionbien() != null){
				setBienesTOs(detservmod.getBienes(idGarantia,detalleTO.getRelacionbien()));
			}
			setTramiteTOs(detserv.getTramites(idGarantia,tramite));
			setLongitud(tramiteTOs.size());
			//corellana
                        setEsLeasing(detalleTO.getIdtipogarantia()==16);
                        
                        
			setTextosFormulario(inscripcionService.getTextosFormularioByIdGarantia(getDetalleTO().getIdtipogarantia()));
			
			if(getDetalleTO().getCambio().equalsIgnoreCase("V")){
				setaBoolean(true);
			}else{setaBoolean(false);}
			
			if(getDetalleTO().getNoGarantiaPreviaOt().equalsIgnoreCase("V")){
				setaBooleanNoGaraOt(true);
			}else{setaBooleanNoGaraOt(false);}
			
			if(getDetalleTO().getEsPrioritaria().equalsIgnoreCase("V")){
				setaPrioridad(true);
			}else{setaPrioridad(false);}
			
			if(getDetalleTO().getEnRegistro().equalsIgnoreCase("V")){
				setaRegistro(true);
			}else{setaRegistro(false);}
			
			habilitaobligacion=true;			
			int valor = getDetalleTO().getIdtipogarantia();	
			switch(valor){					
					case 8:
						setTitulofecha("Fecha de surgimiento del Derecho de Retenci�n ");
						habilitaobligacion=false;
						break;
					case 9:
						setTitulofecha("Fecha de surgimiento del Derecho de Retenci�n ");
						habilitaobligacion=false;						
						break;
					case 12:
						setTitulofecha("Fecha de surgimiento del del Privilegio Especial ");
						setTituloacto(" Acto, Contrato o Fundamento Legal del cual surge el Privilegio Especial");
						habilitaobligacion=true;						
						break;	
					default :
						setTitulofecha("Fecha de celebraci�n del Acto o Contrato :");
						habilitaobligacion=true;						
						break;
								
				}
			setAutoridad(jserv.showJuezTram(tramite));
		    setDetalleBasaTO(detserv.getDatosBasa(idGarantia, tramite));
		    
		    if(datosGaranTO.getIdTipoAsiento().intValue()==2){
		    	datosAnotacion=detserv.getDatosAnotacion(tramite);	    	
		    }
		    
		    
			regresa = Constants.SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
		}
		return regresa;
	}
	
	public String consulta(){
		String regresa = Constants.FAILED;
		try{
			int idgarantia = Integer.parseInt(getIdGarantia()); 
			int Idtramite = Integer.parseInt(getIdTramite());
			DetalleServiceImpl detserv = new DetalleServiceImpl();
			setAcreedorTOs(detserv.getAcreedor(idgarantia, Idtramite));
			setOtorganteTOs(detserv.getOtorgante(idgarantia, Idtramite));
			setDeudorTOs(detserv.getDeudor(idgarantia, Idtramite));
			setDetalleTO(detserv.getDetalle(idgarantia, Idtramite));
			setTramiteTOs(detserv.getTramites(idgarantia,Idtramite));
			regresa = Constants.SUCCESS;
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
	public String getFechaInscripcion() {
		return fechaInscripcion;
	}

	public void setFechaInscripcion(String fechaInscripcion) {
		this.fechaInscripcion = fechaInscripcion;
	}

	public String getFechaAsiento() {
		return fechaAsiento;
	}

	public void setFechaAsiento(String fechaAsiento) {
		this.fechaAsiento = fechaAsiento;
	}

	public String getFechaUltAsiento() {
		return fechaUltAsiento;
	}

	public void setFechaUltAsiento(String fechaUltAsiento) {
		this.fechaUltAsiento = fechaUltAsiento;
	}

	public Integer getLongitud() {
		return longitud;
	}

	public void setLongitud(Integer longitud) {
		this.longitud = longitud;
	}

	public Boolean getNoHayCancel() {
		return noHayCancel;
	}
	public void setNoHayCancel(Boolean noHayCancel) {
		this.noHayCancel = noHayCancel;
	}
	public String getTitulofecha() {
		return titulofecha;
	}
	public void setTitulofecha(String titulofecha) {
		this.titulofecha = titulofecha;
	}

	public List<TipoBienTO> getBienesTOs() {
		return bienesTOs;
	}
	public void setBienesTOs(List<TipoBienTO> bienesTOs) {
		this.bienesTOs = bienesTOs;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public static String getSuccess() {
		return SUCCESS;
	}
	public String getNomAcreedor() {
		return nomAcreedor;
	}
	public void setNomAcreedor(String nomAcreedor) {
		this.nomAcreedor = nomAcreedor;
	}
	public void setAcreedorTOs(List<PartesTO> acreedorTOs) {
		AcreedorTOs = acreedorTOs;
	}
	public List<PartesTO> getAcreedorTOs() {
		return AcreedorTOs;
	}
	public void setDeudorTOs(List<PartesTO> deudorTOs) {
		DeudorTOs = deudorTOs;
	}
	public List<PartesTO> getDeudorTOs() {
		return DeudorTOs;
	}	
	public void setDetalleTO(DetalleTO detalleTO) {
		this.detalleTO = detalleTO;
	}
	public DetalleTO getDetalleTO() {
		return detalleTO;
	}
	public void setOtorganteTOs(List<PartesTO> otorganteTOs) {
		OtorganteTOs = otorganteTOs;
	}
	public List<PartesTO> getOtorganteTOs() {
		return OtorganteTOs;
	}

	public void setIdGarantia(String idGarantia) {
		this.idGarantia = idGarantia;
	}
	public String getIdGarantia() {
		return idGarantia;
	}
	public void setTramiteTOs(List<DetalleTO> tramiteTOs) {
		this.tramiteTOs = tramiteTOs;
	}
	
	public List<DetalleTO> getTramiteTOs() {
		return tramiteTOs;
	}
	
	public void setIdTramite(String idTramite) {
		this.idTramite = idTramite;
	}
	public String getIdTramite() {
		return idTramite;
	}

	public void setHabilitaobligacion(boolean habilitaobligacion) {
		this.habilitaobligacion = habilitaobligacion;
	}
	public boolean isHabilitaobligacion() {
		return habilitaobligacion;
	}
	public void setHayAcreedores(boolean hayAcreedores) {
		this.hayAcreedores = hayAcreedores;
	}
	public boolean isHayAcreedores() {
		return hayAcreedores;
	}
	public void setHayOtorgantes(boolean hayOtorgantes) {
		this.hayOtorgantes = hayOtorgantes;
	}
	public boolean isHayOtorgantes() {
		return hayOtorgantes;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getExt() {
		return ext;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	public void setAutoridad(String autoridad) {
		this.autoridad = autoridad;
	}
	public String getAutoridad() {
		return autoridad;
	}
	public void setHayautoridad(boolean hayautoridad) {
		this.hayautoridad = hayautoridad;
	}
	public boolean isHayautoridad() {
		return hayautoridad;
	}
	public void setaBoolean(boolean aBoolean) {
		this.aBoolean = aBoolean;
	}
	public boolean isaBoolean() {
		return aBoolean;
	}
	public void setDetalleBasaTO(DetalleTO detalleBasaTO) {
		this.detalleBasaTO = detalleBasaTO;
	}
	public DetalleTO getDetalleBasaTO() {
		return detalleBasaTO;
	}
	public void setTituloacto(String tituloacto) {
		this.tituloacto = tituloacto;
	}
	public String getTituloacto() {
		return tituloacto;
	}

	public DatosGaranTO getDatosGaranTO() {
		return datosGaranTO;
	}

	public void setDatosGaranTO(DatosGaranTO datosGaranTO) {
		this.datosGaranTO = datosGaranTO;
	}

	public DetalleTO getElem() {
		return elem;
	}

	public void setElem(DetalleTO elem) {
		this.elem = elem;
	}

	public Integer getIdGarantiaAnt() {
		return idGarantiaAnt;
	}


	public void setIdGarantiaAnt(Integer idGarantiaAnt) {
		this.idGarantiaAnt = idGarantiaAnt;
	}


	public Integer getIdTramiteAnt() {
		return idTramiteAnt;
	}


	public void setIdTramiteAnt(Integer idTramiteAnt) {
		this.idTramiteAnt = idTramiteAnt;
	}


	public Boolean getHayAnterior() {
		return hayAnterior;
	}


	public void setHayAnterior(Boolean hayAnterior) {
		this.hayAnterior = hayAnterior;
	}
        
        public Boolean getEsLeasing() {
		return esLeasing;
	}


	public void setEsLeasing(Boolean esLeasing) {
		this.esLeasing = esLeasing;
	}


	public Boolean getHaySiguiente() {
		return haySiguiente;
	}


	public void setHaySiguiente(Boolean haySiguiente) {
		this.haySiguiente = haySiguiente;
	}


	public void setBvalida(String bvalida) {
		this.bvalida = bvalida;
	}


	public String getBvalida() {
		return bvalida;
	}


	public void setVieneFirma(Boolean vieneFirma) {
		this.vieneFirma = vieneFirma;
	}


	public Boolean getVieneFirma() {
		return vieneFirma;
	}

	public DatosAnotacion getDatosAnotacion() {
		return datosAnotacion;
	}

	public void setDatosAnotacion(DatosAnotacion datosAnotacion) {
		this.datosAnotacion = datosAnotacion;
	}

	public boolean isaBooleanNoGaraOt() {
		return aBooleanNoGaraOt;
	}

	public void setaBooleanNoGaraOt(boolean aBooleanNoGaraOt) {
		this.aBooleanNoGaraOt = aBooleanNoGaraOt;
	}

	public boolean isaPrioridad() {
		return aPrioridad;
	}

	public void setaPrioridad(boolean aPrioridad) {
		this.aPrioridad = aPrioridad;
	}

	public boolean isaRegistro() {
		return aRegistro;
	}

	public void setaRegistro(boolean aRegistro) {
		this.aRegistro = aRegistro;
	}

	public List<BienEspecialTO> getBienesEspTOs() {
		return bienesEspTOs;
	}

	public void setBienesEspTOs(List<BienEspecialTO> bienesEspTOs) {
		this.bienesEspTOs = bienesEspTOs;
	}

	public boolean isHayBienes() {
		return hayBienes;
	}

	public void setHayBienes(boolean hayBienes) {
		this.hayBienes = hayBienes;
	}

	public Boolean getVigenciaValida() {
		return vigenciaValida;
	}

	public void setVigenciaValida(Boolean vigenciaValida) {
		this.vigenciaValida = vigenciaValida;
	}

	public List<String> getTextosFormulario() {
		return textosFormulario;
	}

	public void setTextosFormulario(List<String> textosFormulario) {
		this.textosFormulario = textosFormulario;
	}

	public InscripcionService getInscripcionService() {
		return inscripcionService;
	}

	public void setInscripcionService(InscripcionService inscripcionService) {
		this.inscripcionService = inscripcionService;
	}
	
	
}
