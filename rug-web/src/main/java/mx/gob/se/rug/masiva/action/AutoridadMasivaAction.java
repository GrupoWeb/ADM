package mx.gob.se.rug.masiva.action;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mx.gob.se.rug.acreedores.service.AcreedoresService;
import mx.gob.se.rug.acreedores.service.impl.AcreedoresServiceImpl;
import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.common.dto.Mensaje;
import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;
import mx.gob.se.rug.inscripcion.dao.FirmaMasivaDAO;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.to.FirmaMasivaTO;
import mx.gob.se.rug.masiva.daemon.CargaMasivaController;
import mx.gob.se.rug.masiva.dao.MasivaDAO;
import mx.gob.se.rug.masiva.exception.GarantiaRepetidaException;
import mx.gob.se.rug.masiva.exception.NoDataFound;
import mx.gob.se.rug.masiva.exception.StoreProcedureException;
import mx.gob.se.rug.masiva.exception.XSDException;
import mx.gob.se.rug.masiva.resultado.to.ResultadoCargaMasiva;
import mx.gob.se.rug.masiva.resultado.to.Resumen;
import mx.gob.se.rug.masiva.resultado.to.TramiteRes;
import mx.gob.se.rug.masiva.resultado.to.Tramites;
import mx.gob.se.rug.masiva.service.MasivaService;
import mx.gob.se.rug.masiva.to.AcreedorAutoridad;
import mx.gob.se.rug.masiva.to.Acreedores;
import mx.gob.se.rug.masiva.to.Anotacion;
import mx.gob.se.rug.masiva.to.AnotacionGarantia;
import mx.gob.se.rug.masiva.to.ArchivoTO;
import mx.gob.se.rug.masiva.to.CargaMasiva;
import mx.gob.se.rug.masiva.to.CargaMasivaProcess;
import mx.gob.se.rug.masiva.to.ResCargaMasiva;
import mx.gob.se.rug.masiva.to.Tramite;
import mx.gob.se.rug.masiva.to.string.CargaMasivaPreProcesed;
import mx.gob.se.rug.masiva.validate.ValidateDataType;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.to.UsuarioTO;

import org.apache.struts2.interceptor.ServletRequestAware;

public class AutoridadMasivaAction extends RugBaseAction implements
		ServletRequestAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int ID_TRAMITE_ACREEDORES = 12;
	private static final int ID_TRAMITE_ANOTACION = 2;
	private static final int ID_TRAMITE_ANOTACION_SIN_GARANTIA = 10;

	// atributos
	private HttpServletRequest request;
	private Integer idTipoTramite;
	private Integer idTipoProceso;
	private Integer idAcreedor;
	private Integer idUsuario;
	private Integer idTramiteTempFirma;
	
	private String detalleTecnico;
	

	private ResCargaMasiva resCargaMasiva;
	
	//Resumen
	private Integer idArchivoResultado;
	private Integer idArchivoRes;
	private Integer sizeListaTramites;
	

	// file
	private File autoridadfile;
	private String autoridadfileContentType;
	private String autoridadfileFileName;

	// servicios
	private InscripcionService inscripcionService;
	private MasivaService masivaService;
	private AcreedoresService acreedoresService;

	public void setAcreedoresService(AcreedoresService acreedoresService) {
		this.acreedoresService = acreedoresService;
	}
	
	public void setInscripcionService(InscripcionService inscripcionService) {
		this.inscripcionService = inscripcionService;
	}
	
	public void setMasivaService(MasivaService masivaService) {
		this.masivaService = masivaService;
	}
	
	public String getDetalleTecnico() {
		return detalleTecnico;
	}

	public void setDetalleTecnico(String detalleTecnico) {
		this.detalleTecnico = detalleTecnico;
	}

	public Integer getIdTramiteTempFirma() {
		return idTramiteTempFirma;
	}

	public void setIdTramiteTempFirma(Integer idTramiteTempFirma) {
		this.idTramiteTempFirma = idTramiteTempFirma;
	}



	// daos
	private MasivaDAO masivaDAO = new MasivaDAO();

	
	

	// Actions
	
	public String firma(){
		System.out.println("--entro a firma de autoridad masiva --");
		String regresa = FAILED;
		try{
			Integer idTramiteNuevo = (Integer) sessionMap
					.get(Constants.ID_TRAMITE_NUEVO);
			System.out.println("--id-tramite-firma-::: "+idTramiteNuevo);
			if (masivaDAO.isCargaMasiva(idTramiteNuevo)) {
				regresa = SUCCESS;
			}else{
				setMensaje(new Mensaje());
				getMensaje().setId("999");
				getMensaje().setMensaje("Sucedio un error en el proceso de firma de la carga masiva con el perfil autoridad : idTramite:" +idTramiteNuevo);
			}
		}catch(Exception e){
			e.printStackTrace();
			setMensaje(new Mensaje());
			getMensaje().setId("999");
			getMensaje().setMensaje("Sucedio un error en el proceso de firma masiva con el perfil autoridad");
		}
		if (getMensaje() != null) {
			System.out.println("id:" + getMensaje().getId());
			System.out.println("mensaje:" + getMensaje().getMensaje());
		}
		System.out.println("regresa:" + regresa);
		return regresa;
	}
	
	public String iniciaCargaMasiva() {
		String regresa = FAILED;
		System.out.println("AutoridadMasivaAction.iniciaCargaMasiva()");
		try {
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			setIdUsuario(usuario.getPersona().getIdPersona());
			// verificar si el usuario en session es autoridad
			if (masivaService.isAutoridad(usuario.getPersona().getIdPersona())) {
				regresa = SUCCESS;
			} else {
				setMensaje(new Mensaje());
				getMensaje().setId("Rug-AutoridadMasiva-001");
				getMensaje()
						.setMensaje(
								"El usuario seleccionado no tiene los privilegios de autoridad");
			}
		} catch (Exception e) {
			e.printStackTrace();
			setMensaje(new Mensaje());
			getMensaje().setId("999");
			getMensaje().setMensaje("Sucedio un error en el sistema");
		}
		if (getMensaje() != null) {
			System.out.println("id:" + getMensaje().getId());
			System.out.println("mensaje:" + getMensaje().getMensaje());
		}
		System.out.println("regresa:" + regresa);
		return regresa;
	}

	private ArchivoTO guardaArchivo(String sha1,UsuarioTO usuario) throws IOException, InfrastructureException{
		ArchivoTO archivoTO = new ArchivoTO();
		archivoTO.setAlgoritoHash(sha1);
		archivoTO.setArchivo(masivaService.getBytesFromFile(autoridadfile));
		archivoTO.setDescripcion("Archivo de carga masiva del usuario : "
				+ usuario.getNombre() + ", con el id :"
				+ usuario.getPersona().getIdPersona());
		archivoTO.setIdUsuario(usuario.getPersona().getIdPersona());
		archivoTO.setNombreArchivo(autoridadfileFileName);
		archivoTO.setTipoArchivo("xml");
		PlSql plSql = inscripcionService.insertArchivo(archivoTO);
		
		if(plSql.getIntPl()!=0){
			throw new InfrastructureException("No se pudo guardar el archivo");
		}
		archivoTO.setIdArchivo(plSql.getResIntPl());//Id Archivo
		return archivoTO;
	}
	public String cargaMasivaXML() {
		setIdUsuario(((UsuarioTO)sessionMap.get(Constants.USUARIO)).getPersona().getIdPersona());
		String regresa = FAILED;
		System.out.println("--AutoridadMasivaAction.cargaMasivaXML()--");
		UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
		MasivaDAO masivaDAO = new MasivaDAO();
		ValidateDataType dataType= new ValidateDataType();
		try {
			// validar si el archivo es correcto:
			if (autoridadfile != null) { 
				// validar que el archivo sea XML:
				if (!autoridadfileFileName.contains(".xml")&& !autoridadfileFileName.contains(".Xml")&& !autoridadfileFileName.contains(".XML")) {
					setMensaje(new Mensaje());
					getMensaje().setId("Rug-AutoridadMasiva-005");
					getMensaje().setMensaje("El archivo no es XML");
				} else {
					// validar que el XML corresponda al XSD:
					if (masivaService.matchXmltoXsd(autoridadfile, request)) {
						String sha1 = masivaService.getSha1FromFile(autoridadfile);
						// validamos que el archivo no alla sido subido con
						// anterioridad:
						if (!inscripcionService.existeSha1(sha1)) { 
						
							//Guardamos Archivo
							ArchivoTO archivoInicial =guardaArchivo(sha1,usuario );
							
							CargaMasivaProcess masivaProcess= new CargaMasivaProcess();
							masivaProcess.setIdArchivo(archivoInicial.getIdArchivo());
							masivaProcess.setIdUsuario(usuario.getPersona().getIdPersona());
							masivaProcess.setIdTipoTramite(idTipoTramite);
							
							if(idTipoProceso.intValue()==1){//Atendido
								masivaProcess.setbTipoProceso("A");
							}else{//Desatendido
								masivaProcess.setbTipoProceso("D");
							}
							
							masivaDAO.insertProcessCarga(masivaProcess);
						
							ValidateDataType validateDataType = new ValidateDataType();
							
							String xmlFromDB = validateDataType.getFileFromDB(masivaProcess.getIdArchivo());
							validateDataType.validateCargaMasiva(xmlFromDB, masivaProcess.getIdTipoTramite());
								
							switch (idTipoProceso.intValue()) {
							case 1:
								//Proceso Atendido
								String xml=dataType.getFileFromDB(archivoInicial.getIdArchivo());
								CargaMasivaPreProcesed cargaMasivaPreProcess= dataType.parseCargaMasiva(xml, idTipoTramite);
								if(cargaMasivaPreProcess.getTotalTramites()==cargaMasivaPreProcess.getTramiteIncorrectos().size()){
									//todos MAL
									Resumen resumen = new Resumen();
									CargaMasivaAction cargaMasivaAction  = new CargaMasivaAction();
									resumen = cargaMasivaAction.getResumenFiltroError(cargaMasivaPreProcess);
									
									ResultadoCargaMasiva resultado = new ResultadoCargaMasiva();
									resultado.setResumen(resumen);
									Tramites tramites = new  Tramites();
									tramites.getTramite().addAll(cargaMasivaPreProcess.getTramiteIncorrectos());
									resultado.setTramites(tramites);
									
									ArchivoTO archivoRes = new ArchivoTO();
									byte[] bytes2 = cargaMasivaAction.convertXMLObjetc(resultado);
									archivoRes.setAlgoritoHash(cargaMasivaAction.getSha1FromFile(bytes2));
									archivoRes.setArchivo(bytes2);
									archivoRes.setDescripcion("Archivo Resumen masiva del usuario : "
													+ usuario.getNombre()
													+ ", con el id :"
													+ usuario.getPersona().getIdPersona()
													+ ", resultado de una carga que contenia archivos incorrectos");
									archivoRes.setIdUsuario(usuario.getPersona().getIdPersona());
									archivoRes.setNombreArchivo(autoridadfileFileName+" Resumen");
									archivoRes.setTipoArchivo("xml");
									
									
									
									
									
									//jsp
									setIdArchivoRes(inscripcionService.insertArchivo(archivoRes).getResIntPl());
									setIdArchivoResultado(archivoInicial.getIdArchivo());
									setSizeListaTramites(0);
									/*
									 * */
									
									regresa="resultado";
								}else{
									//hubo Buenos
									//setResCargaMasiva(procesoAtendido(cargaMasivaPreProcess, sha1,	usuario,archivoInicial));
									
									
									CargaMasivaController cargaMasivaController= new CargaMasivaController();
									cargaMasivaController.procesaCargaMasiva(masivaProcess);
									
									this.setIdArchivoResultado(masivaProcess.getIdArchivo());
									
									
									regresa="resultado";
									
								}
								break;
							case 2:
								//Desatendido
								regresa = "desatendido";
								break;

							default:
								setMensaje(new Mensaje());
								getMensaje().setId("Rug-AutoridadMasiva-009");
								getMensaje().setMensaje("El proceso seleccionado no existe ");
								break;
							}
//							masivaProcess.setIdArchivoResumen(getIdArchivoRes());
//							masivaDAO.actualizaProcessCargaIdResumen(masivaProcess);

						} else {
							setMensaje(new Mensaje());
							getMensaje().setId("Rug-AutoridadMasiva-007");
							getMensaje()
									.setMensaje(
											"El Archivo que se esta tratando de subir ya fue dado de alta.");
						}
					} else {
						setMensaje(new Mensaje());
						getMensaje().setId("Rug-AutoridadMasiva-006");
						getMensaje().setMensaje(
								"El XML no corresponde con el XSD.");
					}
				}
			} else {
				setMensaje(new Mensaje());
				getMensaje().setId("Rug-AutoridadMasiva-002");
				getMensaje()
						.setMensaje(
								"El archivo esta dañado, favor de intentarlo mas tarde.");
			}

		} catch (XSDException e) {
			setMensaje(new Mensaje());
			getMensaje().setId("Rug-AutoridadMasiva-004");
			getMensaje().setMensaje("El XML no corresponde con el XSD.");
			detalleTecnico=e.getMessage();
			e.printStackTrace();
		}catch (Exception e) {
			setMensaje(new Mensaje());
			getMensaje().setId("Rug-AutoridadMasiva-004");
			getMensaje().setMensaje("Sucedio un error en el sistema :" + e.getMessage());
			detalleTecnico=e.getMessage();
			e.printStackTrace();
		}
		if (getMensaje() != null) {
			System.out.println("id:" + getMensaje().getId());
			System.out.println("mensaje:" + getMensaje().getMensaje());
		}
		System.out.println("regresa:" + regresa);
		return regresa;
	}

	

	// metodos de ayuda
	private ResCargaMasiva procesoAtendido(CargaMasivaPreProcesed cargaMasivaPre,
			String sha1, UsuarioTO usuario,ArchivoTO archivoInicial) {
		ResCargaMasiva respuestaCM = new ResCargaMasiva();
		respuestaCM.setRegresa(FAILED);
		CargaMasiva cargaMasiva = cargaMasivaPre.getCargaMasiva();
		try {
			
				respuestaCM.setIdArchivo(archivoInicial.getIdArchivo());
				switch (idTipoTramite.intValue()) {
				case ID_TRAMITE_ACREEDORES:
					// alta de acreedores./+ 
					if (cargaMasiva.getAcreedores() != null) {
							if (cargaMasiva.getAcreedores().getAcreedorAutoridad().size() > 0) {
								System.out.println(":::Se inicia el alta de acreedores:::");
								respuestaCM = cargaAcreedores(respuestaCM,cargaMasivaPre, usuario,archivoInicial);
							} else {
								setMensaje(new Mensaje());
								getMensaje().setId("Rug-AutoridadMasiva-005");
								getMensaje().setMensaje("El archivo seleccionado no cuenta con etiquetas de acreedores.");
							}
					} else {
						setMensaje(new Mensaje());
						getMensaje().setId("Rug-AutoridadMasiva-005");
						getMensaje().setMensaje("El archivo seleccionado no cuenta con etiquetas de acreedores.");
					}
					setIdTipoTramite(ID_TRAMITE_ACREEDORES);
					break;
				case ID_TRAMITE_ANOTACION:
					// anotacion.
					if (cargaMasiva.getAnotacionGarantia().size() > 0) {
						System.out
								.println(":::Se inicia el alta de anotacion:::");
						respuestaCM = cargaAnotacionGarantia(respuestaCM,
								cargaMasivaPre, usuario,archivoInicial);
					} else {
						setMensaje(new Mensaje());
						getMensaje().setId("Rug-AutoridadMasiva-011");
						getMensaje()
								.setMensaje(
										"El archivo seleccionado no cuenta con etiquetas de anotacion.");
					}
					break;
				case ID_TRAMITE_ANOTACION_SIN_GARANTIA:
					// anotacion sin garantia.
					if (cargaMasiva.getAnotacion().size() > 0) {
						System.out
								.println(":::Se inicia el alta de anotacion:::");
						respuestaCM = cargaAnotacion(respuestaCM, cargaMasivaPre,usuario,archivoInicial);
					} else {
						setMensaje(new Mensaje());
						getMensaje().setId("Rug-AutoridadMasiva-011");
						getMensaje()
								.setMensaje(
										"El archivo seleccionado no cuenta con etiquetas de anotacion.");
					}
					break;

				default:
					setMensaje(new Mensaje());
					getMensaje().setId("Rug-AutoridadMasiva-011");
					getMensaje()
							.setMensaje(
									"El tramite seleccionado no existe dentro del menu de autoridad");
					break;
				}
			
				//Marcador Buenas&Malas
				masivaDAO.actualizaDatosCarga(respuestaCM.getListaTramites().size(),respuestaCM.getListaTramitesErroneos().size(), respuestaCM.getIdArchivo());
				
				
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respuestaCM;
		
	}


	// alta de Anotaciones con Garantia masivas
	private ResCargaMasiva cargaAnotacionGarantia(
			ResCargaMasiva resCargaMasiva, CargaMasivaPreProcesed masivaPreProcesed,
			UsuarioTO usuario ,ArchivoTO archivoInicial) {
		PlSql plSql;
		CargaMasiva cargaMasiva= masivaPreProcesed.getCargaMasiva();
		resCargaMasiva.setListaTramites(new ArrayList<PlSql>());
		resCargaMasiva.setListaTramitesErroneos(new ArrayList<PlSql>());
		Set<Integer> idGarantiasInXML = new  HashSet<Integer>();
		try {
			// recorremos los elementos de anotacion
			Tramite tramite = new Tramite();
			tramite.setIdUsuario(usuario.getPersona().getIdPersona());
			tramite.setIdTipoTramite(ID_TRAMITE_ANOTACION);
			tramite.setIdArchivo(resCargaMasiva.getIdArchivo());
			
			List<AnotacionGarantia> anotacionesCorrectas = new ArrayList<AnotacionGarantia>();
			for (AnotacionGarantia anotacion : cargaMasiva.getAnotacionGarantia()) {
				try{
					masivaService.validaGarantia(anotacion.getIdentificadorGarantia().getIdGarantia().intValue(), idGarantiasInXML);
					
					tramite.setClaveRastreo(anotacion.getIdentificadorGarantia().getClaveRastreo());
					tramite.setIdGarantia(new Integer(anotacion.getIdentificadorGarantia().getIdGarantia().intValue()));
					
					masivaDAO.executeAltaAnotacionMasiva(tramite,anotacion);
					
					//Control tramites Correctos
					plSql = new PlSql();
					plSql.setStrPl("La anotacion fue procesada correctamente");
					plSql.setIntPl(0);
					plSql.setResStrPl(anotacion.getIdentificadorGarantia().getClaveRastreo());
					anotacionesCorrectas.add(anotacion);
					plSql.setResIntPl(tramite.getIdTramite());
					resCargaMasiva.getListaTramites().add(plSql);
					
				
				}catch (StoreProcedureException e) {
					plSql=e.getPlSql();
					plSql.setResStrPl(anotacion.getIdentificadorGarantia().getClaveRastreo());
					
					resCargaMasiva.getListaTramitesErroneos().add(plSql);

					//BAja Tramite Rastreo
					if(anotacion.getIdentificadorGarantia()!=null&&tramite.getIdTramite()!=null){
						masivaDAO.executeBajaClaveRastreoAutoridad(tramite.getIdTramite(), anotacion.getIdentificadorGarantia().getClaveRastreo());
					}
			
					
				}catch (GarantiaRepetidaException e) {
					plSql = e.getPlSql();
					plSql.setResStrPl(anotacion.getIdentificadorGarantia().getClaveRastreo());
					resCargaMasiva.getListaTramitesErroneos().add(plSql);
					
					//BAja Tramite Rastreo
					if(anotacion.getIdentificadorGarantia()!=null){
						masivaDAO.executeBajaClaveRastreoAutoridad(tramite.getIdTramite(), anotacion.getIdentificadorGarantia().getClaveRastreo());
					}
				}catch (NoDataFound e) {
					plSql = e.getPlSql();
					plSql.setResStrPl(anotacion.getIdentificadorGarantia().getClaveRastreo());
					resCargaMasiva.getListaTramitesErroneos().add(plSql);
					//BAja Tramite Rastreo
					if(anotacion.getIdentificadorGarantia()!=null){
						masivaDAO.executeBajaClaveRastreoAutoridad(tramite.getIdTramite(), anotacion.getIdentificadorGarantia().getClaveRastreo());
					}
				}catch (Exception e) {
					plSql = new PlSql();
					plSql.setResStrPl("calve de Rastreo indefinida");
					plSql.setStrPl("Hubo un problema con su tramite favor de validar los campos requeridos");
					resCargaMasiva.getListaTramitesErroneos().add(plSql);
					//BAja Tramite Rastreo
					if(anotacion.getIdentificadorGarantia()!=null){
						masivaDAO.executeBajaClaveRastreoAutoridad(tramite.getIdTramite(), anotacion.getIdentificadorGarantia().getClaveRastreo());
					}
				}
				
			}
			// preguntamos si uvieron tramites correctos
			if (resCargaMasiva.getListaTramites().size() > 0) {
				// preguntamos si hubieron tramites erroneos
				if (resCargaMasiva.getListaTramitesErroneos().size() > 0) {
					// si existen tramites erroneos generamos el nuevo XML que
					// se va a firmar
					CargaMasiva cargaMasiva2 = new CargaMasiva();
					cargaMasiva2.getAnotacionGarantia().addAll(anotacionesCorrectas);
					ArchivoTO archivoN = new ArchivoTO();
					archivoN.setAlgoritoHash(masivaService.getSha1FromFile(masivaService.convertXMLObjetc(cargaMasiva2)));
					archivoN.setArchivo(masivaService.convertXMLObjetc(cargaMasiva2));
					archivoN.setDescripcion("Archivo de carga masiva del usuario : "+ usuario.getNombre()
							+ ", id :"+ usuario.getPersona().getIdPersona()
							+ ", carga que contenia registros incorrectos");
					archivoN.setIdUsuario(usuario.getPersona().getIdPersona());
					archivoN.setNombreArchivo(archivoInicial.getNombreArchivo()+"-correcto");
					archivoN.setTipoArchivo("xml");
					resCargaMasiva.setIdArchivo(inscripcionService.insertArchivo(archivoN).getResIntPl());
				}
			} else {
				setMensaje(new Mensaje());
				getMensaje().setMensaje("No existieron tramites correctos");
			}

			// generamos el XML de resultado
			ArchivoTO archivoRes = new ArchivoTO();
			ResultadoCargaMasiva resultado = new ResultadoCargaMasiva();
			List<TramiteRes> tramitesResultados = new ArrayList<TramiteRes>();
			List<Integer> tramitesId = new ArrayList<Integer>();
			for (PlSql plSqlRes : resCargaMasiva.getListaTramites()) {
				TramiteRes e = new TramiteRes();
				e.setCodigoError(plSqlRes.getIntPl() + "");
				e.setMensajeError(plSqlRes.getStrPl());
				e.setClaveRastreo(plSqlRes.getResStrPl());
				tramitesId.add(plSqlRes.getResIntPl());
				tramitesResultados.add(e);
			}
			for (PlSql plSqlRes : resCargaMasiva.getListaTramitesErroneos()) {
				TramiteRes e = new TramiteRes();
				e.setCodigoError(plSqlRes.getIntPl() + "");
				e.setMensajeError(plSqlRes.getStrPl());
				e.setClaveRastreo(plSqlRes.getResStrPl());
				tramitesResultados.add(e);
			}
			
			//Agregamos Errores de Filtro datos
			tramitesResultados.addAll(masivaPreProcesed.getTramiteIncorrectos());
			
			resultado.setResumen(new Resumen());
			resultado.getResumen().setCorrectos(resCargaMasiva.getListaTramites().size() + "");
			
			//sumamos filtro + proceso
			Integer nIncorrectos=resCargaMasiva.getListaTramitesErroneos().size() + masivaPreProcesed.getTramiteIncorrectos().size();
			resultado.getResumen().setErroneos(nIncorrectos.toString());
			resultado.getResumen().setNumeroRegistros(masivaPreProcesed.getTotalTramites().toString());
			
			resultado.setTramites(new Tramites());
			resultado.getTramites().getTramite().addAll(tramitesResultados);

			byte[] bytes2 = masivaService.convertXMLObjetc(resultado);
			archivoRes.setAlgoritoHash(masivaService.getSha1FromFile(bytes2));
			archivoRes.setArchivo(bytes2);
			archivoRes
					.setDescripcion("Archivo nuevo de carga masiva de anotacion del usuario : "
							+ usuario.getNombre()
							+ ", con el id :"
							+ usuario.getPersona().getIdPersona()
							+ ", resultado de una carga que contenia archivos incorrectos");
			archivoRes.setIdUsuario(usuario.getPersona().getIdPersona());
			archivoRes.setNombreArchivo(archivoInicial.getNombreArchivo()+"-resumen");
			archivoRes.setTipoArchivo("xml");
			resCargaMasiva.setIdArchivoXML(inscripcionService.insertArchivo(archivoRes).getResIntPl());
			// terminamos de generar el XML de resultado

			
			CargaMasivaProcess masivaProcess= new CargaMasivaProcess();
			masivaProcess.setIdArchivo(archivoInicial.getIdArchivo());
			masivaProcess.setIdArchivoResumen(resCargaMasiva.getIdArchivoXML());
			masivaDAO.actualizaProcessCargaIdResumen(masivaProcess);
			
			
			if (resCargaMasiva.getListaTramites().size() > 0) {

				masivaProcess.setIdArchivoFirma(resCargaMasiva.getIdArchivo());
				masivaDAO.actualizaProcessCargaIdFirma(masivaProcess);
				
				Integer idUsuario = usuario.getPersona().getIdPersona();
				FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
				firmaMasivaTO.setIdUsuario(idUsuario);
				firmaMasivaTO.setIdArchivo(resCargaMasiva.getIdArchivo());
				firmaMasivaTO.setIdAcreedor(tramite.getIdAcreedor());
				String tramites = masivaService.listToString(tramitesId);
				System.out.println("--tramites anotacion garantia--::::" + tramites);
				firmaMasivaTO.setTramites(tramites);
				FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
				int valor = firmaDao.crearFirmaMasiva(firmaMasivaTO);
				System.out.println("valor :" + valor);
				if (valor != 0) {
					sessionMap.put(Constants.ID_TRAMITE_NUEVO, valor);
				}
			}
			// terminamos el tramite de firma masiva
			resCargaMasiva.setRegresa(SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			setMensaje(new Mensaje());
			getMensaje().setId("999");
			getMensaje().setMensaje(
					"Sucedio un error en las anotacion, el proceso sera revertido. Mensaje :"
							+ e.getMessage());
			for (PlSql sql : resCargaMasiva.getListaTramites()) {
				if (sql.getResIntPl() != null) {
					inscripcionService.bajaTramiteIncompleto(sql.getResIntPl());
				}
			}
		} finally {
			for (PlSql sql : resCargaMasiva.getListaTramitesErroneos()) {
				if (sql.getResIntPl() != null) {
					inscripcionService.bajaTramiteIncompleto(sql.getResIntPl());
				}
			}
		}

		return resCargaMasiva;
	}

	// alta de Anotaciones sin garantia masivas
	private ResCargaMasiva cargaAnotacion(ResCargaMasiva resCargaMasiva,
			 CargaMasivaPreProcesed masivaPreProcesed, UsuarioTO usuario,ArchivoTO archivoInicial) {
		PlSql plSql = new PlSql();
		CargaMasiva cargaMasiva= masivaPreProcesed.getCargaMasiva();
		resCargaMasiva.setListaTramites(new ArrayList<PlSql>());
		resCargaMasiva.setListaTramitesErroneos(new ArrayList<PlSql>());
		try {
			// recorremos los elementos de anotacion
			Tramite tramite = new Tramite();
			tramite.setIdUsuario(usuario.getPersona().getIdPersona());
			tramite.setIdTipoTramite(ID_TRAMITE_ANOTACION_SIN_GARANTIA);
			List<Anotacion> anotacionesCorrectas = new ArrayList<Anotacion>();
			for (Anotacion anotacion : cargaMasiva.getAnotacion()) {
				// creamos el tramite incompleto
				//plSql = masivaDAO.executeAltaTramiteIncompleto(tramite.getIdUsuario(), 10);
				tramite.setClaveRastreo(anotacion.getIdentificador().getClaveRastreo());
				tramite.setIdPaso(0);
				tramite.setIdEstatus(0);
				tramite.setIdAcreedor(0);
				tramite.setIdArchivo(resCargaMasiva.getIdArchivo());
				try{
					masivaDAO.executeAltaTramiteRestreo(tramite);
					if (anotacion.getOtorgante() != null) {
						plSql = masivaService.agregaOtorganteInd(anotacion.getOtorgante(),tramite);
						if (plSql.getIntPl().intValue() == 0) {
							plSql = masivaDAO.executeAltaAnotacionSinGarantia(anotacion, tramite);
							if (plSql.getIntPl().intValue() == 0) {
								plSql = masivaDAO.executeAltaBitacoraTramite2(tramite.getIdTramite(), 5, 0,null, "V");
								if (plSql.getIntPl().intValue() == 0) {
									anotacionesCorrectas.add(anotacion);
									plSql.setStrPl("La Anotacion fue procesada correctamente");
									plSql.setIntPl(0);
									plSql.setResIntPl(tramite.getIdTramite());
									plSql.setResStrPl(anotacion.getIdentificador().getClaveRastreo());
									resCargaMasiva.getListaTramites()
											.add(plSql);
								} else {
									plSql.setResStrPl(anotacion.getIdentificador().getClaveRastreo());
									resCargaMasiva.getListaTramitesErroneos().add(plSql);
									
									//BAja Tramite Rastreo
									if(anotacion.getIdentificador()!=null){
										masivaDAO.executeBajaClaveRastreoAutoridad(tramite.getIdTramite(), anotacion.getIdentificador().getClaveRastreo());
									}
								}
							} else {
								plSql.setResStrPl(anotacion.getIdentificador().getClaveRastreo());
								resCargaMasiva.getListaTramitesErroneos().add(
										plSql);
								//BAja Tramite Rastreo
								if(anotacion.getIdentificador()!=null){
									masivaDAO.executeBajaClaveRastreoAutoridad(tramite.getIdTramite(), anotacion.getIdentificador().getClaveRastreo());
								}
							}
						} else {
							plSql.setResStrPl(anotacion.getIdentificador().getClaveRastreo());
							resCargaMasiva.getListaTramitesErroneos()
									.add(plSql);
							//BAja Tramite Rastreo
							if(anotacion.getIdentificador()!=null){
								masivaDAO.executeBajaClaveRastreoAutoridad(tramite.getIdTramite(), anotacion.getIdentificador().getClaveRastreo());
							}
						}
					} else {
						plSql.setIntPl(961);
						plSql.setResStrPl(anotacion.getIdentificador().getClaveRastreo());
						plSql.setStrPl("La anotacion debe contener un otorgante");
						//BAja Tramite Rastreo
						if(anotacion.getIdentificador()!=null){
							masivaDAO.executeBajaClaveRastreoAutoridad(tramite.getIdTramite(), anotacion.getIdentificador().getClaveRastreo());
						}
					}
				
				}catch(StoreProcedureException exception){
					plSql=exception.getPlSql();
					plSql.setResStrPl(anotacion.getIdentificador().getClaveRastreo());
					resCargaMasiva.getListaTramitesErroneos().add(plSql);
					//BAja Tramite Rastreo
					if(anotacion.getIdentificador()!=null&&tramite.getIdTramite()!=null){
						masivaDAO.executeBajaClaveRastreoAutoridad(tramite.getIdTramite(), anotacion.getIdentificador().getClaveRastreo());
					}
				}
			}
			// preguntamos si uvieron tramites correctos
			if (resCargaMasiva.getListaTramites().size() > 0) {
				// preguntamos si uvieron tramites erroneos
				if (resCargaMasiva.getListaTramitesErroneos().size() > 0) {
					// si existen tramites erroneos generamos el nuevo XML que
					// se va a firmar
					CargaMasiva cargaMasiva2 = new CargaMasiva();
					cargaMasiva2.getAnotacion().addAll(anotacionesCorrectas);
					ArchivoTO archivoN = new ArchivoTO();
					archivoN.setAlgoritoHash(masivaService.getSha1FromFile(masivaService.convertXMLObjetc(cargaMasiva2)));
					archivoN.setArchivo(masivaService.convertXMLObjetc(cargaMasiva2));
					archivoN.setDescripcion("Archivo nuevo de carga masiva del usuario : "+ usuario.getNombre()+ ",id :"
							+ usuario.getPersona().getIdPersona()+ ", resultado de una carga que contenia registros incorrectos");
					archivoN.setIdUsuario(usuario.getPersona().getIdPersona());
					archivoN.setNombreArchivo(archivoInicial.getNombreArchivo()+"-correctos");
					archivoN.setTipoArchivo("xml");
					resCargaMasiva.setIdArchivo(inscripcionService.insertArchivo(archivoN).getResIntPl());
				}
			} else {
				setMensaje(new Mensaje());
				getMensaje().setMensaje("No existieron tramites correctos");
			}

			// generamos el XML de resultado
			ArchivoTO archivoRes = new ArchivoTO();
			ResultadoCargaMasiva resultado = new ResultadoCargaMasiva();
			List<TramiteRes> tramitesResultados = new ArrayList<TramiteRes>();
			List<Integer> tramitesId = new ArrayList<Integer>();
			for (PlSql plSqlRes : resCargaMasiva.getListaTramites()) {
				TramiteRes e = new TramiteRes();
				e.setCodigoError(plSqlRes.getIntPl() + "");
				e.setMensajeError(plSqlRes.getStrPl());
				e.setClaveRastreo(plSqlRes.getResStrPl());
				tramitesId.add(plSqlRes.getResIntPl());
				tramitesResultados.add(e);
			}
			for (PlSql plSqlRes : resCargaMasiva.getListaTramitesErroneos()) {
				TramiteRes e = new TramiteRes();
				e.setCodigoError(plSqlRes.getIntPl() + "");
				e.setMensajeError(plSqlRes.getStrPl());
				e.setClaveRastreo(plSqlRes.getResStrPl());
				tramitesResultados.add(e);
			}
			
			tramitesResultados.addAll(masivaPreProcesed.getTramiteIncorrectos());
			
			resultado.setResumen(new Resumen());
			resultado.getResumen().setCorrectos(resCargaMasiva.getListaTramites().size() + "");
			
			//sumamos filtro + proceso
			Integer nIncorrectos=resCargaMasiva.getListaTramitesErroneos().size() + masivaPreProcesed.getTramiteIncorrectos().size();
			resultado.getResumen().setErroneos(nIncorrectos.toString());
			resultado.getResumen().setNumeroRegistros(masivaPreProcesed.getTotalTramites().toString());
			
			resultado.setTramites(new Tramites());
			resultado.getTramites().getTramite().addAll(tramitesResultados);

			byte[] bytes2 = masivaService.convertXMLObjetc(resultado);
			archivoRes.setAlgoritoHash(masivaService.getSha1FromFile(bytes2));
			archivoRes.setArchivo(bytes2);
			archivoRes
					.setDescripcion("Archivo nuevo de carga masiva de anotacion del usuario : "
							+ usuario.getNombre()
							+ ", con el id :"
							+ usuario.getPersona().getIdPersona()
							+ ", resultado de una carga que contenia archivos incorrectos");
			archivoRes.setIdUsuario(usuario.getPersona().getIdPersona());
			archivoRes.setNombreArchivo(archivoInicial.getNombreArchivo()+"-Resumen");
			archivoRes.setTipoArchivo("xml");
			resCargaMasiva.setIdArchivoXML(inscripcionService.insertArchivo(
					archivoRes).getResIntPl());
			// terminamos de generar el XML de resultado

			// generamos el tramite de firma masiva en caso de aver correctos
			CargaMasivaProcess masivaProcess= new CargaMasivaProcess();
			masivaProcess.setIdArchivo(archivoInicial.getIdArchivo());
			masivaProcess.setIdArchivoResumen(resCargaMasiva.getIdArchivoXML());
			masivaDAO.actualizaProcessCargaIdResumen(masivaProcess);
			
			
			if (resCargaMasiva.getListaTramites().size() > 0) {

				
				masivaProcess.setIdArchivoFirma(resCargaMasiva.getIdArchivo());
				masivaDAO.actualizaProcessCargaIdFirma(masivaProcess);

				Integer idUsuario = usuario.getPersona().getIdPersona();
				FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
				firmaMasivaTO.setIdUsuario(idUsuario);
				firmaMasivaTO.setIdArchivo(resCargaMasiva.getIdArchivo());
				firmaMasivaTO.setIdAcreedor(0);
				String tramites = masivaService.listToString(tramitesId);
				firmaMasivaTO.setTramites(tramites);
				FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
				int valor = firmaDao.crearFirmaMasiva(firmaMasivaTO);
				System.out.println("valor :" + valor);
				if (valor != 0) {
					sessionMap.put(Constants.ID_TRAMITE_NUEVO, valor);
				}
			}
			// terminamos el tramite de firma masiva
			resCargaMasiva.setRegresa(SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			setMensaje(new Mensaje());
			getMensaje().setId("999");
			getMensaje().setMensaje(
					"Sucedio un error en las anotacion, el proceso sera revertido. Mensaje :"
							+ e.getMessage());
			for (PlSql sql : resCargaMasiva.getListaTramites()) {
				if (sql.getResIntPl() != null) {
					inscripcionService.bajaTramiteIncompleto(sql.getResIntPl());
				}
			}
		} finally {
			for (PlSql sql : resCargaMasiva.getListaTramitesErroneos()) {
				if (sql.getResIntPl() != null) {
					inscripcionService.bajaTramiteIncompleto(sql.getResIntPl());
				}
			}
		}

		return resCargaMasiva;
	}



	
	public String getClaveRastreoAcreedor(mx.gob.se.rug.masiva.to.AcreedorAutoridad acreedorAutoridad){
		String claveRastreoAcreedor= "No clave";
		if(acreedorAutoridad.getRfc() != null){
			claveRastreoAcreedor=acreedorAutoridad.getRfc();
		}else if(acreedorAutoridad.getDenominacionRazonSocial() != null){
			claveRastreoAcreedor=acreedorAutoridad.getDenominacionRazonSocial();
		}else if(acreedorAutoridad.getNombre() != null){
			claveRastreoAcreedor=acreedorAutoridad.getNombre();
			if(acreedorAutoridad.getApellidoPaterno() != null){
				claveRastreoAcreedor=claveRastreoAcreedor+" "+acreedorAutoridad.getApellidoPaterno();
			}
		}else if(acreedorAutoridad.getCorreoElectronico() != null){
			claveRastreoAcreedor=acreedorAutoridad.getCorreoElectronico();
		}
		return claveRastreoAcreedor;
	}
	// termina agregar otorgantes

	// alta de acreedores masivos
	private ResCargaMasiva cargaAcreedores(ResCargaMasiva resCargaMasiva,
			CargaMasivaPreProcesed masivaPreProcesed, UsuarioTO usuario,ArchivoTO archivoTO) {
		PlSql plSql,plSqlTmp;
		MasivaDAO masivaDAO= new MasivaDAO();
		resCargaMasiva.setListaTramites(new ArrayList<PlSql>());
		resCargaMasiva.setListaTramitesErroneos(new ArrayList<PlSql>());
		CargaMasiva cargaMasiva = masivaPreProcesed.getCargaMasiva();
		try {
			List<AcreedorAutoridad> acreedoresCorrectos = new ArrayList<AcreedorAutoridad>();
			// recorremos los elementos de acreedores
				for (AcreedorAutoridad acreedor : cargaMasiva.getAcreedores().getAcreedorAutoridad()) {
					plSql= new PlSql();
					plSqlTmp = new PlSql();
					try{
						// Poblamos lo que sería equivalente a la clave de rastreo
						
						plSqlTmp.setResStrPl(getClaveRastreoAcreedor(acreedor));
											
						plSql = validaDatosCargaAcreedores(acreedor);
						if (plSql.getIntPl() == 0) {
							plSql = validaAcreedor(acreedor, usuario);
							if (plSql.getIntPl() == 0) {
								plSql = altaAcreedor(usuario, acreedor);
								if (plSql.getIntPl() == 0) {
									System.out.println("correcto.plSql.id:"+ plSql.getIntPl());
									System.out.println("correcto.plSql.mensaje:"+ plSql.getStrPl());
									acreedoresCorrectos.add(acreedor);
									plSql.setResStrPl(plSqlTmp.getResStrPl());
									resCargaMasiva.getListaTramites().add(plSql);
								} else {
									System.out.println("erroneo.plSql.id:"+ plSql.getIntPl());
									System.out.println("erroneo.plSql.mensaje:"+ plSql.getStrPl());
									plSql.setResStrPl(plSqlTmp.getResStrPl());
									resCargaMasiva.getListaTramitesErroneos().add(plSql);
								}
							} else {
								System.out.println("erroneo.plSql.id:"+ plSql.getIntPl());
								System.out.println("erroneo.plSql.mensaje:" + plSql.getStrPl());
								plSql.setResStrPl(plSqlTmp.getResStrPl());
								resCargaMasiva.getListaTramitesErroneos().add(plSql);
							}
						} else {
							System.out.println("erroneo.plSql.id:" + plSql.getIntPl());
							System.out.println("erroneo.plSql.mensaje:"
									+ plSql.getStrPl());
							plSql.setResStrPl(plSqlTmp.getResStrPl());
							resCargaMasiva.getListaTramitesErroneos().add(plSql);
						}
					}catch (Exception e) {
						plSql= new PlSql();
						plSql.setIntPl(999);
						plSql.setStrPl(e.getMessage());
						plSql.setResStrPl(plSqlTmp.getResStrPl());
						resCargaMasiva.getListaTramitesErroneos().add(plSql);
					}
					
				}
				
			// preguntamos si uvieron tramites correctos
			if (resCargaMasiva.getListaTramites().size() > 0) {
				// preguntamos si uvieron tramites erroneos
				if (resCargaMasiva.getListaTramitesErroneos().size() > 0) {
					// si existen tramites erroneos generamos el nuevo XML que
					// se va a firmar
					CargaMasiva cargaMasiva2 = new CargaMasiva();
					
					Acreedores acreedores= new Acreedores();
					acreedores.getAcreedorAutoridad().addAll(acreedoresCorrectos);
					cargaMasiva2.setAcreedores(acreedores);
					ArchivoTO archivoN = new ArchivoTO();
					archivoN.setAlgoritoHash(masivaService
							.getSha1FromFile(masivaService
									.convertXMLObjetc(cargaMasiva2)));
					archivoN.setArchivo(masivaService
							.convertXMLObjetc(cargaMasiva2));
					archivoN.setDescripcion("Archivo nuevo de carga masiva del usuario : "
							+ usuario.getNombre()
							+ ", con el id :"
							+ usuario.getPersona().getIdPersona()
							+ ", resultado de una carga que contenia archivos incorrectos");
					archivoN.setIdUsuario(usuario.getPersona().getIdPersona());
					archivoN.setNombreArchivo(archivoTO.getNombreArchivo()+"-Correctos");
					archivoN.setTipoArchivo("xml");
					resCargaMasiva.setIdArchivo(inscripcionService.insertArchivo(archivoN).getResIntPl());
				}
			} else {
				setMensaje(new Mensaje());
				getMensaje().setMensaje("No existieron tramites correctos");
			}

			// generamos el XML de resultado
			ArchivoTO archivoRes = new ArchivoTO();
			ResultadoCargaMasiva resultado = new ResultadoCargaMasiva();
			List<TramiteRes> tramitesResultados = new ArrayList<TramiteRes>();
			List<Integer> tramitesId = new ArrayList<Integer>();
			for (PlSql plSqlRes : resCargaMasiva.getListaTramites()) {
				TramiteRes e = new TramiteRes();
				e.setCodigoError(plSqlRes.getIntPl() + "");
				e.setMensajeError(plSqlRes.getStrPl());
				tramitesId.add(plSqlRes.getResIntPl());
				tramitesResultados.add(e);
			}
			for (PlSql plSqlRes : resCargaMasiva.getListaTramitesErroneos()) {
				TramiteRes e = new TramiteRes();
				e.setCodigoError(plSqlRes.getIntPl() + "");
				e.setMensajeError(plSqlRes.getStrPl());
				e.setClaveRastreo(plSqlRes.getResStrPl());
				tramitesResultados.add(e);
			}
			//Agregamos Errores de Filtro datos
			tramitesResultados.addAll(masivaPreProcesed.getTramiteIncorrectos());
			
			resultado.setResumen(new Resumen());
			resultado.getResumen().setCorrectos(resCargaMasiva.getListaTramites().size() + "");
			//sumamos filtro + proceso
			Integer nIncorrectos=resCargaMasiva.getListaTramitesErroneos().size() + masivaPreProcesed.getTramiteIncorrectos().size();
			resultado.getResumen().setErroneos(nIncorrectos.toString());
			resultado.getResumen().setNumeroRegistros(masivaPreProcesed.getTotalTramites().toString());
			resultado.setTramites(new Tramites());
			resultado.getTramites().getTramite().addAll(tramitesResultados);

			byte[] bytes2 = masivaService.convertXMLObjetc(resultado);
			archivoRes.setAlgoritoHash(masivaService.getSha1FromFile(bytes2));
			archivoRes.setArchivo(bytes2);
			archivoRes.setDescripcion("Archivo nuevo de carga masiva de modificacion del usuario : "
							+ usuario.getNombre()
							+ ", con el id :"
							+ usuario.getPersona().getIdPersona()
							+ ", resultado de una carga que contenia archivos incorrectos");
			archivoRes.setIdUsuario(usuario.getPersona().getIdPersona());
			archivoRes.setNombreArchivo("cmResnuevo");
			archivoRes.setTipoArchivo("xml");
			resCargaMasiva.setIdArchivoXML(inscripcionService.insertArchivo(archivoRes).getResIntPl());
			// terminamos de generar el XML de resultado
			resCargaMasiva.setRegresa(SUCCESS);
			// generamos el tramite de firma masiva en caso de aver correctos
			CargaMasivaProcess masivaProcess= new CargaMasivaProcess();
			masivaProcess.setIdArchivo(archivoTO.getIdArchivo());
			masivaProcess.setIdArchivoResumen(resCargaMasiva.getIdArchivoXML());
			masivaDAO.actualizaProcessCargaIdResumen(masivaProcess);
			
			
			if (resCargaMasiva.getListaTramites().size() > 0) {

				masivaProcess.setIdArchivoFirma(resCargaMasiva.getIdArchivo());
				masivaDAO.actualizaProcessCargaIdFirma(masivaProcess);
				
				Integer idUsuario = usuario.getPersona().getIdPersona();
				FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
				firmaMasivaTO.setIdUsuario(idUsuario);
				firmaMasivaTO.setIdArchivo(resCargaMasiva.getIdArchivo());
				firmaMasivaTO.setIdAcreedor(0);
				String tramites = masivaService.listToString(tramitesId);
				System.out.println("--tramites--");
				firmaMasivaTO.setTramites(tramites);
				FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
				int valor = firmaDao.crearFirmaMasiva(firmaMasivaTO);
				System.out.println("valor :" + valor);
				if (valor != 0) {
					sessionMap.put(Constants.ID_TRAMITE_NUEVO, valor);		
					setIdTramiteTempFirma(valor);
				}else{
					setMensaje(new Mensaje());
					getMensaje().setId("788");
					getMensaje().setMensaje("No se pudo generar el tramite temporal de firma masiva");
					resCargaMasiva.setRegresa(FAILED);
				}
			}
			// terminamos el tramite de firma masiva
			
		} catch (Exception e) {
			e.printStackTrace();
			setMensaje(new Mensaje());
			getMensaje().setId("999");
			getMensaje().setMensaje(
					"Sucedio un error en el alta de acreedore mensaje :"
							+ e.getMessage());
		} finally {
			for (PlSql sql : resCargaMasiva.getListaTramitesErroneos()) {
				if (sql.getResIntPl() != null) {
					inscripcionService.bajaTramiteIncompleto(sql.getResIntPl());
				}
			}
		}
		return resCargaMasiva;
	}

	// damos de alta al acreedor
	public PlSql altaAcreedor(UsuarioTO usuario,
			AcreedorAutoridad acreedorAutoridad) {
		PlSql plSql = new PlSql();
		plSql.setIntPl(0);
		try {
			plSql = masivaDAO.executeAltaTramiteIncompleto(usuario.getPersona().getIdPersona(), 12);
			if (plSql.getIntPl() == 0) {
				AcreedorTO acreedorTO = new AcreedorTO();
				// parseamos los datos
				acreedorTO.setIdTramitePendiente(plSql.getResIntPl());
				acreedorTO.setDomicilioUno(notNullString(acreedorAutoridad.getDomicilioExtranjeroUno()));
				acreedorTO.setDomicilioDos(notNullString(acreedorAutoridad.getDomicilioExtranjeroDos()));
				acreedorTO.setPoblacion(notNullString(acreedorAutoridad.getPoblacion()));
				acreedorTO.setZonaPostal(notNullString(acreedorAutoridad.getZonaPostal()));
				acreedorTO.setTipoPersona(notNullString(acreedorAutoridad.getTipoPersona()));
				acreedorTO.setRazonSocial(notNullString(acreedorAutoridad.getDenominacionRazonSocial()));
				acreedorTO.setRfc(notNullString(acreedorAutoridad.getRfc()));
				acreedorTO.setFolioMercantil("");
				acreedorTO.setCalle(notNullString(acreedorAutoridad.getCalle()));
				acreedorTO.setCalleNumero(notNullString(acreedorAutoridad.getNumeroExterior()));
				acreedorTO.setCalleNumeroInterior(notNullString(acreedorAutoridad.getNumeroInterior()));
				acreedorTO.setNombre(notNullString(acreedorAutoridad.getNombre()));
				acreedorTO.setCurp("");
				acreedorTO.setApellidoPaterno(notNullString(acreedorAutoridad.getApellidoPaterno()));
				acreedorTO.setApellidoMaterno(notNullString(acreedorAutoridad.getApellidoMaterno()));
				acreedorTO.setIdColonia(getIntegerFromBigIntger(acreedorAutoridad.getIdColonia()));
				acreedorTO.setIdLocalidad(getIntegerFromBigIntger(acreedorAutoridad.getIdLocalidad()));
				acreedorTO.setIdNacionalidad(getIntegerFromBigIntger(acreedorAutoridad.getIdNacionalidad()));
				acreedorTO.setTelefono(notNullBigtoString(acreedorAutoridad.getTelefono()));
				acreedorTO.setCorreoElectronico(notNullString(acreedorAutoridad.getCorreoElectronico()));
				acreedorTO.setExtencion(notNullBigtoString(acreedorAutoridad.getTelefonoExtension()));
				acreedorTO.setIdPaisResidencia(getIntegerFromBigIntger(acreedorAutoridad.getIdPaisResidencia()));
				plSql = acreedoresService.altaAcreedorRep(acreedorTO, usuario.getPersona().getIdPersona());
				
				if (plSql.getIntPl() == 0) {
					plSql.setStrPl("Alta del acreedor satisfactoriamente");
					plSql.setResIntPl(acreedorTO.getIdTramitePendiente());
				} 
			}

		} catch (Exception e) {
			plSql.setIntPl(999);
			plSql.setStrPl("Sucedio un error inesperado en el alta del acreedor");
			e.printStackTrace();
		}
		return plSql;
	}

	// validamos que el acreedor no este dado de alta para el usuario
	public PlSql validaAcreedor(AcreedorAutoridad acreedor, UsuarioTO usuario) {
		acreedoresService= new  AcreedoresServiceImpl(); 
		PlSql plSql = new PlSql();
		plSql.setIntPl(0);
		try {
			List<AcreedorTO> listaAcreedores = acreedoresService.getAcreedoresByPersona(Integer.valueOf(usuario
							.getPersona().getIdPersona()));
			List<AcreedorTO> listaAcreedoresSinFirma = acreedoresService
					.getAcreedoresSinFirmaByPersona(Integer.valueOf(usuario
							.getPersona().getIdPersona()));
			List<AcreedorTO> listaTotal = new ArrayList<AcreedorTO>();
			listaTotal.addAll(listaAcreedores);
			listaTotal.addAll(listaAcreedoresSinFirma);
			Iterator<AcreedorTO> itAcreedor = listaTotal.iterator();
			AcreedorTO acre;
			boolean esta = false;
			while (itAcreedor.hasNext() && (!esta)) {
				acre = itAcreedor.next();
				if (acre.getRfc() != null && acreedor.getRfc() !=null && acre.getRfc().equals(acreedor.getRfc().trim())) {
					esta = true;

				}
				if (acreedor.getDenominacionRazonSocial()!=null && !acreedor.getDenominacionRazonSocial().isEmpty()) {
					if (acreedor.getDenominacionRazonSocial().trim()
							.equals(acre.getRazonSocial())) {
						esta = true;
					}
				} else {
					if ((acre.getNombre() != null && acre.getNombre().equals(
							acreedor.getNombre().trim()))
							&& (acre.getApellidoPaterno() != null && acre
									.getApellidoPaterno().equals(
											acreedor.getApellidoPaterno()
													.trim()))
							&& (acre.getApellidoMaterno() != null && acre
									.getApellidoMaterno().equals(
											acreedor.getApellidoMaterno()
													.trim()))) {
						esta = true;
					}
					if ((acre.getNombre() != null && acre.getNombre().equals(
							acreedor.getNombre().trim()))
							&& (acre.getApellidoPaterno() != null && acre
									.getApellidoPaterno().equals(
											acreedor.getApellidoPaterno()
													.trim()))) {
						esta = true;
					}
				}

			}

			if (esta) {
				plSql.setIntPl(905);
				plSql.setStrPl("El RFC o el Nombre esta en uso");
			}

		} catch (Exception e) {
			plSql.setIntPl(999);
			plSql.setStrPl("Sucedio un error inesperado en el alta del acreedor");
			e.printStackTrace();
		}
		return plSql;
	}

	// validamos los datos del acreedor
	public PlSql validaDatosCargaAcreedores(AcreedorAutoridad acreedor) {
		PlSql plSql = new PlSql();
		plSql.setIntPl(0);
		try {
			if (acreedor.getIdPaisResidencia().intValue() == 1) {
				if (acreedor.getCalle() == null) {
					plSql.setIntPl(901);
					plSql.setStrPl("La calle es un atributo obligatorio");
				} else if (acreedor.getNumeroExterior() == null) {
					plSql.setIntPl(902);
					plSql.setStrPl("El numero exterior es un atributo obligatorio");
				} else if (acreedor.getIdColonia() == null
						&& acreedor.getIdLocalidad() == null) {
					plSql.setIntPl(903);
					plSql.setStrPl("La colonia ó Localidad es un atributo obligatorio");
				}
			} else {
				if (acreedor.getDomicilioExtranjeroUno() == null) {
					plSql.setIntPl(904);
					plSql.setStrPl("La atributo domicilio extranjero uno es un atributo obligatorio");
				}
			}
		} catch (Exception e) {
			plSql.setIntPl(999);
			plSql.setStrPl("Sucedio un error inesperado en el alta del acreedor");
			e.printStackTrace();
		}
		return plSql;
	}

	// ayudas

	private String notNullString(String atributo) {
		return atributo == null ? "" : atributo;
	}
	private String notNullBigtoString(BigInteger atributo) {
		return atributo == null ? "" : atributo.toString();
	}

	// getter and setter

	public Integer getIdTipoTramite() {
		return idTipoTramite;
	}

	public void setIdTipoTramite(Integer idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}

	public Integer getIdTipoProceso() {
		return idTipoProceso;
	}

	public void setIdTipoProceso(Integer idTipoProceso) {
		this.idTipoProceso = idTipoProceso;
	}

	public ResCargaMasiva getResCargaMasiva() {
		return resCargaMasiva;
	}

	public void setResCargaMasiva(ResCargaMasiva resCargaMasiva) {
		this.resCargaMasiva = resCargaMasiva;
	}
	public Integer getIdAcreedor() {
		return idAcreedor;
	}

	public void setIdAcreedor(Integer idAcreedor) {
		this.idAcreedor = idAcreedor;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public List<Tramite> getTramites() {
		List<Tramite> tramites = new ArrayList<Tramite>();
		Tramite tramite = new Tramite();
		tramite.setIdTramite(2);
		tramite.setDescripcion("Anotaciones Múltiples con Garantia");
		tramites.add(tramite);
		tramite = new Tramite();
		tramite.setIdTramite(10);
		tramite.setDescripcion("Anotaciones Múltiples");
		tramites.add(tramite);
		tramite = new Tramite();
		tramite.setIdTramite(12);
		tramite.setDescripcion("Alta de Acreedores Múltiples");
		tramites.add(tramite);
		return tramites;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		HttpSession session = request.getSession();
		session.setAttribute(RugSessionListener.KEY_REQUESTURI,
				RequestContext.getAttribute(RequestContext.KEY_REQUESTURI));
		session.setAttribute(RugSessionListener.KEY_REMOTEADDR,
				RequestContext.getAttribute(RequestContext.KEY_REMOTEADDR));

	}
	
	public File getAutoridadfile() {
		return autoridadfile;
	}

	public void setAutoridadfile(File autoridadfile) {
		this.autoridadfile = autoridadfile;
	}

	public String getAutoridadfileContentType() {
		return autoridadfileContentType;
	}

	public void setAutoridadfileContentType(String autoridadfileContentType) {
		this.autoridadfileContentType = autoridadfileContentType;
	}

	public String getAutoridadfileFileName() {
		return autoridadfileFileName;
	}

	public void setAutoridadfileFileName(String autoridadfileFileName) {
		this.autoridadfileFileName = autoridadfileFileName;
	}

	public Integer getIdArchivoResultado() {
		return idArchivoResultado;
	}

	public void setIdArchivoResultado(Integer idArchivoResultado) {
		this.idArchivoResultado = idArchivoResultado;
	}

	public Integer getIdArchivoRes() {
		return idArchivoRes;
	}

	public void setIdArchivoRes(Integer idArchivoRes) {
		this.idArchivoRes = idArchivoRes;
	}

	public Integer getSizeListaTramites() {
		return sizeListaTramites;
	}

	public void setSizeListaTramites(Integer sizeListaTramites) {
		this.sizeListaTramites = sizeListaTramites;
	}
	
	

}
