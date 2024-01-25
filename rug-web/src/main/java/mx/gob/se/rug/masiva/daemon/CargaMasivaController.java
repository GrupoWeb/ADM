package mx.gob.se.rug.masiva.daemon;

import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import mx.gob.se.rug.acreedores.dao.AcreedoresDAO;
import mx.gob.se.rug.acreedores.service.AcreedoresService;
import mx.gob.se.rug.acreedores.service.impl.AcreedoresServiceImpl;
import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.common.dto.Mensaje;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.exception.NoDateInfrastructureException;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.garantia.to.ActoContratoTO;
import mx.gob.se.rug.garantia.to.GarantiaTO;
import mx.gob.se.rug.garantia.to.ObligacionTO;
import mx.gob.se.rug.inscripcion.dao.AltaParteDAO;
import mx.gob.se.rug.inscripcion.dao.FirmaMasivaDAO;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.service.impl.InscripcionServiceImpl;
import mx.gob.se.rug.inscripcion.to.AltaParteTO;
import mx.gob.se.rug.inscripcion.to.BienEspecialTO;
import mx.gob.se.rug.inscripcion.to.FirmaMasivaTO;
import mx.gob.se.rug.inscripcion.to.InscripcionTO;
import mx.gob.se.rug.juez.dao.JuezDAO;
import mx.gob.se.rug.mailservice.MailRegistroService;
import mx.gob.se.rug.masiva.action.AutoridadMasivaAction;
import mx.gob.se.rug.masiva.action.CargaMasivaAction;
import mx.gob.se.rug.masiva.dao.MasivaDAO;
import mx.gob.se.rug.masiva.exception.CargaMasivaException;
import mx.gob.se.rug.masiva.exception.CargaMasivaExceptionMaxNumber;
import mx.gob.se.rug.masiva.exception.GarantiaRepetidaException;
import mx.gob.se.rug.masiva.exception.NoDataFound;
import mx.gob.se.rug.masiva.exception.NoTramiteFound;
import mx.gob.se.rug.masiva.exception.StoreProcedureException;
import mx.gob.se.rug.masiva.resultado.to.CargaMasivaResultado;
import mx.gob.se.rug.masiva.resultado.to.ResultadoCargaMasiva;
import mx.gob.se.rug.masiva.resultado.to.Resumen;
import mx.gob.se.rug.masiva.resultado.to.TramiteRes;
import mx.gob.se.rug.masiva.resultado.to.Tramites;
import mx.gob.se.rug.masiva.service.AvisoPreventivoMasivaService;
import mx.gob.se.rug.masiva.service.MasivaService;
import mx.gob.se.rug.masiva.service.ModificacionService;
import mx.gob.se.rug.masiva.service.TransmisionService;
import mx.gob.se.rug.masiva.service.impl.AvisoPreventivoMasivaServiceImpl;
import mx.gob.se.rug.masiva.service.impl.MasivaServiceImpl;
import mx.gob.se.rug.masiva.service.impl.ModificaGarantiaServiceImpl;
import mx.gob.se.rug.masiva.to.AcreedorAutoridad;
import mx.gob.se.rug.masiva.to.Acreedores;
import mx.gob.se.rug.masiva.to.Anotacion;
import mx.gob.se.rug.masiva.to.AnotacionGarantia;
import mx.gob.se.rug.masiva.to.ArchivoTO;
import mx.gob.se.rug.masiva.to.BienEspecial;
import mx.gob.se.rug.masiva.to.Cancelacion;
import mx.gob.se.rug.masiva.to.CargaMasiva;
import mx.gob.se.rug.masiva.to.CargaMasivaProcess;
import mx.gob.se.rug.masiva.to.ControlError;
import mx.gob.se.rug.masiva.to.Creacion;
import mx.gob.se.rug.masiva.to.Deudor;
import mx.gob.se.rug.masiva.to.Ejecucion;
import mx.gob.se.rug.masiva.to.Garantia;
import mx.gob.se.rug.masiva.to.Inscripcion;
import mx.gob.se.rug.masiva.to.Obligacion;
import mx.gob.se.rug.masiva.to.Otorgante;
import mx.gob.se.rug.masiva.to.RenovacionReduccion;
import mx.gob.se.rug.masiva.to.ResCargaMasiva;
import mx.gob.se.rug.masiva.to.TipoBienMueble;
import mx.gob.se.rug.masiva.to.Tramite;
import mx.gob.se.rug.masiva.to.string.CargaMasivaPreProcesed;
import mx.gob.se.rug.masiva.validate.ValidateDataType;
import mx.gob.se.rug.rectificacion.impl.RectificacionServiceImpl;
import mx.gob.se.rug.to.PersonaTO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;
import mx.gob.se.rug.util.to.DateUtilRug;

public class CargaMasivaController extends Thread{
	
	private CargaMasivaPreProcesed cargaMasivaPreProcesed;
	private InscripcionService inscripcionService = new InscripcionServiceImpl();
	private AcreedoresService acreedoresService = new AcreedoresServiceImpl();
	private AvisoPreventivoMasivaService avisoPreventivoMasivaService= new AvisoPreventivoMasivaServiceImpl();
	private MailRegistroService mailRegistroService;
	private CargaMasivaProcess masivaProcess;
	private ResultadoCargaMasiva resultado;
	private ResCargaMasiva resCargaMasiva;
	private MasivaService masivaService = new MasivaServiceImpl();
	private RugBaseAction rugBaseAction;
	private AutoridadMasivaAction authMasivaAction= new AutoridadMasivaAction();
	private ControlError cerror;
	private Integer idArchivoRes;
	private Integer idAcreedor;
	private Integer idUsuario;
	private Integer tramitesErroneos;
	private Integer tramitesCompletos;
	private Integer sizeListaTramites;
	private Integer idArchivoResultado;
	private Integer idTipoTramite;
	private Integer idTramiteTempFirma;
	
	private String totalTramites;
	private String errorArchivo;
	private String nombreUsuario;
	private String nombreArchivo;
	private String regresa;
	
	private UsuarioTO usuarioTO;
	private ArchivoTO archivoTO;
	
	private MasivaDAO masivaDAO = new MasivaDAO();
	private InscripcionDAO inscripcionDAO = new InscripcionDAO();	

	// listas
	private List<Integer> listaTramitesErrores;
	private List<Integer> listaTramites;
	private List<ControlError> listaErrores;
	private List<mx.gob.se.rug.masiva.resultado.to.TramiteRes> inscripcionesErroneas;
	
	private static final int ID_TRAMITE_ACREEDORES = 12;
	private static final int ID_TRAMITE_ANOTACION = 2;
	private static final int ID_TRAMITE_ANOTACION_SIN_GARANTIA = 10;

	public void run() {
		try{
			MyLogger.CargaMasivaLog.log(Level.INFO, "Inicia");
			while (true) {
				try {
					MyLogger.CargaMasivaLog.log(Level.INFO, "A dormir!");
					sleep((int) ( 1000*60));
					mailRegistroService = new MailRegistroService();
					MasivaDAO masivaDAO = new MasivaDAO();
					MyLogger.CargaMasivaLog.log(Level.INFO, "Desperto!");
					masivaProcess = masivaDAO.getCargaMasiva();
					masivaProcess.setIdStatus(9);
					masivaDAO.actualizaProcessCargaIdStatus(masivaProcess);
					procesaCargaMasiva(masivaProcess);
				}catch (NoDataFoundException e) {
					MyLogger.CargaMasivaLog.log(Level.INFO, "--No hay Carga masiva por procesar");
				} catch (InterruptedException e) {
					MyLogger.CargaMasivaLog.log(Level.SEVERE, "InterruptedException",e);
				}catch (InfrastructureException e) {
					MyLogger.CargaMasivaLog.log(Level.SEVERE, "InfrastructureException",e);
				}
			} 
		}catch (Exception e) {
			MyLogger.CargaMasivaLog.log(Level.INFO, "Hubo una Excepcion",e);
		}
	
	}
	
	public void procesaCargaMasiva(CargaMasivaProcess masivaProcess){
		this.masivaProcess=masivaProcess;
		MasivaDAO masivaDAO= new MasivaDAO();
		idUsuario = masivaProcess.getIdUsuario();
		usuarioTO = masivaDAO.getUsuarioTO(idUsuario);
		//nombreUsuario = masivaDAO.getNombreUsuario(idUsuario);
		nombreUsuario = usuarioTO.getNombre();
		nombreArchivo = masivaDAO.getNombreArchivo(masivaProcess.getIdArchivo());
		
		archivoTO = new ArchivoTO();
		archivoTO.setIdArchivo(masivaProcess.getIdArchivo());
		archivoTO.setNombreArchivo(nombreArchivo);
		
		setListaTramites(new ArrayList<Integer>());
		setListaTramitesErrores(new ArrayList<Integer>());
		inscripcionesErroneas = new ArrayList<mx.gob.se.rug.masiva.resultado.to.TramiteRes>();
		try{
			CargaMasiva cm = validamosArchivo(masivaProcess.getIdTipoTramite(),masivaProcess.getIdArchivo());	
                        System.out.println("masivaProcess.getIdTipoTramite: TT"+masivaProcess.getIdTipoTramite());
			if(masivaProcess.getIdTipoTramite()==10 || masivaProcess.getIdTipoTramite()==2 || masivaProcess.getIdTipoTramite()==12){
				//Tramite de Autoridad
				regresa = "failed";
				if(cargaMasivaPreProcesed.getTotalTramites() == cargaMasivaPreProcesed.getTramiteIncorrectos().size()){
					//todos MAL
					Resumen resumen = new Resumen();
					CargaMasivaAction cargaMasivaAction  = new CargaMasivaAction();
					resumen = cargaMasivaAction.getResumenFiltroError(cargaMasivaPreProcesed);
					
					ResultadoCargaMasiva resultado = new ResultadoCargaMasiva();
					resultado.setResumen(resumen);
					Tramites tramites = new  Tramites();
					tramites.getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
					resultado.setTramites(tramites);
					
					masivaService.generaArchivoResumen(resultado, usuarioTO, archivoTO, masivaProcess);
					
					//jsp
					setIdArchivoResultado(masivaProcess.getIdArchivo());
					/*
					 * */
					regresa="resultado";
				}else{
					//hubo Buenos
					setResCargaMasiva(procesoAtendido(cargaMasivaPreProcesed,usuarioTO,archivoTO,masivaProcess));
					//masivaProcess.setIdArchivoResumen(resCargaMasiva.getIdArchivoXML());
//					setIdArchivoRes(resCargaMasiva.getIdArchivoXML());
//					setIdArchivoResultado(archivoTO.getIdArchivo());
//					setSizeListaTramites(0);
					/*
					 * */
					regresa="resultado";
				}
			}else{
				//Tramite Acreedor	
                                
				seleccionaProceso(cm);
				masivaDAO.actualizaProcessCargaIdResumen(masivaProcess);
				this.setIdArchivoResultado(masivaProcess.getIdArchivo());
				this.setSizeListaTramites(listaTramites.size());
                               
				this.setTramitesErroneos(cargaMasivaPreProcesed.getTramiteIncorrectos().size());				
			}
		
			//Procesamientyo OK
			masivaProcess.setIdStatus(3);
			masivaDAO.actualizaProcessCargaIdStatus(masivaProcess);
			if(masivaProcess.getbTipoProceso().equalsIgnoreCase("D")){
				
				mailRegistroService.sendMailDesatendidoAvisoFin(usuarioTO, String.valueOf(masivaProcess.getIdArchivo()));
			}
		}catch (Exception e) {
			MyLogger.CargaMasivaLog.log(Level.INFO, "InfrastructureException",e);
			masivaProcess.setIdStatus(12);
			e.printStackTrace();
			try {
				masivaDAO.actualizaProcessCargaIdStatus(masivaProcess);
			} catch (CargaMasivaException e1) {
				MyLogger.CargaMasivaLog.log(Level.SEVERE, "InfrastructureException",e1);
			}
		}
	}
	
	private ResCargaMasiva procesoAtendido(CargaMasivaPreProcesed cargaMasivaPre,
			UsuarioTO usuario,ArchivoTO archivoInicial,CargaMasivaProcess masivaProcess) {
		ResCargaMasiva respuestaCM = new ResCargaMasiva();
		respuestaCM.setRegresa("failed");
		CargaMasiva cargaMasiva = cargaMasivaPre.getCargaMasiva();
		try {
			
				respuestaCM.setIdArchivo(archivoInicial.getIdArchivo());
				switch (masivaProcess.getIdTipoTramite()) {
				case ID_TRAMITE_ACREEDORES:
					// alta de acreedores./+ 
					if (cargaMasiva.getAcreedores() != null) {
							if (cargaMasiva.getAcreedores().getAcreedorAutoridad().size() > 0) {
								System.out.println(":::Se inicia el alta de acreedores:::");
								respuestaCM = cargaAcreedores(respuestaCM,cargaMasivaPre, usuario,archivoInicial,masivaProcess);
							} else {
								rugBaseAction.setMensaje(new Mensaje());
								rugBaseAction.getMensaje().setId("Rug-AutoridadMasiva-005");
								rugBaseAction.getMensaje().setMensaje("El archivo seleccionado no cuenta con etiquetas de acreedores.");
								//setErrorArchivo("Rug-AutoridadMasiva-005 / El archivo seleccionado no cuenta con etiquetas de acreedores.");
							}
					} else {
						rugBaseAction.setMensaje(new Mensaje());
						rugBaseAction.getMensaje().setId("Rug-AutoridadMasiva-005");
						rugBaseAction.getMensaje().setMensaje("El archivo seleccionado no cuenta con etiquetas de acreedores.");
						//setErrorArchivo("Rug-AutoridadMasiva-005 / El archivo seleccionado no cuenta con etiquetas de acreedores.");
					}
					setIdTipoTramite(ID_TRAMITE_ACREEDORES);
					break;
				case ID_TRAMITE_ANOTACION:
					// anotacion.
					if (cargaMasiva.getAnotacionGarantia().size() > 0) {
						System.out
								.println(":::Se inicia el alta de anotacion:::");
						respuestaCM = cargaAnotacionGarantia(respuestaCM,cargaMasivaPre, usuario,archivoInicial,masivaProcess);
					} else {
						rugBaseAction.setMensaje(new Mensaje());
						rugBaseAction.getMensaje().setId("Rug-AutoridadMasiva-011");
						rugBaseAction.getMensaje().setMensaje("El archivo seleccionado no cuenta con etiquetas de anotacion.");
						//setErrorArchivo("Rug-AutoridadMasiva-011 / El archivo seleccionado no cuenta con etiquetas de anotacion.");
					}
					break;
				case ID_TRAMITE_ANOTACION_SIN_GARANTIA:
					// anotacion sin garantia.
					if (cargaMasiva.getAnotacion().size() > 0) {
						System.out
								.println(":::Se inicia el alta de anotacion:::");
						respuestaCM = cargaAnotacion(respuestaCM, cargaMasivaPre,usuario,archivoInicial);
					} else {
						rugBaseAction.setMensaje(new Mensaje());
						rugBaseAction.getMensaje().setId("Rug-AutoridadMasiva-011");
						rugBaseAction.getMensaje().setMensaje("El archivo seleccionado no cuenta con etiquetas de anotacion.");
						//setErrorArchivo("Rug-AutoridadMasiva-011 / El archivo seleccionado no cuenta con etiquetas de anotacion.");
					}
					break;

				default:
					rugBaseAction.setMensaje(new Mensaje());
					rugBaseAction.getMensaje().setId("Rug-AutoridadMasiva-011");
					rugBaseAction.getMensaje().setMensaje("El tramite seleccionado no existe dentro del menu de autoridad");
					//setErrorArchivo("Rug-AutoridadMasiva-011 / El tramite seleccionado no existe dentro del menu de autoridad.");
					break;
				}
			
				//Marcador Buenas&Malas
			//	masivaDAO.actualizaDatosCarga(respuestaCM.getListaTramites().size(),respuestaCM.getListaTramitesErroneos().size(), respuestaCM.getIdArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respuestaCM;
	}
	
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
			} 

			// generamos el XML de resultado
		//	ArchivoTO archivoRes = new ArchivoTO();
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

//			byte[] bytes2 = masivaService.convertXMLObjetc(resultado);
//			archivoRes.setAlgoritoHash(masivaService.getSha1FromFile(bytes2));
//			archivoRes.setArchivo(bytes2);
//			archivoRes
//					.setDescripcion("Archivo nuevo de carga masiva de anotacion del usuario : "
//							+ usuario.getNombre()
//							+ ", con el id :"
//							+ usuario.getPersona().getIdPersona()
//							+ ", resultado de una carga que contenia archivos incorrectos");
//			archivoRes.setIdUsuario(usuario.getPersona().getIdPersona());
//			archivoRes.setNombreArchivo(archivoInicial.getNombreArchivo()+"-Resumen");
//			archivoRes.setTipoArchivo("xml");
			
			masivaService.generaArchivoResumen(resultado, usuario, archivoInicial, masivaProcess);
			
			
			resCargaMasiva.setIdArchivoXML(masivaProcess.getIdArchivoResumen());
			
			
			
			
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
					//sessionMap.put(Constants.ID_TRAMITE_NUEVO, valor);
				}
			}
			// terminamos el tramite de firma masiva
			resCargaMasiva.setRegresa("success");
		} catch (Exception e) {
			e.printStackTrace();
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
	
	private ResCargaMasiva cargaAnotacionGarantia(
			ResCargaMasiva resCargaMasiva, CargaMasivaPreProcesed masivaPreProcesed,
			UsuarioTO usuario ,ArchivoTO archivoInicial,CargaMasivaProcess masivaProcess) {
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
					
					masivaService.verifyAcreedorOfGarantia(masivaProcess.getIdAcreedor(), anotacion.getIdentificadorGarantia().getIdGarantia().intValue());
					
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
				}catch (CargaMasivaException e) {
					plSql = new PlSql();
					plSql.setIntPl(e.getCodeError());
					plSql.setStrPl(e.getMessage());
					plSql.setResStrPl(anotacion.getIdentificadorGarantia().getClaveRastreo());
					resCargaMasiva.getListaTramitesErroneos().add(plSql);
					//BAja Tramite Rastreo
					if(anotacion.getIdentificadorGarantia()!=null){
						masivaDAO.executeBajaClaveRastreoAutoridad(tramite.getIdTramite(), anotacion.getIdentificadorGarantia().getClaveRastreo());
					}
				}catch (InfrastructureException e) {
					plSql = new PlSql();
					plSql.setIntPl(99);
					plSql.setStrPl(e.getMessage());
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
			} 

			// generamos el XML de resultado
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

//			byte[] bytes2 = masivaService.convertXMLObjetc(resultado);
//			archivoRes.setAlgoritoHash(masivaService.getSha1FromFile(bytes2));
//			archivoRes.setArchivo(bytes2);
//			archivoRes
//					.setDescripcion("Archivo nuevo de carga masiva de anotacion del usuario : "
//							+ usuario.getNombre()
//							+ ", con el id :"
//							+ usuario.getPersona().getIdPersona()
//							+ ", resultado de una carga que contenia archivos incorrectos");
//			archivoRes.setIdUsuario(usuario.getPersona().getIdPersona());
//			archivoRes.setNombreArchivo(archivoInicial.getNombreArchivo()+"-resumen");
//			archivoRes.setTipoArchivo("xml");
//			
			
			masivaService.generaArchivoResumen(resultado, usuario, archivoInicial, masivaProcess);
			
			resCargaMasiva.setIdArchivoXML(masivaProcess.getIdArchivoResumen());
			// terminamos de generar el XML de resultado

			
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
					//sessionMap.put(Constants.ID_TRAMITE_NUEVO, valor);
				}
			}
			// terminamos el tramite de firma masiva
			resCargaMasiva.setRegresa("success");
		} catch (Exception e) {
			e.printStackTrace();
			rugBaseAction.setMensaje(new Mensaje());
			rugBaseAction.getMensaje().setId("999");
			rugBaseAction.getMensaje().setMensaje(
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
		if(acreedorAutoridad.getRfc() != null&& !acreedorAutoridad.getRfc().trim().equalsIgnoreCase("")){
			claveRastreoAcreedor=acreedorAutoridad.getRfc();
		}else if(acreedorAutoridad.getDenominacionRazonSocial() != null&&! acreedorAutoridad.getDenominacionRazonSocial().trim().equalsIgnoreCase("")){
			claveRastreoAcreedor=acreedorAutoridad.getDenominacionRazonSocial();
		}else if(acreedorAutoridad.getNombre() != null&& !acreedorAutoridad.getNombre().trim().equalsIgnoreCase("")){
			claveRastreoAcreedor=acreedorAutoridad.getNombre();
			if(acreedorAutoridad.getApellidoPaterno() != null&& !acreedorAutoridad.getApellidoPaterno().trim().equalsIgnoreCase("")){
				claveRastreoAcreedor=claveRastreoAcreedor+" "+acreedorAutoridad.getApellidoPaterno();
			}
		}else if(acreedorAutoridad.getCorreoElectronico() != null&& !acreedorAutoridad.getCorreoElectronico().trim().equalsIgnoreCase("")){
			claveRastreoAcreedor=acreedorAutoridad.getCorreoElectronico();
		}
		return claveRastreoAcreedor;
	}
	
	private ResCargaMasiva cargaAcreedores(ResCargaMasiva resCargaMasiva,
			CargaMasivaPreProcesed masivaPreProcesed, UsuarioTO usuario,ArchivoTO archivoTO,CargaMasivaProcess masivaProcess) {
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
						// Poblamos lo que seria equivalente a la clave de rastreo
						plSqlTmp.setResStrPl(getClaveRastreoAcreedor(acreedor));
						
						plSql = authMasivaAction.validaDatosCargaAcreedores(acreedor);
						if (plSql.getIntPl() == 0) {
							plSql = authMasivaAction.validaAcreedor(acreedor, usuario);
							if (plSql.getIntPl() == 0) {
								plSql = authMasivaAction.altaAcreedor(usuario, acreedor);
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
			} 
//			else {
//				rugBaseAction.setMensaje(new Mensaje());
//				rugBaseAction.getMensaje().setMensaje("No existieron tramites correctos");
//			}

			// generamos el XML de resultado
			//ArchivoTO archivoRes = new ArchivoTO();
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

			
//			byte[] bytes2 = masivaService.convertXMLObjetc(resultado);
//			archivoRes.setAlgoritoHash(masivaService.getSha1FromFile(bytes2));
//			archivoRes.setArchivo(bytes2);
//			archivoRes.setDescripcion("Archivo nuevo de carga masiva de modificacion del usuario : "
//							+ usuario.getNombre()
//							+ ", con el id :"
//							+ usuario.getPersona().getIdPersona()
//							+ ", resultado de una carga que contenia archivos incorrectos");
//			archivoRes.setIdUsuario(usuario.getPersona().getIdPersona());
//			archivoRes.setNombreArchivo("cmResnuevo");
//			archivoRes.setTipoArchivo("xml");
			
			masivaService.generaArchivoResumen(resultado, usuario, archivoTO, masivaProcess);
			
			
			resCargaMasiva.setIdArchivoXML(masivaProcess.getIdArchivoResumen());
			
			// terminamos de generar el XML de resultado
			resCargaMasiva.setRegresa("success");
			// generamos el tramite de firma masiva en caso de aver correctos
			//masivaProcess.setIdArchivo(archivoTO.getIdArchivo());
			//masivaDAO.actualizaProcessCargaIdResumen(masivaProcess);
			
			
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
					//sessionMap.put(Constants.ID_TRAMITE_NUEVO, valor);		
					setIdTramiteTempFirma(valor);
				}else{
					rugBaseAction.setMensaje(new Mensaje());
					rugBaseAction.getMensaje().setId("788");
					rugBaseAction.getMensaje().setMensaje("No se pudo generar el tramite temporal de firma masiva");
					resCargaMasiva.setRegresa("failed");
				}
			}
			// terminamos el tramite de firma masiva
			
		} catch (Exception e) {
			e.printStackTrace();
			rugBaseAction.setMensaje(new Mensaje());
			rugBaseAction.getMensaje().setId("999");
			rugBaseAction.getMensaje().setMensaje(
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
	
	private CargaMasiva validamosArchivo(Integer idTipoTramite, Integer idArchivo){
		cargaMasivaPreProcesed = new CargaMasivaPreProcesed();
		ValidateDataType validateDataType = new ValidateDataType();
		String xmlFromDB = validateDataType.getFileFromDB(idArchivo);
		
		try{
			cargaMasivaPreProcesed = validateDataType.parseCargaMasiva(xmlFromDB,idTipoTramite);
		}catch(CargaMasivaException e){
			e.printStackTrace();
		} catch (NoTramiteFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CargaMasivaExceptionMaxNumber e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return cargaMasivaPreProcesed.getCargaMasiva();
	}
	
	private String seleccionaProceso(CargaMasiva cm){
		regresa = "failed";
		try {
			ResCargaMasiva respuestaCM = new ResCargaMasiva();
			//AcreedorTO acreedor = inscripcionService.getAcreedorByID(masivaProcess.getIdAcreedor());
			AcreedorTO acreedor = new AcreedorTO();
			acreedor.setIdPersona(masivaProcess.getIdAcreedor());
			acreedor.setIdAcreedor(masivaProcess.getIdAcreedor().toString());
			idAcreedor=masivaProcess.getIdAcreedor();
			ArchivoTO archivoTO = new ArchivoTO();
			archivoTO.setIdArchivo(masivaProcess.getIdArchivo());
			archivoTO.setNombreArchivo(nombreArchivo);
			
			switch (masivaProcess.getIdTipoTramite()) {
				// Inscripcion
				case 1:					
					if (cm.getInscripcion().size() > 0) {
						System.out.println("--- el archivo contiene inscripciones --");						
						regresa = cargaMasivaInscripcion(cm,acreedor);
					} else if(this.cargaMasivaPreProcesed.getTramiteIncorrectos().size()>0){
						Resumen resumen = new Resumen();
						resumen = getResumenFiltroError(cargaMasivaPreProcesed);
						
						resultado = new ResultadoCargaMasiva();
						resultado.setResumen(resumen);
						Tramites tramites = new  Tramites();
						tramites.getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						resultado.setTramites(tramites);
						
						masivaService.generaArchivoResumen(resultado, null, archivoTO, masivaProcess);
						
						this.setTotalTramites(String.valueOf(cargaMasivaPreProcesed.getTramiteIncorrectos().size()));
						this.setTramitesCompletos(0);
						this.setTramitesErroneos(cargaMasivaPreProcesed.getTramiteIncorrectos().size());
						this.setInscripcionesErroneas(cargaMasivaPreProcesed.getTramiteIncorrectos());
						regresa="resultado";
					}else{
						setErrorArchivo("El archivo seleccionado no contiene Inscripciones ");
					}
					break;
                                case 36:			
                                        System.out.println("--- el archivo contiene Factorajes 00 --"+cm.getInscripcion().size());
					if (cm.getInscripcion().size() > 0) {
						System.out.println("--- el archivo contiene factorajes if --"+cm);						
						regresa = cargaMasivaInscripcion(cm,acreedor);
					} else if(this.cargaMasivaPreProcesed.getTramiteIncorrectos().size()>0){
                                            System.out.println("--- el archivo contiene Factorajes else --");
						Resumen resumen = new Resumen();
						resumen = getResumenFiltroError(cargaMasivaPreProcesed);
						
						resultado = new ResultadoCargaMasiva();
						resultado.setResumen(resumen);
						Tramites tramites = new  Tramites();
						tramites.getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						resultado.setTramites(tramites);
						
						masivaService.generaArchivoResumen(resultado, null, archivoTO, masivaProcess);
						
						this.setTotalTramites(String.valueOf(cargaMasivaPreProcesed.getTramiteIncorrectos().size()));
						this.setTramitesCompletos(0);
						this.setTramitesErroneos(cargaMasivaPreProcesed.getTramiteIncorrectos().size());
						this.setInscripcionesErroneas(cargaMasivaPreProcesed.getTramiteIncorrectos());
						regresa="resultado";
					}else{
						setErrorArchivo("El archivo seleccionado no contiene Factoraje ");
					}
					break;
				case 31: // traslado					
					if (cm.getTraslado().size() > 0) {
						System.out.println("--- el archivo contiene traslados --");						
						regresa = cargaMasivaTraslado(cm,acreedor);
					} else if(this.cargaMasivaPreProcesed.getTramiteIncorrectos().size()>0) {
						Resumen resumen = new Resumen();
						resumen = getResumenFiltroError(cargaMasivaPreProcesed);
						
						resultado = new ResultadoCargaMasiva();
						resultado.setResumen(resumen);
						Tramites tramites = new  Tramites();
						tramites.getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						resultado.setTramites(tramites);
						
						masivaService.generaArchivoResumen(resultado, null, archivoTO, masivaProcess);
						
						this.setTotalTramites(String.valueOf(cargaMasivaPreProcesed.getTramiteIncorrectos().size()));
						this.setTramitesCompletos(0);
						this.setTramitesErroneos(cargaMasivaPreProcesed.getTramiteIncorrectos().size());
						this.setInscripcionesErroneas(cargaMasivaPreProcesed.getTramiteIncorrectos());
						regresa="resultado";
					}else{
						setErrorArchivo("El archivo seleccionado no contiene Traslados ");
					}
					break;
				// Cancelacion
				case 4:
					if (cm.getCancelacion().size() > 0) {
						regresa = cargaMasivaCancelacion(cm,acreedor);
					} else if(this.cargaMasivaPreProcesed.getTramiteIncorrectos().size()>0){
						Resumen resumen = new Resumen();
						resumen = getResumenFiltroError(cargaMasivaPreProcesed);
						
						resultado = new ResultadoCargaMasiva();
						resultado.setResumen(resumen);
						Tramites tramites = new  Tramites();
						tramites.getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						resultado.setTramites(tramites);
						
						masivaService.generaArchivoResumen(resultado, null, archivoTO, masivaProcess);
						
						this.setTotalTramites(String.valueOf(cargaMasivaPreProcesed.getTramiteIncorrectos().size()));
						this.setTramitesCompletos(0);
						this.setTramitesErroneos(cargaMasivaPreProcesed.getTramiteIncorrectos().size());
						this.setInscripcionesErroneas(cargaMasivaPreProcesed.getTramiteIncorrectos());
						regresa="success";
					}else {
						setErrorArchivo("El archivo seleccionado no contiene Cancelaciones ");
					}
					break;
					// Ejecucion
				case 30:
					if (cm.getEjecucion().size() > 0) {
						regresa = cargaMasivaEjecucion(cm,acreedor);
					} else if(this.cargaMasivaPreProcesed.getTramiteIncorrectos().size()>0){
						Resumen resumen = new Resumen();
						resumen = getResumenFiltroError(cargaMasivaPreProcesed);
						
						resultado = new ResultadoCargaMasiva();
						resultado.setResumen(resumen);
						Tramites tramites = new  Tramites();
						tramites.getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						resultado.setTramites(tramites);
						
						masivaService.generaArchivoResumen(resultado, null, archivoTO, masivaProcess);
						
						this.setTotalTramites(String.valueOf(cargaMasivaPreProcesed.getTramiteIncorrectos().size()));
						this.setTramitesCompletos(0);
						this.setTramitesErroneos(cargaMasivaPreProcesed.getTramiteIncorrectos().size());
						this.setInscripcionesErroneas(cargaMasivaPreProcesed.getTramiteIncorrectos());
						regresa="success";
					}else {
						setErrorArchivo("El archivo seleccionado no contiene Ejecuciones ");
					}
					break;
				// Rectificaciones por error
				case 6:
					if (cm.getRectificacionPorError().size() > 0) {
						//regresa = cargaMasivaRectificacion(usuario, cm,inscripcionService.getAcreedorByID(idAcreedorTO),plSql.getResIntPl());
						RectificacionServiceImpl rectificacionService= new RectificacionServiceImpl();
						CargaMasivaResultado cargaMasivaResultado;
						cargaMasivaResultado = rectificacionService.cargaMasivaRectificacion(usuarioTO,cm,acreedor,masivaProcess.getIdArchivo());
						
						cargaMasivaResultado.getResultadoCargaMasiva().getTramites().getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						
						for (Tramite tramite: cargaMasivaResultado.getTramitesCorrectos()) {
							listaTramites.add(tramite.getIdTramite());
						}
						
						for (Tramite tramite: cargaMasivaResultado.getTramitesIncorrectos()) {
							inscripcionesErroneas.add(tramite.getTramiteRes());
						}
						
						
						/*
						 * Extraer a un metodo nuevo
						 */
						cargaMasivaResultado = masivaService.generaFirmaMasiva(cargaMasivaResultado, cargaMasivaPreProcesed, usuarioTO, archivoTO, acreedor);
						Integer idFirmaMasiva = cargaMasivaResultado.getIdFirmaMasiva();
						idTramiteTempFirma=idFirmaMasiva;
						this.setIdArchivoRes(cargaMasivaResultado.getIdArchivoResumen());
						masivaProcess.setIdArchivoResumen(cargaMasivaResultado.getIdArchivoResumen());
						regresa = "resultado";
					} else if(this.cargaMasivaPreProcesed.getTramiteIncorrectos().size()>0){
						Resumen resumen = new Resumen();
						resumen = getResumenFiltroError(cargaMasivaPreProcesed);
						
						resultado = new ResultadoCargaMasiva();
						resultado.setResumen(resumen);
						Tramites tramites = new  Tramites();
						tramites.getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						resultado.setTramites(tramites);
						
						masivaService.generaArchivoResumen(resultado, null, archivoTO, masivaProcess);
						
						this.setTotalTramites(String.valueOf(cargaMasivaPreProcesed.getTramiteIncorrectos().size()));
						this.setTramitesCompletos(0);
						this.setTramitesErroneos(cargaMasivaPreProcesed.getTramiteIncorrectos().size());
						this.setInscripcionesErroneas(cargaMasivaPreProcesed.getTramiteIncorrectos());
						regresa="success";
					}else {
						setErrorArchivo("El archivo seleccionado no contiene Rectificaciones por error ");
					}
					break;
				// Modificacion
				case 7:
					if (cm.getModificacion().size() > 0) {
						ModificacionService modificacionService= new ModificacionService();
						CargaMasivaResultado cargaMasivaResultado;
						cargaMasivaResultado=modificacionService.cargaMasivaModificacion(usuarioTO, cm, acreedor, masivaProcess.getIdArchivo());
						
						
						cargaMasivaResultado.getResultadoCargaMasiva().getTramites().getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						
						for (Tramite tramite: cargaMasivaResultado.getTramitesCorrectos()) {
							listaTramites.add(tramite.getIdTramite());
						}
						
						for (Tramite tramite: cargaMasivaResultado.getTramitesIncorrectos()) {
							inscripcionesErroneas.add(tramite.getTramiteRes());
						}
						
						
				//Temporal ya que es un proceso general
						//Generar Firma MAsiva
						cargaMasivaResultado = masivaService.generaFirmaMasiva(cargaMasivaResultado, cargaMasivaPreProcesed, usuarioTO, archivoTO, acreedor);
						Integer idFirmaMasiva = cargaMasivaResultado.getIdFirmaMasiva();
						idTramiteTempFirma=idFirmaMasiva;
						masivaProcess.setIdArchivoResumen(cargaMasivaResultado.getIdArchivoResumen());						
						regresa = "resultado";
					} else if(this.cargaMasivaPreProcesed.getTramiteIncorrectos().size()>0){
						Resumen resumen = new Resumen();
						resumen = getResumenFiltroError(cargaMasivaPreProcesed);
						
						resultado = new ResultadoCargaMasiva();
						resultado.setResumen(resumen);
						Tramites tramites = new  Tramites();
						tramites.getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						resultado.setTramites(tramites);
						
						masivaService.generaArchivoResumen(resultado, null, archivoTO, masivaProcess);
						
						this.setTotalTramites(String.valueOf(cargaMasivaPreProcesed.getTramiteIncorrectos().size()));
						this.setTramitesCompletos(0);
						this.setTramitesErroneos(cargaMasivaPreProcesed.getTramiteIncorrectos().size());
						this.setInscripcionesErroneas(cargaMasivaPreProcesed.getTramiteIncorrectos());
						regresa="success";
					}else {
						setErrorArchivo("El archivo seleccionado no contiene Modificaciones");
					}
	
					break;
				// Transmisiones Multiples
				case 8:
					if (cm.getTransmision().size() > 0) {
						TransmisionService transmisionService= new TransmisionService();
						CargaMasivaResultado cargaMasivaResultado;
						cargaMasivaResultado=transmisionService.cargaMasivaTransmision(usuarioTO, cm, acreedor, masivaProcess.getIdArchivo());
						
						cargaMasivaResultado.getResultadoCargaMasiva().getTramites().getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						
						for (Tramite tramite: cargaMasivaResultado.getTramitesCorrectos()) {
							listaTramites.add(tramite.getIdTramite());
						}
						
						for (Tramite tramite: cargaMasivaResultado.getTramitesIncorrectos()) {
							inscripcionesErroneas.add(tramite.getTramiteRes());
						}
						
						
						
				//Temporal ya que es un proceso general
						//Generar Firma MAsiva
						cargaMasivaResultado = masivaService.generaFirmaMasiva(cargaMasivaResultado, cargaMasivaPreProcesed, usuarioTO, archivoTO, acreedor);
						Integer idFirmaMasiva = cargaMasivaResultado.getIdFirmaMasiva();
						idTramiteTempFirma=idFirmaMasiva;
						this.setIdArchivoRes(cargaMasivaResultado.getIdArchivoResumen());
						masivaProcess.setIdArchivoResumen(cargaMasivaResultado.getIdArchivoResumen());						
						regresa = "resultado";
					} else if(this.cargaMasivaPreProcesed.getTramiteIncorrectos().size()>0){
						Resumen resumen = new Resumen();
						resumen = getResumenFiltroError(cargaMasivaPreProcesed);
						
						resultado = new ResultadoCargaMasiva();
						resultado.setResumen(resumen);
						Tramites tramites = new  Tramites();
						tramites.getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						resultado.setTramites(tramites);
						
						masivaService.generaArchivoResumen(resultado, null, archivoTO, masivaProcess);
						
						this.setTotalTramites(String.valueOf(cargaMasivaPreProcesed.getTramiteIncorrectos().size()));
						this.setTramitesCompletos(0);
						this.setTramitesErroneos(cargaMasivaPreProcesed.getTramiteIncorrectos().size());
						this.setInscripcionesErroneas(cargaMasivaPreProcesed.getTramiteIncorrectos());
						regresa="success";
					}else {
						setErrorArchivo("El archivo seleccionado no contiene Transmisiones");
					}
	
					break;
				// Renovacion o reduccion de vigencia
				case 9:
					if (cm.getRenovacionReduccion().size() > 0) {						
						regresa = cargaMasivaRenovacion(usuarioTO,cm,acreedor,masivaProcess.getIdArchivo());
					} else if(this.cargaMasivaPreProcesed.getTramiteIncorrectos().size()>0){
						Resumen resumen = new Resumen();
						resumen = getResumenFiltroError(cargaMasivaPreProcesed);
						
						resultado = new ResultadoCargaMasiva();
						resultado.setResumen(resumen);
						Tramites tramites = new  Tramites();
						tramites.getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						resultado.setTramites(tramites);
						
						masivaService.generaArchivoResumen(resultado, null, archivoTO, masivaProcess);
						
						this.setTotalTramites(String.valueOf(cargaMasivaPreProcesed.getTramiteIncorrectos().size()));
						this.setTramitesCompletos(0);
						this.setTramitesErroneos(cargaMasivaPreProcesed.getTramiteIncorrectos().size());
						this.setInscripcionesErroneas(cargaMasivaPreProcesed.getTramiteIncorrectos());
						regresa="success";
					}else {
						setErrorArchivo("El archivo seleccionado no contiene Renovaciones o reducciones de vigencia");
					}
					break;
				case 3://Aviso Preventivo
					if (cm.getAvisoPreventivo().size() > 0) {
						System.out.println(":::Se inicia el alta de avisos preventivos:::");
						// se busca al acreedor que se selecciono
						//avisoPreventivoMasivaService.setMensaje(getMensaje());
						//avisoPreventivoMasivaService.setSessionMap(sessionMap);
						respuestaCM.setIdArchivo(masivaProcess.getIdArchivo());
						respuestaCM = avisoPreventivoMasivaService.cargaAvisoPreventivo(respuestaCM,cargaMasivaPreProcesed,cm,usuarioTO,acreedor,masivaProcess.getIdArchivo(),nombreArchivo);
						idTramiteTempFirma=respuestaCM.getIdTramiteNuevo();
						regresa = respuestaCM.getRegresa();
						
						masivaProcess.setIdArchivoResumen(respuestaCM.getIdArchivoXML());
						this.setIdArchivoRes(respuestaCM.getIdArchivoXML());
						
						for (PlSql tramite: respuestaCM.getListaTramites()) {
							listaTramites.add(tramite.getResIntPl());
						}
						/*
						inscripcionesErroneas.addAll(respuestaCM.getTramitesResultado());
						for(TramiteRes tramiteRes: respuestaCM.getTramitesResultado()){
							if(!tramiteRes.getCodigoError().equalsIgnoreCase("0")){
								inscripcionesErroneas.add(tramiteRes);
							}
						}*/
						regresa = "success";
					} else if(this.cargaMasivaPreProcesed.getTramiteIncorrectos().size()>0){
						Resumen resumen = new Resumen();
						resumen = getResumenFiltroError(cargaMasivaPreProcesed);
						
						resultado = new ResultadoCargaMasiva();
						resultado.setResumen(resumen);
						Tramites tramites = new  Tramites();
						tramites.getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						resultado.setTramites(tramites);
						
						masivaService.generaArchivoResumen(resultado, null, archivoTO, masivaProcess);
						
						this.setTotalTramites(String.valueOf(cargaMasivaPreProcesed.getTramiteIncorrectos().size()));
						this.setTramitesCompletos(0);
						this.setTramitesErroneos(cargaMasivaPreProcesed.getTramiteIncorrectos().size());
						this.setInscripcionesErroneas(cargaMasivaPreProcesed.getTramiteIncorrectos());
						regresa="success";
					}else {
						setErrorArchivo("El archivo seleccionado no cuenta con etiquetas de avisos preventivos.");
					}
	
					break;
				default:
					setErrorArchivo("El tramite seleccionado no existe para cargas masivas");
					break;
			}
			//Baja tramite Rastreo
			if(inscripcionesErroneas!=null&&inscripcionesErroneas.size()>0){
				for (TramiteRes tramite: inscripcionesErroneas) {
					if( tramite.getClaveRastreo()!=null)
					masivaDAO.executeBajaClaveRastreo(acreedor.getIdPersona(), masivaProcess.getIdArchivo(),tramite.getClaveRastreo());
				}
			}
			
		}catch (Exception e) {
			setErrorArchivo(e.getMessage() + "-" + e.getCause());
			e.printStackTrace();
		}
		return regresa;
	}
	
	private String cargaMasivaRenovacion(UsuarioTO usuario, CargaMasiva cm,
			AcreedorTO acreedor, Integer idArchivo) {
		String regresa = "failed";
		int consecutivo = 1;
		Tramite tramite;
		resultado = new ResultadoCargaMasiva();
		this.listaTramites = new ArrayList<Integer>();
		listaErrores = new ArrayList<ControlError>();
		resultado.setTramites(new Tramites());
		try {
			acreedor = inscripcionService.getAcreedorByID(idAcreedor);
			for (RenovacionReduccion reduccion : cm.getRenovacionReduccion()) {
				tramite = new Tramite();
				tramite.setIdArchivo(idArchivo);
				tramite.setIdUsuario(usuario.getPersona().getIdPersona());
				tramite.setConsecutivo(consecutivo++);
				tramite.setIdGarantia(reduccion
						.getIdentificadorGarantia().getIdGarantia().intValue());
				tramite.setIdTipoTramite(9);
				tramite.setIdEstatus(5);
				tramite.setIdAcreedor(idAcreedor);
				tramite.setClaveRastreo(reduccion.getIdentificadorGarantia()
						.getClaveRastreo());
				cerror = agregaRenovacion(reduccion, tramite);
				if (cerror != null) {
					listaErrores.add(cerror);
				}
			}
		} catch (Exception e) {
			java.util.Date date = new java.util.Date();
			Resumen resumen = new Resumen();
			resumen.setMensajeError("Sucedio un error en el sistema la fecha :"
					+ date.toString() + ", enviar el XML a soporte. codError["
					+ date.getTime() + "]");
			resultado.setResumen(resumen);
			e.printStackTrace();
		} finally {
			try {
				tramitesErroneos = listaTramitesErrores.size();
				Resumen resumen = getResumen(this.cargaMasivaPreProcesed);					
				resultado.setResumen(resumen);
				
				
//				ArchivoTO archivoRes = new ArchivoTO();
//				byte[] bytes2 = convertXMLObjetc(resultado);
//				archivoRes.setAlgoritoHash(getSha1FromFile(bytes2));
//				archivoRes.setArchivo(bytes2);
//				archivoRes
//						.setDescripcion("Archivo nuevo de carga masiva de modificacion del usuario : "
//								+ usuario.getNombre()
//								+ ", con el id :"
//								+ usuario.getPersona().getIdPersona()
//								+ ", resultado de una carga que contenia archivos incorrectos");
//				archivoRes.setIdUsuario(usuario.getPersona().getIdPersona());
//				archivoRes.setNombreArchivo("cmResnuevo");
//				archivoRes.setTipoArchivo("xml");
//				setIdArchivoRes(inscripcionService.insertArchivo(archivoRes).getResIntPl());
//				masivaProcess.setIdArchivoResumen(getIdArchivoRes());
				
				masivaService.generaArchivoResumen(resultado, null, archivoTO, masivaProcess);
				if (listaTramitesErrores.size() > 0) {
					System.out.println("#### sucedieron errores en la carga");
					// crear nuevo archivo XML y guardarlo para el tramite de
					// firma
					List<RenovacionReduccion> listaCorrectos = new ArrayList<RenovacionReduccion>();
					for (mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcionRes : resultado
							.getTramites().getTramite()) {
						if (inscripcionRes.getCodigoError().equals("0")) {
							for (RenovacionReduccion ins : cm
									.getRenovacionReduccion()) {
								if (ins.getIdentificadorGarantia()
										.getClaveRastreo()
										.trim()
										.equals(inscripcionRes
												.getClaveRastreo().trim())) {
									listaCorrectos.add(ins);
									break;
								}
							}
						}
					}

					if (listaCorrectos.size() > 0) {

						System.out
								.println("### se genera un nuevo archivo XML");
						CargaMasiva carga = new CargaMasiva();
						carga.getRenovacionReduccion().addAll(listaCorrectos);
						byte[] bytes = convertXMLObjetc(carga);

						ArchivoTO archivoN = new ArchivoTO();
						archivoN.setAlgoritoHash(getSha1FromFile(bytes));
						archivoN.setArchivo(bytes);
						archivoN.setDescripcion("Archivo nuevo de carga masiva del usuario : "
								+ usuario.getNombre()
								+ ", con el id :"
								+ usuario.getPersona().getIdPersona()
								+ ", resultado de una carga que contenia archivos incorrectos");
						archivoN.setIdUsuario(usuario.getPersona()
								.getIdPersona());
						archivoN.setNombreArchivo("cmResnuevo");
						archivoN.setTipoArchivo("xml");
						idArchivo = inscripcionService.insertArchivo(archivoN)
								.getResIntPl();
						System.out
								.println("### se genero el nuevo archivo con el id :"
										+ idArchivo);
					}

				}

				if (listaTramites.size() > 0) {
					setListaTramites(listaTramites);
					masivaProcess.setIdArchivoFirma(idArchivo);
					masivaDAO.actualizaProcessCargaIdFirma(masivaProcess);
					Integer idUsuario = usuario.getPersona().getIdPersona();
					FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
					firmaMasivaTO.setIdUsuario(idUsuario);
					firmaMasivaTO.setIdArchivo(idArchivo);
					firmaMasivaTO.setIdAcreedor(idAcreedor);
					String tramites = listToString(getListaTramites());
					firmaMasivaTO.setTramites(tramites);
					FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
					idTramiteTempFirma = firmaDao.crearFirmaMasiva(firmaMasivaTO);
				}
				regresa = "resultado";

			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			} catch (JAXBException e) {

				e.printStackTrace();
			} catch (Exception e) {

				e.printStackTrace();
			}
			for (Integer tramiteErroneo : listaTramitesErrores) {
				if(tramiteErroneo!=null){
					inscripcionDAO.bajaTramiteIncompleto(tramiteErroneo);
				}
			}

		}
		return regresa;
	}
	
	private ControlError agregaRenovacion(RenovacionReduccion renovacion,Tramite tramite) {
		ControlError regresa = null;
		PlSql plSql =null;
		mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcionRes = new mx.gob.se.rug.masiva.resultado.to.TramiteRes();
		if (regresa == null) {
			
			try {
				masivaService.verifyAcreedorOfGarantia(tramite.getIdAcreedor(), renovacion.getIdentificadorGarantia().getIdGarantia().intValue());
				plSql =new PlSql();
				plSql.setIntPl(0);
			} catch (InfrastructureException e) {
				plSql =new PlSql();
				plSql.setIntPl(999);
				plSql.setResIntPl(999);
				plSql.setStrPl(e.getMessage());
				e.printStackTrace();
				
			} catch (CargaMasivaException e) {
				plSql =new PlSql();
				plSql.setIntPl(999);
				plSql.setResIntPl(999);
				plSql.setStrPl(e.getMessage());
			}
			
			if (plSql.getIntPl().intValue() == 0) {
			
			
				plSql = masivaDAO.executeAltaProrrogaMasiva(tramite,renovacion);
				
				if (plSql.getIntPl().intValue() == 0) {
					tramite.setIdTramite(plSql.getResIntPl());
					listaTramites.add(tramite.getIdTramite());
					inscripcionRes
							.setMensajeError("La renovacion o reduccion fue procesada correctamente");
					inscripcionRes.setClaveRastreo(renovacion
							.getIdentificadorGarantia().getClaveRastreo());
					inscripcionRes.setCodigoError("0");
					resultado.getTramites().getTramite().add(inscripcionRes);
					regresa = null;
				} else {
					listaTramitesErrores.add(tramite.getIdTramite());
					inscripcionRes.setMensajeError(plSql.getStrPl());
					inscripcionRes.setClaveRastreo(renovacion
							.getIdentificadorGarantia().getClaveRastreo());
					inscripcionRes.setCodigoError(plSql.getIntPl().toString());
					inscripcionesErroneas.add(inscripcionRes);
					resultado.getTramites().getTramite().add(inscripcionRes);
				}
			} else {
				listaTramitesErrores.add(tramite.getIdTramite());
				inscripcionRes.setMensajeError(plSql.getStrPl());
				inscripcionRes.setClaveRastreo(renovacion
						.getIdentificadorGarantia().getClaveRastreo());
				inscripcionRes.setCodigoError(plSql.getIntPl().toString());
				inscripcionesErroneas.add(inscripcionRes);
				resultado.getTramites().getTramite().add(inscripcionRes);
			}
		
		}/*else{
			regresa.setClaveRastreo(renovacion.getIdentificadorGarantia().getClaveRastreo());
		}*/
		return regresa;
	}
	
	private ControlError agregaCancelacion(Cancelacion cancelacion,
			Tramite tramite) {
		ControlError regresa = null;
		PlSql plSql = null;
		mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcionRes = new mx.gob.se.rug.masiva.resultado.to.TramiteRes();
		//regresa = verificaDatosCancelacion(cancelacion, tramite, inscripcionRes);
		//if (regresa == null) {
		try {
			masivaService.verifyAcreedorOfGarantia(tramite.getIdAcreedor(), cancelacion.getIdentificadorGarantia().getIdGarantia().intValue());
			
			plSql =new PlSql();
			plSql.setIntPl(0);
		} catch (InfrastructureException e) {
			plSql =new PlSql();
			plSql.setIntPl(999);
			plSql.setResIntPl(999);
			plSql.setStrPl(e.getMessage());
			e.printStackTrace();
			
		} catch (CargaMasivaException e) {
			plSql =new PlSql();
			plSql.setIntPl(999);
			plSql.setResIntPl(999);
			plSql.setStrPl(e.getMessage());
		}
		
		if (plSql.getIntPl().intValue() == 0) {
		
			plSql = masivaDAO.executeAltaCancelacionMasiva(tramite,	cancelacion);
			
			if (plSql.getIntPl().intValue() == 0) {
				listaTramites.add(plSql.getResIntPl());
				inscripcionRes
						.setMensajeError("La cancelacion fue procesada correctamente");
				inscripcionRes.setClaveRastreo(cancelacion.getIdentificadorGarantia()
						.getClaveRastreo());
				inscripcionRes.setCodigoError("0");
				resultado.getTramites().getTramite().add(inscripcionRes);
				regresa = null;
			} else {
				listaTramitesErrores.add(plSql.getResIntPl().intValue());
				inscripcionRes.setMensajeError(plSql.getStrPl());
				inscripcionRes.setClaveRastreo(cancelacion.getIdentificadorGarantia()
						.getClaveRastreo());
				inscripcionRes.setCodigoError(plSql.getIntPl().toString());
				inscripcionesErroneas.add(inscripcionRes);
				resultado.getTramites().getTramite().add(inscripcionRes);
			}
		} else {
			listaTramitesErrores.add(plSql.getResIntPl().intValue());
			inscripcionRes.setMensajeError(plSql.getStrPl());
			inscripcionRes.setClaveRastreo(cancelacion.getIdentificadorGarantia()
					.getClaveRastreo());
			inscripcionRes.setCodigoError(plSql.getIntPl().toString());
			inscripcionesErroneas.add(inscripcionRes);
			resultado.getTramites().getTramite().add(inscripcionRes);
		}
		//}
//		if (regresa != null) {
//			regresa.setClaveRastreo(tramite.getClaveRastreo());
//		}
		return regresa;
	}
	
	private ControlError agregaEjecucion(Ejecucion ejecucion,
			Tramite tramite) {
		ControlError regresa = null;
		PlSql plSql = null;
		mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcionRes = new mx.gob.se.rug.masiva.resultado.to.TramiteRes();
		//regresa = verificaDatosCancelacion(cancelacion, tramite, inscripcionRes);
		//if (regresa == null) {
		try {
			masivaService.verifyAcreedorOfGarantia(tramite.getIdAcreedor(), ejecucion.getIdentificadorGarantia().getIdGarantia().intValue());
			
			plSql =new PlSql();
			plSql.setIntPl(0);
		} catch (InfrastructureException e) {
			plSql =new PlSql();
			plSql.setIntPl(999);
			plSql.setResIntPl(999);
			plSql.setStrPl(e.getMessage());
			e.printStackTrace();
			
		} catch (CargaMasivaException e) {
			plSql =new PlSql();
			plSql.setIntPl(999);
			plSql.setResIntPl(999);
			plSql.setStrPl(e.getMessage());
		}
		
		if (plSql.getIntPl().intValue() == 0) {
		
			plSql = masivaDAO.executeAltaEjecucionMasiva(tramite,ejecucion);
			
			if (plSql.getIntPl().intValue() == 0) {
				listaTramites.add(plSql.getResIntPl());
				inscripcionRes
						.setMensajeError("La ejecucion fue procesada correctamente");
				inscripcionRes.setClaveRastreo(ejecucion.getIdentificadorGarantia()
						.getClaveRastreo());
				inscripcionRes.setCodigoError("0");
				resultado.getTramites().getTramite().add(inscripcionRes);
				regresa = null;
			} else {
				listaTramitesErrores.add(plSql.getResIntPl().intValue());
				inscripcionRes.setMensajeError(plSql.getStrPl());
				inscripcionRes.setClaveRastreo(ejecucion.getIdentificadorGarantia()
						.getClaveRastreo());
				inscripcionRes.setCodigoError(plSql.getIntPl().toString());
				inscripcionesErroneas.add(inscripcionRes);
				resultado.getTramites().getTramite().add(inscripcionRes);
			}
		} else {
			listaTramitesErrores.add(plSql.getResIntPl().intValue());
			inscripcionRes.setMensajeError(plSql.getStrPl());
			inscripcionRes.setClaveRastreo(ejecucion.getIdentificadorGarantia()
					.getClaveRastreo());
			inscripcionRes.setCodigoError(plSql.getIntPl().toString());
			inscripcionesErroneas.add(inscripcionRes);
			resultado.getTramites().getTramite().add(inscripcionRes);
		}
		//}
//		if (regresa != null) {
//			regresa.setClaveRastreo(tramite.getClaveRastreo());
//		}
		return regresa;
	}
	
//	private ControlError verificaDatosCancelacion(Cancelacion cancelacion,
//			Tramite tramite,
//			mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcionRes) {
//		ControlError regresa = null;
//
//		if (cancelacion.getPersonaSolicitaAutoridadInstruyeAsiento() == null) {
//			
//		} else {
//			inscripcionRes
//					.setMensajeError("Los datos de cancelacion son obligatorios, y no puede ir vacio. ");
//			inscripcionRes.setClaveRastreo(tramite.getClaveRastreo());
//			inscripcionRes.setCodigoError("501");
//			regresa = new ControlError();
//			PlSql plSql = new PlSql();
//			plSql.setResStrPl(inscripcionRes.getMensajeError());
//			plSql.setStrPl(inscripcionRes.getCodigoError());
//			plSql.setIntPl(501);
//			regresa.setPlSql(plSql);
//			regresa.setIdInscripcion(tramite.getConsecutivo());
//		}
//		if (regresa != null) {
//			resultado.getTramites().getTramite().add(inscripcionRes);
//			regresa = new ControlError();
//			PlSql plSql = new PlSql();
//			plSql.setResStrPl(inscripcionRes.getCodigoError());
//			plSql.setStrPl(inscripcionRes.getCodigoError());
//			regresa.setPlSql(plSql);
//			regresa.setIdInscripcion(tramite.getConsecutivo());
//		}
//		return regresa;
//	}
	
	// cancelacion codigos de error = 500
		public String cargaMasivaCancelacion(CargaMasiva cm,AcreedorTO acreedor) {
			String regresa = "failed";
			Integer idArchivo= masivaProcess.getIdArchivo();
			System.out.println("entro a carga masiva cancelacion");
			int consecutivo = 1;
			Tramite tramite;
			resultado = new ResultadoCargaMasiva();
			resultado.setTramites(new Tramites());
			this.listaTramites = new ArrayList<Integer>();
			listaErrores = new ArrayList<ControlError>();
			try {				
				acreedor = inscripcionService.getAcreedorByID(masivaProcess.getIdAcreedor());
				for (Cancelacion cancelacion : cm.getCancelacion()) {
					tramite = new Tramite();
					tramite.setIdArchivo(idArchivo);
					tramite.setIdUsuario(masivaProcess.getIdUsuario());
					tramite.setConsecutivo(consecutivo++);
					try {
						tramite.setIdGarantia(Integer.valueOf(cancelacion
								.getIdentificadorGarantia().getIdGarantia().intValue()));
					} catch (Exception e) {
						tramite.setIdGarantia(0);
					}
					tramite.setIdTipoTramite(4);
					tramite.setIdEstatus(5);
					tramite.setIdAcreedor(masivaProcess.getIdAcreedor());
					tramite.setClaveRastreo(cancelacion.getIdentificadorGarantia()
							.getClaveRastreo());
					
					cerror = agregaCancelacion(cancelacion, tramite);
						if (cerror != null) {
							listaTramitesErrores.add(cerror.getIdInscripcion());
							listaErrores.add(cerror);
						
					} else {
						System.out.println("cancelacion exitosa");
					}
				}
			} catch (Exception e) {
				java.util.Date date = new java.util.Date();
				Resumen resumen = new Resumen();
				resumen.setMensajeError("Sucedio un error en el sistema la fecha :"
						+ date.toString() + ", enviar el XML a soporte. codError["
						+ date.getTime() + "]");
				resultado.setResumen(resumen);
				System.out.println("ThreadCargaMasiva:::archivo [" + idArchivo
						+ "]::: Sucedio un error el proceso:::");
				e.printStackTrace();
			} finally {
				Resumen resumen = getResumen(this.cargaMasivaPreProcesed);					
				resultado.setResumen(resumen);
				try {
					tramitesErroneos = listaTramitesErrores.size();
					
					
//					ArchivoTO archivoRes = new ArchivoTO();
//					byte[] bytes2 = convertXMLObjetc(resultado);
//					archivoRes.setAlgoritoHash(getSha1FromFile(bytes2));
//					archivoRes.setArchivo(bytes2);
//					archivoRes
//							.setDescripcion("Archivo nuevo de carga masiva de modificacion del usuario : "
//									+ nombreUsuario
//									+ ", con el id :"
//									+ masivaProcess.getIdUsuario()
//									+ ", resultado de una carga que contenia archivos incorrectos");
//					archivoRes.setIdUsuario(masivaProcess.getIdUsuario());
//					archivoRes.setNombreArchivo(nombreArchivo+"Resumen");
//					archivoRes.setTipoArchivo("xml");
//					setIdArchivoRes(inscripcionService.insertArchivo(archivoRes).getResIntPl());
//					masivaProcess.setIdArchivoResumen(getIdArchivoRes());
					
					masivaService.generaArchivoResumen(resultado, null, archivoTO, masivaProcess);


					if (listaTramitesErrores.size() > 0) {
						System.out.println("#### sucedieron errores en la carga");
						// crear nuevo archivo XML y guardarlo para el tramite de
						// firma
						List<Cancelacion> listaCorrectos = new ArrayList<Cancelacion>();
						for (mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcionRes : resultado
								.getTramites().getTramite()) {
							if (inscripcionRes.getCodigoError().equals("0")) {
								for (Cancelacion ins : cm.getCancelacion()) {
									if (ins.getIdentificadorGarantia()
											.getClaveRastreo()
											.trim()
											.equals(inscripcionRes
													.getClaveRastreo().trim())) {
										listaCorrectos.add(ins);
										break;
									}
								}
							}
						}

						if (listaCorrectos.size() > 0) {

							System.out.println("### se genera un nuevo archivo XML");
							CargaMasiva carga = new CargaMasiva();
							carga.getCancelacion().addAll(listaCorrectos);
							byte[] bytes = convertXMLObjetc(carga);

							ArchivoTO archivoN = new ArchivoTO();
							archivoN.setAlgoritoHash(getSha1FromFile(bytes));
							archivoN.setArchivo(bytes);
							archivoN.setDescripcion("Archivo nuevo de carga masiva del usuario : "
									+ nombreUsuario
									+ ", con el id :"
									+ masivaProcess.getIdUsuario()
									+ ", resultado de una carga que contenia archivos incorrectos");
							archivoN.setIdUsuario(masivaProcess.getIdUsuario());
							archivoN.setNombreArchivo(nombreArchivo+ "nuevo");
							archivoN.setTipoArchivo("xml");
							idArchivo = inscripcionService.insertArchivo(archivoN)
									.getResIntPl();
							System.out
									.println("### se genero el nuevo archivo con el id :"
											+ idArchivo);
						}

					}
					//sessionMap.put(Constants.TRAMITES, listaTramites);
					if (listaTramites.size() > 0) {
						/*idArchivo = (Integer) sessionMap.get(Constants.ID_ARCHIVO);
						archivoTO.setIdArchivo(idArchivo);*/
						masivaProcess.setIdArchivoFirma(idArchivo);
						masivaDAO.actualizaProcessCargaIdFirma(masivaProcess);
						Integer idAcreedor = masivaProcess.getIdAcreedor();
						Integer idUsuario = masivaProcess.getIdUsuario();
						FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
						firmaMasivaTO.setIdUsuario(idUsuario);
						firmaMasivaTO.setIdArchivo(idArchivo);
						firmaMasivaTO.setIdAcreedor(idAcreedor);
						String tramites = listToString(listaTramites);
						firmaMasivaTO.setTramites(tramites);
						FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
						int valor = firmaDao.crearFirmaMasiva(firmaMasivaTO);
						idTramiteTempFirma=valor;
						System.out
								.println("firmaDao.crearFirmaMasiva(firmaMasivaTO)--valor--"
										+ valor);						
					}
					regresa = "resultado";

				} catch (FileNotFoundException e) {

					System.out.println("ThreadCargaMasiva:::archivo [" + idArchivo
							+ "]::: sucedio un error faltal:::");
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {

					System.out.println("ThreadCargaMasiva:::archivo [" + idArchivo
							+ "]::: sucedio un error faltal:::");
					e.printStackTrace();
				} catch (JAXBException e) {

					System.out.println("ThreadCargaMasiva:::archivo [" + idArchivo
							+ "]::: sucedio un error faltal:::");
					e.printStackTrace();
				} catch (Exception e) {

					System.out.println("ThreadCargaMasiva:::archivo [" + idArchivo
							+ "]::: sucedio un error faltal:::");
					e.printStackTrace();
				}
				for (Integer tramiteErroneo : listaTramitesErrores) {
					inscripcionDAO.bajaTramiteIncompleto(tramiteErroneo);
				}
			}
			return regresa;
		}
		
		public String cargaMasivaEjecucion(CargaMasiva cm,AcreedorTO acreedor) {
			String regresa = "failed";
			Integer idArchivo= masivaProcess.getIdArchivo();
			System.out.println("entro a carga masiva ejecucion");
			int consecutivo = 1;
			Tramite tramite;
			resultado = new ResultadoCargaMasiva();
			resultado.setTramites(new Tramites());
			this.listaTramites = new ArrayList<Integer>();
			listaErrores = new ArrayList<ControlError>();
			try {				
				acreedor = inscripcionService.getAcreedorByID(masivaProcess.getIdAcreedor());
				for (Ejecucion ejecucion : cm.getEjecucion()) {
					tramite = new Tramite();
					tramite.setIdArchivo(idArchivo);
					tramite.setIdUsuario(masivaProcess.getIdUsuario());
					tramite.setConsecutivo(consecutivo++);
					try {
						tramite.setIdGarantia(Integer.valueOf(ejecucion
								.getIdentificadorGarantia().getIdGarantia().intValue()));
					} catch (Exception e) {
						tramite.setIdGarantia(0);
					}
					tramite.setIdTipoTramite(4);
					tramite.setIdEstatus(5);
					tramite.setIdAcreedor(masivaProcess.getIdAcreedor());
					tramite.setClaveRastreo(ejecucion.getIdentificadorGarantia()
							.getClaveRastreo());
					
					cerror = agregaEjecucion(ejecucion, tramite);
						if (cerror != null) {
							listaTramitesErrores.add(cerror.getIdInscripcion());
							listaErrores.add(cerror);
						
					} else {
						System.out.println("cancelacion exitosa");
					}
				}
			} catch (Exception e) {
				java.util.Date date = new java.util.Date();
				Resumen resumen = new Resumen();
				resumen.setMensajeError("Sucedio un error en el sistema la fecha :"
						+ date.toString() + ", enviar el XML a soporte. codError["
						+ date.getTime() + "]");
				resultado.setResumen(resumen);
				System.out.println("ThreadCargaMasiva:::archivo [" + idArchivo
						+ "]::: Sucedio un error el proceso:::");
				e.printStackTrace();
			} finally {
				Resumen resumen = getResumen(this.cargaMasivaPreProcesed);					
				resultado.setResumen(resumen);
				try {
					tramitesErroneos = listaTramitesErrores.size();
					
					
//					ArchivoTO archivoRes = new ArchivoTO();
//					byte[] bytes2 = convertXMLObjetc(resultado);
//					archivoRes.setAlgoritoHash(getSha1FromFile(bytes2));
//					archivoRes.setArchivo(bytes2);
//					archivoRes
//							.setDescripcion("Archivo nuevo de carga masiva de modificacion del usuario : "
//									+ nombreUsuario
//									+ ", con el id :"
//									+ masivaProcess.getIdUsuario()
//									+ ", resultado de una carga que contenia archivos incorrectos");
//					archivoRes.setIdUsuario(masivaProcess.getIdUsuario());
//					archivoRes.setNombreArchivo(nombreArchivo+"Resumen");
//					archivoRes.setTipoArchivo("xml");
//					setIdArchivoRes(inscripcionService.insertArchivo(archivoRes).getResIntPl());
//					masivaProcess.setIdArchivoResumen(getIdArchivoRes());
					
					masivaService.generaArchivoResumen(resultado, null, archivoTO, masivaProcess);


					if (listaTramitesErrores.size() > 0) {
						System.out.println("#### sucedieron errores en la carga");
						// crear nuevo archivo XML y guardarlo para el tramite de
						// firma
						List<Ejecucion> listaCorrectos = new ArrayList<Ejecucion>();
						for (mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcionRes : resultado
								.getTramites().getTramite()) {
							if (inscripcionRes.getCodigoError().equals("0")) {
								for (Ejecucion ins : cm.getEjecucion()) {
									if (ins.getIdentificadorGarantia()
											.getClaveRastreo()
											.trim()
											.equals(inscripcionRes
													.getClaveRastreo().trim())) {
										listaCorrectos.add(ins);
										break;
									}
								}
							}
						}

						if (listaCorrectos.size() > 0) {

							System.out.println("### se genera un nuevo archivo XML");
							CargaMasiva carga = new CargaMasiva();
							carga.getEjecucion().addAll(listaCorrectos);
							byte[] bytes = convertXMLObjetc(carga);

							ArchivoTO archivoN = new ArchivoTO();
							archivoN.setAlgoritoHash(getSha1FromFile(bytes));
							archivoN.setArchivo(bytes);
							archivoN.setDescripcion("Archivo nuevo de carga masiva del usuario : "
									+ nombreUsuario
									+ ", con el id :"
									+ masivaProcess.getIdUsuario()
									+ ", resultado de una carga que contenia archivos incorrectos");
							archivoN.setIdUsuario(masivaProcess.getIdUsuario());
							archivoN.setNombreArchivo(nombreArchivo+ "nuevo");
							archivoN.setTipoArchivo("xml");
							idArchivo = inscripcionService.insertArchivo(archivoN)
									.getResIntPl();
							System.out
									.println("### se genero el nuevo archivo con el id :"
											+ idArchivo);
						}

					}
					//sessionMap.put(Constants.TRAMITES, listaTramites);
					if (listaTramites.size() > 0) {
						/*idArchivo = (Integer) sessionMap.get(Constants.ID_ARCHIVO);
						archivoTO.setIdArchivo(idArchivo);*/
						masivaProcess.setIdArchivoFirma(idArchivo);
						masivaDAO.actualizaProcessCargaIdFirma(masivaProcess);
						Integer idAcreedor = masivaProcess.getIdAcreedor();
						Integer idUsuario = masivaProcess.getIdUsuario();
						FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
						firmaMasivaTO.setIdUsuario(idUsuario);
						firmaMasivaTO.setIdArchivo(idArchivo);
						firmaMasivaTO.setIdAcreedor(idAcreedor);
						String tramites = listToString(listaTramites);
						firmaMasivaTO.setTramites(tramites);
						FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
						int valor = firmaDao.crearFirmaMasiva(firmaMasivaTO);
						idTramiteTempFirma=valor;
						System.out
								.println("firmaDao.crearFirmaMasiva(firmaMasivaTO)--valor--"
										+ valor);						
					}
					regresa = "resultado";

				} catch (FileNotFoundException e) {

					System.out.println("ThreadCargaMasiva:::archivo [" + idArchivo
							+ "]::: sucedio un error faltal:::");
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {

					System.out.println("ThreadCargaMasiva:::archivo [" + idArchivo
							+ "]::: sucedio un error faltal:::");
					e.printStackTrace();
				} catch (JAXBException e) {

					System.out.println("ThreadCargaMasiva:::archivo [" + idArchivo
							+ "]::: sucedio un error faltal:::");
					e.printStackTrace();
				} catch (Exception e) {

					System.out.println("ThreadCargaMasiva:::archivo [" + idArchivo
							+ "]::: sucedio un error faltal:::");
					e.printStackTrace();
				}
				for (Integer tramiteErroneo : listaTramitesErrores) {
					inscripcionDAO.bajaTramiteIncompleto(tramiteErroneo);
				}
			}
			return regresa;
		}
	
	private Resumen getResumen(CargaMasivaPreProcesed cMasivaPreProcesed){
		java.util.Date date = new java.util.Date();
		int tramitesIncorrectosFiltro = getTramitesIncorrectosFiltro(cMasivaPreProcesed);
		
		Resumen resumen = new Resumen();
		resumen.setMensajeError("Archivo generado en la fecha :" + date.toString());
		resumen.setCorrectos(String.valueOf(listaTramites.size()));
		resumen.setErroneos(String.valueOf(listaTramitesErrores.size() + tramitesIncorrectosFiltro));
		//resumen.setNumeroRegistros(String.valueOf(listaTramites.size() + listaTramitesErrores.size()));
		resumen.setNumeroRegistros(String.valueOf(cMasivaPreProcesed.getTotalTramites()));
		return resumen;
	}
	private Resumen getResumenFiltroError(CargaMasivaPreProcesed cMasivaPreProcesed){
		java.util.Date date = new java.util.Date();
		int tramitesIncorrectosFiltro = cMasivaPreProcesed.getTramiteIncorrectos().size();
		
		Resumen resumen = new Resumen();
		resumen.setMensajeError("Archivo generado en la fecha :" + date.toString());
		resumen.setCorrectos(String.valueOf(0));
		resumen.setErroneos(String.valueOf(tramitesIncorrectosFiltro));
		resumen.setNumeroRegistros(String.valueOf(tramitesIncorrectosFiltro));
		return resumen;
	}
	
	private Integer getTramitesIncorrectosFiltro(CargaMasivaPreProcesed cMasivaPreProcesed){
		int contTramitesIncorrectos = 0;
		if(cMasivaPreProcesed.getTramiteIncorrectos() != null && cMasivaPreProcesed.getTramiteIncorrectos().size() > 0){
			contTramitesIncorrectos = cMasivaPreProcesed.getTramiteIncorrectos().size();
			resultado.getTramites().getTramite().addAll(cMasivaPreProcesed.getTramiteIncorrectos());
		}
		return contTramitesIncorrectos;
	}
	
	public byte[] convertXMLObjetc(Object obj) throws JAXBException,
		FileNotFoundException, UnsupportedEncodingException {
		JAXBContext context = JAXBContext.newInstance(obj.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
		StringWriter stringWriter = new StringWriter();
		marshaller.marshal(obj, stringWriter);
		return stringWriter.toString().getBytes("ISO-8859-1");
	}
	
	private String getDigest(byte[] attributes) {
		String digestivo = null;
		if (attributes == null) {
			return null;
		}
		try {
			java.security.MessageDigest messageDigest = java.security.MessageDigest
					.getInstance("SHA-1");
			byte[] digest = messageDigest.digest(attributes);
			digestivo = new String(
					org.apache.commons.codec.binary.Base64.encodeBase64(digest));
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return digestivo;
	}
	
	private String listToString(List<Integer> lista) {
		String regresa = "";
		Iterator<Integer> it = lista.iterator();
		if (it.hasNext()) {
			regresa += it.next();
		}
		while (it.hasNext()) {
			regresa += ("|" + it.next());
		}
		return regresa;
	}
	
	// inscripciones
	public String cargaMasivaInscripcion(CargaMasiva cm, AcreedorTO acreedor) {
		System.out.println("--- cargaMasivaInscripcion");
		String regresa = "failed";
		Integer idArchivo = masivaProcess.getIdArchivo();
		this.listaTramites = new ArrayList<Integer>();
		mx.gob.se.rug.masiva.resultado.to.Tramites inscripciones = new mx.gob.se.rug.masiva.resultado.to.Tramites();
		resultado = new ResultadoCargaMasiva();
		resultado.setTramites(inscripciones);
		
		UsuarioTO usuario= new UsuarioTO();
		PersonaTO personaTO= new PersonaTO();
		personaTO.setIdPersona(masivaProcess.getIdUsuario());
		usuario.setPersona(personaTO);
		
		listaErrores = listaErrores == null ? new ArrayList<ControlError>()	: listaErrores;
		
		try {				
			acreedor = new AcreedorTO();
			acreedor = acreedoresService.getAcreRepById(masivaProcess.getIdAcreedor());			
						
			try {
				System.out.println("ThreadCargaMasiva:::archivo [" + masivaProcess.getIdArchivo()
						+ "]::: Inicia el proceso::");
				if (cm.getInscripcion().size() > 0) {
					int consecutivo = 0;
					for (Inscripcion inscripcion : cm.getInscripcion()) {
						cerror = agregaInscripcion(inscripcion, usuario, acreedor,
								consecutivo++, masivaProcess.getIdArchivo(), 1);
						if (cerror != null) {
							listaErrores.add(cerror);
							System.out
									.println("ThreadCargaMasiva:::archivo ["
											+ masivaProcess.getIdArchivo()
											+ "]::: sucedio un error en la isncripcion ::"
											+ cerror.getClaveRastreo());
						}
					}
				}
			} catch (Exception e) {
				java.util.Date date = new java.util.Date();
				Resumen resumen = new Resumen();
				resumen.setMensajeError("Sucedio un error en el sistema la fecha :"
						+ date.toString()
						+ ", enviar el XML a soporte. codError["
						+ date.getTime() + "]");
				resultado.setResumen(resumen);
				System.out.println("ThreadCargaMasiva:::archivo [" + masivaProcess.getIdArchivo()
						+ "]::: Sucedio un error el proceso:::");
				e.printStackTrace();
			} finally {
				try {
					Resumen resumen = getResumen(this.cargaMasivaPreProcesed);					
					resultado.setResumen(resumen);
					
					masivaService.generaArchivoResumen(resultado, null, archivoTO, masivaProcess);
					
					if (listaTramitesErrores.size() > 0) {
						System.out
								.println("#### sucedieron errores en la carga");
						// crear nuevo archivo XML y guardarlo para el tramite
						// de firma
						List<Inscripcion> listaCorrectos = new ArrayList<Inscripcion>();
						for (mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcionRes : resultado
								.getTramites().getTramite()) {
							if (inscripcionRes.getCodigoError().equals("0")) {
								for (Inscripcion ins : cm.getInscripcion()) {
									if (ins.getIdentificador()
											.getClaveRastreo()
											.trim()
											.equals(inscripcionRes
													.getClaveRastreo().trim())) {
										listaCorrectos.add(ins);
										break;
									}
								}
							}
						}

						if (listaCorrectos.size() > 0) {

							System.out.println("### se genera un nuevo archivo XML, solo con los correctos.");
							CargaMasiva carga = new CargaMasiva();
							carga.getInscripcion().addAll(listaCorrectos);
							byte[] bytes = convertXMLObjetc(carga);

							ArchivoTO archivoN = new ArchivoTO();
							archivoN.setAlgoritoHash(getSha1FromFile(bytes));
							archivoN.setArchivo(bytes);
							archivoN.setDescripcion("Archivo nuevo de carga masiva del usuario : "
									+ nombreUsuario
									+ ", con el id :"
									+ masivaProcess.getIdUsuario()
									+ ", resultado de una carga que contenia archivos incorrectos");
							archivoN.setIdUsuario(masivaProcess.getIdUsuario());
							archivoN.setNombreArchivo(nombreArchivo + "nuevo");
							archivoN.setTipoArchivo("xml");
							idArchivo = inscripcionService.insertArchivo(archivoN).getResIntPl();
							System.out.println("### se genero el nuevo archivo con el id :"+ idArchivo);
						}

					}

					if (listaTramites.size() > 0) {
						//@SuppressWarnings("unchecked")
						
						//List<Integer> listaTramites2 = (List<Integer>) sessionMap.get(Constants.TRAMITES);
						masivaProcess.setIdArchivoFirma(idArchivo);
						masivaDAO.actualizaProcessCargaIdFirma(masivaProcess);
						//setListaTramites(listaTramites2);
						setListaTramites(this.listaTramites);
						//idArchivo = (Integer) sessionMap.get(Constants.ID_ARCHIVO);
						//archivoTO.setIdArchivo(idArchivo);
						Integer idUsuario = masivaProcess.getIdUsuario();
						FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
						firmaMasivaTO.setIdUsuario(idUsuario);
						firmaMasivaTO.setIdArchivo(idArchivo);
						firmaMasivaTO.setIdAcreedor(masivaProcess.getIdAcreedor());
						String tramites = listToString(getListaTramites());
						firmaMasivaTO.setTramites(tramites);
						FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
						int valor = firmaDao.crearFirmaMasiva(firmaMasivaTO);	
						idTramiteTempFirma=valor;
					}

					regresa = "resultado";
				} catch (FileNotFoundException e) {

					System.out.println("ThreadCargaMasiva:::archivo ["
							+ idArchivo + "]::: sucedio un error faltal:::");
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {

					System.out.println("ThreadCargaMasiva:::archivo ["
							+ idArchivo + "]::: sucedio un error faltal:::");
					e.printStackTrace();
				} catch (JAXBException e) {

					System.out.println("ThreadCargaMasiva:::archivo ["
							+ idArchivo + "]::: sucedio un error faltal:::");
					e.printStackTrace();
				} catch (Exception e) {

					System.out.println("ThreadCargaMasiva:::archivo ["
							+ idArchivo + "]::: sucedio un error faltal:::");
					e.printStackTrace();
				}
			}
		} catch (Exception e) {

			setErrorArchivo("sucedio un error con el procesamiento del archivo revisar con el siguiente identificados: DAMARCH"
					+ idArchivo + " Message::" + e.getMessage());
		} finally {
			InscripcionDAO inscripcionDAO = new InscripcionDAO();
			setTramitesErroneos(listaTramitesErrores.size());
			setTotalTramites(""
					+ (listaTramitesErrores.size() + listaTramites.size()));
			for (Integer tramiteErroneo : listaTramitesErrores) {
				inscripcionDAO.bajaTramiteIncompleto(tramiteErroneo);
			}

		}
		System.out.println("--- cargaMasivaInscripcion ---" + regresa);
		return regresa;
	}
	
	// traslados
		public String cargaMasivaTraslado(CargaMasiva cm, AcreedorTO acreedor) {
			System.out.println("--- cargaMasivaTraslado");
			String regresa = "failed";
			Integer idArchivo = masivaProcess.getIdArchivo();
			this.listaTramites = new ArrayList<Integer>();
			mx.gob.se.rug.masiva.resultado.to.Tramites inscripciones = new mx.gob.se.rug.masiva.resultado.to.Tramites();
			resultado = new ResultadoCargaMasiva();
			resultado.setTramites(inscripciones);
			
			UsuarioTO usuario= new UsuarioTO();
			PersonaTO personaTO= new PersonaTO();
			personaTO.setIdPersona(masivaProcess.getIdUsuario());
			usuario.setPersona(personaTO);
			
			listaErrores = listaErrores == null ? new ArrayList<ControlError>()	: listaErrores;
			
			try {				
				acreedor = new AcreedorTO();
				acreedor = acreedoresService.getAcreRepById(masivaProcess.getIdAcreedor());			
							
				try {
					System.out.println("ThreadCargaMasiva:::archivo [" + masivaProcess.getIdArchivo()
							+ "]::: Inicia el proceso::");
					if (cm.getTraslado().size() > 0) {
						int consecutivo = 0;
						for (Inscripcion inscripcion : cm.getTraslado()) {
							cerror = agregaInscripcion(inscripcion, usuario, acreedor,
									consecutivo++, masivaProcess.getIdArchivo(), 31);
							if (cerror != null) {
								listaErrores.add(cerror);
								System.out
										.println("ThreadCargaMasiva:::archivo ["
												+ masivaProcess.getIdArchivo()
												+ "]::: sucedio un error en la isncripcion ::"
												+ cerror.getClaveRastreo());
							}
						}
					}
				} catch (Exception e) {
					java.util.Date date = new java.util.Date();
					Resumen resumen = new Resumen();
					resumen.setMensajeError("Sucedio un error en el sistema la fecha :"
							+ date.toString()
							+ ", enviar el XML a soporte. codError["
							+ date.getTime() + "]");
					resultado.setResumen(resumen);
					System.out.println("ThreadCargaMasiva:::archivo [" + masivaProcess.getIdArchivo()
							+ "]::: Sucedio un error el proceso:::");
					e.printStackTrace();
				} finally {
					try {
						Resumen resumen = getResumen(this.cargaMasivaPreProcesed);					
						resultado.setResumen(resumen);
						
						masivaService.generaArchivoResumen(resultado, null, archivoTO, masivaProcess);
						
						if (listaTramitesErrores.size() > 0) {
							System.out
									.println("#### sucedieron errores en la carga");
							// crear nuevo archivo XML y guardarlo para el tramite
							// de firma
							List<Inscripcion> listaCorrectos = new ArrayList<Inscripcion>();
							for (mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcionRes : resultado
									.getTramites().getTramite()) {
								if (inscripcionRes.getCodigoError().equals("0")) {
									for (Inscripcion ins : cm.getTraslado()) {
										if (ins.getIdentificador()
												.getClaveRastreo()
												.trim()
												.equals(inscripcionRes
														.getClaveRastreo().trim())) {
											listaCorrectos.add(ins);
											break;
										}
									}
								}
							}

							if (listaCorrectos.size() > 0) {

								System.out.println("### se genera un nuevo archivo XML, solo con los correctos.");
								CargaMasiva carga = new CargaMasiva();
								carga.getInscripcion().addAll(listaCorrectos);
								byte[] bytes = convertXMLObjetc(carga);

								ArchivoTO archivoN = new ArchivoTO();
								archivoN.setAlgoritoHash(getSha1FromFile(bytes));
								archivoN.setArchivo(bytes);
								archivoN.setDescripcion("Archivo nuevo de carga masiva del usuario : "
										+ nombreUsuario
										+ ", con el id :"
										+ masivaProcess.getIdUsuario()
										+ ", resultado de una carga que contenia archivos incorrectos");
								archivoN.setIdUsuario(masivaProcess.getIdUsuario());
								archivoN.setNombreArchivo(nombreArchivo + "nuevo");
								archivoN.setTipoArchivo("xml");
								idArchivo = inscripcionService.insertArchivo(archivoN).getResIntPl();
								System.out.println("### se genero el nuevo archivo con el id :"+ idArchivo);
							}

						}

						if (listaTramites.size() > 0) {
							//@SuppressWarnings("unchecked")
							
							//List<Integer> listaTramites2 = (List<Integer>) sessionMap.get(Constants.TRAMITES);
							masivaProcess.setIdArchivoFirma(idArchivo);
							masivaDAO.actualizaProcessCargaIdFirma(masivaProcess);
							//setListaTramites(listaTramites2);
							setListaTramites(this.listaTramites);
							//idArchivo = (Integer) sessionMap.get(Constants.ID_ARCHIVO);
							//archivoTO.setIdArchivo(idArchivo);
							Integer idUsuario = masivaProcess.getIdUsuario();
							FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
							firmaMasivaTO.setIdUsuario(idUsuario);
							firmaMasivaTO.setIdArchivo(idArchivo);
							firmaMasivaTO.setIdAcreedor(masivaProcess.getIdAcreedor());
							String tramites = listToString(getListaTramites());
							firmaMasivaTO.setTramites(tramites);
							FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
							int valor = firmaDao.crearFirmaMasiva(firmaMasivaTO);	
							idTramiteTempFirma=valor;
						}

						regresa = "resultado";
					} catch (FileNotFoundException e) {

						System.out.println("ThreadCargaMasiva:::archivo ["
								+ idArchivo + "]::: sucedio un error faltal:::");
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {

						System.out.println("ThreadCargaMasiva:::archivo ["
								+ idArchivo + "]::: sucedio un error faltal:::");
						e.printStackTrace();
					} catch (JAXBException e) {

						System.out.println("ThreadCargaMasiva:::archivo ["
								+ idArchivo + "]::: sucedio un error faltal:::");
						e.printStackTrace();
					} catch (Exception e) {

						System.out.println("ThreadCargaMasiva:::archivo ["
								+ idArchivo + "]::: sucedio un error faltal:::");
						e.printStackTrace();
					}
				}
			} catch (Exception e) {

				setErrorArchivo("sucedio un error con el procesamiento del archivo revisar con el siguiente identificados: DAMARCH"
						+ idArchivo + " Message::" + e.getMessage());
			} finally {
				InscripcionDAO inscripcionDAO = new InscripcionDAO();
				setTramitesErroneos(listaTramitesErrores.size());
				setTotalTramites(""
						+ (listaTramitesErrores.size() + listaTramites.size()));
				for (Integer tramiteErroneo : listaTramitesErrores) {
					inscripcionDAO.bajaTramiteIncompleto(tramiteErroneo);
				}

			}
			System.out.println("--- cargaMasivaInscripcion ---" + regresa);
			return regresa;
		}
	
	private ControlError agregaInscripcion(Inscripcion inscripcion,
			UsuarioTO usuario, AcreedorTO acreedor, Integer numInscripcion,
			Integer idArchivo, Integer idTipoTramite) {
		ControlError regresa = null;
		String claveRastreo = "No identificada";
		TramiteRes tramite = new TramiteRes();
		JuezDAO autoridad= new JuezDAO();

		try {
			Integer idAcreedor = Integer.valueOf(acreedor.getIdAcreedor());
			InscripcionTO inscripcionTO = new InscripcionTO();
			inscripcionTO.setIdTipoTramite(idTipoTramite);
			inscripcionTO.setIdPersona(usuario.getPersona().getIdPersona());
			InscripcionDAO inscripcionDAO = new InscripcionDAO();

			regresa = revisaGarantia(inscripcion, tramite, numInscripcion);
			if (regresa == null) {
				PlSql plIns = inscripcionDAO.insertInscripcionClaveRastreo(
						inscripcionTO, acreedor, inscripcion.getIdentificador()
								.getClaveRastreo(), idArchivo);
				claveRastreo = inscripcion.getIdentificador().getClaveRastreo();
				if (plIns.getIntPl().intValue() == 0) {
					Tramite tramite2 = new Tramite();
					tramite2.setIdTramite(plIns.getResIntPl().intValue());
					tramite2.setIdAcreedor(idAcreedor);
					tramite2.setIdUsuario(usuario.getPersona().getIdPersona());
					tramite2.setConsecutivo(numInscripcion);	
				
					
					autoridad.insertAutoridadInstruyeAsiento(tramite2.getIdTramite(), inscripcion.getPersonaSolicitaAutoridadInstruyeAsiento());
					
					regresa = agregaOtorgante(inscripcion.getPartes().getOtorgante(), tramite2, tramite);
					if (regresa == null) {
						regresa = agregarDeudores(inscripcion, usuario.getPersona().getIdPersona(),
								idAcreedor, plIns.getResIntPl().intValue(),
								numInscripcion);
						if (regresa == null) {
							if(inscripcion.getPartes().getAcreedorAdicional().size()>0){
								
								masivaService.addAcreedorAdicional(inscripcion.getPartes().getAcreedorAdicional(),usuario,tramite2);
							}
							if (regresa == null) {
								/** carga bienes especiales**/
								regresa = agregarBienesEspeciales(inscripcion, usuario.getPersona().getIdPersona(),
										idAcreedor, plIns.getResIntPl().intValue(),
										numInscripcion);
								
								if(regresa == null) {
									regresa = agregaGarantia(inscripcion,
											usuario.getPersona().getIdPersona(), idAcreedor, plIns
													.getResIntPl().intValue(),
											numInscripcion);
									if (regresa.getPlSql().getIntPl().intValue() == 0) {
										GarantiasDAO dao = new GarantiasDAO();
										inscripcionTO.setGarantiaTO(garantiaToGarantiaTO(inscripcion.getGarantia()));
										inscripcionTO.getGarantiaTO().setIdGarantia(regresa.getPlSql().getResIntPl());
										inscripcionTO.setMeses(inscripcion.getVigencia().getMeses().toString());
										if (dao.actualizaMeses(inscripcionTO)) {
											if (dao.altaBitacoraTramite(
													new Integer(plIns.getResIntPl()
															.intValue()),
													new Integer(5), new Integer(3),
													null, "V")) {
												listaTramites.add(plIns.getResIntPl().intValue());
												tramite.setMensajeError("La inscripcion fue procesada correctamente");
												tramite.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
												tramite.setCodigoError("0");
												resultado.getTramites().getTramite().add(tramite);
												regresa = null;
											} else {
												listaTramitesErrores.add(plIns
														.getResIntPl().intValue());
												regresa = new ControlError();
												tramite.setMensajeError("La inscripcion no se pudo dar de alta en la bitacora");
												tramite.setClaveRastreo(inscripcion
														.getIdentificador()
														.getClaveRastreo());
												tramite.setCodigoError("1");
												inscripcionesErroneas.add(tramite);
												resultado.getTramites()
														.getTramite().add(tramite);
											}
	
										} else {
											listaTramitesErrores.add(plIns
													.getResIntPl().intValue());
											tramite.setMensajeError(regresa
													.getPlSql().getStrPl());
											tramite.setClaveRastreo(inscripcion
													.getIdentificador()
													.getClaveRastreo());
											tramite.setCodigoError(regresa
													.getPlSql().getIntPl()
													.toString());
											inscripcionesErroneas.add(tramite);
											resultado.getTramites().getTramite()
													.add(tramite);
										}
									} else {
										listaTramitesErrores.add(plIns
												.getResIntPl().intValue());
										tramite.setMensajeError(regresa.getPlSql()
												.getStrPl());
										tramite.setClaveRastreo(inscripcion
												.getIdentificador()
												.getClaveRastreo());
										tramite.setCodigoError(regresa.getPlSql()
												.getIntPl().toString());
										inscripcionesErroneas.add(tramite);
										resultado.getTramites().getTramite()
												.add(tramite);
									}
								} else {
									listaTramitesErrores.add(plIns
											.getResIntPl().intValue());
									tramite.setMensajeError(regresa.getPlSql()
											.getStrPl());
									tramite.setClaveRastreo(inscripcion
											.getIdentificador()
											.getClaveRastreo());
									tramite.setCodigoError(regresa.getPlSql()
											.getIntPl().toString());
									inscripcionesErroneas.add(tramite);
									resultado.getTramites().getTramite()
											.add(tramite);
								}
							} 
						} else {
							listaTramitesErrores.add(plIns.getResIntPl()
									.intValue());
							tramite.setMensajeError(regresa.getPlSql()
									.getStrPl());
							tramite.setClaveRastreo(inscripcion
									.getIdentificador().getClaveRastreo());
							tramite.setCodigoError(regresa.getPlSql()
									.getIntPl().toString());
							inscripcionesErroneas.add(tramite);
							resultado.getTramites().getTramite().add(tramite);
						}
					} else {
						listaTramitesErrores
								.add(plIns.getResIntPl().intValue());
						tramite.setMensajeError(regresa.getPlSql().getStrPl());
						tramite.setClaveRastreo(inscripcion.getIdentificador()
								.getClaveRastreo());
						tramite.setCodigoError("67");
						inscripcionesErroneas.add(tramite);
						resultado.getTramites().getTramite().add(tramite);
					}
				} else {
					listaTramitesErrores.add(plIns.getResIntPl().intValue());
					tramite.setMensajeError(plIns.getStrPl());
					tramite.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
					tramite.setCodigoError(plIns.getIntPl().toString());
					inscripcionesErroneas.add(tramite);
					resultado.getTramites().getTramite().add(tramite);
				}
			}
		} catch (CargaMasivaException e) {
			listaTramitesErrores.add(1);
			regresa = new ControlError();
			tramite=e.getTramiteIncorrecto(inscripcion.getIdentificador().getClaveRastreo());
			inscripcionesErroneas.add(tramite);
			resultado.getTramites().getTramite().add(tramite);
			e.printStackTrace();
		
		} catch (InfrastructureException e) {
			listaTramitesErrores.add(1);
			regresa = new ControlError();
			tramite.setCodigoError("999");
			tramite.setMensajeError(e.getMessage());
			tramite.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
			inscripcionesErroneas.add(tramite);
			resultado.getTramites().getTramite().add(tramite);
			e.printStackTrace();
		} catch (NoDataFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			regresa = new ControlError();
			tramite.setMensajeError("No se pudo generar la inscripcion");
			tramite.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
			tramite.setCodigoError("2");
			inscripcionesErroneas.add(tramite);
			resultado.getTramites().getTramite().add(tramite);
			e.printStackTrace();
		}
		if (regresa != null) {
			regresa.setClaveRastreo(claveRastreo);
		}
		return regresa;
	}
	
	private ControlError agregaOtorgante(List<Otorgante> otorgantes,
			Tramite tramite,
			mx.gob.se.rug.masiva.resultado.to.TramiteRes tramiteRes) throws InfrastructureException {
		ControlError regresa = null;
		masivaProcess.getIdUsuario();
		UsuarioTO usuario= new UsuarioTO();
		PersonaTO persona= new PersonaTO();
		persona.setIdPersona(masivaProcess.getIdUsuario());
		usuario.setPersona(persona);
		try {
			if (otorgantes == null) {
				System.out.println("RUG-Action:CargaMasivaAction.agregaOtorgante------ La inscripcion no cuenta con un otorgante ::::");
				regresa = new ControlError();
				PlSql plSql = new PlSql();
				plSql.setResStrPl("La inscripcion no cuenta con un Otorgante");
				regresa.setPlSql(plSql);
				regresa.setIdInscripcion(tramite.getConsecutivo());
			}else{
				masivaService.addOtorgantes(otorgantes, usuario, tramite);
			}
		} catch (CargaMasivaException e) {
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setResStrPl(e.getMessage());
			plSql.setStrPl(e.getMessage());
			plSql.setIntPl(e.getCodeError());
			plSql.setResIntPl(e.getCodeError());
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(tramite.getConsecutivo());
			
			e.printStackTrace();
			
		} 
		
//		for (Otorgante otorgante : otorgantes) {
//			regresa = agregaOtorganteInd(otorgante, masivaProcess.getIdUsuario(),
//					tramite.getIdAcreedor(), tramite.getIdTramite(),
//					tramite.getConsecutivo(), tramiteRes);
//			if (regresa != null) {
//				break;
//			}
//		}

		return regresa;
	}
	
//	private ControlError agregaOtorganteInd(Otorgante otorgante,
//			Integer idUsuario, Integer idAcreedor, Integer idInscripcion,
//			Integer numInscripcion,
//			mx.gob.se.rug.masiva.resultado.to.TramiteRes tramite) {
//		ControlError regresa = null;
//
//		try {
//			AltaParteDAO altaParteDAO = new AltaParteDAO();
//		System.out.println("RUG-Action:CargaMasivaAction.agregaOtorgante------ Iniciamos agregarOtorgante ::::");
//		if (otorgante == null) {
//			System.out
//					.println("RUG-Action:CargaMasivaAction.agregaOtorgante------ La inscripcion no cuenta con un otorgante ::::");
//			regresa = new ControlError();
//			PlSql plSql = new PlSql();
//			plSql.setResStrPl("La inscripcion no cuenta con un Otorgante");
//			regresa.setPlSql(plSql);
//			regresa.setIdInscripcion(numInscripcion);
//		} else {
//			
////			
////			System.out.println("Folop Electronio :::"
////					+ otorgante.getFolioElectronico());
////			System.out.println("tipo persona::"
////					+ otorgante.getTipoPersona());
////			if(otorgante.getTipoPersona().trim().equals("PF")||otorgante.getTipoPersona().trim().equals("PM")){
////					if (otorgante.getTipoPersona().trim().equals("PF")
////							&& otorgante.getFolioElectronico() != null
////							&& !otorgante.getFolioElectronico().trim().equals("")) {
////						OtorganteTO otorganteTO = altaParteDAO
////								.getOtorganteFisicoByFolioElectronico(otorgante
////										.getFolioElectronico());
////						if (otorganteTO != null) {
////							System.out
////									.println("RUG-Action:CargaMasivaAction.agregaOtorgante------ Se relaciona con el folio electronico ::::");
////							if (!altaParteDAO.relParte(
////									otorganteTO.getIdOtorgante(), idInscripcion, 1)) {
////								regresa = new ControlError();
////								PlSql plSql = new PlSql();
////								plSql.setResStrPl("No se pudo relacionar al otorgante");
////								plSql.setStrPl("No se pudo relacionar al otorgante");
////								plSql.setIntPl(591);
////								plSql.setResIntPl(887);
////								regresa.setPlSql(plSql);
////								regresa.setIdInscripcion(numInscripcion);
////		
////							}
////						} else {
////							System.out
////									.println("RUG-Action:CargaMasivaAction.agregaOtorgante------ el Folio electronico proporcionado es invalido ::::");
////							regresa = new ControlError();
////							PlSql plSql = new PlSql();
////							plSql.setResStrPl("El Folio Electronico no existe");
////							plSql.setStrPl("El Folio Electronico no existe");
////							plSql.setIntPl(591);
////							plSql.setResIntPl(887);
////							regresa.setPlSql(plSql);
////							regresa.setIdInscripcion(numInscripcion);
////		
////						}
////					} else {
////						System.out
////								.println("RUG-Action:CargaMasivaAction.agregaOtorgante------ Se trata de agregar una nueva persona ::::");
////						AltaParteTO altaParteTO = new AltaParteTO();
////						altaParteTO.setIdParte(1);
////						altaParteTO.setIdTramite(idInscripcion);
////						altaParteTO.setFolioMercantil(otorgante
////								.getFolioElectronico());
////						altaParteTO.setCurp(otorgante.getCurp());
////						altaParteTO.setNombre(otorgante.getNombre());
////						altaParteTO.setApellidoMaterno(otorgante
////								.getApellidoMaterno());
////						altaParteTO.setApellidoPaterno(otorgante
////								.getApellidoPaterno());
////						altaParteTO.setRazonSocial(otorgante
////								.getDenominacionRazonSocial());
////						altaParteTO.setIdNacionalidad(otorgante
////								.getIdNacionalidad().intValue());
////						altaParteTO.setTipoPersona(otorgante.getTipoPersona());
////						altaParteTO.setHayDomicilio("F");
////						altaParteTO.setIdUsuario(idUsuario);
////						altaParteTO.setIdPersona(idUsuario);
////						if (altaParteTO.getTipoPersona().trim().equals("PM")) {
////							System.out.println("es una persona moral");
////							if (altaParteTO.getFolioMercantil() == null
////									|| altaParteTO.getFolioMercantil().trim()
////											.equals("")) {
////								System.out
////										.println("El folio electronico es obligatorio");
////								regresa = new ControlError();
////								PlSql plSql = new PlSql();
////								plSql.setResStrPl("Error en el Otorgante:  El campo folio electronico es obligatorio.");
////								plSql.setStrPl("Error en el Otorgante:  El campo folio electronico es obligatorio.");
////								plSql.setIntPl(2);
////								regresa.setPlSql(plSql);
////								regresa.setIdInscripcion(numInscripcion);
////							} else {
////								PlSql intPer = altaParteDAO.insert(altaParteTO);
////								if (intPer == null) {
////									System.out
////											.println("RUG-Action:CargaMasivaAction.agregaOtorgante------ No se pudo generar la nueva persona ::::");
////									regresa = new ControlError();
////									PlSql plSql = new PlSql();
////									plSql.setResStrPl("Error en el Otorgante - No se pudo agregar al otorgante");
////									plSql.setStrPl("Error en el Otorgante - No se pudo agregar al otorgante");
////									regresa.setPlSql(plSql);
////									regresa.setIdInscripcion(numInscripcion);
////								} else {
////									if (intPer.getIntPl().intValue() != 0) {
////										System.out
////												.println("RUG-Action:CargaMasivaAction.agregaOtorgante------ la nueva persona fue dada de alta con el siguiente ID  :::: "
////														+ intPer.getIntPl()
////																.intValue());
////										regresa = new ControlError();
////										regresa.setPlSql(intPer);
////									}
////								}
////							}
////		
////						} else {
////							PlSql intPer = altaParteDAO.insert(altaParteTO);
////							if (intPer == null) {
////								System.out
////										.println("RUG-Action:CargaMasivaAction.agregaOtorgante------ No se pudo generar la nueva persona ::::");
////								regresa = new ControlError();
////								PlSql plSql = new PlSql();
////								plSql.setResStrPl("Error en el Otorgante - No se pudo agregar al otorgante");
////								plSql.setStrPl("Error en el Otorgante - No se pudo agregar al otorgante");
////								regresa.setPlSql(plSql);
////								regresa.setIdInscripcion(numInscripcion);
////							} else {
////								if (intPer.getIntPl().intValue() != 0) {
////									regresa = new ControlError();
////									regresa.setPlSql(intPer);
////								} else {
////									FolioElectronicoDAO folioElectronicoDAO = new FolioElectronicoDAO();
////									String strMsj = folioElectronicoDAO
////											.creaFolioElectronico(intPer
////													.getResIntPl().intValue());
////									if (strMsj != null) {
////										mx.gob.se.rug.masiva.resultado.to.Otorgante otorgante2 = new mx.gob.se.rug.masiva.resultado.to.Otorgante();
////										otorgante2.setCurp(altaParteTO.getCurp());
////										otorgante2.setFolioElectronico(strMsj);
////										System.out
////												.println("--se genero el siguiente folio electronico de persona fisica -- "
////														+ strMsj);
////										otorgante2.setNombreCompleto(altaParteTO
////												.getNombre()
////												+ " "
////												+ altaParteTO.getApellidoPaterno()
////												+ " "
////												+ altaParteTO.getApellidoMaterno());
////										tramite.getOtorgante().add(otorgante2);
////									} else {
////										regresa = new ControlError();
////										PlSql plSql = new PlSql();
////										plSql.setIntPl(501);
////										plSql.setResStrPl("Error al tratar de generar folio del RUG, reportarlo al area de sistemas");
////										plSql.setStrPl("Error al tratar de generar folio del RUG, reportarlo al area de sistemas");
////										regresa.setPlSql(plSql);
////										regresa.setIdInscripcion(numInscripcion);
////									}
////		
////								}
////							}
////						}
////					}
////			}else{
////		throw new CargaMasivaException(33);
////	}		
//			
//			
//}
//} catch (NumberFormatException e) {
//			regresa = new ControlError();
//			PlSql plSql = new PlSql();
//			plSql.setIntPl(999);
//			plSql.setResStrPl("Error al tratar de convertir a entero:"
//					+ e.getMessage() + ", " + e.getCause());
//			plSql.setStrPl("Error al tratar de convertir a entero:"
//					+ e.getMessage() + ", " + e.getCause());
//			regresa.setPlSql(plSql);
//			regresa.setIdInscripcion(numInscripcion);
//			e.printStackTrace();
//		} catch (Exception e) {
//			regresa = new ControlError();
//			PlSql plSql = new PlSql();
//			plSql.setIntPl(999);
//			plSql.setResStrPl(e.getMessage() + ", " + e.getCause());
//			plSql.setStrPl(e.getMessage() + ", " + e.getCause());
//			regresa.setPlSql(plSql);
//			regresa.setIdInscripcion(numInscripcion);
//			e.printStackTrace();
//		}
//		return regresa;
//
//	}
//	
	
	private ControlError agregarBienesEspeciales(Inscripcion inscripcion,
			Integer idUsuario, Integer idAcreedor, Integer idInscripcion,
			Integer numInscripcion) {
		ControlError regresa = null;
		try {
			if(inscripcion.getGarantia().getCreacion().getBienesEspeciales().getBienEspecial() != null) {
				List<BienEspecial> bienesEspeciales = inscripcion.getGarantia().getCreacion().getBienesEspeciales().getBienEspecial();
				if(bienesEspeciales.size() > 0) {
					Iterator<BienEspecial> itBe = bienesEspeciales.iterator();
					PlSql plSql = new PlSql();
					plSql.setIntPl(0);
					while (plSql.getIntPl().intValue() == 0 && itBe.hasNext()) {
						BienEspecial tBienEspecial = itBe.next();
						BienEspecialTO bienEspecialTO = new BienEspecialTO();
						bienEspecialTO.setIdTramite(idInscripcion);
						bienEspecialTO.setDescripcion(tBienEspecial.getDescripcion());
						bienEspecialTO.setTipoBien(new Integer(tBienEspecial.getTipoBienEspecial()));						
						bienEspecialTO.setIdentificador(tBienEspecial.getIdentificador());
						bienEspecialTO.setTipoIdentificador(new Integer(tBienEspecial.getTipoIdentificador())); 					
						inscripcionService.registrarBien(bienEspecialTO);
					}
					if (plSql.getIntPl().intValue() != 0) {
						regresa = new ControlError();
						regresa.setPlSql(plSql);
						regresa.setIdInscripcion(numInscripcion);
					}
				}
			}			
		} catch (Exception e) {
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setResStrPl(e.getMessage() + ", " + e.getCause());
			plSql.setStrPl(e.getMessage() + ", " + e.getCause());
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(numInscripcion);
			e.printStackTrace();
		}
		
		return regresa;
	}
	
	private ControlError agregarDeudores(Inscripcion inscripcion,
			Integer idUsuario, Integer idAcreedor, Integer idInscripcion,
			Integer numInscripcion) {
		ControlError regresa = null;
		try {
			List<Deudor> deudores = inscripcion.getPartes().getDeudor();
			if (deudores.size() > 0) {
				Iterator<Deudor> itDe = deudores.iterator();
				PlSql plSql = new PlSql();
				plSql.setIntPl(0);
				AltaParteDAO altaParteDAO = new AltaParteDAO();
				while (plSql.getIntPl().intValue() == 0 && itDe.hasNext()) {
					Deudor deudor = itDe.next();
					AltaParteTO altaParteTO = new AltaParteTO();
					altaParteTO.setIdParte(2);
					altaParteTO.setIdTramite(idInscripcion);
					altaParteTO.setRazonSocial(deudor.getDenominacionRazonSocial());
					altaParteTO.setRfc(deudor.getRfc());
					altaParteTO.setTipoPersona(deudor.getTipoPersona());
					altaParteTO.setNombre(deudor.getNombre());
					altaParteTO.setApellidoPaterno(deudor.getApellidoPaterno());
					altaParteTO.setApellidoMaterno(deudor.getApellidoMaterno());
					altaParteTO.setHayDomicilio("V");
					altaParteTO.setIdNacionalidad(getIntegerFromBigIntger(deudor.getIdNacionalidad()));
					altaParteTO.setIdPersona(Integer.valueOf(idUsuario));
					altaParteTO.setIdPersona(idUsuario);
					
					altaParteTO.setCurp(deudor.getCurp());
					altaParteTO.setDomicilioUno(deudor.getDomicilio());
					altaParteTO.setCorreoElectronico(deudor.getEmail());
					plSql = altaParteDAO.insert(altaParteTO);
				}
				if (plSql.getIntPl().intValue() != 0) {
					regresa = new ControlError();
					regresa.setPlSql(plSql);
					regresa.setIdInscripcion(numInscripcion);
				}

			}

		} catch (NumberFormatException e) {
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setIntPl(999);
			plSql.setResStrPl("Error al tratar de convertir a entero:"
					+ e.getMessage() + ", " + e.getCause());
			plSql.setStrPl("Error al tratar de convertir a entero:"
					+ e.getMessage() + ", " + e.getCause());
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(numInscripcion);
			e.printStackTrace();
		} catch (Exception e) {
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setResStrPl(e.getMessage() + ", " + e.getCause());
			plSql.setStrPl(e.getMessage() + ", " + e.getCause());
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(numInscripcion);
			e.printStackTrace();
		}

		return regresa;

	}
	

	
	public Integer getIntegerFromBigIntger(BigInteger bigInteger){
		Integer resultado=null;
		if (bigInteger!=null){
			resultado= new Integer(bigInteger.intValue());
		}
		
		return resultado;
	}
	
	private ControlError agregaGarantia(Inscripcion inscripcion,Integer idUsuario, Integer idAcreedor, Integer idInscripcion,Integer numInscripcion) {
		ControlError regresa = null;
		ModificaGarantiaServiceImpl garantiaServiceImpl= new ModificaGarantiaServiceImpl();
		System.out.println("RUG-Action:CargaMasivaAction.agregaGarantia------Se trate de dar de alta la nueva garantia :::: ");
		try {
			
			
			/*Date fechaCreacionCelebracion=null;
			
			if(inscripcion.getGarantia().getCreacion().getFechaCelebracion() != null){
				fechaCreacionCelebracion = new Date(inscripcion.getGarantia().getCreacion().getFechaCelebracion().toGregorianCalendar().getTime().getTime());
				garantiaServiceImpl.validaDatos(fechaCreacionCelebracion, "FechaCelebracion", false, true, 1);
			}else{
				throw new CargaMasivaException(63);
			}
			
			
			Date fechaObligacionCelebracion=null;
			
			if(inscripcion.getGarantia().getObligacion().getFechaCelebracion() != null){
				fechaObligacionCelebracion = new Date(inscripcion.getGarantia().getObligacion().getFechaCelebracion().toGregorianCalendar().getTime().getTime());
				garantiaServiceImpl.validaDatos(fechaObligacionCelebracion, "FechaCelebracion", false, false, 1);
			}
			
			Date fechaObligacionTerminacion=null;
			
			if(inscripcion.getGarantia().getObligacion().getFechaTerminacion() != null){
				fechaObligacionTerminacion = new Date(inscripcion.getGarantia().getObligacion().getFechaTerminacion().toGregorianCalendar().getTime().getTime());
				garantiaServiceImpl.validaDatos(fechaObligacionTerminacion, "FechaTerminacion", false, false, 2);
			}*/
				
			GarantiasDAO garantiasDAO = new GarantiasDAO();
			InscripcionTO inscripcionTO = new InscripcionTO();
			inscripcionTO.setIdInscripcion(idInscripcion);
			inscripcionTO.setIdPersona(idUsuario);
			GarantiaTO garantiaTO = garantiaToGarantiaTO(inscripcion.getGarantia());
			PlSql insGar = garantiasDAO.insertGarantia(inscripcionTO, garantiaTO);
			regresa = new ControlError();
			regresa.setPlSql(insGar);
			regresa.setIdInscripcion(numInscripcion);
			if (insGar.getIntPl().intValue() != 0) {
				System.out.println("RUG-Action:CargaMasivaAction.agregaGarantia------Sucedio un error en la alta de la garantia :::: ");
			}
	

		} catch (CargaMasivaException e) {
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setResStrPl(e.getMessage());
			plSql.setStrPl(e.getMessage());
			plSql.setIntPl(e.getCodeError());
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(numInscripcion);
			
		} catch (Exception e) {
			if (e instanceof NullPointerException) {
				regresa = new ControlError();
				PlSql plSql = new PlSql();
				plSql.setResStrPl("Sucedio un error de precicion nupex, favor de reportarlo. ");
				plSql.setStrPl("Sucedio un error de precicion nupex, favor de reportarlo. ");
				plSql.setIntPl(999);
				regresa.setPlSql(plSql);
				regresa.setIdInscripcion(numInscripcion);
			} else {
				regresa = new ControlError();
				PlSql plSql = new PlSql();
				plSql.setResStrPl("error no definido");
				plSql.setStrPl("error no definido");
				plSql.setIntPl(999);
				regresa.setPlSql(plSql);
				regresa.setIdInscripcion(numInscripcion);
			}

			e.printStackTrace();
		}

		return regresa;
	}
	
	private GarantiaTO garantiaToGarantiaTO(Garantia garantia) throws InfrastructureException, NoDataFound, CargaMasivaException {
		GarantiaTO garantiaTO = new GarantiaTO();
		try {
			DateUtilRug dateUtilRug = new DateUtilRug();
			Creacion creacion = garantia.getCreacion();
			Obligacion obligacion = garantia.getObligacion();
			ModificaGarantiaServiceImpl garantiaServiceImpl= new ModificaGarantiaServiceImpl();
			
			if(creacion.getIdTipoGarantia()!=null){
				garantiaServiceImpl.validaDatos(creacion.getIdTipoGarantia(), "IdTipoGarantia", false, true, null);
				garantiaTO.setIdTipoGarantia(creacion.getIdTipoGarantia().intValue());
			}
			
			ActoContratoTO actoContratoTO = new ActoContratoTO();
			
			
			/*Date fechaCreacionCelebracion=null;
			
			if(garantia.getCreacion().getFechaCelebracion() != null){
				fechaCreacionCelebracion = new Date(garantia.getCreacion().getFechaCelebracion().toGregorianCalendar().getTime().getTime());
				garantiaServiceImpl.validaDatos(fechaCreacionCelebracion, "FechaCelebracion", false, true, 1);
			}else{
				throw new CargaMasivaException(63);
			}
			actoContratoTO.setFechaCelebracion(dateUtilRug.formatDate(fechaCreacionCelebracion));*/
			
			
			//actoContratoTO.setMontoMaximo(creacion.getMontoMaximo().toString());
			//actoContratoTO.setTipoBienes(listaTipoBienMuebleToString(creacion.getTipoBienMueble()));
			actoContratoTO.setDescripcion(creacion.getDescripcionBienesMuebles());
			actoContratoTO.setOtrosTerminos(creacion.getTerminosCondiciones());
			actoContratoTO.setInstrumentoPub(creacion.getDatosInstrumentoPublico());			
			System.out.println("RUG:CargaMasivaAction.garantiaTOgarantiaTO--------------------------valor del id moneda::"+ creacion.getIdMoneda());
			//actoContratoTO.setTipoMoneda(creacion.getIdMoneda().toString());
			actoContratoTO.setCambiosBienesMonto(creacion.getbDatosModificables());
			actoContratoTO.setNoGarantiaPreviaOt(creacion.getbNoBienesOtorgados());
			actoContratoTO.setGarantiaPrioritaria(creacion.getbEsPrioritaria());
			actoContratoTO.setCpRegistro(creacion.getbEnOtroRegistro());
			if(creacion.getbEnOtroRegistro()){
				actoContratoTO.setTxtRegistro(creacion.getInfoRegistro());
			}
			garantiaTO.setActoContratoTO(actoContratoTO);
			ObligacionTO obligacionTO = new ObligacionTO();
			//obligacionTO.setTipoActoContrato(obligacion.getActoContrato().toString());
			
			/*
			Date fechaObligacionCelebracion=null;
			
			if(garantia.getObligacion().getFechaCelebracion() != null){
				fechaObligacionCelebracion = new Date(garantia.getObligacion().getFechaCelebracion().toGregorianCalendar().getTime().getTime());
				garantiaServiceImpl.validaDatos(fechaObligacionCelebracion, "FechaCelebracion", false, false, 1);
			}
			
			Date fechaObligacionTerminacion=null;
			
			if(garantia.getObligacion().getFechaTerminacion() != null){
				fechaObligacionTerminacion = new Date(garantia.getObligacion().getFechaTerminacion().toGregorianCalendar().getTime().getTime());
				garantiaServiceImpl.validaDatos(fechaObligacionTerminacion, "FechaTerminacion", false, false, 2);
			}
			
			obligacionTO.setFechaCelebracion(dateUtilRug.parseDateToStr(fechaObligacionCelebracion));
			obligacionTO.setFechaTerminacion(dateUtilRug.parseDateToStr(fechaObligacionTerminacion));*/
			obligacionTO.setOtrosTerminos(obligacion.getTerminos());
			garantiaTO.setObligacionTO(obligacionTO);
			garantiaTO.setIdMoneda(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return garantiaTO;
	}
	
	
	private String listaTipoBienMuebleToString(List<TipoBienMueble> tipoBienes) {
		String regresa = "";
		Iterator<TipoBienMueble> it = tipoBienes.iterator();
		if (it.hasNext()) {
			regresa = Integer.valueOf(it.next().getId().intValue()).toString();
		}
		while (it.hasNext()) {
			regresa += ("|" + Integer.valueOf(it.next().getId().intValue()).toString());
		}
		return regresa;
	}
	
	public ControlError revisaGarantia(Inscripcion inscripcion,
			mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcionRes,
			Integer consecutivo) {
		ControlError regresa = null;
		try {
			if (inscripcion.getGarantia().getCreacion().getDescripcionBienesMuebles() == null) {
			}
		} catch (Exception e) {
			listaTramitesErrores.add(0);
			regresa = new ControlError();
			inscripcionRes.setMensajeError("La descripcion de bienes muebles, es un dato obligatorio.");
			inscripcionRes.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
			inscripcionRes.setCodigoError("310");
		}
		if (regresa == null) {
			try {
				if (inscripcion.getGarantia().getCreacion()
						.getTerminosCondiciones() == null) {
					regresa = new ControlError();
					inscripcionRes.setMensajeError("la observaciones adicionales es un campo obligatorio.");
					inscripcionRes.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
					inscripcionRes.setCodigoError("311");
				}
			} catch (Exception e) {
				regresa = new ControlError();
				inscripcionRes
						.setMensajeError("Error verifique los valores de la garantia.");
				inscripcionRes.setClaveRastreo(inscripcion.getIdentificador()
						.getClaveRastreo());
				inscripcionRes.setCodigoError("310");
			}
		}
		if (regresa == null) {
			try {
				if (inscripcion.getGarantia().getCreacion().getDatosInstrumentoPublico() == null) {
					regresa = new ControlError();
					inscripcionRes
							.setMensajeError("Los datos del instrumento publico es un dato obligatorio.");
					inscripcionRes.setClaveRastreo(inscripcion
							.getIdentificador().getClaveRastreo());
					inscripcionRes.setCodigoError("312");
				}
			} catch (Exception e) {
				regresa = new ControlError();
				inscripcionRes
						.setMensajeError("Error verifique los valores de la garantia..");
				inscripcionRes.setClaveRastreo(inscripcion.getIdentificador()
						.getClaveRastreo());
				inscripcionRes.setCodigoError("312");
			}
		}
		if (regresa != null) {
			inscripcionesErroneas.add(inscripcionRes);
			resultado.getTramites().getTramite().add(inscripcionRes);
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setResStrPl(inscripcionRes.getCodigoError());
			plSql.setStrPl(inscripcionRes.getCodigoError());
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(consecutivo);
		}
		return regresa;
	}
	
	private String getSha1FromFile(byte[] in) {
		return getDigest(in);
	}
	
	public Integer getIdArchivoRes() {
		return idArchivoRes;
	}

	public void setIdArchivoRes(Integer idArchivoRes) {
		this.idArchivoRes = idArchivoRes;
	}
	
	public String getTotalTramites() {
		return totalTramites;
	}

	public void setTotalTramites(String totalTramites) {
		this.totalTramites = totalTramites;
	}

	public Integer getTramitesErroneos() {
		return tramitesErroneos;
	}

	public void setTramitesErroneos(Integer tramitesErroneos) {
		this.tramitesErroneos = tramitesErroneos;
	}
	
	public Integer getTramitesCompletos() {
		return tramitesCompletos;
	}

	public void setTramitesCompletos(Integer tramitesCompletos) {
		this.tramitesCompletos = tramitesCompletos;
	}
	
	public List<mx.gob.se.rug.masiva.resultado.to.TramiteRes> getInscripcionesErroneas() {
		return inscripcionesErroneas;
	}

	public void setInscripcionesErroneas(
			List<mx.gob.se.rug.masiva.resultado.to.TramiteRes> inscripcionesErroneas) {
		this.inscripcionesErroneas = inscripcionesErroneas;
	}
	
	public String getErrorArchivo() {
		return errorArchivo;
	}

	public void setErrorArchivo(String errorArchivo) {
		this.errorArchivo = errorArchivo;
	}
	
	public List<Integer> getListaTramites() {
		return listaTramites;
	}

	public void setListaTramites(List<Integer> listaTramites) {
		this.listaTramites = listaTramites;
	}
	
	public List<Integer> getListaTramitesErrores() {
		return listaTramitesErrores;
	}

	public void setListaTramitesErrores(List<Integer> listaTramitesErrores) {
		this.listaTramitesErrores = listaTramitesErrores;
	}
	
	public Integer getIdArchivoResultado() {
		return idArchivoResultado;
	}

	public void setIdArchivoResultado(Integer idArchivoResultado) {
		this.idArchivoResultado = idArchivoResultado;
	}
	
	public Integer getSizeListaTramites() {
		return sizeListaTramites;
	}

	public void setSizeListaTramites(Integer sizeListaTramites) {
		this.sizeListaTramites = sizeListaTramites;
	}
	
	public ResCargaMasiva getResCargaMasiva() {
		return resCargaMasiva;
	}

	public void setResCargaMasiva(ResCargaMasiva resCargaMasiva) {
		this.resCargaMasiva = resCargaMasiva;
	}
	
	public Integer getIdTipoTramite() {
		return idTipoTramite;
	}

	public void setIdTipoTramite(Integer idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}
	
	public Integer getIdTramiteTempFirma() {
		return idTramiteTempFirma;
	}

	public void setIdTramiteTempFirma(Integer idTramiteTempFirma) {
		this.idTramiteTempFirma = idTramiteTempFirma;
	}

	public AcreedoresService getAcreedoresService() {
		return acreedoresService;
	}

	public void setAcreedoresService(AcreedoresService acreedoresService) {
		this.acreedoresService = acreedoresService;
	}
}