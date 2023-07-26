package mx.gob.se.rug.modificacion.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.detallegarantia.service.impl.DetalleServiceImpl;
import mx.gob.se.rug.detallegarantia.to.DetalleTO;
import mx.gob.se.rug.detallegarantia.to.PartesTO;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.service.impl.InscripcionServiceImpl;
import mx.gob.se.rug.inscripcion.to.BienEspecialTO;
import mx.gob.se.rug.inscripcion.to.InscripcionTO;
import mx.gob.se.rug.inscripcion.to.MonedaTO;
import mx.gob.se.rug.inscripcion.to.TipoBienTO;
import mx.gob.se.rug.inscripcion.to.TipoGarantiaTO;
import mx.gob.se.rug.juez.service.JuezService;
import mx.gob.se.rug.modificacion.service.impl.ModificacionServiceImp;
import mx.gob.se.rug.modificacion.to.ModificacionTO;
import mx.gob.se.rug.modificacion.to.TipoBienesTO;
import mx.gob.se.rug.to.TipoTo;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.transmision.serviceimp.TransmisionServiceImp;
import mx.gob.se.rug.util.MyLogger;
import mx.gob.se.rug.util.to.DateUtilRug;


public class ModificacionAction extends RugBaseAction  {
	private static final String SUCCESS = "success";
	private static final long serialVersionUID = 1L;

	private List<PartesTO> otorganteTOs;
	private List<PartesTO> acreedorTOs;	
	private List<PartesTO> deudorTOs;
	private List<TipoBienesTO> tipobienTOs;
	private List<BienEspecialTO> bienesEspTOs;
	private DetalleTO detalleTO;
	private ModificacionTO modificaTO;
	private String idTramite;	
	private String nomAcreedor;
	private String idGarantia;

	private Integer idGarantiaModifica;


	private Integer idgarantia;
	private String modtipoacto;
	private String modfechaceleb;
	private String modterminos;
	private String modotrosterminos;
	private String modmonto;
	private String modotrosgarantia;
	private String modotroscontrato;
	private String modtipobien;
	private String moddescripcion;
	private boolean hayAcreedores;
	private boolean hayOtorgantes;
	private boolean hayDeudores;
	private boolean hayBienes;
	private String modfechacelebcontrato;
	private UsuarioTO usuario;
	private AcreedorTO acreedorTO;
	private List <AcreedorTO> listaAcreedores;
	private InscripcionTO inscripcionTO;
	private InscripcionService inscripcionService ;
	private Integer idAcreedorTO;
	private List<TipoBienTO> bienes;
	private String tramite;
	private String tramiteinc;
	private String idpersona;
	private List<TipoGarantiaTO> tipoGarantiaTOs;
	private Integer modtipogarantia;
	private String autoridad;
	private List<MonedaTO> listaMonedas;
	private Integer idTipoMoneda;
	private Integer moneda;
	private String modfechatermino;
	private String fechaFinOb;
	private boolean aBoolean;
	private boolean aMonto;
	private String modinstrumento;
	private String FechaModificacion;
	private String fechaCelebOb;
	private String tipoContOb;
	private String otrosTermOb;
	private String fechaCelebracionC;
	private String vigencia;
	private String instrumento;
	// nuevos
	private String txtregistro;
	private boolean aPrioridad;
	private boolean aRegistro;
	private List <TipoTo> listaBienEspecial;
	private List <TipoTo> listaUsos;
	
	private List<String> textosFormulario;
	
	private String esModificacion;
	
	public void cargarBienesEspeciales() {
		TipoTo tipo0 = new TipoTo();
		tipo0.setIdTipo("0");
		tipo0.setDesTipo("Seleccione");
		TipoTo tipo1 = new TipoTo();
		tipo1.setIdTipo("1");
		tipo1.setDesTipo("Veh\u00edculos");
		TipoTo tipo4 = new TipoTo();
        tipo4.setIdTipo("5");
        tipo4.setDesTipo("Veh\u00edculos de 2 y 3 ruedas");
        TipoTo tipo5 = new TipoTo();
        tipo5.setIdTipo("6");
        tipo5.setDesTipo("Derecho de Cr\u00e9dito (no facturas)");
		TipoTo tipo2 = new TipoTo();
		tipo2.setIdTipo("2");
		tipo2.setDesTipo("Facturas");
		TipoTo tipo3 = new TipoTo();
		tipo3.setIdTipo("3");
		tipo3.setDesTipo("Otros");
		
		listaBienEspecial = new ArrayList<TipoTo>();
                
                if (getDetalleTO().getIdtipogarantia() != 2)
                {
                    listaBienEspecial.add(tipo0);
                    listaBienEspecial.add(tipo1);
                    listaBienEspecial.add(tipo2);
					listaBienEspecial.add(tipo4);
					listaBienEspecial.add(tipo5);
                    listaBienEspecial.add(tipo3);
                
                }
                else{
                    listaBienEspecial.add(tipo0);
                    listaBienEspecial.add(tipo2);
                }
                
		cargaUsos();
	}
	
	public void cargaUsos() {
		listaUsos = new ArrayList<TipoTo>();
		listaUsos.add(new TipoTo("0","Seleccione"));
		listaUsos.add(new TipoTo("P0","P0"));
		listaUsos.add(new TipoTo("A0","A0"));
		listaUsos.add(new TipoTo("C0","C0"));
		listaUsos.add(new TipoTo("CC","CC"));
		listaUsos.add(new TipoTo("CD","CD"));
		listaUsos.add(new TipoTo("DIS","DIS"));
		listaUsos.add(new TipoTo("M0","M0"));
		listaUsos.add(new TipoTo("MI","MI"));
		listaUsos.add(new TipoTo("O0","O0"));
		listaUsos.add(new TipoTo("TC","TC"));
		listaUsos.add(new TipoTo("TE","TE"));
		listaUsos.add(new TipoTo("TRC","TRC"));
		listaUsos.add(new TipoTo("U0","U0"));
		listaUsos.add(new TipoTo("00","00"));
	}

	public String getModfechatermino() {
		return modfechatermino;
	}
	public void setModfechatermino(String modfechatermino) {
		this.modfechatermino = modfechatermino;
	}
	public Integer getIdTipoMoneda() {
		return idTipoMoneda;
	}
	public void setIdTipoMoneda(Integer idTipoMoneda) {
		this.idTipoMoneda = idTipoMoneda;
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
	public InscripcionService getInscripcionService() {
		return inscripcionService;
	}
	public void setInscripcionService(InscripcionService inscripcionService) {
		this.inscripcionService = inscripcionService;
	}
	public Integer getIdAcreedorTO() {
		return idAcreedorTO;
	}
	public void setIdAcreedorTO(Integer idAcreedorTO) {
		this.idAcreedorTO = idAcreedorTO;
	}

	public ModificacionAction(){
		super();
	}
	public void setOtorganteTOs(List<PartesTO> otorganteTOs) {
		this.otorganteTOs = otorganteTOs;
	}
	public List<PartesTO> getotorganteTOs() {
		return otorganteTOs;
	}	
	
	public void setAcreedorTOs(List<PartesTO> acreedorTOs) {
		this.acreedorTOs = acreedorTOs;
	}
	public List<PartesTO> getAcreedorTOs() {
		return acreedorTOs;
	}
	public List<PartesTO> getacreedorTOs() {
		return acreedorTOs;
	}	
	public void setDeudorTOs(List<PartesTO> deudorTOs) {
		this.deudorTOs = deudorTOs;
	}
	public List<PartesTO> getdeudorTOs() {
		return deudorTOs;
	}	
	public void setDetalleTO(DetalleTO detalleTO) {
		this.detalleTO = detalleTO;
	}
	public DetalleTO getDetalleTO() {
		return detalleTO;
	}	
	public void setIdgarantia(Integer idgarantia) {
		this.idgarantia = idgarantia;
	}
	public Integer getIdgarantia() {
		return idgarantia;
	}
	public void setModtipoacto(String modtipoacto) {
		this.modtipoacto = modtipoacto;
	}
	public String getModtipoacto() {
		return modtipoacto;
	}
	public void setModfechaceleb(String Modfechaceleb) {
		this.modfechaceleb = Modfechaceleb;
	}
	public String getModfechaceleb() {
		return modfechaceleb;
	}
	public void setModterminos(String modterminos) {
		this.modterminos = modterminos;
	}
	public String getModterminos() {
		return modterminos;
	}
	public void setModotrosterminos(String modotrosterminos) {
		this.modotrosterminos = modotrosterminos;
	}
	public String getModotrosterminos() {
		return modotrosterminos;
	}
	
	public void setModotrosgarantia(String modotrosgarantia) {
		this.modotrosgarantia = modotrosgarantia;
	}
	public String getModotrosgarantia() {
		return modotrosgarantia;
	}
	public void setModtipobien(String modtipobien) {
		this.modtipobien = modtipobien;
	}
	public String getModtipobien() {
		return modtipobien;
	}
	public void setModdescripcion(String moddescripcion) {
		this.moddescripcion = moddescripcion;
	}
	public String getModdescripcion() {
		return moddescripcion;
	}
	
	public void setModificaTO(ModificacionTO modificaTO) {
		this.modificaTO = modificaTO;
	}
	public ModificacionTO getmodificaTO() {
		return modificaTO;
	}
	
	public void setIdGarantia(String idGarantia) {
		this.idGarantia = idGarantia;
	}
	public String getIdGarantia() {
		return idGarantia;
	}
	public List<TipoBienesTO> getTipobienTOs() {
		return tipobienTOs;
	}
	public void setIdpersona(String idpersona) {
		this.idpersona = idpersona;
	}
	public String getIdpersona() {
		return idpersona;
	}
	public void setTipoBienTOs(List<TipoBienesTO> tipobienTOs) {
		this.tipobienTOs = tipobienTOs;
	}
	
	public void setAcreedorTO(AcreedorTO acreedorTO) {
		this.acreedorTO = acreedorTO;
	}
	public void setModtipogarantia(Integer modtipogarantia) {
		this.modtipogarantia = modtipogarantia;
	}
	public Integer getModtipogarantia() {
		return modtipogarantia;
	}
	public AcreedorTO getAcreedorTO() {
		return acreedorTO;
	}
	public void setListaAcreedores(List <AcreedorTO> listaAcreedores) {
		this.listaAcreedores = listaAcreedores;
	}
	public List <AcreedorTO> getListaAcreedores() {
		return listaAcreedores;
	}
	public void setIdTramite(String idTramite) {
		this.idTramite = idTramite;
	}
	public String getIdTramite() {
		return idTramite;
	}	
	public void setTramite(String tramite) {
		this.tramite = tramite;
	}
	public String getTramite() {
		return tramite;
	}
	public void setTramiteinc(String tramiteinc) {
		this.tramiteinc = tramiteinc;
	}
	public String getTramiteinc() {
		return tramiteinc;
	}
	public void setHayAcreedores(boolean hayAcreedores) {
		this.hayAcreedores = hayAcreedores;
	}
	public boolean isHayAcreedores() {
		return hayAcreedores;
	}
	public void setHayDeudores(boolean hayDeudores) {
		this.hayDeudores = hayDeudores;
	}
	public boolean isHayDeudores() {
		return hayDeudores;
	}
	
	public void setBienes(List<TipoBienTO> bienes) {
		this.bienes = bienes;
	}
	public List<TipoBienTO> getBienes() {
		return bienes;
	}
	public void setTipoGarantiaTOs(List<TipoGarantiaTO> tipoGarantiaTOs) {
		this.tipoGarantiaTOs = tipoGarantiaTOs;
	}
	public List<TipoGarantiaTO> getTipoGarantiaTOs() {
		return tipoGarantiaTOs;
	}
	public void setNomAcreedor(String nomAcreedor) {
		this.nomAcreedor = nomAcreedor;
	}
	public String getNomAcreedor() {
		return nomAcreedor;
	}
	public void setModfechacelebcontrato(String modfechacelebcontrato) {
		this.modfechacelebcontrato = modfechacelebcontrato;
	}
	public String getModfechacelebcontrato() {
		return modfechacelebcontrato;
	}
	public String modificacion(){
		 
                    
            return SUCCESS;
	}
	
	public String iniciaModificacion(){
		String regresa = "failed";
		if (getIdTramite()!=null){
			sessionMap.put(Constants.ID_TRAMITE, Integer.valueOf(getIdTramite()));
			if (getIdGarantia()!=null){
				sessionMap.put(Constants.ID_GARANTIA, Integer.valueOf(getIdGarantia()));
				setEsModificacion("0");
				regresa="success";
			}
		}
		return regresa;
	}
	
	public Date parseStrDate(String strDate){
		SimpleDateFormat formater = new SimpleDateFormat("MM/dd/yyyy");
		Date date = null;
		try {
			date = formater.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public String  creaTramite () {
		String regresa="failed";
		try{
			
			
			regresa = "success";
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return regresa;
	}
	
	
	public String inicia(){
		String regresa = "failed";
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_DDMMYY);
		if (esModificacion!=null){
			//modificar a garantias pendientes
			try{							
				Integer idgarantia = (Integer) sessionMap.get(Constants.ID_GARANTIA);
				Integer tramite = (Integer) sessionMap.get(Constants.ID_TRAMITE);
				UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
				Integer idPersona = usuario.getPersona().getIdPersona();
				setIdpersona(idPersona.toString());			
				inscripcionTO = new InscripcionTO();
				
				
				MyLogger.Logger.log(Level.INFO, "idgarantia: " + idgarantia);
				//MyLogger.Logger.log(Level.INFO,"tramite: " + tramite);


				DetalleServiceImpl detservd = new DetalleServiceImpl();
				ModificacionServiceImp detserv = new ModificacionServiceImp();
				TransmisionServiceImp detservt = new TransmisionServiceImp();
				InscripcionService inscripcionService = new InscripcionServiceImpl();
				
				// Funcionalidad para obtener el utlimo id Tramite registrado y evitar
				// la mezcla de hotcakes
				Integer idUltimoTramite = detservd.getIdUltimoTramite(idgarantia);
				if(idUltimoTramite != tramite ){
					tramite = idUltimoTramite;
				}
				MyLogger.Logger.log(Level.INFO, "tramite final para echale galleta otra: " + tramite);
				
				setNomAcreedor(detservd.showAcreedorR(idgarantia));			
				setAcreedorTOs(detserv.getAcreedor(idgarantia,tramite));
				if (getacreedorTOs().size()>0){
					setHayAcreedores(true);
				}else{
					setHayAcreedores(false);
				}							
				setOtorganteTOs(detserv.getOtorgante(idgarantia,tramite));
				if (getotorganteTOs().size()>0){
					setHayOtorgantes(true);
				}else{
					setHayOtorgantes(false);
				}	
				setDetalleTO(detserv.getDetalleModificacion(idgarantia,tramite));
				setTipoBienTOs(detserv.getTipoBienes());
				setTipoGarantiaTOs(detservt.getTiposGarantia());
				setModtipogarantia(getDetalleTO().getIdtipogarantia());
                                //orellana: para el caso especial de factoraje solo debe cargar la factura
				cargarBienesEspeciales();
				setTextosFormulario(inscripcionService.getTextosFormularioByIdGarantia(getDetalleTO().getIdtipogarantia()));
				
				/*if(getDetalleTO().getIdtipogarantia()==2) { //Factoraje
					setParteUno("Cedente(s)");
					setParteDos("Factor(es) o Cesario (s)");
					setParteTres("Deudores(s) Cedido(s)");
					setTituloUno("Descripci\u00f3n de los Derechos de Cr\u00e9dito Objeto de Cesi\u00f3n");
					setTituloDos("Datos generales del contrato de factoraje, contrato de descuento o cesi\u00f3n de derechos de cr\u00e9dito");
				} else {
					setParteUno("Deudor (es) Garante (s)");
					setParteDos("Acreedor (es) Garantizado (s)");
					setParteTres("");
					setTituloUno("Descripci\u00f3n de los Bienes Muebles objeto de la Garant\u00eda Mobiliaria:");
					setTituloDos("Informaci\u00f3n General del Contrato de la Garant\u00eda");
				}*/
				
				setListaMonedas(inscripcionService.getMonedas());
				
				if(getDetalleTO().getIdmoneda()==null){
					setMoneda(22);
				}else{setMoneda(getDetalleTO().getIdmoneda());}
				if( getDetalleTO().getRelacionbien()!= 0){
					setBienes(detserv.getBienes(idgarantia,getDetalleTO().getRelacionbien()));
				}else{
					setBienes(detserv.getBienes(idgarantia,tramite));
				}
				
				if(getDetalleTO().getFechaCelebracionObligacion()== null){
					setFechaCelebracionC("");
				}else{
					setFechaCelebracionC(sdf.format(getDetalleTO().getFechaCelebracionObligacion()));
				}
								
				if(getDetalleTO().getVigencia()==null) {
					setVigencia("");
				} else {
					setVigencia(getDetalleTO().getVigencia() + " A\u00f1os");
				}
				
				if(getDetalleTO().getInstrumento()==null) {
					setInstrumento("");
				} else {
					setInstrumento(getDetalleTO().getInstrumento());
				}
		
				if(getDetalleTO().getFecacelebcontrato()==null){
					setFechaCelebOb("");
				}else{
					setFechaCelebOb(sdf.format(getDetalleTO().getFecacelebcontrato()));
				}
				
				if(getDetalleTO().getFechaFinOb() == null){
					setFechaFinOb("");
				}else{
					setFechaFinOb(sdf.format(getDetalleTO().getFechaFinOb()));
				}
				setModotrosterminos(getDetalleTO().getOtrosterminos());
				setModmonto(getDetalleTO().getMonto());
				setIdgarantia(getDetalleTO().getIdgarantia());
				setModotrosgarantia(getDetalleTO().getOtrosgarantia());
				
				setModotroscontrato(getDetalleTO().getOtroscontrato());
				
				setModdescripcion(getDetalleTO().getDescbienes());

				if(getDetalleTO().getNoGarantiaPreviaOt().equalsIgnoreCase("V")){
					setaBoolean(true);
				}else{setaBoolean(false);}
				
				if(getDetalleTO().getCambio().equalsIgnoreCase("V")){
					setaMonto(true);
				}else{setaMonto(false);}
				
				//nuevos
				if(getDetalleTO().getEsPrioritaria().equalsIgnoreCase("V")){
					setaPrioridad(true);
				}else{setaPrioridad(false);}
				
				if(getDetalleTO().getEnRegistro().equalsIgnoreCase("V")){
					setaRegistro(true);
					setTxtregistro(getDetalleTO().getTxtRegistro());
				}else{setaRegistro(false);}
				
				inscripcionTO.setIdInscripcion(detserv.insertatramiteinc(Integer.valueOf(usuario.getPersona().getIdPersona()),7));
				System.out.println("Garantia en el proceso modificar " + getIdGarantia());
				MyLogger.Logger.log(Level.INFO, "tramite incompleto 2"+ inscripcionTO.getIdInscripcion());
				Integer idTramiteNuevo = detserv.altapartesTramiteInc(new Integer(usuario.getPersona().getIdPersona()),new Integer("7"),idgarantia);
				setIdTramite(idTramiteNuevo.toString());
				detserv.altapartesBienesInc(idUltimoTramite, idTramiteNuevo);
				setBienesEspTOs(detservd.getListaBienes(idTramiteNuevo, 1));
				if (getBienesEspTOs().size()>0){
					setHayBienes(true);
				}else{
					setHayBienes(false);
				}
				sessionMap.put(Constants.ID_TRAMITE_NUEVO,idTramiteNuevo);	
				if (idTramiteNuevo.intValue() != 0){
					regresa = "success";
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
			// hasta aqui
		}else{
			try{			
				Integer idgarantia = (Integer) sessionMap.get(Constants.ID_GARANTIA);
				Integer tramite = (Integer) sessionMap.get(Constants.ID_TRAMITE);
				UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
				Integer idPersona = usuario.getPersona().getIdPersona();
				setIdpersona(idPersona.toString());			
				inscripcionTO = new InscripcionTO();
				
				MyLogger.Logger.log(Level.INFO, "inicio del error, INGRESO DE LOS PARAMETROS");
				MyLogger.Logger.log(Level.INFO, "Garantia: " + idgarantia + " Tramite: " + tramite + " usuario:" + usuario.getPersona().getIdPersona());
				// regresa = "success";
				
				// MyLogger.Logger.log(Level.INFO,"tramite: " + tramite);

				setIdGarantiaModifica(idgarantia);
				
				DetalleServiceImpl detservd = new DetalleServiceImpl();
				ModificacionServiceImp detserv = new ModificacionServiceImp();
				TransmisionServiceImp detservt = new TransmisionServiceImp();
				InscripcionService inscripcionService = new InscripcionServiceImpl();

				
				// Funcionalidad para obtener el utlimo id Tramite registrado y evitar
				// la mezcla de hotcakes
				Integer idUltimoTramite = detservd.getIdUltimoTramite(idgarantia);
				if(idUltimoTramite != tramite ){
					tramite = idUltimoTramite;
				}
				MyLogger.Logger.log(Level.INFO, "tramite final para echale galleta: otro error " + tramite);
				
				setNomAcreedor(detservd.showAcreedorR(idgarantia));			
				setAcreedorTOs(detserv.getAcreedor(idgarantia,tramite));
				if (getacreedorTOs().size()>0){
					setHayAcreedores(true);
				}else{
					setHayAcreedores(false);
				}			
				setOtorganteTOs(detserv.getOtorgante(idgarantia,tramite));
				if (getotorganteTOs().size()>0){
					setHayOtorgantes(true);
				}else{
					setHayOtorgantes(false);
				}	
				setDetalleTO(detserv.getDetalleModificacion(idgarantia,tramite));
				setTipoBienTOs(detserv.getTipoBienes());
				setTipoGarantiaTOs(detservt.getTiposGarantia());
				setModtipogarantia(getDetalleTO().getIdtipogarantia());
				
                                //orellana: para el caso especial de factoraje solo debe cargar la factura
                                cargarBienesEspeciales();
                                
				setTextosFormulario(inscripcionService.getTextosFormularioByIdGarantia(getDetalleTO().getIdtipogarantia()));				
				
				setListaMonedas(inscripcionService.getMonedas());
				if(getDetalleTO().getIdmoneda()==null){
					setMoneda(22);
				}else{setMoneda(getDetalleTO().getIdmoneda());}
				if( getDetalleTO().getRelacionbien()!= 0){
					setBienes(detserv.getBienes(idgarantia,getDetalleTO().getRelacionbien()));
				}else{
					setBienes(detserv.getBienes(idgarantia,tramite));
				}
								
				if(getDetalleTO().getVigencia()==null) {
					setVigencia("");
				} else {
					setVigencia(getDetalleTO().getVigencia() + " A\u00f1os");
				}
				
				if(getDetalleTO().getInstrumento()==null) {
					setInstrumento("");
				} else {
					setInstrumento(getDetalleTO().getInstrumento());
				}
				
				if(getDetalleTO().getFechaCelebracionObligacion()== null){
					setFechaCelebracionC("");
				}else{
					setFechaCelebracionC(sdf.format(getDetalleTO().getFechaCelebracionObligacion()));
				}
		
				if(getDetalleTO().getFecacelebcontrato()==null){
					setFechaCelebOb("");
				}else{
					setFechaCelebOb(sdf.format(getDetalleTO().getFecacelebcontrato()));
				}
				
				if(getDetalleTO().getFechaFinOb() == null){
					setFechaFinOb("");
				}else{
					setFechaFinOb(sdf.format(getDetalleTO().getFechaFinOb()));
				}
				setModotrosterminos(getDetalleTO().getOtrosterminos());
				setModmonto(getDetalleTO().getMonto());
				setIdgarantia(getDetalleTO().getIdgarantia());
				setModotrosgarantia(getDetalleTO().getOtrosgarantia());
				
				setModotroscontrato(getDetalleTO().getOtroscontrato());
				
				setModdescripcion(getDetalleTO().getDescbienes());

				if(getDetalleTO().getNoGarantiaPreviaOt().equalsIgnoreCase("V")){
					setaBoolean(true);
				}else{setaBoolean(false);}
				
				if(getDetalleTO().getCambio().equalsIgnoreCase("V")){
					setaMonto(true);
				}else{setaMonto(false);}
				
				//Nuevos
				if(getDetalleTO().getEsPrioritaria().equalsIgnoreCase("V")){
					setaPrioridad(true);
				}else{setaPrioridad(false);}
				
				if(getDetalleTO().getEnRegistro().equalsIgnoreCase("V")){
					setaRegistro(true);
					setTxtregistro(getDetalleTO().getTxtRegistro());
				}else{setaRegistro(false);}
				
				inscripcionTO.setIdInscripcion(detserv.insertatramiteinc(Integer.valueOf(usuario.getPersona().getIdPersona()),7));
				MyLogger.Logger.log(Level.INFO, "Id Garantia de otro tramite else "+ getIdGarantia());
				Integer idTramiteNuevo = detserv.altapartesTramiteInc(new Integer(usuario.getPersona().getIdPersona()),new Integer("7"),idgarantia);
				MyLogger.Logger.log(Level.INFO, "Integer Usuario: " + new Integer(usuario.getPersona().getIdPersona()) + " integer numero " + new Integer("7") + " id Garantia jj " + idgarantia + " idTRamite: " + idTramiteNuevo);
				setIdTramite(idTramiteNuevo.toString());
				detserv.altapartesBienesInc(idUltimoTramite, idTramiteNuevo);
				setBienesEspTOs(detservd.getListaBienes(idTramiteNuevo, 1));
				if (getBienesEspTOs().size()>0){
					setHayBienes(true);
				}else{
					setHayBienes(false);
				}
				sessionMap.put(Constants.ID_TRAMITE_NUEVO,idTramiteNuevo);	
				// regresa = "success";
				MyLogger.Logger.log(Level.INFO, "idTramiteNuevo: " + idTramiteNuevo);
				if (idTramiteNuevo.intValue() != 0){
					regresa = "success";
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return regresa;
	}	
	    

	
	public String guarda(){
		String regresa = "failure";
		try{
			DateUtilRug dateUtil = new DateUtilRug();
			Integer idGarantiaVar = (Integer) sessionMap.get(Constants.ID_GARANTIA);
			Integer idTramiteNuevoVar = (Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO);
			Integer idTramiteVar= (Integer) sessionMap.get(Constants.ID_TRAMITE);
			usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			ModificacionServiceImp detserv = new ModificacionServiceImp();
			System.out.println("Parametros que traer metodo guardar: " + sessionMap.get(Constants.ID_GARANTIA) + " " + sessionMap.get(Constants.ID_TRAMITE_NUEVO) + " " + sessionMap.get(Constants.ID_TRAMITE) + " " + sessionMap.get(Constants.USUARIO));
		
			setDetalleTO(detserv.getDetalle(idGarantiaVar,idTramiteVar));
			setModtipogarantia(getDetalleTO().getIdtipogarantia());
			System.out.println("Tipo de garantia " + getDetalleTO().getIdtipogarantia());
			inscripcionTO = new InscripcionTO();
			modificaTO = new ModificacionTO();
			modificaTO.setIdtramite(idTramiteNuevoVar);
			modificaTO.setIdgarantia(idGarantiaVar);
			modificaTO.setIdpersona(usuario.getPersona().getIdPersona());
			modificaTO.setModtipobien(getModtipobien()+"|");
			modificaTO.setModdescripcion(getModdescripcion());
			modificaTO.setTipogarantia(getModtipogarantia().toString());
			modificaTO.setFecacelebcontrato(detalleTO.getFechaCelebracionObligacion());
			modificaTO.setModmonto(getModmonto());
			modificaTO.setModotrosgarantia(getModotrosgarantia());
			modificaTO.setIdmoneda(getIdTipoMoneda());
			
			modificaTO.setTipocontrato(getTipoContOb());//TipoContrato-OBLIGA
			modificaTO.setFechainiob(getDetalleTO().getFecacelebcontrato());//FI-OBLIGA
			modificaTO.setFechafinob(getFechaFinOb()==null ? null : dateUtil.parseStrDate1(getFechaFinOb().trim()));//FF-OBLIGA
			modificaTO.setModotroscontrato(getModotroscontrato());//OtrosTerm-OBLIGA
			
			modificaTO.setModtipoacto(getModotrosterminos());//TipoContrato-BASA
			modificaTO.setModfechaceleb(getModfechaceleb()==null ? null : dateUtil.parseStrDate1(getModfechaceleb().trim()));//FI-BASA
			modificaTO.setFechatermino(null);//FF-BASA
			modificaTO.setModterminos(getModotrosterminos());//OtrosTerm-BASA
			modificaTO.setModinstrumento(getInstrumento());
			modificaTO.setCambioBienesMonto(isaMonto()== true ? "V" : "F");
			
			//nuevos campos
			modificaTO.setEsPrioritaria(isaPrioridad()== true ? "V" : "F");
			modificaTO.setEnRegistro(isaRegistro()== true ? "V" : "F");
			modificaTO.setTxtRegistro(getTxtregistro());
			modificaTO.setNoGarantiaPreviaOt(isaBoolean()== true ? "V" : "F");
			
			// inscripcionTO.setIdTipoTramite(7);
			inscripcionTO.setIdTipoTramite(40);
			inscripcionTO.setIdPersona(usuario.getPersona().getIdPersona());
			
			modificaTO.setIdgarantia(detserv.actualiza(modificaTO, detalleTO));
			
			if (getAutoridad()!= null && !getAutoridad().trim().equals("")){
				MyLogger.Logger.log(Level.INFO, "entra con autoridad: " + getAutoridad());
				JuezService juezService = new JuezService();
				juezService.insertJuez(Integer.valueOf(idTramiteNuevoVar), getAutoridad());
			}
			
			boolean valor = detserv.altaBitacoraTramite(modificaTO.getIdtramite(), 5, 0, null, "V");
			MyLogger.Logger.log(Level.INFO, "alta bitacora ."+ valor + " nuevo " + idTramiteNuevoVar + " id tramite " + modificaTO.getIdtramite());							
			setIdTramite(String.valueOf(idTramiteNuevoVar));
			sessionMap.put(Constants.ID_TRAMITE_NUEVO,new Integer(idTramiteNuevoVar));
			
			regresa = SUCCESS;
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return regresa;
	}
	public void setListaMonedas(List<MonedaTO> listaMonedas) {
		this.listaMonedas = listaMonedas;
	}
	public List<MonedaTO> getListaMonedas() {
		return listaMonedas;
	}
	public void setMoneda(Integer moneda) {
		this.moneda = moneda;
	}
	public Integer getMoneda() {
		return moneda;
	}
	public void setModmonto(String modmonto) {
		this.modmonto = modmonto;
	}
	public String getModmonto() {
		return modmonto;
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
	public void setModinstrumento(String modinstrumento) {
		this.modinstrumento = modinstrumento;
	}
	public String getModinstrumento() {
		return modinstrumento;
	}
	public void setFechaModificacion(String fechaModificacion) {
		FechaModificacion = fechaModificacion;
	}
	public String getFechaModificacion() {
		return FechaModificacion;
	}

	public String getFechaCelebOb() {
		return fechaCelebOb;
	}

	public void setFechaCelebOb(String fechaCelebOb) {
		this.fechaCelebOb = fechaCelebOb;
	}

	public String getOtrosTermOb() {
		return otrosTermOb;
	}
	public void setOtrosTermOb(String otrosTermOb) {
		this.otrosTermOb = otrosTermOb;
	}
	public String getTipoContOb() {
		return tipoContOb;
	}
	public void setTipoContOb(String tipoContOb) {
		this.tipoContOb = tipoContOb;
	}
	public void setFechaCelebracionC(String fechaCelebracionC) {
		this.fechaCelebracionC = fechaCelebracionC;
	}
	public String getFechaCelebracionC() {
		return fechaCelebracionC;
	}
	public String getEsModificacion() {
		return esModificacion;
	}
	public void setEsModificacion(String esModificacion) {
		this.esModificacion = esModificacion;
	}
	public String getVigencia() {
		return vigencia;
	}
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}
	public String getInstrumento() {
		return instrumento;
	}
	public void setInstrumento(String instrumento) {
		this.instrumento = instrumento;
	}
	public boolean isaMonto() {
		return aMonto;
	}
	public void setaMonto(boolean aMonto) {
		this.aMonto = aMonto;
	}
	public String getTxtregistro() {
		return txtregistro;
	}
	public void setTxtregistro(String txtregistro) {
		this.txtregistro = txtregistro;
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
	public List<TipoTo> getListaBienEspecial() {
		return listaBienEspecial;
	}
	public void setListaBienEspecial(List<TipoTo> listaBienEspecial) {
		this.listaBienEspecial = listaBienEspecial;
	}

	public List<TipoTo> getListaUsos() {
		return listaUsos;
	}

	public void setListaUsos(List<TipoTo> listaUsos) {
		this.listaUsos = listaUsos;
	}

	public String getModotroscontrato() {
		return modotroscontrato;
	}

	public void setModotroscontrato(String modotroscontrato) {
		this.modotroscontrato = modotroscontrato;
	}

	public boolean isHayOtorgantes() {
		return hayOtorgantes;
	}

	public void setHayOtorgantes(boolean hayOtorgantes) {
		this.hayOtorgantes = hayOtorgantes;
	}

	public List<String> getTextosFormulario() {
		return textosFormulario;
	}

	public void setTextosFormulario(List<String> textosFormulario) {
		this.textosFormulario = textosFormulario;
	}

	public Integer getIdGarantiaModifica() {
		return idGarantiaModifica;
	}

	public void setIdGarantiaModifica(Integer idGarantiaModifica) {
		this.idGarantiaModifica = idGarantiaModifica;
	}

}

