package mx.gob.se.rug.action;

/*
 * @autor Erika Astorga Rodríguez
 * */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.acreedores.service.impl.AcreedoresServiceImpl;
import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.detallegarantia.service.impl.DetalleServiceImpl;
import mx.gob.se.rug.detallegarantia.to.DetalleTO;
import mx.gob.se.rug.detallegarantia.to.PartesTO;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.service.impl.InscripcionServiceImpl;
import mx.gob.se.rug.inscripcion.to.InscripcionTO;
import mx.gob.se.rug.inscripcion.to.MonedaTO;
import mx.gob.se.rug.inscripcion.to.TipoBienTO;
import mx.gob.se.rug.inscripcion.to.TipoGarantiaTO;
import mx.gob.se.rug.juez.service.JuezService;
import mx.gob.se.rug.modificacion.service.impl.ModificacionServiceImp;
import mx.gob.se.rug.modificacion.to.ModificacionTO;
import mx.gob.se.rug.modificacion.to.TipoBienesTO;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.transmision.serviceimp.TransmisionServiceImp;
import mx.gob.se.rug.transmision.to.TransmisionTO;
import mx.gob.se.rug.util.MyLogger;
import mx.gob.se.rug.util.to.DateUtilRug;

@SuppressWarnings("serial")
public class RectificacionAction extends RugBaseAction{
	
	private UsuarioTO usuario;
	private String idGarantia;

	private List<PartesTO> acreedorTOs;
	private List<PartesTO> deudorTOs;
	private List<TipoBienesTO> tipoBienTOs;
	private List<TipoGarantiaTO> tipoGarantiaTOs;
	private DetalleTO detalleTO;
	private TransmisionTO transmiTOs;	
	private ModificacionTO modificaTO;
	private InscripcionTO inscripcionTO;
	private InscripcionService inscripcionService ;	
	
	private String idTramite;
	private String modlimite;
	private String modtipoacto;
	private String modfechaceleb;
	private String modfechacelebcontrato;
	private String modterminos;
	private String modotrosterminos;	
	private String modotrosgarantia;
	private String modtipobien;
	private String moddescripcion;
	private String modotroscontrato;
	private List<TipoBienTO> bienes;
	private String nomAcreedor;
	private Integer idparte;
	private Integer idtramiteinc; 
	private boolean hayOtorgante;
	private String idpersona;
	private String idtipopersona;
	private Integer idColonia;
	private String tramite;	
	private boolean hayAcreedores;
	private String autoridad;
	private List<MonedaTO> listaMonedas;
	private Integer modtipogarantia;
	private String modfechatermino;
	private String modmonto;
	private Integer moneda;
	private Integer idTipoMoneda;
	private String modinstrumento;
	private String FechaModificacion;
	private String fechaFinOb;
	private boolean aBoolean;
	private String fechaCelebOb;
	private String tipoContOb;
	private String otrosTermOb;
	private String fechaCelebracionC;
	private boolean cambio;
	private boolean habilitaobligacion;
	private String titulofecha;
	private String perfil;
	private AcreedorTO acreedorRepresentado;
	private List<AcreedorTO> acreedoresDisponibles;
	private String acreedorTransmite;
	private String modtipoant;
	private String autoridadInstruyeTramite;
	private String autoridadInstruyeTramiteTitulo;
	
	public String rectificacion(){
		String regresa = "failed";
		String DATE_FORMAT = "dd/MM/yyyy";
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		try{		
			 
			Integer idgarantia = (Integer) sessionMap.get(Constants.ID_GARANTIA);
			Integer idTramite = (Integer) sessionMap.get(Constants.ID_TRAMITE);
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);	
						
			MyLogger.Logger.log(Level.INFO, "idgarantia " + idgarantia);
			MyLogger.Logger.log(Level.INFO, "idTramite " + idTramite);			
			
			AcreedoresServiceImpl acreedoresServiceImpl = new AcreedoresServiceImpl(); 
			DetalleServiceImpl detservm = new DetalleServiceImpl();			
			TransmisionServiceImp detserv = new TransmisionServiceImp();
			ModificacionTO modificaTO  =new ModificacionTO();			
			inscripcionTO = new InscripcionTO();
		//	JuezService jserv = new JuezService();
			
			ModificacionServiceImp detservmod = new ModificacionServiceImp();			
			InscripcionService inscripcionService = new InscripcionServiceImpl();
			
			// Funcionalidad para obtener el utlimo id Tramite registrado y evitar
			// la mezcla de hotcakes
			Integer idUltimoTramite = detservm.getIdUltimoTramite(idgarantia);
			if(idUltimoTramite != idTramite ){
				idTramite = idUltimoTramite;
			}
			
			
			setIdpersona(new Integer(usuario.getPersona().getIdPersona()).toString());
			setNomAcreedor(detservm.showAcreedorR(idgarantia));
			
			// traemos la informacion del acreedor representado
			setAcreedorRepresentado(detservm.showAcreedorDetalle(idgarantia));
			// traemos la lista de acreedores posibles para la transmision
			String idAcreedorNoDisponible = getAcreedorRepresentado().getIdAcreedor();
			setAcreedoresDisponibles(acreedoresServiceImpl.getAcreedoresDisponiblesTransmision(getIdpersona(), idAcreedorNoDisponible));
			
			
			setAcreedorTOs(detserv.getAcreedor(idgarantia,idTramite));
			if (getAcreedorTOs().size()>0){
				setHayAcreedores(true);
			}else{
				setHayAcreedores(false);
			}
			
			setDeudorTOs(detserv.getDeudor(idgarantia,idTramite));
			setTipoBienTOs(detserv.getTipoBienes());
			setTipoGarantiaTOs(detserv.getTiposGarantia());			
			setListaMonedas(inscripcionService.getMonedas());
			
			setDetalleTO(detservmod.getDetalleModificacion(idgarantia,idTramite));
			
			if( getDetalleTO().getRelacionbien()!=0){
				setBienes(detservmod.getBienes(idgarantia, getDetalleTO().getRelacionbien()));
			}else{
				setBienes(detservmod.getBienes(idgarantia,idTramite));
			}
			
			setModtipogarantia(getDetalleTO().getIdtipogarantia());			
			modificaTO.setIdtramite(6);
			modificaTO.setIdgarantia(getDetalleTO().getIdgarantia());
			modificaTO.setIdcontrato(getDetalleTO().getIdcontrato());
			modificaTO.setIdpersona(usuario.getPersona().getIdPersona());
			setModtipobien(getDetalleTO().getTiposbienes());
			setModtipoacto(getDetalleTO().getTipocontrato());
			if(getDetalleTO().getDescbienes()==null){
				setModdescripcion("");
			}else{setModdescripcion(getDetalleTO().getDescbienes());}
			
			if(getDetalleTO().getFechaCelebracionObligacion()==null){
				setModfechaceleb("");				
			}else{setModfechaceleb(sdf.format(getDetalleTO().getFechaCelebracionObligacion()));}
			
			if(getDetalleTO().getFechaCelebracionObligacion() == null){
				setFechaCelebracionC("");
			}else{
				setFechaCelebracionC(sdf.format(getDetalleTO().getFechaCelebracionObligacion()));
			}
			
			setModterminos(getDetalleTO().getOtrosterminos());
			setModotroscontrato(getDetalleTO().getOtroscontrato());
			setModlimite(String.valueOf(getDetalleTO().getMonto()));
			setModotrosgarantia(getDetalleTO().getOtrosgarantia());
			if(getDetalleTO().getFechaFinOb()==null){
				setModfechatermino("");
			}else{setModfechatermino(sdf.format(getDetalleTO().getFechaFinOb()));}
			
			if(getDetalleTO().getFecacelebcontrato()==null){
				setModfechacelebcontrato("");				
			}else{setModfechacelebcontrato(sdf.format(getDetalleTO().getFecacelebcontrato()));}
			setModtipogarantia(new Integer (getDetalleTO().getIdtipogarantia()));
			
			if(getDetalleTO().getOtrosterminos()==null){
				setModotrosterminos("");
			}else{setModotrosterminos(getDetalleTO().getOtrosterminos());}
			
			if(getDetalleTO().getFechaFinOb()==null){
				setModfechatermino("");
			}else{setModfechatermino(sdf.format(getDetalleTO().getFechaFinOb()));}
			
			MyLogger.Logger.log(Level.INFO, "setModfechatermino " + getModfechatermino());
			
			if(getDetalleTO().getCambio().equalsIgnoreCase("V")){
				setaBoolean(true);
			}else{setaBoolean(false);}
			
			setModmonto(getDetalleTO().getMonto());
			if(getDetalleTO().getIdmoneda()==null){
				setMoneda(22);
			}else{setMoneda(getDetalleTO().getIdmoneda());}
			
			if(getDetalleTO().getInstrumento()==null){
				setModinstrumento("");
			}else{setModinstrumento(getDetalleTO().getInstrumento());}
			
			if(getDetalleTO().getFechaFinOb()==null){
				setFechaFinOb("");
			}else{setFechaFinOb(sdf.format(getDetalleTO().getFechaFinOb()));}
						
			if(getDetalleTO().getCambio().equals("V")){
				setCambio(true);
			}else{setCambio(false);}
			
			setHabilitaobligacion(true);			
			int valor = getDetalleTO().getIdtipogarantia();	
			//1-7 el mismo
			//8,9 dif fecha
			switch(valor){					
					case 8:
						setTitulofecha("Fecha de surgimiento del Derecho de Retención :");
						setHabilitaobligacion(false);
						break;
					case 9:
						setTitulofecha("Fecha de surgimiento del Derecho de Retención :");
						setHabilitaobligacion(false);						
						break;
					default :
						setTitulofecha("Fecha de celebración del Acto o Contrato :");
						setHabilitaobligacion(true);						
						break;
								
				}
			MyLogger.Logger.log(Level.INFO, "perfil " + usuario.getPersona().getPerfil() );
			if(usuario.getPersona().getPerfil().trim().equals("AUTORIDAD")){
				setPerfil("20");
			}else{setPerfil("0");}
			Integer id = detservmod.altapartesTramiteInc(new Integer(usuario.getPersona().getIdPersona()),new Integer("6"),idgarantia);
			setIdTramite(id.toString());				
			sessionMap.put(Constants.ID_TRAMITE_NUEVO,id);	
			if (id.intValue() > 0){
				regresa = "success";
			}
			
			
			
			
//			//Autoridad Instruye el Tramite a rectificar
//			AutoridadInstruye autoridadInstruye= new AutoridadInstruye();
//			try{
//			autoridadInstruye=detservmod.getAutoridadInstruye(idgarantia);
//			setAutoridadInstruyeTramite(autoridadInstruye.getAnotacionJuez());
//			setAutoridadInstruyeTramiteTitulo(autoridadInstruye.getEtiquetaTipoTramite());
//			}catch(NoDataFoundException dataFoundException){
//			    MyLogger.Logger.log(Level.FINEST,"No se encontro Autoridad",dataFoundException);	
//			}
			
		}catch(Exception e){
			e.printStackTrace();
			regresa=FAILED;
		}
		return regresa;
	}

	public String guarda(){
		String regresa = "failed";
		MyLogger.Logger.log(Level.INFO, "modotrosterminos:"+getModotrosterminos());
		MyLogger.Logger.log(Level.INFO, "ID tramite nuevo: "+(Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO));
		MyLogger.Logger.log(Level.INFO, "ID garantia: "+sessionMap.get(Constants.ID_GARANTIA));
		
		Integer idgarantia = (Integer) sessionMap.get(Constants.ID_GARANTIA);		
		Integer idTramiteNuevoVar = (Integer)sessionMap.get(Constants.ID_TRAMITE_NUEVO);
		usuario = (UsuarioTO) sessionMap.get("usuario");
		
		DateUtilRug dateUtil = new DateUtilRug();
		
		try{
			
			ModificacionServiceImp detserv = new ModificacionServiceImp();
			
			MyLogger.Logger.log(Level.INFO, "Entre a rectifica");
			
			if(modificaTO==null) modificaTO = new ModificacionTO();
			detalleTO = detalleTO == null ? new DetalleTO() : detalleTO;
					
			modificaTO.setIdgarantia(idgarantia);
			modificaTO.setTipogarantia(getModtipogarantia().toString());
			modificaTO.setIdpersona(usuario.getPersona().getIdPersona());
			MyLogger.Logger.log(Level.INFO, "getFechaCelebracionC() " + getFechaCelebracionC());
			modificaTO.setFechacelebgarantia(getFechaCelebracionC().trim().equals("") ? null : dateUtil.parseStrDate1(getFechaCelebracionC()));
			if(getModtipobien()==null){
				modificaTO.setModtipobien(detalleTO.getTiposbienes());
			}else{modificaTO.setModtipobien(getModtipobien()+"|");}
			if(getModdescripcion()==null){
				modificaTO.setModdescripcion(detalleTO.getDescbienes());
			}else{modificaTO.setModdescripcion(getModdescripcion());}
						
			//TIPO CONTRATO OBLIGA			
			modificaTO.setTipocontrato(getModtipoacto());
			
			MyLogger.Logger.log(Level.INFO, "-- INicia ultima seccion --");
			MyLogger.Logger.log(Level.INFO, "modtipoacto:"+getModtipoacto() +"Este era el dato anterior:"+getModtipoant());
			MyLogger.Logger.log(Level.INFO, "modotrosterminos:"+getModotrosterminos());
			MyLogger.Logger.log(Level.INFO, "Fecha de fin q al parecer no llega: "+getModfechatermino());
			MyLogger.Logger.log(Level.INFO, "Fecha de inicio q al parecer no llega: "+getModfechacelebcontrato());
			MyLogger.Logger.log(Level.INFO, "-- termina ultima seccion --");

			modificaTO.setFechainiob(getModfechacelebcontrato().trim().equals("") ? null :  dateUtil.parseStrDate1(getModfechacelebcontrato()));
		
			if(getModotrosterminos()==null){
				modificaTO.setModotroscontrato(detalleTO.getOtroscontrato());
			}else{modificaTO.setModotroscontrato(getModotrosterminos());}
			
			modificaTO.setFechafinob(getModfechatermino().trim().equals("") ? null : dateUtil.parseStrDate1(getModfechatermino()));			
			
			if(getModmonto()==null){
				modificaTO.setModmonto(detalleTO.getMonto());
			}else{modificaTO.setModmonto(String.valueOf(getModmonto()));}
			if(getModotrosgarantia()==null){
				modificaTO.setModotrosgarantia(detalleTO.getOtrosgarantia());
			}else{modificaTO.setModotrosgarantia(getModotrosgarantia());}
			
			//TIPO CONTRATO BASA
			modificaTO.setModtipoacto(null);				
			modificaTO.setModfechaceleb(null);
			modificaTO.setModterminos(null);
			modificaTO.setFechatermino(null);
			modificaTO.setIdmoneda(getIdTipoMoneda());			
			modificaTO.setModinstrumento(getModinstrumento());
			modificaTO.setCambioBienesMonto(getCambio() == true ? "V" : "F" );
			
			if(idTramiteNuevoVar.intValue() !=0){
				
				modificaTO.setIdtramite(idTramiteNuevoVar);
				if(modificaTO != null){	
				modificaTO.setIdgarantia(detserv.modificaRectificacion(modificaTO));
				if (getAutoridad()!= null && !getAutoridad().trim().equals("") && usuario.getPersona().getPerfil().equals("AUTORIDAD")){
					MyLogger.Logger.log(Level.INFO, "-- Entra a dar de alta a la autoridad :P -- ");
					JuezService juezService = new JuezService();
					juezService.insertJuez(Integer.valueOf(idTramiteNuevoVar), getAutoridad());
				}
				int cancelacion = detserv.altaCancelTramite(idTramiteNuevoVar,new Integer(idgarantia), new Integer(usuario.getPersona().getIdPersona()));
				MyLogger.Logger.log(Level.WARNING, "--cancelacion " + cancelacion);
				
				if(!getAcreedorTransmite().equals("-1")){
					MyLogger.Logger.log(Level.WARNING, "--Esta cambiando el acreedor.--");
					// TODO: PL3a
					detserv.alta_Parte_Rectifica(Integer.parseInt(getAcreedorTransmite()),idTramiteNuevoVar);
				}
				
				
//				////Autoridad Instruye el Tramite a rectificar
//				try{
//					detserv.getAutoridadInstruye(idgarantia);
//					detserv.saveAutoridadInstruyeTramite(modificaTO.getIdtramite(), autoridadInstruyeTramite);
//					}catch(NoDataFoundException dataFoundException){
//					    MyLogger.Logger.log(Level.FINEST,"No se encontro Autoridad no se guarda",dataFoundException);	
//					}
//				
				
				
				if (cancelacion != 0){
					boolean valor = detserv.altaBitacoraTramite(modificaTO.getIdtramite(), 5, 0, null, "V");
					MyLogger.Logger.log(Level.INFO, "alta bitacora ."+ valor);							
					setIdTramite(String.valueOf(idTramiteNuevoVar));
					regresa = "success";
				}
				
			}
		}
		
		}catch(Exception e){
			regresa = "failed";
			e.printStackTrace();
		}
		return regresa;
	}
	
	
	public String getModfechatermino() {
		return modfechatermino;
	}

	public void setModfechatermino(String modfechatermino) {
		this.modfechatermino = modfechatermino;
	}

	public String getFechaFinOb() {
		return fechaFinOb;
	}

	public void setFechaFinOb(String fechaFinOb) {
		this.fechaFinOb = fechaFinOb;
	}

	public boolean isaBoolean() {
		return aBoolean;
	}

	public void setaBoolean(boolean aBoolean) {
		this.aBoolean = aBoolean;
	}

	public Integer getModtipogarantia() {
		return modtipogarantia;
	}

	
	
	
	public void setModtipogarantia(Integer modtipogarantia) {
		this.modtipogarantia = modtipogarantia;
	}

	public String getAutoridad() {
		return autoridad;
	}

	public void setAutoridad(String autoridad) {
		this.autoridad = autoridad;
	}
	public UsuarioTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioTO usuario) {
		this.usuario = usuario;
	}

	public InscripcionTO getInscripcionTO() {
		return inscripcionTO;
	}

	public void setInscripcionTO(InscripcionTO inscripcionTO) {
		this.inscripcionTO = inscripcionTO;
	}
	public String getIdTramite() {
		return idTramite;
	}

	public void setIdTramite(String idTramite) {
		this.idTramite = idTramite;
	}
		

		public void setIdGarantia(String idGarantia) {
			this.idGarantia = idGarantia;
		}

		public String getIdGarantia() {
			return idGarantia;
		}
		public void setInscripcionService(InscripcionService inscripcionService) {
			this.inscripcionService = inscripcionService;
		}

		public InscripcionService getInscripcionService() {
			return inscripcionService;
		}
	
	

		public void setModotrosgarantia(String modotrosgarantia) {
		this.modotrosgarantia = modotrosgarantia;
	}

	public String getModotrosgarantia() {
		return modotrosgarantia;
	}

		public void setModotrosterminos(String modotrosterminos) {
		this.modotrosterminos = modotrosterminos;
	}

	public String getModotrosterminos() {
		return modotrosterminos;
	}


	public void setModtipobien(String modtipobien) {
			this.modtipobien = modtipobien;
		}

		public String getModtipobien() {
			return modtipobien;
		}

	public void setModterminos(String modterminos) {
			this.modterminos = modterminos;
		}

		public String getModterminos() {
			return modterminos;
		}

	public void setModfechacelebcontrato(String modfechacelebcontrato) {
			this.modfechacelebcontrato = modfechacelebcontrato;
		}

		public String getModfechacelebcontrato() {
			return modfechacelebcontrato;
		}

	public void setModfechaceleb(String modfechaceleb) {
			this.modfechaceleb = modfechaceleb;
		}

		public void setTipoBienTOs(List<TipoBienesTO> tipoBienTOs) {
		this.tipoBienTOs = tipoBienTOs;
	}

	public List<TipoBienesTO> getTipoBienTOs() {
		return tipoBienTOs;
	}		
	
	public void setDetalleTO(DetalleTO detalleTO) {
		this.detalleTO = detalleTO;
	}

	public DetalleTO getDetalleTO() {
		return detalleTO;
	}

		public void setTransmiTOs(TransmisionTO transmiTOs) {
		this.transmiTOs = transmiTOs;
	}

	public TransmisionTO getTransmiTOs() {
		return transmiTOs;
	}

	public void setModificaTO(ModificacionTO modificaTO) {
		this.modificaTO = modificaTO;
	}

	public ModificacionTO getModificaTO() {
		return modificaTO;
	}

		public String getModfechaceleb() {
			return modfechaceleb;
		}

	public void setTipoGarantiaTOs(List<TipoGarantiaTO> tipoGarantiaTOs) {
			this.tipoGarantiaTOs = tipoGarantiaTOs;
		}

		public List<TipoGarantiaTO> getTipoGarantiaTOs() {
			return tipoGarantiaTOs;
		}

	public void setModdescripcion(String moddescripcion) {
			this.moddescripcion = moddescripcion;
		}

		public String getModdescripcion() {
			return moddescripcion;
		}

	public void setModtipoacto(String modtipoacto) {
			this.modtipoacto = modtipoacto;
		}

		public void setIdColonia(Integer idColonia) {
		this.idColonia = idColonia;
	}

	public Integer getIdColonia() {
		return idColonia;
	}

		public void setHayAcreedores(boolean hayAcreedores) {
		this.hayAcreedores = hayAcreedores;
	}

	public boolean isHayAcreedores() {
		return hayAcreedores;
	}

		public void setModotroscontrato(String modotroscontrato) {
		this.modotroscontrato = modotroscontrato;
	}

	public String getModotroscontrato() {
		return modotroscontrato;
	}

		public String getModtipoacto() {
			return modtipoacto;
		}

	public void setBienes(List<TipoBienTO> bienes) {
			this.bienes = bienes;
		}

		public void setNomAcreedor(String nomAcreedor) {
		this.nomAcreedor = nomAcreedor;
	}

	public void setDeudorTOs(List<PartesTO> deudorTOs) {
			this.deudorTOs = deudorTOs;
		}

		public List<PartesTO> getDeudorTOs() {
			return deudorTOs;
		}

	public void setIdtramiteinc(Integer idtramiteinc) {
			this.idtramiteinc = idtramiteinc;
		}

		public Integer getIdtramiteinc() {
			return idtramiteinc;
		}

	public String getNomAcreedor() {
		return nomAcreedor;
	}

		public void setIdparte(Integer idparte) {
		this.idparte = idparte;
	}

	public Integer getIdparte() {
		return idparte;
	}

		public List<TipoBienTO> getBienes() {
			return bienes;
		}

	public void setTramite(String tramite) {
			this.tramite = tramite;
		}

		public String getTramite() {
			return tramite;
		}

	public void setIdpersona(String idpersona) {
			this.idpersona = idpersona;
		}

		public String getIdpersona() {
			return idpersona;
		}

	public void setIdtipopersona(String idtipopersona) {
			this.idtipopersona = idtipopersona;
		}

		public String getIdtipopersona() {
			return idtipopersona;
		}

	public void setHayOtorgante(boolean hayOtorgante) {
			this.hayOtorgante = hayOtorgante;
		}

		public boolean isHayOtorgante() {
			return hayOtorgante;
		}

	public RectificacionAction(){
		super();
	}
	
	public String  tramiteRectificacion () {
		String regresa="failed";
		try{
			regresa = "success";
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return regresa;
	}

	public Date parseStrDate(String strDate){
		SimpleDateFormat formater = new SimpleDateFormat("MM/dd/yyyy");
		Date date = null;
		try {
			date = formater.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	

	public void setAcreedorTOs(List<PartesTO> acreedorTOs) {
		this.acreedorTOs = acreedorTOs;
	}

	public List<PartesTO> getAcreedorTOs() {
		return acreedorTOs;
	}

	public void setListaMonedas(List<MonedaTO> listaMonedas) {
		this.listaMonedas = listaMonedas;
	}

	public List<MonedaTO> getListaMonedas() {
		return listaMonedas;
	}

	public void setModlimite(String modlimite) {
		this.modlimite = modlimite;
	}

	public String getModlimite() {
		return modlimite;
	}

	public void setModmonto(String modmonto) {
		this.modmonto = modmonto;
	}

	public String getModmonto() {
		return modmonto;
	}

	public void setMoneda(Integer moneda) {
		this.moneda = moneda;
	}

	public Integer getMoneda() {
		return moneda;
	}

	public void setIdTipoMoneda(Integer idTipoMoneda) {
		this.idTipoMoneda = idTipoMoneda;
	}

	public Integer getIdTipoMoneda() {
		return idTipoMoneda;
	}

	public void setFechaModificacion(String fechaModificacion) {
		FechaModificacion = fechaModificacion;
	}

	public String getFechaModificacion() {
		return FechaModificacion;
	}

	public void setModinstrumento(String modinstrumento) {
		this.modinstrumento = modinstrumento;
	}

	public String getModinstrumento() {
		return modinstrumento;
	}

	public void setFechaCelebOb(String fechaCelebOb) {
		this.fechaCelebOb = fechaCelebOb;
	}

	public String getFechaCelebOb() {
		return fechaCelebOb;
	}

	public void setTipoContOb(String tipoContOb) {
		this.tipoContOb = tipoContOb;
	}

	public String getTipoContOb() {
		return tipoContOb;
	}

	public void setOtrosTermOb(String otrosTermOb) {
		this.otrosTermOb = otrosTermOb;
	}

	public String getOtrosTermOb() {
		return otrosTermOb;
	}
	public String getFechaCelebracionC() {
		return fechaCelebracionC;
	}

	public void setFechaCelebracionC(String fechaCelebracionC) {
		this.fechaCelebracionC = fechaCelebracionC;
	}

	public void setCambio(boolean cambio) {
		this.cambio = cambio;
	}

	public boolean getCambio() {
		return cambio;
	}

	public void setHabilitaobligacion(boolean habilitaobligacion) {
		this.habilitaobligacion = habilitaobligacion;
	}

	public boolean isHabilitaobligacion() {
		return habilitaobligacion;
	}

	public void setTitulofecha(String titulofecha) {
		this.titulofecha = titulofecha;
	}

	public String getTitulofecha() {
		return titulofecha;
	}

	public void setAcreedorRepresentado(AcreedorTO acreedorRepresentado) {
		this.acreedorRepresentado = acreedorRepresentado;
	}

	public AcreedorTO getAcreedorRepresentado() {
		return acreedorRepresentado;
	}

	public void setAcreedoresDisponibles(List<AcreedorTO> acreedoresDisponibles) {
		this.acreedoresDisponibles = acreedoresDisponibles;
	}

	public List<AcreedorTO> getAcreedoresDisponibles() {
		return acreedoresDisponibles;
	}

	public void setAcreedorTransmite(String acreedorTransmite) {
		this.acreedorTransmite = acreedorTransmite;
	}

	public String getAcreedorTransmite() {
		return acreedorTransmite;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setModtipoant(String modtipoant) {
		this.modtipoant = modtipoant;
	}

	public String getModtipoant() {
		return modtipoant;
	}

	public String getAutoridadInstruyeTramite() {
		return autoridadInstruyeTramite;
	}

	public void setAutoridadInstruyeTramite(String autoridadInstruyeTramite) {
		this.autoridadInstruyeTramite = autoridadInstruyeTramite;
	}

	public String getAutoridadInstruyeTramiteTitulo() {
		return autoridadInstruyeTramiteTitulo;
	}

	public void setAutoridadInstruyeTramiteTitulo(
			String autoridadInstruyeTramiteTitulo) {
		this.autoridadInstruyeTramiteTitulo = autoridadInstruyeTramiteTitulo;
	}
	
	
	
	
}

	
	
	
	
	
	
	
	
	