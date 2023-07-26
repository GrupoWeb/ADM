package mx.gob.se.rug.action;


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
import mx.gob.se.rug.inscripcion.dao.AltaParteDAO;
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
import mx.gob.se.rug.fwk.action.RugBaseAction;

@SuppressWarnings("serial")
public class TransmisionAction extends RugBaseAction{
	
	private UsuarioTO usuario;
	private String idGarantia;
	
	private List<PartesTO> deudorTOs;
	private List<TipoBienesTO> tipoBienTOs;
	private List<TipoGarantiaTO> tipoGarantiaTOs;
	private DetalleTO detalleTO;
	private TransmisionTO transmiTOs;	
	private ModificacionTO modificaTO;
	private InscripcionTO inscripcionTO;
	private InscripcionService inscripcionService ;	
	private boolean hayDeudores;
	
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
	private String idpersona;
	private String idtipopersona;
	private Integer idColonia;
	private boolean hayOtorgante;
	private String nomAcreedor;
	private Integer idparte;
	private Integer idtramiteinc;	
	private String tramite;
	private List<TipoBienTO> bienes;
	private Integer modtipogarantia;
	private String autoridad;
	private List<MonedaTO> listaMonedas;
	private Integer idTipoMoneda;
	private Integer moneda;
	private Integer idTramiteTemporal;
	private String modfechatermino;
	private String fechaFinOb;
	private boolean aBoolean;
	
	private AcreedorTO acreedorRepresentado;
	private List<AcreedorTO> acreedoresDisponibles;
	private String acreedorTransmite;
	private String fechaCelebracionC;
	
		public TransmisionAction(){
			super();		
		}		
		

		

		public String  tramiteTransmision () {
			String regresa="failed";
			try{
				regresa = "success";
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return regresa;
		}
		
		public String transmision(){
			String regresa = "failed";
			String DATE_FORMAT = "dd/MM/yyyy";
		    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		    MyLogger.Logger.log(Level.INFO, "Entro en trasmicion");
			Integer idgarantia = (Integer) sessionMap.get(Constants.ID_GARANTIA);
			Integer tramite = (Integer) sessionMap.get(Constants.ID_TRAMITE);
			MyLogger.Logger.log(Level.INFO, "idgarantia " + idgarantia);
			MyLogger.Logger.log(Level.INFO, "tramite n_n " + tramite);			
			try{				
				AcreedoresServiceImpl acreedoresServiceImpl = new AcreedoresServiceImpl(); 
				DetalleServiceImpl detservm = new DetalleServiceImpl();
				TransmisionServiceImp detserv = new TransmisionServiceImp();
				ModificacionServiceImp detservmd = new ModificacionServiceImp();
				InscripcionService inscripcionService = new InscripcionServiceImpl();
				JuezService jserv = new JuezService();				
				ModificacionTO modificaTO  =new ModificacionTO();
				inscripcionTO = new InscripcionTO();
				UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");		
				setIdpersona(new Integer(usuario.getPersona().getIdPersona()).toString());
				
				// Funcionalidad para obtener el utlimo id Tramite registrado y evitar
				// la mezcla de hotcakes
				Integer idUltimoTramite = detservm.getIdUltimoTramite(idgarantia);
				if(idUltimoTramite.intValue() != tramite.intValue() ){
					tramite = idUltimoTramite;
				}
				MyLogger.Logger.log(Level.INFO, "tramite final para echale galleta: " + tramite);

				// traemos la informacion del acreedor representado
				setAcreedorRepresentado(detservm.showAcreedorDetalle(idgarantia));
				// traemos la lista de acreedores posibles para la transmision
				String idAcreedorNoDisponible = getAcreedorRepresentado().getIdAcreedor();
				setAcreedoresDisponibles(acreedoresServiceImpl.getAcreedoresDisponiblesTransmision(getIdpersona(), idAcreedorNoDisponible));
				
				// aqui traemos el tipo de garantia
				setNomAcreedor(detservm.showAcreedorR(idgarantia));
				setDetalleTO(detservmd.getDetalleModificacion(idgarantia,tramite));
				setModtipogarantia(getDetalleTO().getIdtipogarantia());
				setDeudorTOs(detserv.getDeudor(idgarantia,tramite));
				
				if (getDeudorTOs().size()>0){
					setHayDeudores(true);
				}else{
					setHayDeudores(false);
				}
				
				if (getDetalleTO()!=null){
					if( getDetalleTO()!= null && getDetalleTO().getRelacionbien() != 0){
						setBienes(detservmd.getBienes(idgarantia, getDetalleTO().getRelacionbien()));
					}else{
						setBienes(detservmd.getBienes(idgarantia,tramite));
					}
					setTipoGarantiaTOs(detserv.getTiposGarantia());
					setListaMonedas(inscripcionService.getMonedas());			
					setTipoBienTOs(detservmd.getTipoBienes());
					setModtipogarantia(getDetalleTO().getIdtipogarantia());
					modificaTO.setIdtramite(13);
					modificaTO.setIdgarantia(getDetalleTO().getIdgarantia());
					modificaTO.setIdcontrato(getDetalleTO().getIdcontrato());
					modificaTO.setIdpersona(usuario.getPersona().getIdPersona());
					setModtipobien(getDetalleTO().getTiposbienes());	
					setModdescripcion(getDetalleTO().getDescbienes());
					setModotroscontrato(getDetalleTO().getOtroscontrato());
					setModlimite(getDetalleTO().getMonto());				
					setListaMonedas(inscripcionService.getMonedas());
					if(getDetalleTO().getIdmoneda()==null){
						setMoneda(22);
					}else{setMoneda(getDetalleTO().getIdmoneda());}
					setModotrosgarantia(getDetalleTO().getOtrosgarantia());
					
					if(getDetalleTO().getFechaCelebracionObligacion() == null ){
						setFechaCelebracionC("");
					}else{
						setFechaCelebracionC(sdf.format(getDetalleTO().getFechaCelebracionObligacion()));
					}
					
					if(getDetalleTO().getFecacelebcontrato()== null){
						setModfechacelebcontrato("");
					}else{
						setModfechacelebcontrato(sdf.format(getDetalleTO().getFecacelebcontrato()));
					}
					
					if(getDetalleTO().getFechaFinOb() == null ){
						setFechaFinOb("");
					}else{
						setFechaFinOb(sdf.format(getDetalleTO().getFechaFinOb()));
					}
					
					if(getDetalleTO().getCambio().equalsIgnoreCase("V")){
						setaBoolean(true);
					}else{setaBoolean(false);}
					
					setAutoridad(jserv.showJuezTram(tramite));
					if(getDetalleTO().getCambio().equalsIgnoreCase("V")){
						setaBoolean(true);
					}else{setaBoolean(false);}

					//TODO: ESTO DEBE DE IR?¿
					Integer id = detservmd.altapartesTramiteInc(new Integer(usuario.getPersona().getIdPersona()),new Integer("8"),idgarantia);
					
					MyLogger.Logger.log(Level.INFO, "id tramite al llamar altapartesTramiteInc:"+id);
					inscripcionTO.setIdInscripcion(new Integer(id));
					sessionMap.put(Constants.ID_TRAMITE_NUEVO,id);	
					setIdTramiteTemporal(new Integer (id));
					
					setIdTramite(inscripcionTO.getIdInscripcion().toString());				
					//
					if (id.intValue() != 0){
						regresa = "success";
					}
					
				}else{
					MyLogger.Logger.log(Level.WARNING, "No funciona la trasmicion");
				}
				
				
			}catch(Exception e){
				e.printStackTrace();
				regresa = "failure";
			}
			return regresa;
		}

				
		public String guarda(){
			String regresa = "failure";
			
			MyLogger.Logger.log(Level.INFO, "entro a guarda");
			DateUtilRug dateUtil = new DateUtilRug();
			Integer idgarantia = (Integer) sessionMap.get(Constants.ID_GARANTIA);
			MyLogger.Logger.log(Level.INFO, "idgarantia=" + idgarantia);
			Integer idTramiteNuevoVar = (Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO);
			MyLogger.Logger.log(Level.INFO, "idTramiteNuevoVar=" + idTramiteNuevoVar);
			usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			
			if(modificaTO==null) modificaTO = new ModificacionTO();
			if (detalleTO == null) detalleTO = new DetalleTO();
			
			try{
				MyLogger.Logger.log(Level.INFO, "ID_GARANTIA=" + sessionMap.get(Constants.ID_GARANTIA));
				MyLogger.Logger.log(Level.INFO, "ID_TRAMITE_NUEVO=" + sessionMap.get(Constants.ID_TRAMITE_NUEVO));
				
				ModificacionServiceImp detserv = new ModificacionServiceImp();
				
				MyLogger.Logger.log(Level.INFO, "Entre a guardar transmision");
				
				inscripcionTO = new InscripcionTO();				
				modificaTO =new ModificacionTO();
		
				     
				if(idTramiteNuevoVar!=0){					
					MyLogger.Logger.log(Level.INFO, "ID getIdgarantia " +  idgarantia);
					modificaTO.setIdtramite(idTramiteNuevoVar);					
					modificaTO.setIdgarantia(new Integer(idgarantia));
					modificaTO.setIdpersona(usuario.getPersona().getIdPersona());
					modificaTO.setModtipoacto(getModtipoacto());
					modificaTO.setModfechaceleb(getModfechaceleb().trim().equals("") ? null : dateUtil.parseStrDate1(getModfechaceleb()));
					modificaTO.setFechatermino(getModfechatermino().trim().equals("") ? null : dateUtil.parseStrDate1(getModfechatermino()));
					modificaTO.setModterminos(getModterminos());
					
					if(modificaTO != null ){
						if (getAutoridad()!= null && !getAutoridad().trim().equals("")){
							MyLogger.Logger.log(Level.WARNING, "entra a la de la autoridad pk si la debes de persistir");
							JuezService juezService = new JuezService();
							juezService.insertJuez(idTramiteNuevoVar, getAutoridad());
						}
												
						if(!getAcreedorTransmite().equals("-1")){
							MyLogger.Logger.log(Level.INFO, "--Esta cambiando el acreedor.--");
							// TODO: PL3
							AltaParteDAO altaParteDAO = new AltaParteDAO();
							altaParteDAO.relParte(Integer.parseInt(getAcreedorTransmite()), idTramiteNuevoVar, 4, null);
						}
						
						modificaTO.setIdgarantia(detserv.actualiza(modificaTO, detalleTO));
						
						boolean valor = detserv.altaBitacoraTramite(idTramiteNuevoVar, 5, 0, null, "V");
						MyLogger.Logger.log(Level.INFO, "Alta bitacora ."+ valor);							
						setIdTramite(Integer.valueOf(idTramiteNuevoVar).toString());
						sessionMap.put(Constants.ID_TRAMITE_NUEVO,new Integer(idTramiteNuevoVar));
						
						regresa = "success";

					}
				}
							
			}catch(Exception e){
				e.printStackTrace();
				regresa = "failure";
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
		
		public void setIdGarantia(String idGarantia) {
			this.idGarantia = idGarantia;
		}

		public String getIdGarantia() {
			return idGarantia;
		}

		public void setTipoGarantiaTOs(List<TipoGarantiaTO> tipoGarantiaTOs) {
			this.tipoGarantiaTOs = tipoGarantiaTOs;
		}

		public List<TipoGarantiaTO> getTipoGarantiaTOs() {
			return tipoGarantiaTOs;
		}

		public void setTransmiTOs(TransmisionTO transmiTOs) {
			this.transmiTOs = transmiTOs;
		}		
		public TransmisionTO gettransmiTOs() {
			return transmiTOs;
		}	
				
		public void setIdTramite(String idTramite) {
			this.idTramite = idTramite;
		}

		public String getIdTramite() {
			return idTramite;
		}

		public void setTipoBienTOs(List<TipoBienesTO> tipoBienTOs) {
			this.tipoBienTOs = tipoBienTOs;
		}

		public List<TipoBienesTO> getTipoBienTOs() {
			return tipoBienTOs;
		}
				
		public void setHayDeudores(boolean hayDeudores) {
			this.hayDeudores = hayDeudores;
		}

		public boolean isHayDeudores() {
			return hayDeudores;
		}

		public void setModfechacelebcontrato(String modfechacelebcontrato) {
			this.modfechacelebcontrato = modfechacelebcontrato;
		}

		public String getModfechacelebcontrato() {
			return modfechacelebcontrato;
		}

		public void setModotroscontrato(String modotroscontrato) {
			this.modotroscontrato = modotroscontrato;
		}

		public String getModotroscontrato() {
			return modotroscontrato;
		}
		public void setNomAcreedor(String nomAcreedor) {
			this.nomAcreedor = nomAcreedor;
		}

		public String getNomAcreedor() {
			return nomAcreedor;
		}

		public void setIdpersona(String idpersona) {
			this.idpersona = idpersona;
		}

		public void setDetalleTO(DetalleTO detalleTO) {
			this.detalleTO = detalleTO;
		}

		public DetalleTO getDetalleTO() {
			return detalleTO;
		}

		public String getIdpersona() {
			return idpersona;
		}

		public void setIdColonia(Integer idColonia) {
			this.idColonia = idColonia;
		}

		public Integer getIdColonia() {
			return idColonia;
		}

		public void setBienes(List<TipoBienTO> bienes) {
			this.bienes = bienes;
		}

		public List<TipoBienTO> getBienes() {
			return bienes;
		}
		
		public void setHayOtorgante(boolean hayOtorgante) {
			this.hayOtorgante = hayOtorgante;
		}

		public boolean isHayOtorgante() {
			return hayOtorgante;
		}

		public void setDeudorTOs(List<PartesTO> deudorTOs) {
			this.deudorTOs = deudorTOs;
		}

		public void setListaMonedas(List<MonedaTO> listaMonedas) {
			this.listaMonedas = listaMonedas;
		}

		public List<MonedaTO> getListaMonedas() {
			return listaMonedas;
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

		public void setIdTipoMoneda(Integer idTipoMoneda) {
			this.idTipoMoneda = idTipoMoneda;
		}

		public Integer getIdTipoMoneda() {
			return idTipoMoneda;
		}
		public void setMoneda(Integer moneda) {
			this.moneda = moneda;
		}

		public Integer getMoneda() {
			return moneda;
		}

		public void setModfechatermino(String modfechatermino) {
			this.modfechatermino = modfechatermino;
		}

		public String getModfechatermino() {
			return modfechatermino;
		}

		public void setFechaFinOb(String fechaFinOb) {
			this.fechaFinOb = fechaFinOb;
		}

		public String getFechaFinOb() {
			return fechaFinOb;
		}

		public void setaBoolean(boolean aBoolean) {
			this.aBoolean = aBoolean;
		}

		public boolean isaBoolean() {
			return aBoolean;
		}

		public void setModlimite(String modlimite) {
			this.modlimite = modlimite;
		}

		public String getModlimite() {
			return modlimite;
		}
		
		public Integer getModtipogarantia() {
			return modtipogarantia;
		}

		public void setModtipogarantia(Integer modtipogarantia) {
			this.modtipogarantia = modtipogarantia;
		}

		public TransmisionTO getTransmiTOs() {
			return transmiTOs;
		}

		public void setModificaTO(ModificacionTO modificaTO) {
			this.modificaTO = modificaTO;
		}

		public String getAutoridad() {
			return autoridad;
		}

		public void setAutoridad(String autoridad) {
			this.autoridad = autoridad;
		}

		public String getTramite() {
			return tramite;
		}

		public void setTramite(String tramite) {
			this.tramite = tramite;
		}

		public Integer getIdparte() {
			return idparte;
		}

		public void setIdparte(Integer idparte) {
			this.idparte = idparte;
		}	

		public String getIdtipopersona() {
			return idtipopersona;
		}

		public void setIdtipopersona(String idtipopersona) {
			this.idtipopersona = idtipopersona;
		}

		public String getModtipoacto() {
			return modtipoacto;
		}

		public void setModtipoacto(String modtipoacto) {
			this.modtipoacto = modtipoacto;
		}

		public String getModfechaceleb() {
			return modfechaceleb;
		}

		public void setModfechaceleb(String modfechaceleb) {
			this.modfechaceleb = modfechaceleb;
		}

		public String getModterminos() {
			return modterminos;
		}

		public void setModterminos(String modterminos) {
			this.modterminos = modterminos;
		}

		public String getModotrosterminos() {
			return modotrosterminos;
		}

		public void setModotrosterminos(String modotrosterminos) {
			this.modotrosterminos = modotrosterminos;
		}

		public String getModotrosgarantia() {
			return modotrosgarantia;
		}

		public void setModotrosgarantia(String modotrosgarantia) {
			this.modotrosgarantia = modotrosgarantia;
		}

		public String getModtipobien() {
			return modtipobien;
		}

		public void setModtipobien(String modtipobien) {
			this.modtipobien = modtipobien;
		}

		public String getModdescripcion() {
			return moddescripcion;
		}

		public void setModdescripcion(String moddescripcion) {
			this.moddescripcion = moddescripcion;
		}
		public UsuarioTO getUsuario() {
			return usuario;
		}

		public void setUsuario(UsuarioTO usuario) {
			this.usuario = usuario;
		}

		public ModificacionTO getModificaTO() {
			return modificaTO;
		}

		public void setmodificaTO(ModificacionTO modificaTO) {
			this.modificaTO = modificaTO;
		}

		public InscripcionTO getInscripcionTO() {
			return inscripcionTO;
		}

		public void setInscripcionTO(InscripcionTO inscripcionTO) {
			this.inscripcionTO = inscripcionTO;
		}

		public InscripcionService getInscripcionService() {
			return inscripcionService;
		}

		public void setInscripcionService(InscripcionService inscripcionService) {
			this.inscripcionService = inscripcionService;
		}




		public void setIdTramiteTemporal(Integer idTramiteTemporal) {
			this.idTramiteTemporal = idTramiteTemporal;
		}




		public Integer getIdTramiteTemporal() {
			return idTramiteTemporal;
		}




		/**
		 * @param acreedorRepresentado the acreedorRepresentado to set
		 */
		public void setAcreedorRepresentado(AcreedorTO acreedorRepresentado) {
			this.acreedorRepresentado = acreedorRepresentado;
		}

		/**
		 * @return the acreedorRepresentado
		 */
		public AcreedorTO getAcreedorRepresentado() {
			return acreedorRepresentado;
		}

		/**
		 * @param acreedoresDisponibles the acreedoresDisponibles to set
		 */
		public void setAcreedoresDisponibles(List<AcreedorTO> acreedoresDisponibles) {
			this.acreedoresDisponibles = acreedoresDisponibles;
		}

		/**
		 * @return the acreedoresDisponibles
		 */
		public List<AcreedorTO> getAcreedoresDisponibles() {
			return acreedoresDisponibles;
		}

		/**
		 * @param acreedorTrasmite the acreedorTrasmite to set
		 */
		public void setAcreedorTransmite(String acreedorTransmite) {
			this.acreedorTransmite = acreedorTransmite;
		}

		/**
		 * @return the acreedorTrasmite
		 */
		public String getAcreedorTransmite() {
			return acreedorTransmite;
		}




		public void setFechaCelebracionC(String fechaCelebracionC) {
			this.fechaCelebracionC = fechaCelebracionC;
		}




		public String getFechaCelebracionC() {
			return fechaCelebracionC;
		}
			
}
