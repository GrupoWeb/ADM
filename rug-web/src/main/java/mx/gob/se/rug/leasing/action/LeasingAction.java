package mx.gob.se.rug.leasing.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import mx.gob.se.rug.acreedores.dao.AcreedoresDAO;
import mx.gob.se.rug.acreedores.service.AcreedoresService;
import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.garantia.to.ActoContratoTO;
import mx.gob.se.rug.garantia.to.BoletaPagoTO;
import mx.gob.se.rug.garantia.to.GarantiaTO;
import mx.gob.se.rug.garantia.to.ObligacionTO;

import mx.gob.se.rug.inscripcion.dao.BienPendienteDAO;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.service.impl.InscripcionServiceImpl;
import mx.gob.se.rug.inscripcion.to.BienEspecialTO;
import mx.gob.se.rug.inscripcion.to.BienPendienteTO;
import mx.gob.se.rug.inscripcion.to.DeudorTO;
import mx.gob.se.rug.inscripcion.to.InscripcionTO;
import mx.gob.se.rug.inscripcion.to.MonedaTO;
import mx.gob.se.rug.inscripcion.to.NacionalidadTO;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
import mx.gob.se.rug.inscripcion.to.TipoBienInscripcionTXT;
import mx.gob.se.rug.inscripcion.to.TipoBienTO;
import mx.gob.se.rug.inscripcion.to.TipoGarantiaTO;
import mx.gob.se.rug.juez.dao.JuezDAO;
import mx.gob.se.rug.juez.service.JuezService;


import mx.gob.se.rug.seguridad.dao.PrivilegiosDAO;
import mx.gob.se.rug.seguridad.to.PrivilegioTO;
import mx.gob.se.rug.seguridad.to.PrivilegiosTO;
import mx.gob.se.rug.to.TipoPersona;
import mx.gob.se.rug.to.TipoTo;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

/**
 * 
 * @author Licenciado Abraham Stalin Aguilar Valencia Action del RUG el
 *         principal tramite dentro del sistema en el cual se dan de alta
 *         garantias en el RUG, favor de no modificar sin antes informarme de
 *         ello.
 * 
 */
public class LeasingAction extends RugBaseAction {
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	private Integer idAcreedorTO;
	private String firmaSi;
	private Integer idInscripcion;
	private Integer idLocalidad;
	private Integer idColonia;
	private String idTipoGarantia;
	private boolean insPublico;
	private String tipoGarantiaStr;
	private String tipoBienesStr;
	
	private String tipoBienAll;
	private Integer idTipoMoneda;
	private String idPersona;
	private boolean hayOtorgante;
	private boolean hayAcreedores;
	private boolean hayDeudores;
	private boolean hayBienes;
	private boolean estaElMismo;
	private String tipoFirma;
	private boolean errorInscripcion;
	private boolean actualizaGarantia = false;

	private List<AcreedorTO> listaAcreedores;
	private List<TipoPersona> listaTipoPersona;
	private List<DeudorTO> listaDeudores;
	private List<TipoBienTO> listaTipoBien;
	private List<TipoBienTO> listaTipoBien2;
	private List<TipoGarantiaTO> listaTipoGarantia;
	private List<MonedaTO> listaMonedas;
	private List<NacionalidadTO> listaNacionalidad;
	private List<TipoBienInscripcionTXT> listaTipoBienes;
	private List<BienPendienteTO> listaBienesPendientes;
	private List<BoletaPagoTO> listaBoletaPago;
	private List<String> opcionesCM;
	private List <OtorganteTO> otorganteTO;
	private List <TipoTo> listaBienEspecial;
	private AcreedorTO acreedorTO;
	private AcreedorTO acreedorTORep;
	private DeudorTO deudorTO;
	private InscripcionTO inscripcionTO;
	private ObligacionTO obligacionTO;
	private ActoContratoTO actoContratoTO;
	private GarantiaTO garantiaTO;
	private String mdDescripcion;
	private String mdIdentificador;
	private String mdIdentificador1;
	private String mdIdentificador2;
	private String mdIdentificador3;
	private TipoTo mdBienEspecial;
	private List<BienEspecialTO> listaBienes;
	private List <TipoTo> listaUsos;	

	private String autoridad;
	private int num;

	private String mdNumeroBoleta;
	private String mdCantidad;
	private String saldo;
	
	private String datos = "";
	private List<Integer> ListaIdInscripciones;
	private List<Integer> ListaIdPersonasInscripciones;
	private List<Integer> ListaIdGarantias;
	private String erro;
	
	//servicios
	
	private InscripcionService inscripcionService;
	private AcreedoresService acreedoresService;
	
	public void setInscripcionService(InscripcionService inscripcionService) {
		this.inscripcionService = inscripcionService;
	}
	public void setAcreedoresService(AcreedoresService acreedoresService) {
		this.acreedoresService = acreedoresService;
	}

	
	/*
	 * Este metodo se utiliza para cambiar el estatus de un tramite al paso 3
	 */
	
        public String carlos(){
            return "descripcion de la moneda";
            
        }
	
	public String load() {
		return SUCCESS;
	}

	public String cambiaEstatusGarantia(){		
		String regresa = "failed";
		try{
			if (getIdInscripcion() != null){
				
				boolean actbit = inscripcionService.insertBitacoraTramite(
						getIdInscripcion(), 1, 3, null,
						"V");
				
				if (actbit){
					sessionMap.put(Constants.ID_TRAMITE_NUEVO, new Integer(
							getIdInscripcion()));
					regresa = "success";
				}else{
					throw new Exception("RUG-InscripcionAction>>CambiaEstatusGarantia:::no se pudo actualizar la bitacora tramite");
				}
			}else{
				throw new Exception("RUG-InscripcionAction>>CambiaEstatusGarantia:::la inscripcion no cuenta con idTramite");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return regresa;
	}

	/*
	 * Aqui se carga la lista de los acreedores representados de el usuario que
	 * va en session.
	 */

	public String iniciaInscripcion() {
		MyLogger.Logger.log(Level.INFO, "RUG-InscripcionAction>>iniciaInscripcion:::--entro a iniciaInscipcion");
		String regresa = "failed";
		try {
			UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
			Integer idPersona = usuario.getPersona().getIdPersona();
			PrivilegiosDAO privilegiosDAO = new PrivilegiosDAO();
			List<AcreedorTO> lista = inscripcionService.getAcreedoresByID(usuario.getPersona().getIdPersona());
			List<AcreedorTO> listaAcreedores = new ArrayList<AcreedorTO>();
			Iterator<AcreedorTO> it = lista.iterator();
			AcreedorTO acre;
			while (it.hasNext()) {
				acre = it.next();
				if (privilegiosDAO.getTienePermiso(acre.getIdPersona(),
						idPersona, new Integer(1))) {
					listaAcreedores.add(acre);
				}
			}
			setListaAcreedores(listaAcreedores);
			if (getListaAcreedores().size() > 0) {
				setIdInscripcion(null);
				regresa = "success";
			}else{
				throw new Exception("RUG-InscripcionAction>>iniciaInscripcion:::este usuario no cuenta con un acreedor con permiso para inscribir");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return regresa;
	}

	/*
	 * Termina la carga de acreedores representados
	 */

	public String creaInscripcion() {
		String regresa = "failed";
		try {
			MyLogger.Logger.log(Level.INFO, "--valor de idAcreedorTO--"+idAcreedorTO);
			UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
			InscripcionTO inscripcionTO = new InscripcionTO();
			inscripcionTO.setIdPersona(usuario.getPersona().getIdPersona());
			setIdPersona(new Integer(usuario.getPersona().getIdPersona())
					.toString());
			AcreedorTO acreedorDefault = new AcreedorTO(); 
			setAcreedorTO(acreedorDefault);
//			setAcreedorTORep(inscripcionService.getAcreedorByID(idAcreedorTO));
			setAcreedorTORep(acreedorDefault);
			if (getAcreedorTO() != null) {
				inscripcionTO.setIdTipoTramite(1);
				inscripcionTO.setIdPersona(usuario.getPersona().getIdPersona());
                                //inscripcionTO.getGarantiaTO().setIdTipoGarantia(16);
				Integer idTramite = inscripcionService.iniciaInscripcion(
						inscripcionTO, acreedorTO);
				if (idTramite.intValue() != 0) {
					inscripcionTO.setIdInscripcion(idTramite);
					setDeudorTO(new DeudorTO());
					setOtorganteTO(new ArrayList<OtorganteTO>());					
					setInscripcionTO(inscripcionTO);					
					setDeudorTO(new DeudorTO());
					setListaNacionalidad(inscripcionService.getNacionalidades());
					setEstaElMismo(false);					
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
						regresa = "success";
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

	/*
	 * se termina de crear la inscripcion
	 */
	/*
	 * Paso 1
	 */
	public String paso1() {
		setErro("");
		String regresa = "failed";
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
				//setAcreedorTORep(inscripcionService
						//.getByIDTramite(getIdInscripcion()));
				AcreedorTO acreedorRep = new AcreedorTO();
				setAcreedorTORep(acreedorRep);
				sessionMap.put(Constants.ID_ACREEDOR_REPRESENTADO, new Integer(
						inscripcionTO.getIdPersona()));
			} else {
//				setAcreedorTORep(acreedoresService.getARByID((Integer) sessionMap.get(Constants.ID_ACREEDOR_REPRESENTADO)));
				//setAcreedorTORep(acreedoresService.getAcreRepById((Integer) sessionMap.get(Constants.ID_ACREEDOR_REPRESENTADO)));
				String parteCero = creaInscripcion();
				if(parteCero.equalsIgnoreCase("failed"))
					return regresa;
				
				AcreedorTO acreedorRep = new AcreedorTO();
				setAcreedorTORep(acreedorRep);
				idTramite = (Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO);
			}
			if (getAcreedorTORep() != null) {
				// inscripcionService.insertBitacoraTramite(getIdTramite(),
				// idEstatus, idPaso, fechaCelebracion, banderaFecha)

				setListaTipoPersona(inscripcionService.getTiposPersona());
				//setOtorganteTO(inscripcionService.getOtorganteByID(idTramite));

				if (getOtorganteTO() == null) {
					MyLogger.Logger.log(Level.INFO, "No hay otorgante");
					setHayOtorgante(false);
				} else {

					setHayOtorgante(true);
				}
				setListaDeudores(inscripcionService
						.getDeudoresByTramite(idTramite));
				if (getListaDeudores().size() == 0) {
					setHayDeudores(false);

				} else {
					setHayDeudores(true);
				}
				setListaAcreedores(inscripcionService
						.getAcreedoresByTramite(idTramite));
				if (getListaAcreedores().size() == 0) {
					setHayAcreedores(false);

				} else {
					setHayAcreedores(true);
				}
				regresa = "success";
				setIdPersona(new Integer(usuario.getPersona().getIdPersona())
						.toString());
				setIdInscripcion(idTramite);

			} else {
				throw new Exception("RUG-InscripcionAction>>paso1:::no se logro encontrar al acreedor representado para este tramite");
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return regresa;
	}
	
	public void cargarBienesEspeciales() {
		TipoTo tipo0 = new TipoTo();
		tipo0.setIdTipo("0");
		tipo0.setDesTipo("Seleccione...");
		TipoTo tipo1 = new TipoTo();
		tipo1.setIdTipo("1");
		tipo1.setDesTipo("Veh\u00edculos");
		
                
                TipoTo tipo2 = new TipoTo();
		tipo2.setIdTipo("4");
		tipo2.setDesTipo("Maquinaria"); 
                
		TipoTo tipo3 = new TipoTo();
		tipo3.setIdTipo("3");
		tipo3.setDesTipo("Otros");
		
		listaBienEspecial = new ArrayList<TipoTo>();
		listaBienEspecial.add(tipo0);
		listaBienEspecial.add(tipo1);
		listaBienEspecial.add(tipo2);
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

	/*
	 * Termina el paso 1
	 */

	/*
	 * Paso 2
	 */

	public String paso2() {
		String regresa = "failed";
		cargarBienesEspeciales();
		
		MyLogger.Logger.log(Level.INFO, "entro al paso 2 de la inscripcion");
		try {
			setAutoridad("");
			UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
			Integer idTramite;
			if (getIdInscripcion() != null) {				
				if (inscripcionTO == null) {
					inscripcionTO = new InscripcionTO();
				}
				sessionMap.put(Constants.ID_TRAMITE_NUEVO, new Integer(getIdInscripcion()));
				idTramite = getIdInscripcion();
				System.out.println("jj inscripcion tramite leasing" + idTramite);
				inscripcionTO.setIdInscripcion(idTramite);
				inscripcionTO.setIdPersona(usuario.getPersona().getIdPersona());
				setAcreedorTORep(inscripcionService.getByIDTramite(getIdInscripcion()));
				sessionMap.put(Constants.ID_ACREEDOR_REPRESENTADO, new Integer(usuario.getPersona().getIdPersona()));
			} else {
				System.out.println("Parte else leasing ");
//				setAcreedorTORep(acreedoresService.getARByID((Integer) sessionMap.get(Constants.ID_ACREEDOR_REPRESENTADO)));
				setAcreedorTORep(acreedoresService.getAcreRepById((Integer) sessionMap.get(Constants.ID_ACREEDOR_REPRESENTADO)));
				
				idTramite = (Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO);
				if (inscripcionTO == null) {
					inscripcionTO = new InscripcionTO();
				}
			}
			/*
			 * Validacion de los textos igual que en la inscripcion de la
			 * garantia tambien que no se debe mostrar cuando sea derivada de de
			 * arrendamiento: campo de fecha : Fecha de celebracion del Acto o
			 * Contrato
			 */
			System.out.println("Id Tramite Leasing " + idTramite);
			if (idTramite != null) {
				Integer idGarantia = inscripcionService.getIDGarantiaByIDTramite(new Integer(idTramite));
				if (idGarantia == 0) {
					setActualizaGarantia(false);
					setGarantiaTO(new GarantiaTO());
					setObligacionTO(new ObligacionTO());
					setActoContratoTO(new ActoContratoTO());	
					setIdTipoMoneda(22);		
					
				} else {
					PrivilegiosDAO privilegiosDAO = new PrivilegiosDAO();
					PrivilegiosTO privilegiosTO = new PrivilegiosTO();
					privilegiosTO.setIdRecurso(new Integer(6));

					privilegiosTO = privilegiosDAO.getPrivilegios(privilegiosTO,(UsuarioTO) sessionMap.get(Constants.USUARIO));
					Map<Integer, PrivilegioTO> priv = privilegiosTO.getMapPrivilegio();
					if ((priv.get(new Integer(20)) != null)) {
						JuezDAO dao = new JuezDAO();
						String cadena = dao.showJuez(idTramite);					
					
						setAutoridad(cadena);
					} 
					setListaTipoBien2(inscripcionService.getTipoBienesByIdGarantiaPendiente(idGarantia));
					
					GarantiaTO gTO = inscripcionService.getInscripcionByID(new Integer(idGarantia));
					if (gTO == null) {
						setActualizaGarantia(false);
						GarantiaTO  garantiaTO = new GarantiaTO();
						garantiaTO.setIdMoneda(22);
						setGarantiaTO(garantiaTO);					
						
					} else {							
						setActualizaGarantia(true);
						setIdTipoMoneda(gTO.getIdMoneda());						
						setGarantiaTO(gTO);
						ActoContratoTO acTO = getGarantiaTO().getActoContratoTO();
						setIdTipoGarantia(gTO.getIdTipoGarantia().toString());
						
						if (acTO == null) {
							setActoContratoTO(new ActoContratoTO());
							getActoContratoTO().setCambiosBienesMonto(false);
							getActoContratoTO().setNoGarantiaPreviaOt(false);
							getActoContratoTO().setGarantiaPrioritaria(false);
							getActoContratoTO().setCpRegistro(false);
						} else {
							setActoContratoTO(acTO);
						}
						
						
						
						ObligacionTO obTO = getGarantiaTO().getObligacionTO();
						if (obTO == null) {
							setObligacionTO(new ObligacionTO());
						} else {
							setObligacionTO(obTO);
						}
					}
				}

				setListaTipoBien(inscripcionService.getTiposBien());
				setListaTipoGarantia(inscripcionService.getTiposGarantia());
				setListaMonedas(inscripcionService.getMonedas());

				boolean actbit = inscripcionService.insertBitacoraTramite(Integer.valueOf(idTramite), 1, 2, null, "V");
				if (actbit) {
					setIdInscripcion(Integer.valueOf(idTramite));
					regresa = "success";
				}else{
					throw new Exception("RUG-InscripcionAction>>paso2::: no se logro actualizar la bitacora de esta Inscripcion");
				}
			} else {
				throw new Exception("RUG-InscripcionAction>>paso2::: no se logro especificar el IdTramite");
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return regresa;
	}

	/*
	 * termina paso 2
	 */
	/*
	 * Se Agrega la garantia
	 */
	public String agregarGarantia() {
		String regresa = "failed";
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
				setAcreedorTORep(inscripcionService.getByIDTramite(getIdInscripcion()));
				sessionMap.put(Constants.ID_ACREEDOR_REPRESENTADO, new Integer(acreedorTORep.getIdPersona()));
			} else {
//				setAcreedorTORep(acreedoresService.getARByID((Integer) sessionMap.get(Constants.ID_ACREEDOR_REPRESENTADO)));
				setAcreedorTORep(acreedoresService.getAcreRepById((Integer) sessionMap.get(Constants.ID_ACREEDOR_REPRESENTADO)));
				idTramite = (Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO);
				if (inscripcionTO == null) {
					inscripcionTO = new InscripcionTO();
				}
			}
			if (idTramite != null && idTramite != 0) {
				
				if(tipoBienAll != null && tipoBienAll.equalsIgnoreCase("true")){
					actoContratoTO.setTipoBienes("10");
				}
				
				actoContratoTO.setTipoGarantia(idTipoGarantia);
				garantiaTO = new GarantiaTO();
                                garantiaTO.setIdTipoGarantia(16);
				garantiaTO.setActoContratoTO(actoContratoTO);
				
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
				garantiaTO.setObligacionTO(obligacionTO);
				
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
				if (idGarantiaPendiente == 0) {
					setActualizaGarantia(false);
				} else {
					setActualizaGarantia(true);
				}
				if (!isActualizaGarantia()) {
					if(getActoContratoTO() == null) {
						garantiaTO.setActoContratoTO(new ActoContratoTO());
					} else {
						garantiaTO.setActoContratoTO(getActoContratoTO());
					}
					if(getObligacionTO() == null) {
						garantiaTO.setObligacionTO(new ObligacionTO());
					}
					
					Integer idGarantia = inscripcionService.insertaGarantia(inscripcionTO, garantiaTO);
					if (idGarantia == 0) {
						throw new Exception("RUG-InscripcionAction>>agregarGarantia::: no se logro crear la garantia");						
					}
					inscripcionTO.getGarantiaTO().setIdGarantia(idGarantia);
					if (getIdTipoGarantia() != null) {
						setTipoGarantiaStr(inscripcionService.descTipoGarantiaByID(Integer.valueOf(getIdTipoGarantia())));
					} else {
						setTipoGarantiaStr("no tenia idTipoGarantia");						
					}
				} else {	
					if(getActoContratoTO() == null) {
						garantiaTO.setActoContratoTO(new ActoContratoTO());
					} 
					if(getObligacionTO() == null) {
						garantiaTO.setObligacionTO(new ObligacionTO());
					}
					Integer idGarantia = inscripcionService.getIDGarantiaByIDTramite((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO));
					
					inscripcionService.updateGarantia(inscripcionTO,garantiaTO, idGarantia);
					inscripcionTO.getGarantiaTO().setIdGarantia(idGarantia);
					if (getIdTipoGarantia() != null) {
						setTipoGarantiaStr(inscripcionService.descTipoGarantiaByID(Integer.valueOf(getIdTipoGarantia())));
					} else {
						setTipoGarantiaStr("no tenia idTipoGarantia");						
					}
				}
				setActoContratoTO(getGarantiaTO().getActoContratoTO());
				setObligacionTO(getGarantiaTO().getObligacionTO());
				if (inscripcionTO == null) {
					inscripcionTO = new InscripcionTO();
				}
				setOtorganteTO(inscripcionService.getOtorganteByID((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO)));
				setListaDeudores(inscripcionService.getDeudoresByTramite((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO)));
				setListaAcreedores(inscripcionService.getAcreedoresByTramite((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO)));
				if (getOtorganteTO() == null) {
					throw new Exception("RUG-InscripcionAction>>agregarGarantia::: la inscripcion no cuenta con un otorgante y es necesario para llegar al paso 2 o 3");					
				} else {
					setHayOtorgante(true);
				}
				if (getListaDeudores().size() > 0) {				
					setHayDeudores(true);
				}
				if (getListaAcreedores().size() > 0) {
					setHayAcreedores(true);					
				} 
				boolean actbit = inscripcionService.insertBitacoraTramite(
						Integer.valueOf((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO)), 1, 3, null,"V");
				if (actbit) {
					setIdInscripcion(idTramite);
					regresa = "success";
				}else{
					throw new Exception("RUG-InscripcionAction>>agregarGarantia::: no se logro actualizar la bitacora de esta inscripcion");
				}
			} else {
				throw new Exception("RUG-InscripcionAction>>agregarGarantia::: no se logro especificar el IdTramite");
			}

		} catch (Exception e) {
			regresa = "failed";
			e.printStackTrace();
		}
		return regresa;
	}

	/*
	 * termina agregar Garantia
	 */

	/*
	 * Paso 3
	 */

	public String paso3() {
		String regresa = "failed";
		try {
			UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
								
			setSaldo("1");
			
			Integer idTramite;
			if (getIdInscripcion() != null) {				
				if (inscripcionTO == null) {
					inscripcionTO = new InscripcionTO();
				}
				sessionMap.put(Constants.ID_TRAMITE_NUEVO, new Integer(getIdInscripcion()));
				idTramite = getIdInscripcion();
				inscripcionTO.setIdInscripcion(idTramite);
				inscripcionTO.setIdPersona(usuario.getPersona().getIdPersona());
				setAcreedorTORep(inscripcionService.getByIDTramite(getIdInscripcion()));
				sessionMap.put(Constants.ID_ACREEDOR_REPRESENTADO, new Integer(usuario.getPersona().getIdPersona()));

			} else {
				setAcreedorTORep(acreedoresService.getARByID((Integer) sessionMap.get(Constants.ID_ACREEDOR_REPRESENTADO)));
				//en esta parte tenemos que enviar el id de la garantia para obtener el curp
//				setAcreedorTORep(acreedoresService.getAcreRepById((Integer) sessionMap.get(Constants.ID_ACREEDOR_REPRESENTADO)));
				idTramite = (Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO);
				if (inscripcionTO == null) {
					inscripcionTO = new InscripcionTO();
				}
			}
			if (idTramite != null) {
				Integer idGarantia = inscripcionService.getIDGarantiaByIDTramite(idTramite);
				PrivilegiosDAO privilegiosDAO = new PrivilegiosDAO();
				PrivilegiosTO privilegiosTO = new PrivilegiosTO();
				privilegiosTO.setIdRecurso(new Integer(6));
				privilegiosTO = privilegiosDAO.getPrivilegios(privilegiosTO,(UsuarioTO) sessionMap.get(Constants.USUARIO));
				Map<Integer, PrivilegioTO> priv = privilegiosTO.getMapPrivilegio();
				if ((priv.get(new Integer(20)) != null)) {
					JuezDAO dao = new JuezDAO();
					setAutoridad( dao.showJuez(idTramite));
				} 
				if (idGarantia > 0) {					
					setGarantiaTO(inscripcionService.getInscripcionByID(idGarantia));
					setActoContratoTO(getGarantiaTO().getActoContratoTO());
					setObligacionTO(getGarantiaTO().getObligacionTO());
					setIdTipoGarantia(getGarantiaTO().getIdTipoGarantia().toString());
					setInsPublico(false);
					if (getActoContratoTO().getInstrumentoPub() != null && (!getActoContratoTO().getInstrumentoPub().equals(""))) {
						setInsPublico(true);
					}
					JuezService juezService = new JuezService();					
					setAutoridad(juezService.showJuez(idTramite)); 
					setOtorganteTO(inscripcionService.getOtorganteByID(Integer.valueOf(idTramite)));
					setListaDeudores(inscripcionService.getDeudoresByTramite(idTramite));
					setListaAcreedores(inscripcionService.getAcreedoresByTramite(idTramite));
					//setListaBoletaPago(inscripcionService.getBoletasByGarantia(idGarantia));
					setListaBienes(inscripcionService.getListaBienes(idTramite, 1));
					Integer idTipoGarantia = getGarantiaTO().getIdTipoGarantia();					
					if (acreedorTORep.getRfc() == null) {
						acreedorTORep.setRfc(acreedorTORep.getCurp());
					} else if (acreedorTORep.getCurp() == null) {
						acreedorTORep.setCurp(acreedorTORep.getRfc());
					}					
					setListaBienesPendientes(new BienPendienteDAO().bienesPendientesById(idGarantia));
					if (idTipoGarantia != null) {
						setTipoGarantiaStr(inscripcionService.descTipoGarantiaByID(idTipoGarantia));
						if (getOtorganteTO() == null) {
							throw new Exception("La inscripcion no contiene un otorgante");
						} else {							
							setHayOtorgante(true);
						}
						if (getListaDeudores().size() > 0) {						
							setHayDeudores(true);
						}
						if (getListaAcreedores().size() > 0) {
							setHayAcreedores(true);							
						}
						if (getListaBienes().size() > 0) {
							setHayBienes(true);							
						}
						setListaTipoBienes(new ArrayList<TipoBienInscripcionTXT>());
						
						boolean actbit = inscripcionService.insertBitacoraTramite(Integer.valueOf(idTramite), 1, 3, null,"V");
						if (actbit) {
							setIdInscripcion(new Integer(idTramite));
							regresa = "success";
						} else{
							throw new Exception("RUG>>paso 3:::la bitacora de la inscripcion no pudo ser actualizada");
						}
					} else {
						setTipoGarantiaStr("no tenia idTipoGarantia");
						throw new Exception("RUG-InscripcionAction>>paso 3:::la garantia no cuenta con tipo de garantia (idTipoGarantia)");
					}
				}else{
					throw new Exception("RUG-InscripcionAction>>paso 3::: es necesario contar con una garantia");
				}
			}else{
				throw new Exception("RUG-InscripcionAction>>paso 3:::esta Inscripcion no tiene un identificador (idTramite)");
			}
		} catch (Exception e) {
			regresa = "failed";
			e.printStackTrace();
		}

		return regresa;
	}
		
	/*
	 * Se actualiza la Vigencia
	 */

	public String actualizaVigencia() {
		String regresa = "failed";
		System.out.println("Entra a actualizar vigencia");
		try {			
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
				setAcreedorTORep(inscripcionService.getByIDTramite(getIdInscripcion()));
				sessionMap.put(Constants.ID_ACREEDOR_REPRESENTADO, new Integer(usuario.getPersona().getIdPersona()));
			} else {				
				setAcreedorTORep(acreedoresService.getARByID((Integer) sessionMap.get(Constants.ID_ACREEDOR_REPRESENTADO)));
				idTramite = (Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO);
				if (inscripcionTO == null) {
					inscripcionTO = new InscripcionTO();
				}
			}			
			if (idTramite != null) {			
					InscripcionDAO inscripcionDAO = new InscripcionDAO();
					GarantiaTO garantiaTO = inscripcionDAO.getByID(new GarantiasDAO().getIDGarantiaByIdTramite(idTramite));
					inscripcionTO.setGarantiaTO(garantiaTO);					
					boolean sePudo = inscripcionService.actualizaVigencia(inscripcionTO, Integer.valueOf(idTramite), 5, 3,null, "V");
					if (sePudo) {						
						regresa = "success";
						sessionMap.put(Constants.ID_TRAMITE_NUEVO, new Integer(idTramite));
					}else{
						throw new Exception("RUG-InscripcionAction>>actualizaVigencia:::no se pudo actualizar la bitacora de esta inscripcion");
					}
			}else{
				throw new Exception("RUG-InscripcionAction>>actualizaVigencia:::esta Inscripcion no tiene un identificador (idTramite)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return regresa;
	}

	public String getIdTipoGarantia() {
		return idTipoGarantia;
	}

	public void setIdTipoGarantia(String idTipoGarantia) {
		this.idTipoGarantia = idTipoGarantia;
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
	public String getDatos() {
		return datos;
	}

	public void setDatos(String datos) {
		this.datos = datos;
	}

	public List<Integer> getListaIdInscripciones() {
		return ListaIdInscripciones;
	}

	public void setListaIdInscripciones(List<Integer> listaIdInscripciones) {
		ListaIdInscripciones = listaIdInscripciones;
	}

	public List<Integer> getListaIdPersonasInscripciones() {
		return ListaIdPersonasInscripciones;
	}

	public void setListaIdPersonasInscripciones(
			List<Integer> listaIdPersonasInscripciones) {
		ListaIdPersonasInscripciones = listaIdPersonasInscripciones;
	}

	public List<Integer> getListaIdGarantias() {
		return ListaIdGarantias;
	}

	public void setListaIdGarantias(List<Integer> listaIdGarantias) {
		ListaIdGarantias = listaIdGarantias;
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	public GarantiaTO getGarantiaTO() {
		return garantiaTO;
	}

	public void setGarantiaTO(GarantiaTO garantiaTO) {
		this.garantiaTO = garantiaTO;
	}

	public void setListaAcreedores(List<AcreedorTO> listaAcreedores) {
		this.listaAcreedores = listaAcreedores;
	}

	public List<AcreedorTO> getListaAcreedores() {
		return listaAcreedores;
	}

	public void setOtorganteTO(List <OtorganteTO> otorganteTO) {
		this.otorganteTO = otorganteTO;
	}

	public List<OtorganteTO> getOtorganteTO() {
		return otorganteTO;
	}

	public void setDeudorTO(DeudorTO deudorTO) {
		this.deudorTO = deudorTO;
	}

	public DeudorTO getDeudorTO() {
		return deudorTO;
	}

	public AcreedorTO getAcreedorTO() {
		return acreedorTO;
	}

	public void setAcreedorTO(AcreedorTO acreedorTO) {
		this.acreedorTO = acreedorTO;
	}

	public void setInscripcionTO(InscripcionTO inscripcionTO) {
		this.inscripcionTO = inscripcionTO;
	}

	public InscripcionTO getInscripcionTO() {
		return inscripcionTO;
	}

	public void setIdAcreedorTO(Integer idAcreedorTO) {
		this.idAcreedorTO = idAcreedorTO;
	}

	public Integer getIdAcreedorTO() {
		return idAcreedorTO;
	}

	public void setListaTipoPersona(List<TipoPersona> listaTipoPersona) {
		this.listaTipoPersona = listaTipoPersona;
	}

	public List<TipoPersona> getListaTipoPersona() {
		return listaTipoPersona;
	}

	public void setHayOtorgante(boolean hayOtorgante) {
		this.hayOtorgante = hayOtorgante;
	}

	public boolean isHayOtorgante() {
		return hayOtorgante;
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

	public void setListaDeudores(List<DeudorTO> listaDeudores) {
		this.listaDeudores = listaDeudores;
	}

	public List<DeudorTO> getListaDeudores() {
		return listaDeudores;
	}

	public void setListaTipoGarantia(List<TipoGarantiaTO> listaTipoGarantia) {
		this.listaTipoGarantia = listaTipoGarantia;
	}

	public List<TipoGarantiaTO> getListaTipoGarantia() {
		return listaTipoGarantia;
	}

	public void setListaTipoBien(List<TipoBienTO> listaTipoBien) {
		this.listaTipoBien = listaTipoBien;
	}

	public List<TipoBienTO> getListaTipoBien() {
		return listaTipoBien;
	}

	public List<String> getOpcionesCM() {
		return opcionesCM;
	}

	public void setOpcionesCM(List<String> opcionesCM) {
		this.opcionesCM = opcionesCM;
	}

	public void setIdInscripcion(Integer idInscripcion) {
		this.idInscripcion = idInscripcion;
	}

	public Integer getIdInscripcion() {
		return idInscripcion;
	}

	public void setEstaElMismo(boolean estaElMismo) {
		this.estaElMismo = estaElMismo;
	}

	public boolean isEstaElMismo() {
		return estaElMismo;
	}

	public void setAcreedorTORep(AcreedorTO acreedorTORep) {
		this.acreedorTORep = acreedorTORep;
	}

	public AcreedorTO getAcreedorTORep() {
		return acreedorTORep;
	}

	public void setIdLocalidad(Integer idLocalidad) {
		this.idLocalidad = idLocalidad;
	}

	public Integer getIdLocalidad() {
		return idLocalidad;
	}

	public void setIdColonia(Integer idColonia) {
		this.idColonia = idColonia;
	}

	public Integer getIdColonia() {
		return idColonia;
	}

	public void setListaMonedas(List<MonedaTO> listaMonedas) {
		this.listaMonedas = listaMonedas;
	}

	public List<MonedaTO> getListaMonedas() {
		return listaMonedas;
	}

	public void setListaNacionalidad(List<NacionalidadTO> listaNacionalidad) {
		this.listaNacionalidad = listaNacionalidad;
	}

	public List<NacionalidadTO> getListaNacionalidad() {
		return listaNacionalidad;
	}

	public void setTipoBienesStr(String tipoBienesStr) {
		this.tipoBienesStr = tipoBienesStr;
	}

	public String getTipoBienesStr() {
		return tipoBienesStr;
	}

	public void setTipoGarantiaStr(String tipoGarantiaStr) {
		this.tipoGarantiaStr = tipoGarantiaStr;
	}

	public String getTipoGarantiaStr() {
		return tipoGarantiaStr;
	}

	public void setListaTipoBien2(List<TipoBienTO> listaTipoBien2) {
		this.listaTipoBien2 = listaTipoBien2;
	}

	public List<TipoBienTO> getListaTipoBien2() {
		return listaTipoBien2;
	}

	public void setListaTipoBienes(List<TipoBienInscripcionTXT> listaTipoBienes) {
		this.listaTipoBienes = listaTipoBienes;
	}

	public List<TipoBienInscripcionTXT> getListaTipoBienes() {
		return listaTipoBienes;
	}

	public void setListaBienesPendientes(
			List<BienPendienteTO> listaBienesPendientes) {
		this.listaBienesPendientes = listaBienesPendientes;
	}

	public List<BienPendienteTO> getListaBienesPendientes() {
		return listaBienesPendientes;
	}

	public void setErrorInscripcion(boolean errorInscripcion) {
		this.errorInscripcion = errorInscripcion;
	}

	public boolean isErrorInscripcion() {
		return errorInscripcion;
	}

	public void setActualizaGarantia(boolean actualizaGarantia) {
		this.actualizaGarantia = actualizaGarantia;
	}

	public boolean isActualizaGarantia() {
		return actualizaGarantia;
	}

	public String getAutoridad() {
		return autoridad;
	}

	public void setAutoridad(String autoridad) {
		this.autoridad = autoridad;
	}

	public void setIdTipoMoneda(Integer idTipoMoneda) {
		this.idTipoMoneda = idTipoMoneda;
	}

	public Integer getIdTipoMoneda() {
		return idTipoMoneda;
	}

	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}

	public String getIdPersona() {
		return idPersona;
	}

	public void setInsPublico(boolean insPublico) {
		this.insPublico = insPublico;
	}

	public boolean isInsPublico() {
		return insPublico;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public void setTipoFirma(String tipoFirma) {
		this.tipoFirma = tipoFirma;
	}

	public String getTipoFirma() {
		return tipoFirma;
	}

	public void setFirmaSi(String firmaSi) {
		this.firmaSi = firmaSi;
	}

	public String getFirmaSi() {
		return firmaSi;
	}
	public String getMdNumeroBoleta() {
		return mdNumeroBoleta;
	}
	public void setMdNumeroBoleta(String mdNumeroBoleta) {
		this.mdNumeroBoleta = mdNumeroBoleta;
	}
	public String getMdCantidad() {
		return mdCantidad;
	}
	public void setMdCantidad(String mdCantidad) {
		this.mdCantidad = mdCantidad;
	}
	public List<BoletaPagoTO> getListaBoletaPago() {
		return listaBoletaPago;
	}
	public void setListaBoletaPago(List<BoletaPagoTO> listaBoletaPago) {
		this.listaBoletaPago = listaBoletaPago;
	}
	public String getSaldo() {
		return saldo;
	}
	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}
	public String getMdDescripcion() {
		return mdDescripcion;
	}
	public void setMdDescripcion(String mdDescripcion) {
		this.mdDescripcion = mdDescripcion;
	}
	public String getMdIdentificador() {
		return mdIdentificador;
	}
	public void setMdIdentificador(String mdIdentificador) {
		this.mdIdentificador = mdIdentificador;
	}
	public String getMdIdentificador1() {
		return mdIdentificador1;
	}
	public void setMdIdentificador1(String mdIdentificador1) {
		this.mdIdentificador1 = mdIdentificador1;
	}
	public String getMdIdentificador2() {
		return mdIdentificador2;
	}
	public void setMdIdentificador2(String mdIdentificador2) {
		this.mdIdentificador2 = mdIdentificador2;
	}
	public List<TipoTo> getListaBienEspecial() {
		return listaBienEspecial;
	}
	public void setListaBienEspecial(List<TipoTo> listaBienEspecial) {
		this.listaBienEspecial = listaBienEspecial;
	}
	public TipoTo getMdBienEspecial() {
		return mdBienEspecial;
	}
	public void setMdBienEspecial(TipoTo mdBienEspecial) {
		this.mdBienEspecial = mdBienEspecial;
	}
	public List<BienEspecialTO> getListaBienes() {
		return listaBienes;
	}
	public void setListaBienes(List<BienEspecialTO> listaBienes) {
		this.listaBienes = listaBienes;
	}
	public boolean isHayBienes() {
		return hayBienes;
	}
	public void setHayBienes(boolean hayBienes) {
		this.hayBienes = hayBienes;
	}
        
        public String getDescripcionMoneda(){
            

            for (MonedaTO moneda : inscripcionService.getMonedas())
            {
                if (moneda.getIdMoneda().equals(getGarantiaTO().getIdMoneda()))
                    return moneda.getDescMoneda();
                //return moneda.getMoneda();
            }
            return "";
        }
        
        public String getMontoComa(){
            
            String number = actoContratoTO.getMontoMaximo();
            double amount = Double.parseDouble(number);
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            return(formatter.format(amount));
        }


}
