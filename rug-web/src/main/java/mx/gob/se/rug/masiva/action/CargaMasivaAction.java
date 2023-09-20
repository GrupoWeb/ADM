package mx.gob.se.rug.masiva.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import mx.gob.economia.dgi.framework.security.user.dto.User;
import mx.gob.se.rug.acceso.dao.AccesoDAO;
import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.common.dto.Mensaje;
import mx.gob.se.rug.common.exception.UserException;
import mx.gob.se.rug.common.service.HomeService;
import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.dto.PersonaFisica;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.exception.NoDateInfrastructureException;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;
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
import mx.gob.se.rug.inscripcion.to.FirmaMasivaTO;
import mx.gob.se.rug.inscripcion.to.InscripcionTO;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
import mx.gob.se.rug.juez.dao.JuezDAO;
import mx.gob.se.rug.masiva.daemon.CargaMasivaController;
import mx.gob.se.rug.masiva.dao.MasivaDAO;
import mx.gob.se.rug.masiva.exception.CargaMasivaException;
import mx.gob.se.rug.masiva.exception.CargaMasivaExceptionMaxNumber;
import mx.gob.se.rug.masiva.exception.CargaMasivaFileLoadException;
import mx.gob.se.rug.masiva.exception.NoDataFound;
import mx.gob.se.rug.masiva.exception.NoTramiteFound;
import mx.gob.se.rug.masiva.resultado.to.CargaMasivaResultado;
import mx.gob.se.rug.masiva.resultado.to.ResultadoCargaMasiva;
import mx.gob.se.rug.masiva.resultado.to.Resumen;
import mx.gob.se.rug.masiva.resultado.to.TramiteRes;
import mx.gob.se.rug.masiva.resultado.to.Tramites;
import mx.gob.se.rug.masiva.service.AvisoPreventivoMasivaService;
import mx.gob.se.rug.masiva.service.MasivaService;
import mx.gob.se.rug.masiva.service.ModificacionService;
import mx.gob.se.rug.masiva.service.TransmisionService;
import mx.gob.se.rug.masiva.service.impl.ModificaGarantiaServiceImpl;
import mx.gob.se.rug.masiva.to.AcreedorAdicional;
import mx.gob.se.rug.masiva.to.ArchivoTO;
import mx.gob.se.rug.masiva.to.Cancelacion;
import mx.gob.se.rug.masiva.to.CargaMasiva;
import mx.gob.se.rug.masiva.to.CargaMasivaProcess;
import mx.gob.se.rug.masiva.to.ControlError;
import mx.gob.se.rug.masiva.to.Creacion;
import mx.gob.se.rug.masiva.to.Deudor;
import mx.gob.se.rug.masiva.to.Garantia;
import mx.gob.se.rug.masiva.to.Inscripcion;
import mx.gob.se.rug.masiva.to.Obligacion;
import mx.gob.se.rug.masiva.to.Otorgante;
import mx.gob.se.rug.masiva.to.RectificacionPorError;
import mx.gob.se.rug.masiva.to.RenovacionReduccion;
import mx.gob.se.rug.masiva.to.ResCargaMasiva;
import mx.gob.se.rug.masiva.to.TipoBienMueble;
import mx.gob.se.rug.masiva.to.TipoTramiteCargaMasiva;
import mx.gob.se.rug.masiva.to.Tramite;
import mx.gob.se.rug.masiva.to.string.CargaMasivaPreProcesed;
import mx.gob.se.rug.masiva.validate.ValidateDataType;
import mx.gob.se.rug.partes.dao.FolioElectronicoDAO;
import mx.gob.se.rug.rectificacion.impl.RectificacionServiceImpl;
import mx.gob.se.rug.to.PersonaTO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;
import mx.gob.se.rug.util.to.DateUtilRug;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 
 * @author Abraham Stalin Aguilar Valencia
 * 
 */
public class CargaMasivaAction extends RugBaseAction implements
		ServletRequestAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	
	// Atributos
	private String errorArchivo;
	private String totalTramites;
	private String mensajeErroXsd;
	private Integer idListaProceso;
	private Integer idListaTramite;
	private Integer idAcreedorTO;
	private Integer idAcreedor;
	private Integer idArchivoRes;
	private Integer idArchivoResultado;
	private Integer tramitesErroneos;
	private Integer tramitesCompletos;
	private Integer tramitesIncorrectosFiltro;
	private Integer idUsuario;
	private Integer idFirmaMasiva;
	private Integer sizeListaTramites;
	private ResultadoCargaMasiva resultado;
	private ControlError cerror;	
	private Double costoTramiteMasivo;
	
	// file
	private File archivo;
	private String archivoContentType;
	private String archivoFileName;
	
	// listas
	private List<Integer> listaTramites;
	private List<Integer> listaTramitesErrores;
	private List<TipoProcesoCargaMasiva> listaProceso;
	private List<TipoTramiteCargaMasiva> listaTipoTramite;
	private List<AcreedorTO> listaAcreedores;
	private List<ControlError> listaErrores;
	private List<mx.gob.se.rug.masiva.resultado.to.TramiteRes> inscripcionesErroneas;

	private MasivaDAO masivaDAO = new MasivaDAO();
	private InscripcionService inscripcionService = new InscripcionServiceImpl();
	private InscripcionDAO inscripcionDAO = new InscripcionDAO();
	private MasivaService masivaService;
	private AvisoPreventivoMasivaService avisoPreventivoMasivaService;
	
	private ArchivoTO archivoTO;
	private CargaMasivaPreProcesed cargaMasivaPreProcesed = new CargaMasivaPreProcesed();
	private CargaMasivaProcess masivaProcess;
	
	//idGarantias
	private String detalleTecnico;
	
	///injection
	private HomeService homeService;
	
	
	

	/*
	 * Tenemos que inyectar las dependencias con Spring
	 */

	public HomeService getHomeService() {
		return homeService;
	}

	public void setHomeService(HomeService homeService) {
		this.homeService = homeService;
	}

	public void setMasivaService(MasivaService masivaService) {
		this.masivaService = masivaService;
	}
	
	public void setAvisoPreventivoMasivaService(
			AvisoPreventivoMasivaService avisoPreventivoMasivaService) {
		this.avisoPreventivoMasivaService = avisoPreventivoMasivaService;
	}

	public String firmarTodos() {

		String regresa = "failed";
		try {
			System.out
					.println("RugAction::CargaMasivaAction.iniciaCargaMasiva----- se inicia la carga Masiva");

			Integer idTramiteNuevo = (Integer) sessionMap
					.get(Constants.ID_TRAMITE_NUEVO);
			if (masivaDAO.isCargaMasiva(idTramiteNuevo)) {
				regresa = "success";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return regresa;
	}


	public String iniciaCargaMasiva() {
		String regresa = "failed";
		try {
			System.out
					.println("RugAction::CargaMasivaAction.iniciaCargaMasiva----- se inicia la carga Masiva");
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			sessionMap.put(Constants.TRAMITES_ERRONEOS,	new ArrayList<Integer>());
			sessionMap.put(Constants.TRAMITES, new ArrayList<Integer>());
			sessionMap.put(Constants.ID_ARCHIVO, null);
			setIdUsuario(usuario.getPersona().getIdPersona());
			setListaAcreedores(new ArrayList<AcreedorTO>());
			for (AcreedorTO acre : inscripcionService.getAcreedoresByID(usuario
					.getPersona().getIdPersona())) {
				getListaAcreedores().add(acre);
                                System.out.println("Acreedor masiva: " + acre);
			}
			regresa = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return regresa;
	}

	public boolean matchXmltoXsd(File xml) {
		System.out.println("-- start matchXmltoXsd--");
		boolean regresa = false;
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		try {
			@SuppressWarnings("deprecation") File fileXsd = new File(request.getRealPath("resources") + "/xsd/carga-masiva-simple.xsd");
			Schema schema = factory.newSchema(fileXsd);
			Validator validator = schema.newValidator();
			Source source = new StreamSource(xml);
			validator.validate(source);
			regresa = true;
		} catch (SAXParseException ex) {
			StringBuffer sb= new StringBuffer();
			sb.append("Linea:");
			sb.append(ex.getLineNumber());
			sb.append("	Columna:");
			sb.append(ex.getColumnNumber());
			sb.append("	Mensaje:");
			sb.append(ex.getMessage());
			setMensajeErroXsd("mensaje error xsd:::" + ex.getMessage());
			System.out.println("mensaje:::" + ex.getMessage());
			setDetalleTecnico(sb.toString());
			ex.printStackTrace();
		} catch (SAXException ex) {
			setMensajeErroXsd("mensaje error xsd:::" + ex.getMessage());
			System.out.println("mensaje:::" + ex.getMessage());
			setDetalleTecnico(ex.getMessage());
			ex.printStackTrace();
		} catch (IOException e) {
			setMensajeErroXsd("mensaje error xsd:::" + e.getMessage());
			e.printStackTrace();
			setDetalleTecnico(e.getMessage());
		}
		return regresa;
	}

	public String cargaMasivaTXT() {
		String regresa = "failed";
		/*
		 * Aqui el archivo TXT sera transformado a objetos
		 */
		return regresa;
	}

	public String cargaMasivaXML() {
		String regresa = "failed";
//		ResCargaMasiva respuestaCM = new ResCargaMasiva();
		try{
			if (!archivoFileName.contains(".xml")
					&& !archivoFileName.contains(".Xml")
					&& !archivoFileName.contains(".XML")) {
				setErrorArchivo("El archivo que estas tratando de subir no es XML");
				return regresa;
			}
			                 System.out.println("1");
			setListaTramites(new ArrayList<Integer>());
                        System.out.println("2");
			setListaTramitesErrores(new ArrayList<Integer>());
                        System.out.println("3");
			inscripcionesErroneas = new ArrayList<mx.gob.se.rug.masiva.resultado.to.TramiteRes>();
                        System.out.println("4");
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
                        System.out.println("5");
			setIdUsuario(usuario.getPersona().getIdPersona());
                        System.out.println("6");
			String sha1 = getSha1FromFile(getArchivo());
			System.out.println("7");
			if(inscripcionService.existeSha1(sha1)){
                            System.out.println("8");
				setErrorArchivo("El archivo que tratas de subir al sistema ya fue dado de alta con anterioridad.");
			}else{
                            System.out.println("9");
				if(matchXmltoXsd(archivo)){
                                    System.out.println("10");
					archivoTO = this.creaArchivoTo(usuario,sha1);
                                        System.out.println("11");
					masivaProcess = new CargaMasivaProcess();
                                        System.out.println("12");
					masivaProcess.setIdArchivo(archivoTO.getIdArchivo());
                                        System.out.println("13");
					masivaProcess.setIdTipoTramite(idListaTramite.intValue());					
                                        System.out.println("14");
					masivaProcess.setIdUsuario(idUsuario);
                                        System.out.println("15");
					masivaProcess.setIdAcreedor(idUsuario);
                                        System.out.println("16");
					if(idListaProceso.intValue() == 1){
                                            System.out.println("17");
						masivaProcess.setbTipoProceso("A");
					}else{
                                            System.out.println("18");
						masivaProcess.setbTipoProceso("D");
					}
                                        System.out.println("19");
					masivaDAO.insertProcessCarga(masivaProcess);
					System.out.println("20");
					ValidateDataType validateDataType = new ValidateDataType();
					System.out.println("21");
					String xmlFromDB = validateDataType.getFileFromDB(masivaProcess.getIdArchivo());
                                        System.out.println("22");
					validateDataType.validateCargaMasiva(xmlFromDB, masivaProcess.getIdTipoTramite());
					System.out.println("23");
					if(idListaProceso.intValue() == 1){
                                            System.out.println("24");
						// proceso atendido
						CargaMasivaController cargaMasivaController= new CargaMasivaController();
                                                System.out.println("25");
						cargaMasivaController.procesaCargaMasiva(masivaProcess);
						System.out.println("26");
						//this.setIdArchivoResultado(masivaProcess.getIdArchivoResumen());
						regresa ="resultado";
                                                System.out.println("27");
//						CargaMasiva cm = validamosArchivo(idListaTramite.intValue(),archivoTO.getIdArchivo());
//						regresa = seleccionaProceso(cm,usuario,archivoTO);
//						
						/*if(cargaMasivaController.getTramitesErroneos()>=0) {
							this.setIdArchivoResultado(masivaProcess.getIdArchivoResumen());
						} else {*/
						//masivaDAO.actualizaProcessCargaIdResumen(masivaProcess);
							this.setIdArchivoResultado(masivaProcess.getIdArchivo());
                                                        System.out.println("28");
						//}
					}else{
                                            System.out.println("29");
						regresa="batch";
					}
				}else{
                                    System.out.println("30");
					setErrorArchivo("El archivo que estas tratando de subir no corresponde al XSD.");
				}
			}
		}catch (CargaMasivaExceptionMaxNumber e) {
                    System.out.println("31");
			setErrorArchivo(e.getLocalizedMessage());
			setMensajeErroXsd(e.getLocalizedMessage());
			setDetalleTecnico(e.getMessage());
			e.printStackTrace();
		
		}catch (NoTramiteFound e) {
                    System.out.println("32");
			setErrorArchivo(e.getLocalizedMessage());
			setMensajeErroXsd(e.getLocalizedMessage());
			setDetalleTecnico(e.getMessage());
			e.printStackTrace();
		
		}catch (Exception e) {
                    System.out.println("33");
			setErrorArchivo("El archivo que estas tratando de subir no corresponde a las especificaciones del XSD");
			setMensajeErroXsd(stack2string(e));
			setDetalleTecnico(e.getMessage());
			e.printStackTrace();
		}
		System.out.println("- Finalizo la cargaMasivaXML -" + regresa);
		return regresa;
	}
	
	public String obtenResultado() throws CargaMasivaFileLoadException,NoDataFoundException{
		UsuarioTO usuarioTO= (UsuarioTO) sessionMap.get(Constants.USUARIO);
		if (usuarioTO== null){
			try {
				
				
				UsuarioTO u = new UsuarioTO();
				PersonaTO persona = new PersonaTO();
				AccesoDAO udao = new AccesoDAO();
				
				
				
				User user = homeService.getUser(principal.getUserPrincipal().toString());
				MyLogger.Logger.log(Level.INFO, "IMPIRME EL NOMBRE 001: "+user.getProfile().getNombre());
				u.setNombre(principal.getUserPrincipal().toString());
				
				logger.debug(sessionMap);

				if(principal.getUserPrincipal().toString().equals(u.getNombre())){
					MyLogger.Logger.log(Level.INFO, " logeado ");
					PersonaFisica personaFisica = udao.getIdPersona(u.getNombre());
					
					persona.setIdPersona(personaFisica.getIdPersona());
					persona.setPerfil(personaFisica.getPerfil());
					
					u.setPersona(persona);
					sessionMap.put(mx.gob.se.rug.constants.Constants.USUARIO,u);
					MyLogger.Logger.log(Level.INFO, "IMPRIME NOMBE 4"+persona.getIdPersona());
					MyLogger.Logger.log(Level.INFO, "IMPRIME NOMBE 5"+u.getNombre());
					user.getProfile().setEmail(u.getNombre());
					sessionMap.put(mx.gob.se.rug.common.constants.Constants.SESSION_USER, user);
					usuarioTO= (UsuarioTO) sessionMap.get(Constants.USUARIO);
				}		
				
			} catch (UserException e) {
				MyLogger.Logger.log(Level.SEVERE, "Error al tratar de iniciar la session:",e);
			}
		}
		
		String regresa = "failed";
		int idArchivoResDb = 0;
		ValidateDataType validateDataType = new ValidateDataType();
		ResultadoCargaMasiva resultadoCargaMasiva = null;
		idArchivoResDb = masivaDAO.getIdArchivoResDb(idArchivoResultado);
		String xmlArchivoResultado = validateDataType.getFileFromDB(idArchivoResDb);
		InputStream stream = new ByteArrayInputStream(xmlArchivoResultado.getBytes());
		try{
			resultadoCargaMasiva = (ResultadoCargaMasiva) unmarshallResultadoCargaMasiva(stream);
		}catch(Exception e){
			e.printStackTrace();
			throw new CargaMasivaFileLoadException(103);
		}
		if(resultadoCargaMasiva.getTramites() == null){
			this.setTotalTramites(resultadoCargaMasiva.getResumen().getErroneos());
		}else{
			this.setTotalTramites(String.valueOf(resultadoCargaMasiva.getTramites().getTramite().size()));			
		}
		try {
			Integer idTramiteFirma = masivaDAO.getIdTramiteFirmaDb(idArchivoResultado);
			this.setCostoTramiteMasivo(inscripcionDAO.getCostoByIdTipoTramiteMasivo(idTramiteFirma));
			sessionMap.put(Constants.ID_TRAMITE_NUEVO, idTramiteFirma);
		}catch(Exception e){
			e.printStackTrace();
			sessionMap.put(Constants.ID_TRAMITE_NUEVO, null);
		}
		this.setIdArchivoRes(idArchivoResDb);
		this.setSizeListaTramites(Integer.valueOf(resultadoCargaMasiva.getResumen().getCorrectos()));
		this.setInscripcionesErroneas(resultadoCargaMasiva.getTramites().getTramite());
		this.setTramitesCompletos(Integer.valueOf(resultadoCargaMasiva.getResumen().getCorrectos()));
		this.setTramitesErroneos(Integer.valueOf(resultadoCargaMasiva.getResumen().getErroneos()));			
		
		regresa = "success";
		return regresa;
	}
	
	private ResultadoCargaMasiva unmarshallResultadoCargaMasiva(InputStream InputStreamXML) throws CargaMasivaFileLoadException{
		
		ResultadoCargaMasiva resultadoCargaMasiva = null;
		JAXBContext jaxbContext;
		javax.xml.bind.Unmarshaller unmarshaller;
		try{
			jaxbContext = JAXBContext.newInstance(ResultadoCargaMasiva.class.getPackage().getName());
			unmarshaller = jaxbContext.createUnmarshaller();
			resultadoCargaMasiva = (ResultadoCargaMasiva) unmarshaller.unmarshal(InputStreamXML);
		}catch (Exception e){
			e.printStackTrace();
			throw new CargaMasivaFileLoadException(103);
		}
		return resultadoCargaMasiva;
	}
	
	private ArchivoTO creaArchivoTo(UsuarioTO usuario,	String sha1){
		try{
			archivoTO = new ArchivoTO();
			archivoTO.setAlgoritoHash(sha1);
			archivoTO.setArchivo(getBytesFromFile(archivo));
			archivoTO.setDescripcion("Carga masiva del usuario:"+ usuario.getNombre()+ ", id:"+ usuario.getPersona().getIdPersona());
			archivoTO.setIdUsuario(usuario.getPersona()	.getIdPersona());
			archivoTO.setNombreArchivo(archivoFileName);
			archivoTO.setTipoArchivo("xml");
			PlSql plSql = inscripcionService.insertArchivo(archivoTO);
			archivoTO.setIdArchivo(plSql.getResIntPl());
	
			if (archivoTO.getIdArchivo().intValue() == 0) {
				setErrorArchivo("El archivo no se pudo guardar en el sistema.");
			}
		}catch (Exception e) {
			setErrorArchivo(e.getMessage() + "-" + e.getCause());
			e.printStackTrace();
		}
		return archivoTO;
	}
	
	private CargaMasiva validamosArchivo(Integer idTipoTramite, Integer idArchivo) throws NoTramiteFound,CargaMasivaExceptionMaxNumber{
		cargaMasivaPreProcesed = new CargaMasivaPreProcesed();
		ValidateDataType validateDataType = new ValidateDataType();
		String xmlFromDB = validateDataType.getFileFromDB(idArchivo);
		try{
			cargaMasivaPreProcesed = validateDataType.parseCargaMasiva(xmlFromDB,idTipoTramite);
		}catch(CargaMasivaException e){
			e.printStackTrace();
		}		
		return cargaMasivaPreProcesed.getCargaMasiva();
	}

	private String seleccionaProceso(CargaMasiva cm,UsuarioTO usuario,ArchivoTO archivoTO){
		String regresa = "failed";
		try {
			ResCargaMasiva respuestaCM = new ResCargaMasiva();
			AcreedorTO acreedor = inscripcionService.getAcreedorByID(idAcreedorTO);
			
			switch (idListaTramite.intValue()) {
				// Inscripcion
				case 1:					
					if (cm.getInscripcion().size() > 0) {
						System.out.println("--- el archivo contiene inscripciones --");						
						regresa = cargaMasivaInscripcion(usuario,cm,acreedor,archivoTO);
					} else if(this.cargaMasivaPreProcesed.getTramiteIncorrectos().size()>0){
						Resumen resumen = new Resumen();
						resumen = getResumenFiltroError(cargaMasivaPreProcesed);
						
						resultado = new ResultadoCargaMasiva();
						resultado.setResumen(resumen);
						Tramites tramites = new  Tramites();
						tramites.getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						resultado.setTramites(tramites);
						
						
						masivaService.generaArchivoResumen(resultado, usuario, archivoTO, masivaProcess);
						
						
						this.setTotalTramites(String.valueOf(cargaMasivaPreProcesed.getTramiteIncorrectos().size()));
						this.setTramitesCompletos(0);
						this.setTramitesErroneos(cargaMasivaPreProcesed.getTramiteIncorrectos().size());
						this.setInscripcionesErroneas(cargaMasivaPreProcesed.getTramiteIncorrectos());
						regresa="resultado";
					}else{
						setErrorArchivo("El archivo seleccionado no contiene Inscripciones ");
					}
					break;
	
				// Cancelacion
				case 4:
					if (cm.getCancelacion().size() > 0) {
						regresa = cargaMasivaCancelacion(usuario, cm,acreedor,archivoTO);
					} else if(this.cargaMasivaPreProcesed.getTramiteIncorrectos().size()>0){
						Resumen resumen = new Resumen();
						resumen = getResumenFiltroError(cargaMasivaPreProcesed);
						
						resultado = new ResultadoCargaMasiva();
						resultado.setResumen(resumen);
						Tramites tramites = new  Tramites();
						tramites.getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						resultado.setTramites(tramites);
						
						masivaService.generaArchivoResumen(resultado, usuario, archivoTO, masivaProcess);
						
						this.setTotalTramites(String.valueOf(cargaMasivaPreProcesed.getTramiteIncorrectos().size()));
						this.setTramitesCompletos(0);
						this.setTramitesErroneos(cargaMasivaPreProcesed.getTramiteIncorrectos().size());
						this.setInscripcionesErroneas(cargaMasivaPreProcesed.getTramiteIncorrectos());
						regresa="success";
					}else {
						setErrorArchivo("El archivo seleccionado no contiene Cancelaciones ");
					}
					break;
				// Rectificaciones por error
				case 6:
					if (cm.getRectificacionPorError().size() > 0) {
						//regresa = cargaMasivaRectificacion(usuario, cm,inscripcionService.getAcreedorByID(idAcreedorTO),plSql.getResIntPl());
						RectificacionServiceImpl rectificacionService= new RectificacionServiceImpl();
						CargaMasivaResultado cargaMasivaResultado;
						cargaMasivaResultado = rectificacionService.cargaMasivaRectificacion(usuario,cm,acreedor,archivoTO.getIdArchivo());
						cargaMasivaResultado.getResultadoCargaMasiva().getTramites().getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						
						for (Tramite tramite: cargaMasivaResultado.getTramitesCorrectos()) {
							listaTramites.add(tramite.getIdTramite());
						}
						
						for (Tramite tramite: cargaMasivaResultado.getTramitesIncorrectos()) {
							inscripcionesErroneas.add(tramite.getTramiteRes());
						}
						/*
						 * TODO
						 * Extraer a un metodo nuevo
						 */
						cargaMasivaResultado = masivaService.generaFirmaMasiva(cargaMasivaResultado, cargaMasivaPreProcesed, usuario, archivoTO, acreedor);
						idFirmaMasiva = cargaMasivaResultado.getIdFirmaMasiva();
						if(idFirmaMasiva!=null){
							sessionMap.put(Constants.ID_TRAMITE_NUEVO, idFirmaMasiva);
						}
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
						
						masivaService.generaArchivoResumen(resultado, usuario, archivoTO, masivaProcess);
						
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
						cargaMasivaResultado=modificacionService.cargaMasivaModificacion(usuario, cm, acreedor, archivoTO.getIdArchivo());
						
						
						
						cargaMasivaResultado.getResultadoCargaMasiva().getTramites().getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						
						for (Tramite tramite: cargaMasivaResultado.getTramitesCorrectos()) {
							listaTramites.add(tramite.getIdTramite());
						}
						
						for (Tramite tramite: cargaMasivaResultado.getTramitesIncorrectos()) {
							inscripcionesErroneas.add(tramite.getTramiteRes());
						}
						
				//Temporal ya que es un proceso general
						//Generar Firma MAsiva
						cargaMasivaResultado = masivaService.generaFirmaMasiva(cargaMasivaResultado, cargaMasivaPreProcesed, usuario, archivoTO, acreedor);
						idFirmaMasiva = cargaMasivaResultado.getIdFirmaMasiva();
						if(idFirmaMasiva!=null){
							sessionMap.put(Constants.ID_TRAMITE_NUEVO, idFirmaMasiva);
						}
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
						
						masivaService.generaArchivoResumen(resultado, usuario, archivoTO, masivaProcess);
						
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
						cargaMasivaResultado=transmisionService.cargaMasivaTransmision(usuario, cm, acreedor, archivoTO.getIdArchivo());
						cargaMasivaResultado.getResultadoCargaMasiva().getTramites().getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						
						for (Tramite tramite: cargaMasivaResultado.getTramitesCorrectos()) {
							listaTramites.add(tramite.getIdTramite());
						}
						
						for (Tramite tramite: cargaMasivaResultado.getTramitesIncorrectos()) {
							inscripcionesErroneas.add(tramite.getTramiteRes());
						}
				//Temporal ya que es un proceso general
						//Generar Firma MAsiva
						cargaMasivaResultado = masivaService.generaFirmaMasiva(cargaMasivaResultado, cargaMasivaPreProcesed, usuario, archivoTO, acreedor);
						idFirmaMasiva = cargaMasivaResultado.getIdFirmaMasiva();
						if(idFirmaMasiva!=null){
							sessionMap.put(Constants.ID_TRAMITE_NUEVO, idFirmaMasiva);
						}
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
						
						masivaService.generaArchivoResumen(resultado, usuario, archivoTO, masivaProcess);
						
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
						System.out.println("--- el archivo contiene modificaciones --");
						regresa = cargaMasivaRenovacion(usuario, cm,acreedor,archivoTO.getIdArchivo());
					} else if(this.cargaMasivaPreProcesed.getTramiteIncorrectos().size()>0){
						Resumen resumen = new Resumen();
						resumen = getResumenFiltroError(cargaMasivaPreProcesed);
						
						resultado = new ResultadoCargaMasiva();
						resultado.setResumen(resumen);
						Tramites tramites = new  Tramites();
						tramites.getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						resultado.setTramites(tramites);
						
						masivaService.generaArchivoResumen(resultado, usuario, archivoTO, masivaProcess);
						
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
						respuestaCM.setIdArchivo(archivoTO.getIdArchivo());
						respuestaCM = avisoPreventivoMasivaService.cargaAvisoPreventivo(respuestaCM,cargaMasivaPreProcesed,cm,usuario,acreedor,archivoTO.getIdArchivo(),archivoTO.getNombreArchivo());
						regresa = respuestaCM.getRegresa();
						idFirmaMasiva= respuestaCM.getIdTramiteNuevo();
						sessionMap.put(Constants.ID_TRAMITE_NUEVO, respuestaCM.getIdTramiteNuevo());
						masivaProcess.setIdArchivoResumen(respuestaCM.getIdArchivoXML());
						this.setIdArchivoRes(respuestaCM.getIdArchivoXML());
						
						for (PlSql tramite: respuestaCM.getListaTramites()) {
							listaTramites.add(tramite.getResIntPl());
						}
						/*
						//inscripcionesErroneas.addAll(respuestaCM.getTramitesResultado());
						for(TramiteRes tramiteRes: respuestaCM.getTramitesResultado()){
							if(!tramiteRes.getCodigoError().equalsIgnoreCase("0")){
								inscripcionesErroneas.add(tramiteRes);
							}
						}*/
						regresa = "resultado";
					} else if(this.cargaMasivaPreProcesed.getTramiteIncorrectos().size()>0){
						Resumen resumen = new Resumen();
						resumen = getResumenFiltroError(cargaMasivaPreProcesed);
						
						resultado = new ResultadoCargaMasiva();
						resultado.setResumen(resumen);
						Tramites tramites = new  Tramites();
						tramites.getTramite().addAll(cargaMasivaPreProcesed.getTramiteIncorrectos());
						resultado.setTramites(tramites);
						
						masivaService.generaArchivoResumen(resultado, usuario, archivoTO, masivaProcess);
						
						this.setTotalTramites(String.valueOf(cargaMasivaPreProcesed.getTramiteIncorrectos().size()));
						this.setTramitesCompletos(0);
						this.setTramitesErroneos(cargaMasivaPreProcesed.getTramiteIncorrectos().size());
						this.setInscripcionesErroneas(cargaMasivaPreProcesed.getTramiteIncorrectos());
						regresa="success";
					}else {
						setMensaje(new Mensaje());
						getMensaje().setId("Rug-AutoridadMasiva-011");
						getMensaje()
								.setMensaje(
										"El archivo seleccionado no cuenta con etiquetas de avisos preventivos.");
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
					masivaDAO.executeBajaClaveRastreo(acreedor.getIdPersona(),masivaProcess.getIdArchivo()  ,tramite.getClaveRastreo());
					
				}
			}
			

	
		}catch (Exception e) {
			setErrorArchivo(e.getMessage() + "-" + e.getCause());
			e.printStackTrace();
		}
		return regresa;
	}

	// cancelacion codigos de error = 500
	public String cargaMasivaCancelacion(UsuarioTO usuario, CargaMasiva cm,
			AcreedorTO acreedor, ArchivoTO archivoTO) {
		String regresa = "failed";
		Integer idArchivo= archivoTO.getIdArchivo();
		System.out.println("entro a carga masiva cancelacion");
		int consecutivo = 1;
		Tramite tramite;
		resultado = new ResultadoCargaMasiva();
		resultado.setTramites(new Tramites());
		this.listaTramites = new ArrayList<Integer>();
		listaErrores = new ArrayList<ControlError>();
		try {
			sessionMap.put(Constants.ID_ARCHIVO, idArchivo);
			sessionMap.put(Constants.ID_ACREEDOR_REPRESENTADO, idAcreedorTO);
			sessionMap.put(Constants.ID_ARCHIVO, idArchivo);
			acreedor = inscripcionService.getAcreedorByID(idAcreedorTO);
			for (Cancelacion cancelacion : cm.getCancelacion()) {
				tramite = new Tramite();
				tramite.setIdArchivo(idArchivo);
				tramite.setIdUsuario(usuario.getPersona().getIdPersona());
				tramite.setConsecutivo(consecutivo++);
				try {
					tramite.setIdGarantia(Integer.valueOf(cancelacion
							.getIdentificadorGarantia().getIdGarantia().intValue()));
				} catch (Exception e) {
					tramite.setIdGarantia(0);
				}
				tramite.setIdTipoTramite(4);
				tramite.setIdEstatus(5);
				tramite.setIdAcreedor(idAcreedorTO);
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
				
				masivaService.generaArchivoResumen(resultado, usuario, archivoTO, masivaProcess);
				
				
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
								+ usuario.getNombre()
								+ ", con el id :"
								+ usuario.getPersona().getIdPersona()
								+ ", resultado de una carga que contenia archivos incorrectos");
						archivoN.setIdUsuario(usuario.getPersona()
								.getIdPersona());
						archivoN.setNombreArchivo(archivoTO.getNombreArchivo()+ "nuevo");
						archivoN.setTipoArchivo("xml");
						idArchivo = inscripcionService.insertArchivo(archivoN)
								.getResIntPl();
						sessionMap.put(Constants.ID_ARCHIVO, idArchivo);
						System.out
								.println("### se genero el nuevo archivo con el id :"
										+ idArchivo);
					}

				}
				sessionMap.put(Constants.TRAMITES, listaTramites);
				if (listaTramites.size() > 0) {
					idArchivo = (Integer) sessionMap.get(Constants.ID_ARCHIVO);
					archivoTO.setIdArchivo(idArchivo);
					masivaProcess.setIdArchivoFirma(idArchivo);
					masivaDAO.actualizaProcessCargaIdFirma(masivaProcess);
					Integer idAcreedor = (Integer) sessionMap
							.get(Constants.ID_ACREEDOR_REPRESENTADO);
					Integer idUsuario = usuario.getPersona().getIdPersona();
					FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
					firmaMasivaTO.setIdUsuario(idUsuario);
					firmaMasivaTO.setIdArchivo(idArchivo);
					firmaMasivaTO.setIdAcreedor(idAcreedor);
					String tramites = listToString(listaTramites);
					firmaMasivaTO.setTramites(tramites);
					FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
					int valor = firmaDao.crearFirmaMasiva(firmaMasivaTO);
					System.out
							.println("firmaDao.crearFirmaMasiva(firmaMasivaTO)--valor--"
									+ valor);
					if (valor != 0) {
						idFirmaMasiva=valor;
						sessionMap.put(Constants.ID_TRAMITE_NUEVO, valor);
					}
				}
				regresa = "resultado";

			} catch (FileNotFoundException e) {

				System.out.println("ThreadCargaMasiva:::archivo [" + idArchivo
						+ "]::: sucedio un error faltal:::1");
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {

				System.out.println("ThreadCargaMasiva:::archivo [" + idArchivo
						+ "]::: sucedio un error faltal:::11");
				e.printStackTrace();
			} catch (JAXBException e) {

				System.out.println("ThreadCargaMasiva:::archivo [" + idArchivo
						+ "]::: sucedio un error faltal:::111");
				e.printStackTrace();
			} catch (Exception e) {

				System.out.println("ThreadCargaMasiva:::archivo [" + idArchivo
						+ "]::: sucedio un error faltal:::1111");
				e.printStackTrace();
			}
			for (Integer tramiteErroneo : listaTramitesErrores) {
				inscripcionDAO.bajaTramiteIncompleto(tramiteErroneo);
			}

		}
		return regresa;
	}

	public String cargaMasivaRectificacion(UsuarioTO usuario, CargaMasiva cm,
			AcreedorTO acreedor, Integer idArchivo) {
		String regresa = "failed";
		System.out.println("entro a carga masiva rectificacionE");
		int consecutivo = 1;
		Tramite tramite;
		resultado = new ResultadoCargaMasiva();
		resultado.setTramites(new Tramites());
		this.listaTramites = new ArrayList<Integer>();
		listaErrores = new ArrayList<ControlError>();
		try {
			sessionMap.put(Constants.ID_ARCHIVO, idArchivo);
			sessionMap.put(Constants.ID_ACREEDOR_REPRESENTADO, idAcreedorTO);
			sessionMap.put(Constants.ID_ARCHIVO, idArchivo);
			acreedor = inscripcionService.getAcreedorByID(idAcreedorTO);
			for (RectificacionPorError rectificacionPorError : cm.getRectificacionPorError()) {
				tramite = new Tramite();
				tramite.setIdArchivo(idArchivo);
				tramite.setIdUsuario(usuario.getPersona().getIdPersona());
				tramite.setConsecutivo(consecutivo++);
				tramite.setIdGarantia(rectificacionPorError.getIdentificadorGarantia().getIdGarantia().intValue());
				tramite.setIdTipoTramite(6);
				tramite.setIdEstatus(5);
				tramite.setIdAcreedor(idAcreedorTO);
				tramite.setClaveRastreo(rectificacionPorError
						.getIdentificadorGarantia().getClaveRastreo());
				cerror = agregaRectificacion(rectificacionPorError, tramite);
					if (cerror != null) {
						//listaTramitesErrores.add(cerror.getIdInscripcion());
						listaErrores.add(cerror);
						System.out
								.println("ThreadCargaMasiva::cargaMasivaRectificacion:archivo ["
										+ idArchivo
										+ "]::: sucedio un error en la rectificacion ::"
										+ cerror.getClaveRastreo());
					}
				
			}
		} catch (Exception e) {
			java.util.Date date = new java.util.Date();
			Resumen resumen = new Resumen();
			resumen.setMensajeError("Sucedio un error en el sistema la fecha :"
					+ date.toString() + ", enviar el XML a soporte. codError["
					+ date.getTime() + "]");
			resultado.setResumen(resumen);
			System.out
					.println("ThreadCargaMasiva::cargaMasivaRectificacion:archivo ["
							+ idArchivo + "]::: Sucedio un error el proceso:::");
			e.printStackTrace();
		} finally {

			try {
				tramitesErroneos = listaTramitesErrores.size();
				ArchivoTO archivoRes = new ArchivoTO();
				byte[] bytes2 = convertXMLObjetc(resultado);
				archivoRes.setAlgoritoHash(getSha1FromFile(bytes2));
				archivoRes.setArchivo(bytes2);
				archivoRes
						.setDescripcion("Archivo nuevo de carga masiva de rectificacion del usuario : "
								+ usuario.getNombre()
								+ ", con el id :"
								+ usuario.getPersona().getIdPersona()
								+ ", resultado de una carga que contenia archivos incorrectos");
				archivoRes.setIdUsuario(usuario.getPersona().getIdPersona());
				archivoRes.setNombreArchivo("cmResnuevo");
				archivoRes.setTipoArchivo("xml");
				setIdArchivoRes(inscripcionService.insertArchivo(archivoRes)
						.getResIntPl());
				if (listaTramitesErrores.size() > 0) {
					System.out.println("#### sucedieron errores en la carga");
					// crear nuevo archivo XML y guardarlo para el tramite de
					// firma
					List<RectificacionPorError> listaCorrectos = new ArrayList<RectificacionPorError>();
					for (mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcionRes : resultado
							.getTramites().getTramite()) {
						if (inscripcionRes.getCodigoError().equals("0")) {
							for (RectificacionPorError ins : cm
									.getRectificacionPorError()) {
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
						carga.getRectificacionPorError().addAll(listaCorrectos);
						byte[] bytes = convertXMLObjetc(carga);

						ArchivoTO archivoN = new ArchivoTO();
						archivoN.setAlgoritoHash(getSha1FromFile(bytes));
						archivoN.setArchivo(bytes);
						archivoN.setDescripcion("Archivo nuevo de carga masiva de rectificacion del usuario : "
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
						sessionMap.put(Constants.ID_ARCHIVO, idArchivo);
						System.out
								.println("##cargaMasivaRectificacion# se genero el nuevo archivo con el id :"
										+ idArchivo);
					}

				}
				sessionMap.put(Constants.TRAMITES, listaTramites);
				if (listaTramites.size() > 0) {
					idArchivo = (Integer) sessionMap.get(Constants.ID_ARCHIVO);
					Integer idAcreedor = (Integer) sessionMap
							.get(Constants.ID_ACREEDOR_REPRESENTADO);
					Integer idUsuario = usuario.getPersona().getIdPersona();
					FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
					firmaMasivaTO.setIdUsuario(idUsuario);
					firmaMasivaTO.setIdArchivo(idArchivo);
					firmaMasivaTO.setIdAcreedor(idAcreedor);
					String tramites = listToString(listaTramites);
					firmaMasivaTO.setTramites(tramites);
					FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
					int valor = firmaDao.crearFirmaMasiva(firmaMasivaTO);
					System.out
							.println("-cargaMasivaRectificacion-firmaDao.crearFirmaMasiva(firmaMasivaTO)--valor--"
									+ valor);
					if (valor != 0) {
						sessionMap.put(Constants.ID_TRAMITE_NUEVO, valor);
					}
				}
				regresa = "success";

			} catch (FileNotFoundException e) {

				System.out
						.println("ThreadCargaMasiva::cargaMasivaRectificacion:archivo ["
								+ idArchivo + "]::: sucedio un error faltal:::2");
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {

				System.out
						.println("ThreadCargaMasiva::cargaMasivaRectificacion:archivo ["
								+ idArchivo + "]::: sucedio un error faltal:::22");
				e.printStackTrace();
			} catch (JAXBException e) {

				System.out
						.println("ThreadCargaMasiva::cargaMasivaRectificacion:archivo ["
								+ idArchivo + "]::: sucedio un error faltal:::222");
				e.printStackTrace();
			} catch (Exception e) {

				System.out
						.println("ThreadCargaMasiva::cargaMasivaRectificacion:archivo ["
								+ idArchivo + "]::: sucedio un error faltal:::2222");
				e.printStackTrace();
			}
			for (Integer tramiteErroneo : listaTramitesErrores) {
				inscripcionDAO.bajaTramiteIncompleto(tramiteErroneo);
			}

		}

		return regresa;
	}

	

	public String stack2string(Exception e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			if (sw.toString().contains("UTF-8")) {
				setErrorArchivo("El archivo que estas subiendo tiene caracteres no validos para el encoding: UTF-8 favor de cambiar a : ISO-8859-1, e intentarlo nuevamente ");
			}
			if (sw.toString().length() > 400) {
				return sw.toString().substring(0, 399);
			} else {
				return sw.toString();
			}
		} catch (Exception e2) {
			return "bad stack2string";
		}
	}

	public ControlError revisaGarantia(Inscripcion inscripcion,
			mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcionRes,
			Integer consecutivo) {
		ControlError regresa = null;
		try {
			if (inscripcion.getGarantia().getCreacion().getIdTipoGarantia().intValue() > 0) {
			}
		} catch (Exception e) {
			listaTramitesErrores.add(0);
			regresa = new ControlError();
			inscripcionRes.setMensajeError("El id tipo garantia, es un dato obligatorio.");
			inscripcionRes.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
			inscripcionRes.setCodigoError("310");
		}
		if (regresa == null) {
			try {
				if (inscripcion.getGarantia().getCreacion()
						.getFechaCelebracion() == null) {
					regresa = new ControlError();
					inscripcionRes.setMensajeError("la fecha de celebracion del acto o contrato es un campo obligatorio.");
					inscripcionRes.setClaveRastreo(inscripcion.getIdentificador().getClaveRastreo());
					inscripcionRes.setCodigoError("311");
				}
			} catch (Exception e) {
				regresa = new ControlError();
				inscripcionRes
						.setMensajeError("El id tipo garantia, es un dato obligatorio.");
				inscripcionRes.setClaveRastreo(inscripcion.getIdentificador()
						.getClaveRastreo());
				inscripcionRes.setCodigoError("310");
			}
		}
		if (regresa == null) {
			try {
				if (inscripcion.getGarantia().getCreacion().getIdMoneda() == null) {
					regresa = new ControlError();
					inscripcionRes
							.setMensajeError("El id moneda, es un dato obligatorio.");
					inscripcionRes.setClaveRastreo(inscripcion
							.getIdentificador().getClaveRastreo());
					inscripcionRes.setCodigoError("312");
				}
			} catch (Exception e) {
				regresa = new ControlError();
				inscripcionRes
						.setMensajeError("El id moneda, es un dato obligatorio.");
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

	// inscripciones
	public String cargaMasivaInscripcion(UsuarioTO usuario, CargaMasiva cm, AcreedorTO acreedor, ArchivoTO archivoTO) {
		System.out.println("--- cargaMasivaInscripcion");
		String regresa = "failed";
		Integer idArchivo = archivoTO.getIdArchivo();
		this.listaTramites = new ArrayList<Integer>();
		mx.gob.se.rug.masiva.resultado.to.Tramites inscripciones = new mx.gob.se.rug.masiva.resultado.to.Tramites();
		resultado = new ResultadoCargaMasiva();
		resultado.setTramites(inscripciones);
		listaErrores = listaErrores == null ? new ArrayList<ControlError>()	: listaErrores;
		
		try {
			sessionMap.put(Constants.ID_ACREEDOR_REPRESENTADO, idAcreedorTO);
			sessionMap.put(Constants.ID_ARCHIVO, archivoTO.getIdArchivo());
			acreedor = inscripcionService.getAcreedorByID(idAcreedorTO);
			try {
				System.out.println("ThreadCargaMasiva:::archivo [" + archivoTO.getIdArchivo() + "]::: Inicia el proceso::");
				if (cm.getInscripcion().size() > 0) {
					int consecutivo = 0;
					for (Inscripcion inscripcion : cm.getInscripcion()) {
						cerror = agregaInscripcion(inscripcion, usuario, acreedor, consecutivo++, archivoTO.getIdArchivo());
						if (cerror != null&& cerror.getPlSql()!=null&& cerror.getPlSql().getIntPl().intValue()!=0) {
							listaErrores.add(cerror);
							System.out.println("ThreadCargaMasiva:::archivo [" + archivoTO.getIdArchivo() + "]::: sucedio un error en la isncripcion ::" + cerror.getClaveRastreo());
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
				System.out.println("ThreadCargaMasiva:::archivo [" + archivoTO.getIdArchivo() + "]::: Sucedio un error el proceso:::");
				e.printStackTrace();
			} finally {
				try {
					Resumen resumen = getResumen(this.cargaMasivaPreProcesed);					
					resultado.setResumen(resumen);
					
					
					masivaService.generaArchivoResumen(resultado, usuario, archivoTO, masivaProcess);
					
					if (listaTramitesErrores.size() > 0) {
						System.out.println("#### sucedieron errores en la carga");
						// crear nuevo archivo XML y guardarlo para el tramite
						// de firma
						List<Inscripcion> listaCorrectos = new ArrayList<Inscripcion>();
						for (mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcionRes : resultado.getTramites().getTramite()) {
							if (inscripcionRes.getCodigoError().equals("0")) {
								for (Inscripcion ins : cm.getInscripcion()) {
									if (ins.getIdentificador().getClaveRastreo().trim().equals(inscripcionRes.getClaveRastreo().trim())) {
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
									+ usuario.getNombre()
									+ ", con el id :"
									+ usuario.getPersona().getIdPersona()
									+ ", resultado de una carga que contenia archivos incorrectos");
							archivoN.setIdUsuario(usuario.getPersona()
									.getIdPersona());
							archivoN.setNombreArchivo(archivoTO.getNombreArchivo() + "nuevo");
							archivoN.setTipoArchivo("xml");
							idArchivo = inscripcionService.insertArchivo(archivoN).getResIntPl();
							sessionMap.put(Constants.ID_ARCHIVO, idArchivo);
							System.out.println("### se genero el nuevo archivo con el id :"+ idArchivo);
						}

					}
					sessionMap.put(Constants.TRAMITES, listaTramites);

					if (listaTramites.size() > 0) {
						@SuppressWarnings("unchecked")
						
						List<Integer> listaTramites2 = (List<Integer>) sessionMap.get(Constants.TRAMITES);
						masivaProcess.setIdArchivoFirma(idArchivo);
						masivaDAO.actualizaProcessCargaIdFirma(masivaProcess);
						setListaTramites(listaTramites2);
						idArchivo = (Integer) sessionMap.get(Constants.ID_ARCHIVO);
						archivoTO.setIdArchivo(idArchivo);
						Integer idAcreedor = (Integer) sessionMap.get(Constants.ID_ACREEDOR_REPRESENTADO);
						Integer idUsuario = usuario.getPersona().getIdPersona();
						FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
						firmaMasivaTO.setIdUsuario(idUsuario);
						firmaMasivaTO.setIdArchivo(idArchivo);
						firmaMasivaTO.setIdAcreedor(idAcreedor);
						String tramites = listToString(getListaTramites());
						firmaMasivaTO.setTramites(tramites);
						FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
						int valor = firmaDao.crearFirmaMasiva(firmaMasivaTO);
						if (valor != 0) {
							idFirmaMasiva=valor;
							sessionMap.put(Constants.ID_TRAMITE_NUEVO, valor);
						}
					}

					regresa = "resultado";
				} catch (FileNotFoundException e) {

					System.out.println("ThreadCargaMasiva:::archivo ["
							+ idArchivo + "]::: sucedio un error faltal:::3");
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {

					System.out.println("ThreadCargaMasiva:::archivo ["
							+ idArchivo + "]::: sucedio un error faltal:::33");
					e.printStackTrace();
				} catch (JAXBException e) {

					System.out.println("ThreadCargaMasiva:::archivo ["
							+ idArchivo + "]::: sucedio un error faltal:::333");
					e.printStackTrace();
				} catch (Exception e) {

					System.out.println("ThreadCargaMasiva:::archivo ["
							+ idArchivo + "]::: sucedio un error faltal:::3333");
					e.printStackTrace();
				}
			}
		} catch (Exception e) {

			setErrorArchivo("sucedio un error con el procesamiento del archivo revisar con el siguiente identificados: DAMARCH"
					+ idArchivo + " Message::" + e.getMessage());
		} finally {
			InscripcionDAO inscripcionDAO = new InscripcionDAO();
			setTramitesErroneos(listaTramitesErrores.size());
			setTotalTramites(""	+ (listaTramitesErrores.size() + listaTramites.size()));
			for (Integer tramiteErroneo : listaTramitesErrores) {
				inscripcionDAO.bajaTramiteIncompleto(tramiteErroneo);
			}

		}
		System.out.println("--- cargaMasivaInscripcion ---....." + regresa);
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
	public  Resumen getResumenFiltroError(CargaMasivaPreProcesed cMasivaPreProcesed){
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
			/*Iterator<mx.gob.se.rug.masiva.resultado.to.TramiteRes> cMasivaPreProcesedI = cMasivaPreProcesed.getTramiteIncorrectos().iterator();
			while(cMasivaPreProcesedI.hasNext()){
				mx.gob.se.rug.masiva.resultado.to.TramiteRes tmpTramite = cMasivaPreProcesedI.next();
				resultado.getTramites().getTramite().add(tmpTramite);
				contTramitesIncorrectos++;
			}*/
			contTramitesIncorrectos = cMasivaPreProcesed.getTramiteIncorrectos().size();
			resultado.getTramites().getTramite().addAll(cMasivaPreProcesed.getTramiteIncorrectos());
		}
		return contTramitesIncorrectos;
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
			sessionMap.put(Constants.ID_ARCHIVO, idArchivo);
			sessionMap.put(Constants.ID_ACREEDOR_REPRESENTADO, idAcreedorTO);
			sessionMap.put(Constants.ID_ARCHIVO, idArchivo);
			acreedor = inscripcionService.getAcreedorByID(idAcreedorTO);
			for (RenovacionReduccion reduccion : cm.getRenovacionReduccion()) {
				tramite = new Tramite();
				tramite.setIdArchivo(idArchivo);
				tramite.setIdUsuario(usuario.getPersona().getIdPersona());
				tramite.setConsecutivo(consecutivo++);
				tramite.setIdGarantia(reduccion
						.getIdentificadorGarantia().getIdGarantia().intValue());
				tramite.setIdTipoTramite(9);
				tramite.setIdEstatus(5);
				tramite.setIdAcreedor(idAcreedorTO);
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
				
				
				masivaService.generaArchivoResumen(resultado, usuario, archivoTO, masivaProcess);
				
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
						sessionMap.put(Constants.ID_ARCHIVO, idArchivo);
						System.out
								.println("### se genero el nuevo archivo con el id :"
										+ idArchivo);
					}

				}
				sessionMap.put(Constants.TRAMITES, listaTramites);

				if (listaTramites.size() > 0) {
					setListaTramites(listaTramites);
					idArchivo = (Integer) sessionMap.get(Constants.ID_ARCHIVO);
					masivaProcess.setIdArchivoFirma(idArchivo);
					masivaDAO.actualizaProcessCargaIdFirma(masivaProcess);
					Integer idAcreedor = (Integer) sessionMap
							.get(Constants.ID_ACREEDOR_REPRESENTADO);
					Integer idUsuario = usuario.getPersona().getIdPersona();
					FirmaMasivaTO firmaMasivaTO = new FirmaMasivaTO();
					firmaMasivaTO.setIdUsuario(idUsuario);
					firmaMasivaTO.setIdArchivo(idArchivo);
					firmaMasivaTO.setIdAcreedor(idAcreedor);
					String tramites = listToString(getListaTramites());
					firmaMasivaTO.setTramites(tramites);
					FirmaMasivaDAO firmaDao = new FirmaMasivaDAO();
					int valor = firmaDao.crearFirmaMasiva(firmaMasivaTO);
					if (valor != 0) {
						idFirmaMasiva= valor;
						sessionMap.put(Constants.ID_TRAMITE_NUEVO, valor);
					}
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

	public String getSha1FromFile(byte[] in) {
		return getDigest(in);
	}

	private ControlError agregaRectificacion(
			RectificacionPorError rectificacionPorError, Tramite tramite) {
		ControlError regresa = null;
		String claveRastreo = "No identificada";
		TramiteRes tramiteRes = new TramiteRes();
			try {
					PlSql plSql = masivaDAO.executeAltaParteTramIncRast(tramite);
					if (plSql.getIntPl().intValue() == 0) {
						tramite.setIdTramite(plSql.getResIntPl());
						regresa = verificaDatosOtorgante(rectificacionPorError, tramite, tramiteRes);
						if (regresa == null) {
								regresa = agregarDeudores(rectificacionPorError, tramite);
								if (regresa == null) {
									regresa = verificaDatosAcreedorAdicional(rectificacionPorError, tramite);
									if (regresa == null) {
										plSql = masivaDAO
												.executeModificaGarantiaRectifica(
														tramite, rectificacionPorError);
										if (plSql.getIntPl().intValue() == 0) {
											if (rectificacionPorError.getAcreedor() != null) {
												plSql = masivaDAO
														.cambiaParteAcreedorRepresentado(
																rectificacionPorError
																		.getAcreedor()
																		.getIdAcreedor().intValue(),
																tramite);
											}
												if (plSql.getIntPl().intValue() == 0) {
													plSql = masivaDAO.executeAltaBitacoraTramite(tramite);
															if(plSql.getIntPl().intValue() == 0){
																
															listaTramites.add(tramite.getIdTramite());
															tramiteRes
																	.setMensajeError("La Rectificacion Por Error fue procesada correctamente");
															tramiteRes
																	.setClaveRastreo(rectificacionPorError
																			.getIdentificadorGarantia()
																			.getClaveRastreo());
															tramiteRes.setCodigoError("0");
															resultado.getTramites()
																	.getTramite()
																	.add(tramiteRes);
															regresa = null;
															}else{
																listaTramitesErrores.add(tramite.getIdTramite());
																tramiteRes.setMensajeError(plSql
																		.getStrPl());
																tramiteRes
																		.setClaveRastreo(rectificacionPorError
																				.getIdentificadorGarantia()
																				.getClaveRastreo());
																tramiteRes.setCodigoError(plSql
																		.getIntPl().toString());
																inscripcionesErroneas
																		.add(tramiteRes);
																resultado.getTramites()
																		.getTramite()
																		.add(tramiteRes);
															}
												} else {
													listaTramitesErrores.add(tramite.getIdTramite());
													tramiteRes.setMensajeError(plSql
															.getStrPl());
													tramiteRes
															.setClaveRastreo(rectificacionPorError
																	.getIdentificadorGarantia()
																	.getClaveRastreo());
													tramiteRes.setCodigoError(plSql
															.getIntPl().toString());
													inscripcionesErroneas
															.add(tramiteRes);
													resultado.getTramites()
															.getTramite()
															.add(tramiteRes);
												}
											
										} else {
											regresa = new ControlError();
											regresa.setPlSql(plSql);
											listaTramitesErrores.add(plSql
													.getResIntPl().intValue());
											tramiteRes.setMensajeError(regresa
													.getPlSql().getStrPl());
											tramiteRes
													.setClaveRastreo(rectificacionPorError
															.getIdentificadorGarantia()
															.getClaveRastreo());
											tramiteRes.setCodigoError(regresa
													.getPlSql().getIntPl().toString());
											inscripcionesErroneas.add(tramiteRes);
											resultado.getTramites().getTramite()
													.add(tramiteRes);
										}
									} else {
										listaTramitesErrores.add(plSql.getResIntPl()
												.intValue());
										tramiteRes.setMensajeError(regresa.getPlSql()
												.getStrPl());
										tramiteRes
												.setClaveRastreo(rectificacionPorError
														.getIdentificadorGarantia()
														.getClaveRastreo());
										tramiteRes.setCodigoError(regresa.getPlSql()
												.getIntPl().toString());
										inscripcionesErroneas.add(tramiteRes);
										resultado.getTramites().getTramite()
												.add(tramiteRes);
									}
								} else {
									listaTramitesErrores.add(plSql.getResIntPl()
											.intValue());
									tramiteRes.setMensajeError(regresa.getPlSql()
											.getStrPl());
									tramiteRes.setClaveRastreo(rectificacionPorError
											.getIdentificadorGarantia().getClaveRastreo());
									tramiteRes.setCodigoError(regresa.getPlSql()
											.getIntPl().toString());
									inscripcionesErroneas.add(tramiteRes);
									resultado.getTramites().getTramite()
											.add(tramiteRes);
								}
						} else {
							listaTramitesErrores
									.add(plSql.getResIntPl().intValue());
							tramiteRes.setMensajeError(regresa.getPlSql()
									.getStrPl());
							tramiteRes.setClaveRastreo(rectificacionPorError
									.getIdentificadorGarantia().getClaveRastreo());
							tramiteRes.setCodigoError(regresa.getPlSql()
									.getIntPl().toString());
							inscripcionesErroneas.add(tramiteRes);
							resultado.getTramites().getTramite().add(tramiteRes);
						}
					} else {
						listaTramitesErrores.add(plSql.getResIntPl().intValue());
						tramiteRes.setMensajeError(plSql.getStrPl());
						tramiteRes.setClaveRastreo(rectificacionPorError
								.getIdentificadorGarantia().getClaveRastreo());
						tramiteRes.setCodigoError(plSql.getIntPl().toString());
						inscripcionesErroneas.add(tramiteRes);
						resultado.getTramites().getTramite().add(tramiteRes);
					}
					if (regresa != null) {
						regresa.setClaveRastreo(claveRastreo);
					}
	
			} catch (Exception e) {
				regresa = new ControlError();
				tramiteRes
						.setMensajeError("No se pudo generar la rectificacion por error");
				tramiteRes.setClaveRastreo(rectificacionPorError.getIdentificadorGarantia()
						.getClaveRastreo());
				tramiteRes.setCodigoError("2");
				inscripcionesErroneas.add(tramiteRes);
				resultado.getTramites().getTramite().add(tramiteRes);
				e.printStackTrace();
			}
		if (regresa != null) {
			regresa.setClaveRastreo(claveRastreo);
		}
		return regresa;
	}

	private ControlError agregaCancelacion(Cancelacion cancelacion,
			Tramite tramite) {
		ControlError regresa = null;
		mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcionRes = new mx.gob.se.rug.masiva.resultado.to.TramiteRes();
		//regresa = verificaDatosCancelacion(cancelacion, tramite, inscripcionRes);
		if (regresa == null) {
			PlSql plSql = masivaDAO.executeAltaCancelacionMasiva(tramite,
					cancelacion);
			if (plSql.getIntPl().intValue() == 0) {
				listaTramites.add(plSql.getResIntPl());
				inscripcionRes.setMensajeError("La cancelacion fue procesada correctamente");
				inscripcionRes.setClaveRastreo(cancelacion.getIdentificadorGarantia().getClaveRastreo());
				inscripcionRes.setCodigoError("0");
				resultado.getTramites().getTramite().add(inscripcionRes);
				regresa = null;
//				try{
//					juezDAO.insertAutoridadInstruyeAsiento(plSql.getIntPl().intValue(), cancelacion.getPersonaSolicitaAutoridadInstruyeAsiento());
//				}catch (CargaMasivaException e) {
//					listaTramitesErrores.add(plSql.getResIntPl().intValue());
//					inscripcionRes.setMensajeError(e.getMessage());
//					inscripcionRes.setClaveRastreo(cancelacion.getIdentificadorGarantia().getClaveRastreo());
//					inscripcionRes.setCodigoError(e.getCodeError().toString());
//					inscripcionesErroneas.add(inscripcionRes);
//					resultado.getTramites().getTramite().add(inscripcionRes);
//					
//					e.printStackTrace();
//					
//				} catch (InfrastructureException e) {
//					listaTramitesErrores.add(plSql.getResIntPl().intValue());
//					inscripcionRes.setMensajeError(e.getMessage());
//					inscripcionRes.setClaveRastreo(cancelacion.getIdentificadorGarantia().getClaveRastreo());
//					inscripcionRes.setCodigoError("999");
//					inscripcionesErroneas.add(inscripcionRes);
//					resultado.getTramites().getTramite().add(inscripcionRes);
//					e.printStackTrace();
//				}
				
			} else {
				listaTramitesErrores.add(plSql.getResIntPl().intValue());
				inscripcionRes.setMensajeError(plSql.getStrPl());
				inscripcionRes.setClaveRastreo(cancelacion.getIdentificadorGarantia().getClaveRastreo());
				inscripcionRes.setCodigoError(plSql.getIntPl().toString());
				inscripcionesErroneas.add(inscripcionRes);
				resultado.getTramites().getTramite().add(inscripcionRes);
			}
			
			
		}
//		if (regresa != null) {
//			regresa.setClaveRastreo(tramite.getClaveRastreo());
//		}
		return regresa;

	}
	
	private ControlError agregaRenovacion(RenovacionReduccion renovacion,
			Tramite tramite) {
		ControlError regresa = null;
		mx.gob.se.rug.masiva.resultado.to.TramiteRes inscripcionRes = new mx.gob.se.rug.masiva.resultado.to.TramiteRes();
		if (regresa == null) {
			PlSql plSql = masivaDAO.executeAltaProrrogaMasiva(tramite,renovacion);
			
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
		
		}
		/*if (regresa != null) {
			regresa.setClaveRastreo(renovacion.getIdentificadorGarantia()
					.getClaveRastreo());
		}*/
		return regresa;

	}

	private ControlError agregaInscripcion(Inscripcion inscripcion,	UsuarioTO usuario, AcreedorTO acreedor, Integer numInscripcion,
			Integer idArchivo) {
		ControlError regresa = null;
		String claveRastreo = "No identificada";
		TramiteRes tramite = new TramiteRes();
		JuezDAO autoridad= new JuezDAO();

		try {
			Integer idAcreedor = Integer.valueOf(acreedor.getIdAcreedor());
			InscripcionTO inscripcionTO = new InscripcionTO();
			inscripcionTO.setIdTipoTramite(1);
			inscripcionTO.setIdPersona(usuario.getPersona().getIdPersona());
			InscripcionDAO inscripcionDAO = new InscripcionDAO();

			regresa = revisaGarantia(inscripcion, tramite, numInscripcion);
			if (regresa == null) {
				PlSql plIns = inscripcionDAO.insertInscripcionClaveRastreo(inscripcionTO, acreedor, inscripcion.getIdentificador().getClaveRastreo(), idArchivo);
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
			mx.gob.se.rug.masiva.resultado.to.TramiteRes tramiteRes) {
		ControlError regresa = null;
		for (Otorgante otorgante : otorgantes) {
			regresa = agregaOtorganteInd(otorgante, idUsuario,
					tramite.getIdAcreedor(), tramite.getIdTramite(),
					tramite.getConsecutivo(), tramiteRes);
			if (regresa != null) {
				break;
			}
		}

		return regresa;
	}
	
	private ControlError verificaDatosOtorgante(RectificacionPorError rectificacionPorError,
			Tramite tramite,
			mx.gob.se.rug.masiva.resultado.to.TramiteRes tramiteRes) {
		ControlError regresa = null;
		int otorganteSize=0;
		try {
			 otorganteSize=rectificacionPorError.getPartesRectificacion().getOtorgante().size();
		} catch (Exception e) {
			otorganteSize=0;
		}
		if(otorganteSize>0){
			for (Otorgante otorgante : rectificacionPorError.getPartesRectificacion().getOtorgante()) {
				regresa = agregaOtorganteInd(otorgante, idUsuario,
						tramite.getIdAcreedor(), tramite.getIdTramite(),
						tramite.getConsecutivo(), tramiteRes);
				if (regresa != null) {
					break;
				}
			}
		}
		return regresa;
	}
	
	private ControlError verificaDatosAcreedorAdicional(RectificacionPorError rectificacionPorError,Tramite tramite) {
		ControlError regresa = null;
		int acreedoresAdic=0;
		try {
			acreedoresAdic=rectificacionPorError.getPartesRectificacion().getAcreedorAdicional().size();
		} catch (Exception e) {
			acreedoresAdic=0;
		}
		if(acreedoresAdic>0){
				regresa = agregarAcreedoresAdicionales(rectificacionPorError.getPartesRectificacion().getAcreedorAdicional(),tramite);
				if(regresa.getPlSql().getIntPl()==0){
					regresa=null;
				}
		}
		return regresa;
	}

	private ControlError agregaOtorganteInd(Otorgante otorgante, Integer idUsuario, Integer idAcreedor, Integer idInscripcion,
			Integer numInscripcion,	mx.gob.se.rug.masiva.resultado.to.TramiteRes tramite) {
		ControlError regresa = null;

		try {
			AltaParteDAO altaParteDAO = new AltaParteDAO();
		System.out.println("RUG-Action:CargaMasivaAction.agregaOtorgante------ Iniciamos agregarOtorgante ::::");
		if (otorgante == null) {
			System.out
					.println("RUG-Action:CargaMasivaAction.agregaOtorgante------ La inscripcion no cuenta con un otorgante ::::");
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setResStrPl("La inscripcion no cuenta con un Otorgante");
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(numInscripcion);
		} else {
			System.out.println("Folop Electronio :::"
					+ otorgante.getFolioElectronico());
			System.out.println("tipo persona::"
					+ otorgante.getTipoPersona());
			if(otorgante.getTipoPersona().trim().equals("PF")||otorgante.getTipoPersona().trim().equals("PM")){
					if (otorgante.getTipoPersona().trim().equals("PF")
							&& otorgante.getFolioElectronico() != null
							&& !otorgante.getFolioElectronico().trim().equals("")) {
						OtorganteTO otorganteTO = altaParteDAO
								.getOtorganteFisicoByFolioElectronico(otorgante
										.getFolioElectronico());
						if (otorganteTO != null) {
							System.out.println("RUG-Action:CargaMasivaAction.agregaOtorgante------ Se relaciona con el folio electronico ::::");
							if (!altaParteDAO.relParte(otorganteTO.getIdOtorgante(), idInscripcion, 1, null)) {
								regresa = new ControlError();
								PlSql plSql = new PlSql();
								plSql.setResStrPl("No se pudo relacionar al otorgante");
								plSql.setStrPl("No se pudo relacionar al otorgante");
								plSql.setIntPl(591);
								plSql.setResIntPl(887);
								regresa.setPlSql(plSql);
								regresa.setIdInscripcion(numInscripcion);
		
							}
						} else {
							System.out
									.println("RUG-Action:CargaMasivaAction.agregaOtorgante------ el Folio electronico proporcionado es invalido ::::");
							regresa = new ControlError();
							PlSql plSql = new PlSql();
							plSql.setResStrPl("El Folio Electronico no existe");
							plSql.setStrPl("El Folio Electronico no existe");
							plSql.setIntPl(591);
							plSql.setResIntPl(887);
							regresa.setPlSql(plSql);
							regresa.setIdInscripcion(numInscripcion);
		
						}
					} else {
						System.out
								.println("RUG-Action:CargaMasivaAction.agregaOtorgante------ Se trata de agregar una nueva persona ::::");
						AltaParteTO altaParteTO = new AltaParteTO();
						altaParteTO.setIdParte(1);
						altaParteTO.setIdTramite(idInscripcion);
						altaParteTO.setFolioMercantil(otorgante.getFolioElectronico());
						altaParteTO.setCurp(otorgante.getCurp());
						altaParteTO.setNombre(otorgante.getNombre());
						altaParteTO.setApellidoMaterno(otorgante.getApellidoMaterno());
						altaParteTO.setApellidoPaterno(otorgante.getApellidoPaterno());
						altaParteTO.setRazonSocial(otorgante.getDenominacionRazonSocial());
						altaParteTO.setRfc(otorgante.getRfc());
						altaParteTO.setIdNacionalidad(otorgante.getIdNacionalidad().intValue());
						altaParteTO.setTipoPersona(otorgante.getTipoPersona());
						altaParteTO.setHayDomicilio("F");
						altaParteTO.setIdUsuario(idUsuario);
						altaParteTO.setIdPersona(idUsuario);
						if (altaParteTO.getTipoPersona().trim().equals("PM")) {
							System.out.println("es una persona moral");
							if (altaParteTO.getFolioMercantil() == null
									|| altaParteTO.getFolioMercantil().trim()
											.equals("")) {
								System.out
										.println("El folio electronico es obligatorio");
								regresa = new ControlError();
								PlSql plSql = new PlSql();
								plSql.setResStrPl("Error en el Otorgante:  El campo folio electronico es obligatorio.");
								plSql.setStrPl("Error en el Otorgante:  El campo folio electronico es obligatorio.");
								plSql.setIntPl(2);
								regresa.setPlSql(plSql);
								regresa.setIdInscripcion(numInscripcion);
							} else {
								PlSql intPer = altaParteDAO.insert(altaParteTO);
								if (intPer == null) {
									System.out
											.println("RUG-Action:CargaMasivaAction.agregaOtorgante------ No se pudo generar la nueva persona ::::");
									regresa = new ControlError();
									PlSql plSql = new PlSql();
									plSql.setResStrPl("Error en el Otorgante - No se pudo agregar al otorgante");
									plSql.setStrPl("Error en el Otorgante - No se pudo agregar al otorgante");
									regresa.setPlSql(plSql);
									regresa.setIdInscripcion(numInscripcion);
								} else {
									if (intPer.getIntPl().intValue() != 0) {
										System.out
												.println("RUG-Action:CargaMasivaAction.agregaOtorgante------ la nueva persona fue dada de alta con el siguiente ID  :::: "
														+ intPer.getIntPl()
																.intValue());
										regresa = new ControlError();
										regresa.setPlSql(intPer);
									}
								}
							}
		
						} else {
							PlSql intPer = altaParteDAO.insert(altaParteTO);
							if (intPer == null) {
								System.out
										.println("RUG-Action:CargaMasivaAction.agregaOtorgante------ No se pudo generar la nueva persona ::::");
								regresa = new ControlError();
								PlSql plSql = new PlSql();
								plSql.setResStrPl("Error en el Otorgante - No se pudo agregar al otorgante");
								plSql.setStrPl("Error en el Otorgante - No se pudo agregar al otorgante");
								regresa.setPlSql(plSql);
								regresa.setIdInscripcion(numInscripcion);
							} else {
								if (intPer.getIntPl().intValue() != 0) {
									regresa = new ControlError();
									regresa.setPlSql(intPer);
								} else {
									FolioElectronicoDAO folioElectronicoDAO = new FolioElectronicoDAO();
									String strMsj = folioElectronicoDAO
											.creaFolioElectronico(intPer
													.getResIntPl().intValue());
									if (strMsj != null) {
										mx.gob.se.rug.masiva.resultado.to.Otorgante otorgante2 = new mx.gob.se.rug.masiva.resultado.to.Otorgante();
										otorgante2.setCurp(altaParteTO.getCurp());
										otorgante2.setFolioElectronico(strMsj);
										System.out
												.println("--se genero el siguiente folio electronico de persona fisica -- "
														+ strMsj);
										otorgante2.setNombreCompleto(altaParteTO
												.getNombre()
												+ " "
												+ altaParteTO.getApellidoPaterno()
												+ " "
												+ altaParteTO.getApellidoMaterno());
										tramite.getOtorgante().add(otorgante2);
									} else {
										regresa = new ControlError();
										PlSql plSql = new PlSql();
										plSql.setIntPl(501);
										plSql.setResStrPl("Error al tratar de generar folio del RUG, reportarlo al area de sistemas");
										plSql.setStrPl("Error al tratar de generar folio del RUG, reportarlo al area de sistemas");
										regresa.setPlSql(plSql);
										regresa.setIdInscripcion(numInscripcion);
									}
		
								}
							}
						}
					}
			}else{
		throw new CargaMasivaException(33);
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
			plSql.setIntPl(999);
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
					altaParteTO.setHayDomicilio("F");
					altaParteTO.setIdNacionalidad(getIntegerFromBigIntger(deudor.getIdNacionalidad()));
					altaParteTO.setIdPersona(Integer.valueOf(idUsuario));
					altaParteTO.setIdPersona(idUsuario);
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

	private ControlError agregarDeudores(RectificacionPorError rectificacionPorError, Tramite tramite) {
		ControlError regresa = null;
		int deudorSize=0;
		try {
			deudorSize=rectificacionPorError.getPartesRectificacion().getOtorgante().size();
		} catch (Exception e) {
			deudorSize=0;
		}
		try {
			if (deudorSize > 0) {
				Iterator<Deudor> itDe = rectificacionPorError.getPartesRectificacion().getDeudor().iterator();
				PlSql plSql = new PlSql();
				plSql.setIntPl(0);
				AltaParteDAO altaParteDAO = new AltaParteDAO();
				while (plSql.getIntPl().intValue() == 0 && itDe.hasNext()) {
					Deudor deudor = itDe.next();
					AltaParteTO altaParteTO = new AltaParteTO();
					altaParteTO.setIdParte(2);
					altaParteTO.setIdTramite(tramite.getIdTramite());
					try{
						altaParteTO.setRazonSocial(deudor.getDenominacionRazonSocial());
					}catch(Exception e){
						altaParteTO.setRazonSocial(null);
					}
					altaParteTO.setTipoPersona(deudor.getTipoPersona());
					try{
						altaParteTO.setNombre(deudor.getNombre());
					}catch(Exception e){
						altaParteTO.setNombre(null);
					}
					try{
						altaParteTO.setApellidoPaterno(deudor.getApellidoPaterno());
					}catch(Exception e){
						altaParteTO.setApellidoPaterno(null);
					}
					
					try{
					}catch(Exception e){
						altaParteTO.setApellidoMaterno(null);
					}
					altaParteTO.setHayDomicilio("F");
					altaParteTO.setIdNacionalidad(deudor.getIdNacionalidad().intValue());
					altaParteTO.setIdPersona(Integer.valueOf(tramite.getIdUsuario()));
					plSql = altaParteDAO.insert(altaParteTO);
				}
				if (plSql.getIntPl().intValue() != 0) {
					regresa = new ControlError();
					regresa.setPlSql(plSql);
					regresa.setIdInscripcion(tramite.getConsecutivo());
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
			regresa.setIdInscripcion(tramite.getConsecutivo());
			e.printStackTrace();
		} catch (Exception e) {
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setResStrPl(e.getMessage() + ", " + e.getCause());
			plSql.setStrPl(e.getMessage() + ", " + e.getCause());
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(tramite.getConsecutivo());
			e.printStackTrace();
		}
		
		return regresa;
		
	}

	private ControlError agregarAcreedoresAdicionales(
			List<AcreedorAdicional> acreedores, Tramite tramite) {
		ControlError regresa = null;
		try {
			if (acreedores.size() > 0) {
				Iterator<AcreedorAdicional> itDe = acreedores.iterator();
				PlSql plSql = new PlSql();
				plSql.setIntPl(0);
				AltaParteDAO altaParteDAO = new AltaParteDAO();
				while (plSql.getIntPl().intValue() == 0 && itDe.hasNext()) {
					AcreedorAdicional acreedoAdicional = itDe.next();
					AltaParteTO altaParteTO = new AltaParteTO();
						altaParteTO.setDomicilioUno(acreedoAdicional.getDomicilioExtranjeroUno());
						altaParteTO.setDomicilioDos(acreedoAdicional.getDomicilioExtranjeroDos());
						altaParteTO.setPoblacion(acreedoAdicional.getPoblacion());
						altaParteTO.setZonaPostal(acreedoAdicional.getZonaPostal());
						altaParteTO.setIdParte(3);
						altaParteTO.setIdPersona(idUsuario);
						altaParteTO.setIdTramite(tramite.getIdTramite());
						altaParteTO.setRfc(acreedoAdicional.getRfc());
						altaParteTO.setNombre(acreedoAdicional.getNombre());
						altaParteTO.setApellidoMaterno(acreedoAdicional.getApellidoMaterno());
						altaParteTO.setApellidoPaterno(acreedoAdicional.getApellidoPaterno());
						altaParteTO.setRazonSocial(acreedoAdicional.getDenominacionRazonSocial());
						altaParteTO.setIdNacionalidad(getIntegerFromBigIntger(acreedoAdicional.getIdNacionalidad()));
						altaParteTO.setTipoPersona(acreedoAdicional.getTipoPersona());
						altaParteTO.setHayDomicilio("V");
						altaParteTO.setIdColonia(getIntegerFromBigIntger(acreedoAdicional.getIdColonia()));
						altaParteTO.setIdLocalidad(getIntegerFromBigIntger(acreedoAdicional.getIdLocalidad()));
						altaParteTO.setCalle(acreedoAdicional.getCalle());
						altaParteTO.setNumeroExterior(acreedoAdicional.getNumeroExterior());
						altaParteTO.setNumeroInterior(acreedoAdicional.getNumeroInterior());
						altaParteTO.setCorreoElectronico(acreedoAdicional.getCorreoElectronico());
						altaParteTO.setTelefono(acreedoAdicional.getTelefono());
						altaParteTO.setExtencion(acreedoAdicional.getTelefonoExtension());
						altaParteTO.setIdPersona(idUsuario);
						altaParteTO.setIdUsuario(idUsuario);

					plSql = altaParteDAO.insert(altaParteTO);
				}
				if (plSql.getIntPl().intValue() != 0) {
					regresa = new ControlError();
					regresa.setPlSql(plSql);
					regresa.setIdInscripcion(tramite.getIdTramite());
				}

			}
		} catch (NumberFormatException e) {
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setResStrPl("Error al tratar de convertir a entero:"
					+ e.getMessage() + ", " + e.getCause());
			plSql.setStrPl("Error al tratar de convertir a entero:"
					+ e.getMessage() + ", " + e.getCause());
			plSql.setIntPl(999);
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(tramite.getIdTramite());
			e.printStackTrace();
		} catch (Exception e) {
			regresa = new ControlError();
			PlSql plSql = new PlSql();
			plSql.setResStrPl(e.getMessage() + ", " + e.getCause());
			plSql.setStrPl(e.getMessage() + ", " + e.getCause());
			regresa.setPlSql(plSql);
			regresa.setIdInscripcion(tramite.getIdTramite());
			e.printStackTrace();
		}

		return regresa;

	}

	private ControlError agregaGarantia(Inscripcion inscripcion,
			Integer idUsuario, Integer idAcreedor, Integer idInscripcion,
			Integer numInscripcion) {
		ControlError regresa = null;
		System.out.println("RUG-Action:CargaMasivaAction.agregaGarantia------Se trate de dar de alta la nueva garantia :::: ");
		try {
			
			
		
				
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
			
			
			Date fechaCreacionCelebracion=null;
			
			if(garantia.getCreacion().getFechaCelebracion() != null){
				fechaCreacionCelebracion = new Date(garantia.getCreacion().getFechaCelebracion().toGregorianCalendar().getTime().getTime());
				garantiaServiceImpl.validaDatos(fechaCreacionCelebracion, "FechaCelebracion", false, true, 1);
			}else{
				throw new CargaMasivaException(63);
			}
			actoContratoTO.setFechaCelebracion(dateUtilRug.formatDate(fechaCreacionCelebracion));
			
			
			actoContratoTO.setMontoMaximo(creacion.getMontoMaximo().toString());
			actoContratoTO.setTipoBienes(listaTipoBienMuebleToString(creacion.getTipoBienMueble()));
			actoContratoTO.setDescripcion(creacion.getDescripcionBienesMuebles());
			actoContratoTO.setOtrosTerminos(creacion.getTerminosCondiciones());
			System.out.println("RUG:CargaMasivaAction.garantiaTOgarantiaTO--------------------------valor del id moneda::"+ creacion.getIdMoneda());
			actoContratoTO.setTipoMoneda(creacion.getIdMoneda().toString());
			actoContratoTO.setCambiosBienesMonto(creacion.getbDatosModificables());
			garantiaTO.setActoContratoTO(actoContratoTO);
			ObligacionTO obligacionTO = new ObligacionTO();
			obligacionTO.setTipoActoContrato(obligacion.getActoContrato().toString());
			
			
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
			obligacionTO.setFechaTerminacion(dateUtilRug.parseDateToStr(fechaObligacionTerminacion));
			obligacionTO.setOtrosTerminos(obligacion.getTerminos());
			garantiaTO.setObligacionTO(obligacionTO);
			garantiaTO.setIdMoneda(creacion.getIdMoneda().intValue());
		} catch (NoDateInfrastructureException e) {

		}
		return garantiaTO;
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

	private String getSha1FromFile(File in) {
		String regresa = null;
		try {
			regresa = getDigest(getBytesFromFile(in));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return regresa;
	}

	private byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		byte[] bytes = new byte[(int) length];
		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}
		// Close the input stream and return bytes
		is.close();
		return bytes;
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

	public File getArchivo() {
		return archivo;
	}

	public void setArchivo(File archivo) {
		this.archivo = archivo;
	}

	public String getArchivoContentType() {
		return archivoContentType;
	}

	public void setArchivoContentType(String archivoContentType) {
		this.archivoContentType = archivoContentType;
	}

	public String getArchivoFileName() {
		return archivoFileName;
	}

	public void setArchivoFileName(String archivoFileName) {
		this.archivoFileName = archivoFileName;
	}

	public List<AcreedorTO> getListaAcreedores() {
		return listaAcreedores;
	}

	public void setListaAcreedores(List<AcreedorTO> listaAcreedores) {
		this.listaAcreedores = listaAcreedores;
	}

	public List<TipoProcesoCargaMasiva> getListaProceso() {
		if (listaProceso == null) {
			listaProceso = new ArrayList<TipoProcesoCargaMasiva>();
			TipoProcesoCargaMasiva tipoProceso = new TipoProcesoCargaMasiva();
			tipoProceso.setId(1);
			tipoProceso.setMensaje("Atendido");
			listaProceso.add(tipoProceso);
			tipoProceso = new TipoProcesoCargaMasiva();
			tipoProceso.setId(2);
			tipoProceso.setMensaje("Desatendido");
			listaProceso.add(tipoProceso);

		}
		return listaProceso;
	}

	public void setListaProceso(List<TipoProcesoCargaMasiva> listaProceso) {
		this.listaProceso = listaProceso;
	}

	public String getErrorArchivo() {
		return errorArchivo;
	}

	public void setErrorArchivo(String errorArchivo) {
		this.errorArchivo = errorArchivo;
	}

	public List<Integer> getListaTramitesErrores() {
		return listaTramitesErrores;
	}

	public void setListaTramitesErrores(List<Integer> listaTramitesErrores) {
		this.listaTramitesErrores = listaTramitesErrores;
	}

	public Integer getIdAcreedorTO() {
		return idAcreedorTO;
	}

	public void setIdAcreedorTO(Integer idAcreedorTO) {
		this.idAcreedorTO = idAcreedorTO;
	}
	
	public Integer getIdAcreedor() {
		return idAcreedor;
	}

	public void setIdAcreedor(Integer idAcreedor) {
		this.idAcreedor = idAcreedor;
	}

	public ResultadoCargaMasiva getResultado() {
		return resultado;
	}

	public void setResultado(ResultadoCargaMasiva resultado) {
		this.resultado = resultado;
	}

	public Integer getIdListaProceso() {
		return idListaProceso;
	}

	public void setIdListaProceso(Integer idListaProceso) {
		this.idListaProceso = idListaProceso;
	}

	public Integer getIdListaTramite() {
		return idListaTramite;
	}

	public void setIdListaTramite(Integer idListaTramite) {
		this.idListaTramite = idListaTramite;
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
	
	public Integer getIdArchivoResultado() {
		return idArchivoResultado;
	}

	public void setIdArchivoResultado(Integer idArchivoResultado) {
		this.idArchivoResultado = idArchivoResultado;
	}

	public ControlError getCerror() {
		return cerror;
	}

	public void setCerror(ControlError cerror) {
		this.cerror = cerror;
	}

	public List<ControlError> getListaErrores() {
		return listaErrores;
	}

	public void setListaErrores(List<ControlError> listaErrores) {
		this.listaErrores = listaErrores;
	}

	public List<mx.gob.se.rug.masiva.resultado.to.TramiteRes> getInscripcionesErroneas() {
		return inscripcionesErroneas;
	}

	public void setInscripcionesErroneas(
			List<mx.gob.se.rug.masiva.resultado.to.TramiteRes> inscripcionesErroneas) {
		this.inscripcionesErroneas = inscripcionesErroneas;
	}

	public List<Integer> getListaTramites() {
		return listaTramites;
	}

	public void setListaTramites(List<Integer> listaTramites) {
		this.listaTramites = listaTramites;
	}

	public List<TipoTramiteCargaMasiva> getListaTipoTramite() {
		return listaTipoTramite != null ? listaTipoTramite : new MasivaDAO()
				.getListaTipoTramite();
	}

	public void setListaTipoTramite(
			List<TipoTramiteCargaMasiva> listaTipoTramite) {
		this.listaTipoTramite = listaTipoTramite;
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
	
	public Integer getTramitesIncorrectosFiltro() {
		return tramitesIncorrectosFiltro;
	}

	public void setTramitesIncorrectosFiltro(Integer tramitesIncorrectosFiltro) {
		this.tramitesIncorrectosFiltro = tramitesIncorrectosFiltro;
	}

	public String getMensajeErroXsd() {
		return mensajeErroXsd;
	}

	public void setMensajeErroXsd(String mensajeErroXsd) {
		this.mensajeErroXsd = mensajeErroXsd;
	}

	
	
	public String getDetalleTecnico() {
		return detalleTecnico;
	}




	public void setDetalleTecnico(String detalleTecnico) {
		this.detalleTecnico = detalleTecnico;
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

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public static boolean match2XmltoXsd(File xml) {
		System.out.println("-- start matchXmltoXsd--");
		boolean regresa = false;
		SchemaFactory factory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
		try {
			File fileXsd = new File("c:\\cargaMasiva\\carga-masiva.xsd");
			Schema schema = factory.newSchema(fileXsd);
			Validator validator = schema.newValidator();
			Source source = new StreamSource(xml);
			validator.validate(source);
			regresa = true;
		} catch (SAXException ex) {
			System.out.println("mensaje:::" + ex.getMessage());
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return regresa;
	}

	public static void main(String args[]) {
		match2XmltoXsd(new File("C:\\cargaMasiva\\Modificacion.xml"));
	}

	public Double getCostoTramiteMasivo() {
		return costoTramiteMasivo;
	}

	public void setCostoTramiteMasivo(Double costoTramiteMasivo) {
		this.costoTramiteMasivo = costoTramiteMasivo;
	}

}
