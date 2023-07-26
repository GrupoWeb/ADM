package mx.gob.se.rug.formulario.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.detallegarantia.to.DetalleTO;
import mx.gob.se.rug.detallegarantia.to.PartesTO;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.garantia.to.ActoContratoTO;
import mx.gob.se.rug.garantia.to.GarantiaTO;
import mx.gob.se.rug.garantia.to.ObligacionTO;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.to.BienEspecialTO;
import mx.gob.se.rug.inscripcion.to.DeudorTO;
import mx.gob.se.rug.inscripcion.to.InscripcionTO;
import mx.gob.se.rug.inscripcion.to.MonedaTO;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
import mx.gob.se.rug.inscripcion.to.TipoBienTO;
import mx.gob.se.rug.inscripcion.to.TipoGarantiaTO;
import mx.gob.se.rug.juez.service.JuezService;
import mx.gob.se.rug.modificacion.to.TipoBienesTO;
import mx.gob.se.rug.seguridad.dao.PrivilegiosDAO;
import mx.gob.se.rug.to.TipoTo;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

public class FormularioAction extends RugBaseAction{

	private static final long serialVersionUID = 1L;
	
	private List<PartesTO> acreedorTOs;	
	private List<PartesTO> deudorTOs;
	private List<TipoBienesTO> tipobienTOs;
	private List<BienEspecialTO> bienesEspTOs;
	private DetalleTO detalleTO;
	private String idTramite;	
	private String nomAcreedor;
	private String idGarantia;
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
	private boolean hayDeudores;
	private boolean hayBienes;
	private String modfechacelebcontrato;
	private UsuarioTO usuario;
	private AcreedorTO acreedorTO;
	private List <AcreedorTO> listaAcreedores;
	private List<DeudorTO> listaDeudores;
	private InscripcionTO inscripcionTO;
	private InscripcionService inscripcionService ;
	private Integer idAcreedorTO;
	private List<TipoBienTO> bienes;
	private String tramite;
	private String tramiteinc;
	private String idPersona;
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
	private ObligacionTO obligacionTO;
	private ActoContratoTO actoContratoTO;
	private GarantiaTO garantiaTO;
	// nuevos
	private String txtregistro;
	private boolean aPrioridad;
	private boolean aRegistro;
	private List <TipoTo> listaBienEspecial;
	private List <TipoTo> listaUsos;
	private String txtfregistro;
	
	private String esModificacion;
	private Integer idInscripcion;
	
	public FormularioAction() {
		super();
	}
	
	public String creaInscripcion() {
		String regresa = Constants.FAILED;
		try {
			MyLogger.Logger.log(Level.INFO, "--valor de idAcreedorTO--"+idAcreedorTO);
			UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
			InscripcionTO inscripcionTO = new InscripcionTO();
			inscripcionTO.setIdPersona(usuario.getPersona().getIdPersona());
			setIdPersona(new Integer(usuario.getPersona().getIdPersona())
					.toString());
			AcreedorTO acreedorDefault = new AcreedorTO();
			setAcreedorTO(acreedorDefault);
			if (getAcreedorTO() != null) {
				inscripcionTO.setIdTipoTramite(31);
				inscripcionTO.setIdPersona(usuario.getPersona().getIdPersona());
				Integer idTramite = inscripcionService.iniciaInscripcion(
						inscripcionTO, acreedorTO);
				if (idTramite.intValue() != 0) {
					inscripcionTO.setIdInscripcion(idTramite);															
					setInscripcionTO(inscripcionTO);																				
					sessionMap.put(Constants.ID_ACREEDOR_REPRESENTADO,
							new Integer(inscripcionTO.getIdPersona()));
					sessionMap.put(Constants.ID_TRAMITE_NUEVO, new Integer(
							idTramite));
					boolean actbit = inscripcionService.insertBitacoraTramite(
							new Integer(Integer.valueOf(idTramite)), 1, 1,
							null, "V");
					if (actbit) {
						setIdInscripcion(idTramite);
						setAcreedorTO(new AcreedorTO());
						regresa = Constants.SUCCESS;
					}else{
						throw new Exception("RUG-InscripcionAction>>CreaInscripcion:::no se pudo actualizar la bitacora del tramite");
					}
				}else{
					throw new Exception("RUG-InscripcionAction>>CreaInscripcion:::no se pudo crear el tramite de inscripcion");
				}
			}else{
				throw new Exception("RUG-InscripcionAction>>CreaInscripcion:::el acreedor es nulo");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return regresa;
	}
	
	public String inicia(){		
		MyLogger.Logger.log(Level.INFO, "RUG-InscripcionAction>>iniciaInscripcionFormUnica:::--entro a iniciaInscipcion");
		response.setHeader("Cache-Control","no-cache"); //Forces caches to obtain a new copy of the page from the origin server
		response.setHeader("Cache-Control","no-store"); //Directs caches not to store the page under any circumstance
		response.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale"
		response.setHeader("Pragma","no-cache"); //HTTP 1.0 backward compatibility
		String regresa = Constants.FAILED;
		cargarBienesEspeciales();
		try {			
			UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
			Integer idTramite;
			if (getIdInscripcion() != null) {				
				if (inscripcionTO == null) {
					inscripcionTO = new InscripcionTO();
				}
				
				sessionMap.put(Constants.ID_TRAMITE_NUEVO, new Integer(
						getIdInscripcion()));

				idTramite = getIdInscripcion();
				inscripcionTO.setIdPersona(usuario.getPersona().getIdPersona());				
				sessionMap.put(Constants.ID_ACREEDOR_REPRESENTADO, new Integer(
						inscripcionTO.getIdPersona()));
			} else {
				String parteCero = creaInscripcion();
				if(parteCero.equalsIgnoreCase("failed"))
					return regresa;
				
				idTramite = (Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO);
			}
						
			/*setListaDeudores(inscripcionService
					.getDeudoresByTramite(idTramite));*/
			setListaDeudores(new ArrayList<DeudorTO>());
			if (getListaDeudores().size() == 0) {
				setHayDeudores(false);

			} else {
				setHayDeudores(true);
			}
			/*setListaAcreedores(inscripcionService
					.getAcreedoresByTramite(idTramite));*/
			setListaAcreedores(new ArrayList<AcreedorTO>());
			if (getListaAcreedores().size() == 0) {
				setHayAcreedores(false);

			} else {
				setHayAcreedores(true);
			}			
			setIdPersona(new Integer(usuario.getPersona().getIdPersona())
					.toString());
			setIdInscripcion(idTramite);
			setIdTramite(idTramite.toString());

			regresa = Constants.SUCCESS;

		} catch (Exception e) {
			e.printStackTrace();

		}		
		return regresa;
	}
	
	public String saveformulario() {
		String regresa = Constants.FAILED;
		try {
			if (inscripcionTO == null) {
				inscripcionTO = new InscripcionTO();
			}
			UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
			Integer idTramite;
			if (getIdInscripcion() != null) {				
				if (inscripcionTO == null) {
					inscripcionTO = new InscripcionTO();
				}
				sessionMap.put(Constants.ID_TRAMITE_NUEVO, new Integer(getIdInscripcion()));
				idTramite = getIdInscripcion();
				inscripcionTO.setIdInscripcion(idTramite);
				inscripcionTO.setIdPersona(usuario.getPersona().getIdPersona());				
				sessionMap.put(Constants.ID_ACREEDOR_REPRESENTADO, new Integer(usuario.getPersona().getIdPersona()));
			} else {
				idTramite = (Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO);
				if (inscripcionTO == null) {
					inscripcionTO = new InscripcionTO();
				}
			}
			if (idTramite != null && idTramite != 0) {
				
				setGarantiaTO(new GarantiaTO());
				setObligacionTO(new ObligacionTO());
				setActoContratoTO(new ActoContratoTO());	
				setIdTipoMoneda(22);		
				
				actoContratoTO.setTipoGarantia("MI");
				actoContratoTO.setDescripcion(getModdescripcion());
				MyLogger.Logger.log(Level.INFO, "obtuvo el instrumento: " + getInstrumento());
				actoContratoTO.setInstrumentoPub(getInstrumento());
				actoContratoTO.setOtrosTerminos(getModotrosgarantia());
				actoContratoTO.setCpRegistro(isaRegistro());
				actoContratoTO.setTxtRegistro(getTxtregistro()+" Fecha inscrita: " + getTxtfregistro());
				//MyLogger.Logger.log(Level.INFO, "RUG-InscripcionAction>>Representante" + getModotroscontrato());
				if(obligacionTO==null) {
					obligacionTO = new ObligacionTO();
				}
				StringBuffer representante = new StringBuffer();
				MyLogger.Logger.log(Level.INFO, "entro a cargar el representante: " + idTramite);
				
				List<DeudorTO> deudores = inscripcionService.getDeudoresByTramite(idTramite);
				for(DeudorTO vDeudor : deudores) {
					if(vDeudor.getTipoPersona().equalsIgnoreCase("PM")) {
						representante.append(vDeudor.getUbicacionInscrito());
						representante.append(". ");
					}
				}
				
				List<AcreedorTO> acreedores = inscripcionService.getAcreedoresByTramite(idTramite);
				for(AcreedorTO vAcreedor : acreedores) {
					if(vAcreedor.getTipoPersona().equalsIgnoreCase("PM")) {
						representante.append(vAcreedor.getUbicacionInscrito());
						representante.append(". ");
					}
				}
				obligacionTO.setOtrosTerminos(representante.toString());
								
				garantiaTO = new GarantiaTO();
				garantiaTO.setObligacionTO(obligacionTO);
				garantiaTO.setActoContratoTO(actoContratoTO);
				//garantiaTO.setIdTipoGarantia(Integer.valueOf(idTipoGarantia));
				inscripcionTO.setGarantiaTO(garantiaTO);			 
				inscripcionTO.setIdInscripcion(new Integer((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO)));
				if (getAutoridad() != null && (!getAutoridad().equals(""))) {
					JuezService juezService = new JuezService();					
					juezService.insertJuez((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO), getAutoridad());
				} 
				inscripcionTO.setIdPersona(usuario.getPersona().getIdPersona());
				inscripcionTO.setIdInscripcion(idTramite);
				garantiaTO.setIdMoneda(getIdTipoMoneda());
				Integer idGarantiaPendiente = inscripcionService.getIDGarantiaByIDTramite(idTramite);
				
				Integer idGarantia = inscripcionService.insertaGarantia(inscripcionTO, garantiaTO);
				if (idGarantia == 0) {
					throw new Exception("RUG-InscripcionAction>>agregarGarantia::: no se logro crear la garantia 2");						
				}
				inscripcionTO.getGarantiaTO().setIdGarantia(idGarantia);
					
				
				setActoContratoTO(getGarantiaTO().getActoContratoTO());
				setObligacionTO(getGarantiaTO().getObligacionTO());
				if (inscripcionTO == null) {
					inscripcionTO = new InscripcionTO();
				}
				
				//setOtorganteTO(inscripcionService.getOtorganteByID((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO)));
				setListaDeudores(inscripcionService.getDeudoresByTramite((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO)));
				setListaAcreedores(inscripcionService.getAcreedoresByTramite((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO)));
				 
				boolean actbit = inscripcionService.insertBitacoraTramite(
						Integer.valueOf((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO)), 1, 3, null,"V");
				if (actbit) {
					/** emulo paso 3 **/
					boolean actbit2 = inscripcionService.insertBitacoraTramite(Integer.valueOf(idTramite), 1, 3, null,"V");
					if (actbit2) {
						/** emulo la vigencia **/
						inscripcionTO.setMeses("5");
						boolean sePudo = inscripcionService.actualizaVigencia(inscripcionTO, Integer.valueOf(idTramite), 5, 3,null, "V");
						if (sePudo) {						
							setIdInscripcion(idTramite);							
							sessionMap.put(Constants.ID_TRAMITE_NUEVO, new Integer(idTramite));
							regresa = Constants.SUCCESS;
						}else{
							throw new Exception("RUG-InscripcionAction>>actualizaVigencia:::no se pudo actualizar la bitacora de esta inscripcion");
						}
						
					} else{
						throw new Exception("RUG>>paso 3:::la bitacora de la inscripcion no pudo ser actualizada");
					}
					
				}else{
					throw new Exception("RUG-InscripcionAction>>agregarGarantia::: no se logro actualizar la bitacora de esta inscripcion");
				}
			} else {
				throw new Exception("RUG-InscripcionAction>>agregarGarantia::: no se logro especificar el IdTramite");
			}

		} catch (Exception e) {
			regresa = Constants.FAILED;
			e.printStackTrace();
		}
		return regresa;
	}
	
	
	public void cargarBienesEspeciales() {
		TipoTo tipo0 = new TipoTo();
		tipo0.setIdTipo("0");
		tipo0.setDesTipo("Seleccione");
		TipoTo tipo1 = new TipoTo();
		tipo1.setIdTipo("1");
		tipo1.setDesTipo("Veh\u00edculos");
		/* no facturas
		TipoTo tipo2 = new TipoTo();
		tipo2.setIdTipo("2");
		tipo2.setDesTipo("Facturas");*/
		TipoTo tipo3 = new TipoTo();
		tipo3.setIdTipo("3");
		tipo3.setDesTipo("Otros");
		
		listaBienEspecial = new ArrayList<TipoTo>();
		listaBienEspecial.add(tipo0);
		listaBienEspecial.add(tipo1);
		//listaBienEspecial.add(tipo2);
		listaBienEspecial.add(tipo3);
		
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

	public List<PartesTO> getAcreedorTOs() {
		return acreedorTOs;
	}

	public void setAcreedorTOs(List<PartesTO> acreedorTOs) {
		this.acreedorTOs = acreedorTOs;
	}

	public List<PartesTO> getDeudorTOs() {
		return deudorTOs;
	}

	public void setDeudorTOs(List<PartesTO> deudorTOs) {
		this.deudorTOs = deudorTOs;
	}

	public List<TipoBienesTO> getTipobienTOs() {
		return tipobienTOs;
	}

	public void setTipobienTOs(List<TipoBienesTO> tipobienTOs) {
		this.tipobienTOs = tipobienTOs;
	}

	public List<BienEspecialTO> getBienesEspTOs() {
		return bienesEspTOs;
	}

	public void setBienesEspTOs(List<BienEspecialTO> bienesEspTOs) {
		this.bienesEspTOs = bienesEspTOs;
	}

	public DetalleTO getDetalleTO() {
		return detalleTO;
	}

	public void setDetalleTO(DetalleTO detalleTO) {
		this.detalleTO = detalleTO;
	}

	public String getIdTramite() {
		return idTramite;
	}

	public void setIdTramite(String idTramite) {
		this.idTramite = idTramite;
	}

	public String getNomAcreedor() {
		return nomAcreedor;
	}

	public void setNomAcreedor(String nomAcreedor) {
		this.nomAcreedor = nomAcreedor;
	}

	public String getIdGarantia() {
		return idGarantia;
	}

	public void setIdGarantia(String idGarantia) {
		this.idGarantia = idGarantia;
	}

	public Integer getIdgarantia() {
		return idgarantia;
	}

	public void setIdgarantia(Integer idgarantia) {
		this.idgarantia = idgarantia;
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

	public String getModmonto() {
		return modmonto;
	}

	public void setModmonto(String modmonto) {
		this.modmonto = modmonto;
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

	public boolean isHayAcreedores() {
		return hayAcreedores;
	}

	public void setHayAcreedores(boolean hayAcreedores) {
		this.hayAcreedores = hayAcreedores;
	}

	public boolean isHayDeudores() {
		return hayDeudores;
	}

	public void setHayDeudores(boolean hayDeudores) {
		this.hayDeudores = hayDeudores;
	}

	public boolean isHayBienes() {
		return hayBienes;
	}

	public void setHayBienes(boolean hayBienes) {
		this.hayBienes = hayBienes;
	}

	public String getModfechacelebcontrato() {
		return modfechacelebcontrato;
	}

	public void setModfechacelebcontrato(String modfechacelebcontrato) {
		this.modfechacelebcontrato = modfechacelebcontrato;
	}

	public UsuarioTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioTO usuario) {
		this.usuario = usuario;
	}

	public AcreedorTO getAcreedorTO() {
		return acreedorTO;
	}

	public void setAcreedorTO(AcreedorTO acreedorTO) {
		this.acreedorTO = acreedorTO;
	}

	public List<AcreedorTO> getListaAcreedores() {
		return listaAcreedores;
	}

	public void setListaAcreedores(List<AcreedorTO> listaAcreedores) {
		this.listaAcreedores = listaAcreedores;
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

	public List<TipoBienTO> getBienes() {
		return bienes;
	}

	public void setBienes(List<TipoBienTO> bienes) {
		this.bienes = bienes;
	}

	public String getTramite() {
		return tramite;
	}

	public void setTramite(String tramite) {
		this.tramite = tramite;
	}

	public String getTramiteinc() {
		return tramiteinc;
	}

	public void setTramiteinc(String tramiteinc) {
		this.tramiteinc = tramiteinc;
	}

	public String getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}

	public List<TipoGarantiaTO> getTipoGarantiaTOs() {
		return tipoGarantiaTOs;
	}

	public void setTipoGarantiaTOs(List<TipoGarantiaTO> tipoGarantiaTOs) {
		this.tipoGarantiaTOs = tipoGarantiaTOs;
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

	public List<MonedaTO> getListaMonedas() {
		return listaMonedas;
	}

	public void setListaMonedas(List<MonedaTO> listaMonedas) {
		this.listaMonedas = listaMonedas;
	}

	public Integer getIdTipoMoneda() {
		return idTipoMoneda;
	}

	public void setIdTipoMoneda(Integer idTipoMoneda) {
		this.idTipoMoneda = idTipoMoneda;
	}

	public Integer getMoneda() {
		return moneda;
	}

	public void setMoneda(Integer moneda) {
		this.moneda = moneda;
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

	public boolean isaMonto() {
		return aMonto;
	}

	public void setaMonto(boolean aMonto) {
		this.aMonto = aMonto;
	}

	public String getModinstrumento() {
		return modinstrumento;
	}

	public void setModinstrumento(String modinstrumento) {
		this.modinstrumento = modinstrumento;
	}

	public String getFechaModificacion() {
		return FechaModificacion;
	}

	public void setFechaModificacion(String fechaModificacion) {
		FechaModificacion = fechaModificacion;
	}

	public String getFechaCelebOb() {
		return fechaCelebOb;
	}

	public void setFechaCelebOb(String fechaCelebOb) {
		this.fechaCelebOb = fechaCelebOb;
	}

	public String getTipoContOb() {
		return tipoContOb;
	}

	public void setTipoContOb(String tipoContOb) {
		this.tipoContOb = tipoContOb;
	}

	public String getOtrosTermOb() {
		return otrosTermOb;
	}

	public void setOtrosTermOb(String otrosTermOb) {
		this.otrosTermOb = otrosTermOb;
	}

	public String getFechaCelebracionC() {
		return fechaCelebracionC;
	}

	public void setFechaCelebracionC(String fechaCelebracionC) {
		this.fechaCelebracionC = fechaCelebracionC;
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

	public List<TipoTo> getListaBienEspecial() {
		return listaBienEspecial;
	}

	public void setListaBienEspecial(List<TipoTo> listaBienEspecial) {
		this.listaBienEspecial = listaBienEspecial;
	}

	public String getEsModificacion() {
		return esModificacion;
	}

	public void setEsModificacion(String esModificacion) {
		this.esModificacion = esModificacion;
	}

	public Integer getIdInscripcion() {
		return idInscripcion;
	}

	public void setIdInscripcion(Integer idInscripcion) {
		this.idInscripcion = idInscripcion;
	}

	public List<DeudorTO> getListaDeudores() {
		return listaDeudores;
	}

	public void setListaDeudores(List<DeudorTO> listaDeudores) {
		this.listaDeudores = listaDeudores;
	}

	public ObligacionTO getObligacionTO() {
		return obligacionTO;
	}

	public void setObligacionTO(ObligacionTO obligacionTO) {
		this.obligacionTO = obligacionTO;
	}

	public ActoContratoTO getActoContratoTO() {
		return actoContratoTO;
	}

	public void setActoContratoTO(ActoContratoTO actoContratoTO) {
		this.actoContratoTO = actoContratoTO;
	}

	public GarantiaTO getGarantiaTO() {
		return garantiaTO;
	}

	public void setGarantiaTO(GarantiaTO garantiaTO) {
		this.garantiaTO = garantiaTO;
	}

	public List<TipoTo> getListaUsos() {
		return listaUsos;
	}

	public void setListaUsos(List<TipoTo> listaUsos) {
		this.listaUsos = listaUsos;
	}

	public String getTxtfregistro() {
		return txtfregistro;
	}

	public void setTxtfregistro(String txtfregistro) {
		this.txtfregistro = txtfregistro;
	}

	public String getModotroscontrato() {
		return modotroscontrato;
	}

	public void setModotroscontrato(String modotroscontrato) {
		this.modotroscontrato = modotroscontrato;
	}
	
	
}
