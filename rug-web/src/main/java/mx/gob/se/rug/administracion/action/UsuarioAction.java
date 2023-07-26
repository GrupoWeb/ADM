package mx.gob.se.rug.administracion.action;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;

import mx.gob.economia.dgi.framework.password.dto.Password;
import mx.gob.economia.dgi.framework.password.service.PasswordService;
import mx.gob.se.rug.acreedores.service.AcreedoresCatalogosService;
import mx.gob.se.rug.administracion.dto.RegistroUsuario;
import mx.gob.se.rug.administracion.service.UsuarioService;
import mx.gob.se.rug.common.dto.Mensaje;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.dto.DatosContacto;
import mx.gob.se.rug.dto.PersonaFisica;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.inscripcion.to.NacionalidadTO;
import mx.gob.se.rug.job.service.impl.jobAvisoPSerImpl;
import mx.gob.se.rug.job.to.jobAvisoTO;
import mx.gob.se.rug.mailservice.MailRegistroService;
import mx.gob.se.rug.operaciones.to.OperacionesTO;
import mx.gob.se.rug.seguridad.dao.PrivilegiosDAO;
import mx.gob.se.rug.seguridad.to.PrivilegioTO;
import mx.gob.se.rug.seguridad.to.PrivilegiosTO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import gt.gob.rgm.domain.Usuario;
import gt.gob.rgm.files.service.FilesService;
import gt.gob.rgm.model.Archivo;
import gt.gob.rgm.model.RechazoCuenta;
import gt.gob.rgm.model.RugSecuUsuarios;
import gt.gob.rgm.service.ArchivoService;
import gt.gob.rgm.service.RechazoCuentaService;
import gt.gob.rgm.service.UsuariosService;

//import gt.gob.rgm.sat.ws.Contribuyente;
//import gt.gob.rgm.sat.ws.ContribuyenteService;

@SuppressWarnings("serial")
public class UsuarioAction extends RugBaseAction /* implements CaptchaAction */{

	private static final String SUCCESS = "success";

	private static final String KEY_CAPTCHA_PARAMS = "captchaParams";
	private PersonaFisica personaFisica;
	private RegistroUsuario registroUsuario;
	private UsuarioService usuarioService;
	private PasswordService passwordService;
	private static final String OPERACION_I = "I";
	// private CaptchaService captchaService;
	private String nombre;
	private String captchaKey;
	private MailRegistroService mailRegistroService;
	private AcreedoresCatalogosService acreedoresCatalogosService;
	private PersonaFisica personaFisicaRfc;
	private List<String> listaUsuarios;
	private File upload;
    private String uploadFileName;
    private String uploadContentType;

    
	private List<String> genero;
	
	private List <jobAvisoTO> listaJobAvisoPreventivo;
	private List <String> listaStatusJob;
	

	private jobAvisoTO jobAviso = new jobAvisoTO() ;
	//private Integer IdJob;
	//private String jobName;
	private String estado;
	private String periodicidad;
	//private String UltEjecucion;
	//private String ProxEjecucion;
	private String email;	
	private String otraPregunta;
	private List<Usuario> usuarios;
	private Usuario usuario;
	private Long idPersona;
	private String causa;
	private String nitAnterior;
	
	//@Autowired
	FilesService filesService;
	
	ArchivoService archivoSvc;

	UsuariosService usuariosBusinessService;
	RechazoCuentaService rechazoService;


	public String getPeriodicidad() {
		return periodicidad;
	}

	public void setPeriodicidad(String periodicidad) {
		this.periodicidad = periodicidad;
	}

	public String getCaptchaKey() {
		return captchaKey;
	}

	public void setCaptchaKey(String captchaKey) {
		this.captchaKey = captchaKey;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setPasswordService(PasswordService passwordService) {
		this.passwordService = passwordService;
	}

	public PersonaFisica getPersonaFisica() {
		return personaFisica;
	}

	public void setPersonaFisica(PersonaFisica personaFisica) {
		this.personaFisica = personaFisica;
	}

	public RegistroUsuario getRegistroUsuario() {
		return registroUsuario;
	}

	public void setRegistroUsuario(RegistroUsuario registroUsuario) {
		this.registroUsuario = registroUsuario;
	}

	public List<String> getListaUsuarios() {

		listaUsuarios = new ArrayList<String>();
		//listaUsuarios.add("Autoridad");
		// listaUsuarios.add("Soporte T�cnico");
		listaUsuarios.add("Representante de Acreedores");

		return listaUsuarios;
	}

	public List<String> getGenero() {

		genero = new ArrayList<String>();
		genero.add("Masculino");
		genero.add("Femenino");

		return genero;
	}
	
	public List<String> getListaStatusJob() {
		 listaStatusJob = new ArrayList<String>();
		 listaStatusJob.add("ACTIVO");
		 listaStatusJob.add("INACTIVO");
		 return listaStatusJob;
	}

	/*
	 * public List<String> getNacionalidades() {
	 * 
	 * List<String> nacionalidades = new ArrayList<String>();
	 * nacionalidades.add("MEX"); nacionalidades.add("EXT");
	 * 
	 * return nacionalidades;
	 * 
	 * }
	 */

	public List<NacionalidadTO> getNacionalidades() {
		return usuarioService.getNacionalidades();
	}

	public List<RegistroUsuario> getPreguntas() throws Exception {
		String lenguaje = getLenguaje();

		MyLogger.Logger.log(Level.INFO, "IMPRIME EL LENGUAJE : " + lenguaje);
		List<RegistroUsuario> preguntas = usuarioService.getPreguntas(lenguaje);
		RegistroUsuario otraPregunta = new RegistroUsuario();
		otraPregunta.setPregunta("Agregar otra");
		preguntas.add(otraPregunta);
		return preguntas;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String addExtranjero() {
		PrivilegiosDAO privilegiosDAO = new PrivilegiosDAO();
		PrivilegiosTO privilegiosTO = new PrivilegiosTO();
		privilegiosTO.setIdRecurso(new Integer(9));
		privilegiosTO = privilegiosDAO.getPrivilegios(privilegiosTO,
				(UsuarioTO) sessionMap.get(Constants.USUARIO));
		Map<Integer, PrivilegioTO> priv = privilegiosTO.getMapPrivilegio();
		if (priv.get(new Integer(34)) != null) {
			return SUCCESS;
		} else {
			return ERROR;
		}
	}
	
	
	public String buscarJobMod(){
		jobAvisoPSerImpl jobAviso = new jobAvisoPSerImpl();
		String regresa = "failed";
		HttpServletRequest request = ServletActionContext.getRequest();
		String IdJobS = request.getParameter("idJob");
		Integer IdJobi =Integer.parseInt(IdJobS);

		try {
			listaJobAvisoPreventivo = jobAviso.buscarJobAvisoPrev(IdJobi);
			setListaJobAvisoPreventivo(listaJobAvisoPreventivo);
		} catch (NoDataFoundException e) {
			e.printStackTrace();
		}
				
		if (listaJobAvisoPreventivo.size()>0) {
			regresa = "success";		
		}		
		return regresa ;
	}
	
	public String detallejobAviso(){
		jobAvisoPSerImpl jobAviso= new jobAvisoPSerImpl();
		String regresa = "failed";
		try {	
			String DATE_FORMAT = "dd/MM/yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			//////// se modifica el retorno de la funcion a lista de jobs 
			
			listaJobAvisoPreventivo = jobAviso.getDatosjobAvisoPreventivo(/*IdJob*/);
			setListaJobAvisoPreventivo(listaJobAvisoPreventivo);
			//jobAvisoPre = jobAviso.getDatosjobAvisoPreventivo(IdJob);
			//setJobName(jobAvisoPre.getJobName());
			//setEstado(jobAvisoPre.getEstado());
			//setPeriodicidad(jobAvisoPre.getPeriodicidad());
			//setUltEjecucion(sdf.format(jobAvisoPre.getUltEjecucion()));
			//setProxEjecucion(sdf.format(jobAvisoPre.getProxEjecucion()));		
		
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		if (listaJobAvisoPreventivo.size()>0) {
			regresa = "success";		
		}
		
		return regresa;
	}
	
	//////*----- Se agrega funcion para actualizar el job 
	public String actualizaJobAviso(){
		jobAvisoPSerImpl jobAviso= new jobAvisoPSerImpl();
		PlSql regresa = new PlSql();
		String resul = "failed";
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String idJob = request.getParameter("idJob");
		
		estado = validaEstado (estado);
		
		try {	
			
			regresa = jobAviso.actualizaJobAvisoPreventivo(Integer.parseInt(idJob),estado,periodicidad);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}		
		
		if (regresa.getIntPl()==0){
			
			resul = "success";
		}
		return resul;
	}
	
	
	public String ejecutaJobAviso(){
		jobAvisoPSerImpl jobAviso= new jobAvisoPSerImpl();
		PlSql regresa = new PlSql();
		String resultado = "failed";
		HttpServletRequest request = ServletActionContext.getRequest();
		
		String idJob = request.getParameter("idJob");
		try {	
			
			regresa = jobAviso.ejecutarJobAvisoPreventivo(Integer.parseInt(idJob));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}		
		
		if (regresa.getIntPl()==0){
			resultado ="success";
		}
		
		return resultado;
	}
	
	
	
	public String add() {
		HttpServletRequest request = ServletActionContext.getRequest();
		sessionMap.remove(Constants.CAPTCHA_KEY);
		MyLogger.Logger.log(Level.INFO, "-- add --");
		StringBuffer uri = request.getRequestURL();

		int posicion = uri.indexOf("/", 7);
		String url = uri.substring(0, posicion);

		return SUCCESS;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public String saveExtranjero() throws Exception {
		MyLogger.Logger.log(Level.INFO, "-- save --");

		// construccion de uri
		HttpServletRequest request = ServletActionContext.getRequest();
		StringBuffer uri = request.getRequestURL();
		int posicion = uri.indexOf("/", 7);
		MyLogger.Logger.log(Level.INFO, "POSICION: " + posicion);
		String url = uri.substring(0, posicion);
		MyLogger.Logger.log(Level.INFO, "IMPRIME LA URL" + url);
		registroUsuario.setUri(url);
		MyLogger.Logger.log(Level.INFO, "--obteniedo nuevo parametro uri: --"
				+ registroUsuario.getUri());

		UsuarioTO usuarioTO = (UsuarioTO) sessionMap.get(Constants.USUARIO);
		personaFisica.setClaveUsuarioPadre(usuarioTO.getPersona()
				.getCorreoElectronico());
		String mail = personaFisica.getDatosContacto().getEmailPersonal();
		personaFisica.getDatosContacto().setEmailPersonal(mail.toLowerCase());
		registroUsuario.setTipoOperacion(OPERACION_I);
		registroUsuario.setPassword("temporal123");
		if (registroUsuario.getTipoUsuario().equals("Soporte T�cnico")) {
			registroUsuario.setIdGrupo("16");
			registroUsuario.setTipoUsuario("ACREEDOR");
		}
		if (registroUsuario.getTipoUsuario().equals("Autoridad")) {
			registroUsuario.setIdGrupo("4");
		}
		if (registroUsuario.getTipoUsuario().equals(
				"Representante de Acreedores")) {
			registroUsuario.setIdGrupo("15");
			registroUsuario.setTipoUsuario("ACREEDOR");
		}

		MyLogger.Logger.log(Level.INFO,
				"imprime rfc: " + personaFisica.getRfc());
		personaFisicaRfc = usuarioService.getConsultaRfc(personaFisica);
		if (personaFisicaRfc.getRfc() != null) {
			MyLogger.Logger.log(Level.INFO, "--existe RFC--");
			addActionError(getText("usuario.msg.registro.rfc.error"));
		} else {
			MyLogger.Logger.log(Level.INFO, "--NOO existe RFC--");
			int regresa = usuarioService.altaExtranjero(personaFisica,
					registroUsuario);
			switch (regresa) {
			case 0:
				addActionMessage(getText("usuario.msg.registro.success"));
				// ENVIAR MAIL
				mailRegistroService.sendMailRegistroRepAcreedores(personaFisica,
						registroUsuario);
				return SUCCESS;
			case 1:
				addActionError(getText("usuario.msg.registro.correo.error"));
				break;
			default:
				addActionError(getText("usuario.msg.activacion.error"));
				break;
			}
		}
		return ERROR;
	}

	public String save() throws Exception {

		String captchaKeyOri = (String) sessionMap.get(Constants.CAPTCHA_KEY);
		HttpServletRequest request = ServletActionContext.getRequest();
		StringBuffer uri = request.getRequestURL();
//		int posicion = uri.indexOf("/", 7);
		String url = uri.toString().replace("usuario/save.do", "");
		String url2 = uri.toString().replace(Constants.URL_PATH_REPACE, "");
		registroUsuario.setUri(url);
		if (getCaptchaKey().equalsIgnoreCase(captchaKeyOri)) {
			if(upload == null) {
				addActionError(getText("usuario.msg.registro.error.file"));
				return ERROR;
			}
			String mail = personaFisica.getDatosContacto().getEmailPersonal();
			personaFisica.getDatosContacto().setEmailPersonal(mail.toLowerCase().trim());
			registroUsuario.setTipoOperacion(OPERACION_I);
			personaFisicaRfc = usuarioService.getConsultaRfc(personaFisica);
			if (personaFisicaRfc.getRfc() != null) {
				addActionError(getText("usuario.msg.registro.rfc.error"));
			} else {
				PersonaFisica personaFisicaDocid = usuarioService.getConsultaDocId(personaFisica);
				if(personaFisicaDocid.getDocumentoIdentificacion() != null) {
					addActionError(getText("usuario.msg.registro.docid.error"));
				} else {
					LocalDateTime now = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					String formatDateTime = now.format(formatter);
					String strToken = formatDateTime + "#" + mail;
					byte[] encodedBytes = Base64.getEncoder().encode(strToken.getBytes());
					String token = new String(encodedBytes);
					personaFisica.setToken(token);
					if(registroUsuario.getPregunta().equals("Agregar otra")) {
						registroUsuario.setPregunta(otraPregunta);
					}
					Mensaje mensaje = usuarioService.save(personaFisica, getRegistroUsuario());
					if (mensaje != null) {
						if (mensaje.getRespuesta() == 0) {
							addActionMessage(getText("usuario.msg.registro.success"));
							sessionMap.remove(KEY_CAPTCHA_PARAMS);
							try {
								mailRegistroService.sendEmailRegister(personaFisica, registroUsuario, url2);
							} catch(Exception e) {
								e.printStackTrace();
							}

							if(upload != null) {
								Constants c = new Constants();
								String basepath = c.getParamValue(Constants.FS_BASEPATH);
								String fileUrl = filesService.saveFile(new FileInputStream(upload), basepath, upload.getName(), personaFisica.getDocumentoIdentificacion(), true);
								PersonaFisica nuevaPersona = acreedoresCatalogosService.getRegistro(personaFisica.getDatosContacto().getEmailPersonal());
								Archivo archivo = new Archivo();
								archivo.setObjetoId(nuevaPersona.getIdPersona());
								archivo.setTipo("DI");
								archivo.setUrl(fileUrl);
								archivo.setEstado("AC");
								Instant instant = Instant.now();
								archivo.setFecha(new Timestamp(instant.toEpochMilli()));
								
								archivoSvc.add(archivo);
							}
							return SUCCESS;
						}
						if (mensaje.getRespuesta() == 1) {
							addActionError(mensaje.getMensaje());
							return ERROR;
						}
					}
					addActionError(getText("usuario.msg.registro.error"));
				}
			}
		} else {
			setCaptchaKey("");
			addActionError(getText("usuario.msg.registro.error.captcha"));
		}
		return ERROR;
	}

	public String activation() throws Exception {
		MyLogger.Logger.log(Level.INFO, "-- activation --");
		personaFisica = acreedoresCatalogosService.getRegistro(personaFisica
				.getDatosContacto().getEmailPersonal());

		if (personaFisica.getSituacion().equals("AC")) {
			MyLogger.Logger.log(Level.INFO, "--activa--");
			addActionError(getText("usuario.msg.activacion.ya.realizada"));
			return SUCCESS;
		} else {
			MyLogger.Logger.log(Level.WARNING, "--no activa--");
			Mensaje mensaje = usuarioService.save(personaFisica);
			MyLogger.Logger.log(Level.INFO, "mensaje DAO ACTIVATION: "
					+ mensaje);
			if (mensaje != null) {
				if (mensaje.getRespuesta() == 0) {
					addActionMessage(getText("usuario.msg.activacion.success"));
					return SUCCESS;
				} else {
					addActionError(mensaje.getMensaje());
					return ERROR;
				}
			}
		}
		addActionError(getText("usuario.msg.activacion.error"));
		return ERROR;
	}

	public String activAcreedor() throws Exception {
		MyLogger.Logger.log(Level.INFO, "-- activation --");
		personaFisica = acreedoresCatalogosService.getRegistro(personaFisica
				.getDatosContacto().getEmailPersonal());

		if (personaFisica.getSituacion().equals("AC")) {
			MyLogger.Logger.log(Level.INFO, "--activa--");
			addActionError(getText("usuario.msg.activacion.ya.realizada"));
			return SUCCESS;
		} else {
			Mensaje mensaje = usuarioService.save(personaFisica);
			MyLogger.Logger.log(Level.INFO, "mensaje DAO ACTIVATION: "
					+ mensaje);
			if (mensaje != null) {
				if (mensaje.getRespuesta() == 0) {
					addActionMessage(getText("usuario.msg.activacion.success"));
					return SUCCESS;
				} else {
					addActionError(mensaje.getMensaje());
					return ERROR;
				}
			}
		}
		addActionError(getText("usuario.msg.activacion.error"));
		return ERROR;
	}

	public String recover() throws Exception {
		MyLogger.Logger.log(Level.INFO, "-- recover --");
		MyLogger.Logger.log(Level.INFO, "registroUsuario: " + registroUsuario);
		return SUCCESS;
	}

	public String valid() throws Exception {
		MyLogger.Logger.log(Level.INFO, "-- valid --");
		String forward = null;
		MyLogger.Logger.log(Level.INFO, "-- paso 1 --");
		MyLogger.Logger.log(Level.INFO, "personaFisica: " + personaFisica);

		RegistroUsuario registroUsuarioRecupera = usuarioService
				.getPregunta(personaFisica);
		MyLogger.Logger.log(Level.INFO, "IMPRIME EL REGISTRO RECUPERA: "
				+ registroUsuarioRecupera);
		if (registroUsuarioRecupera.getPregunta() != null) {
			registroUsuario.setPregunta(registroUsuarioRecupera.getPregunta());
			addActionMessage(getText("usuario.msg.valid.success"));
			forward = SUCCESS;
		} else {
			addActionError(getText("usuario.msg.valid.error"));
			forward = ERROR;
		}

		MyLogger.Logger.log(Level.INFO, "registroUsuario: " + registroUsuario);
		MyLogger.Logger.log(Level.INFO, "forward: " + forward);
		return forward;
	}

	public String change() throws Exception {

		String forward = null;

		personaFisica.setNombre(registroUsuario.getNombreAcreedor());
		registroUsuario.setPassword(generatePassword());

		Mensaje mensaje = usuarioService
				.recover(personaFisica, registroUsuario);

		if (mensaje.getRespuesta() == 0) {
			mailRegistroService
					.sendMailRecupera(personaFisica, registroUsuario);
			addActionMessage(getText("usuario.msg.change.success"));

			forward = SUCCESS;
		} else {
			addActionError(getText("usuario.msg.change.error"));
			forward = ERROR;
		}
		return forward;

	}

	public String preActivation() throws Exception {
		MyLogger.Logger.log(Level.INFO, "-- recover --");
		MyLogger.Logger.log(Level.INFO, "--clave de usuario--"
				+ personaFisica.getDatosContacto().getEmailPersonal());
		personaFisica = acreedoresCatalogosService.getRegistro(personaFisica
				.getDatosContacto().getEmailPersonal().toLowerCase());
		return SUCCESS;
	}
	
	public String activated() throws Exception {
		addActionMessage(getText("usuario.msg.activacion.success"));
		return SUCCESS;
	}

	private String generatePassword() {
		String password = null;
		Password passwordParams = new Password();
		passwordParams.setMax(16);
		passwordParams.setMin(8);

		// genera password
		password = passwordService.generatePassword(passwordParams);
		return password;
	}

	public void setMailRegistroService(MailRegistroService mailRegistroService) {
		this.mailRegistroService = mailRegistroService;
	}

	public MailRegistroService getMailRegistroService() {
		return mailRegistroService;
	}

	public void setAcreedoresCatalogosService(
			AcreedoresCatalogosService acreedoresCatalogosService) {
		this.acreedoresCatalogosService = acreedoresCatalogosService;
	}

	public AcreedoresCatalogosService getAcreedoresCatalogosService() {
		return acreedoresCatalogosService;
	}

	public void setPersonaFisicaRfc(PersonaFisica personaFisicaRfc) {
		this.personaFisicaRfc = personaFisicaRfc;
	}

	public PersonaFisica getPersonaFisicaRfc() {
		return personaFisicaRfc;
	}

	//public String getJobName() {
		//return jobName;
	//}

	//public void setJobName(String jobName) {
		//this.jobName = jobName;
	//}

	//public String getEstado() {
		//return Estado;
	//}

	//public void setEstado(String estado) {
		//Estado = estado;
	//}

	//public String getPeriodicidad() {
		//return Periodicidad;
	//}

	//public void setPeriodicidad(String periodicidad) {
		//Periodicidad = periodicidad;
	//}

	//public String getUltEjecucion() {
		//return UltEjecucion;
	//}

	//public void setUltEjecucion(String ultEjecucion) {
		//UltEjecucion = ultEjecucion;
	//}

	//public String getProxEjecucion() {
		//return ProxEjecucion;
	//}

	//public void setProxEjecucion(String proxEjecucion) {
		//ProxEjecucion = proxEjecucion;
	//}

	public jobAvisoTO getJobAviso() {
		return jobAviso;
	}

	public void setJobAviso(jobAvisoTO jobAviso) {
		this.jobAviso = jobAviso;
	}
	
	
	public List<jobAvisoTO> getListaJobAvisoPreventivo() {
		return listaJobAvisoPreventivo;
	}

	public void setListaJobAvisoPreventivo(List<jobAvisoTO> listaJobAvisoPreventivo) {
		this.listaJobAvisoPreventivo = listaJobAvisoPreventivo;
	}

	public static String getSuccess() {
		return SUCCESS;
	}
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String validaEstado (String edo)
	{ if (edo!=null){
		if (edo.equalsIgnoreCase("ACTIVO")){
			edo = "AC";
		}
		else{
			edo = "IN";
		} 			
		
	}else{
		edo = "AC";
	}
	
	 return edo;
	}

	public String getOtraPregunta() {
		return otraPregunta;
	}

	public void setOtraPregunta(String otraPregunta) {
		this.otraPregunta = otraPregunta;
	}

	public FilesService getFilesService() {
		return filesService;
	}

	public void setFilesService(FilesService filesService) {
		this.filesService = filesService;
	}

	public ArchivoService getArchivoSvc() {
		return archivoSvc;
	}

	public void setArchivoSvc(ArchivoService archivoSvc) {
		this.archivoSvc = archivoSvc;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}
	
	public String users() {
		// verificar si es cuenta maestra
		Boolean esMaestra = Boolean.valueOf(sessionMap.get(mx.gob.se.rug.common.constants.Constants.MASTER_USER).toString());
		System.out.println(" Env  master " + mx.gob.se.rug.common.constants.Constants.MASTER_USER + " es maestra "+ esMaestra);
		if(esMaestra) {
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(mx.gob.se.rug.constants.Constants.USUARIO);
			System.out.println("Usuario " + usuario.getPersona().getIdPersona());
			setIdPersona(Integer.valueOf(usuario.getPersona().getIdPersona()).longValue());
			
			// obtener usuarios asociados
			UsuarioTO usuarioLogeado = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			System.out.println("Usuario logeado " + usuarioLogeado.getPersona().getIdPersona());
			usuarios = usuariosBusinessService.getSubusuarios(Integer.valueOf(usuarioLogeado.getPersona().getIdPersona()).longValue());
//			System.out.println("Usuarios Asociados "+ usuarios);
			return SUCCESS;
		} else {
			return "forbidden";
		}
	}
	
	public void setUsuariosBusinessService(UsuariosService usuariosBusinessService) {
		this.usuariosBusinessService = usuariosBusinessService;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public String ammend() throws Exception {
		RechazoCuenta rechazo = (RechazoCuenta) sessionMap.get("rechazo");
		// obtener el usuario basado en la razon de rechazo
		usuario = usuariosBusinessService.getUsuario(rechazo.getIdPersona());
		if(usuario != null) {
			// llenar formulario de rectificacion
			causa = rechazo.getCausa();
			/*personaFisica = new PersonaFisica();
			personaFisica.setTipoPersona(usuario.getTipoPersona());
			personaFisica.setNacionalidadInscrito(usuario.getInscritoComo());
			personaFisica.setNombre(usuario.getNombre());
			personaFisica.setRfc(usuario.getRfc());
			personaFisica.setDocumentoIdentificacion(usuario.getDocId());
			DatosContacto datosContacto = new DatosContacto();
			datosContacto.setEmailPersonal(usuario.getCveUsuario());
			personaFisica.setDatosContacto(datosContacto);*/
			usuario.setPassword("");
			usuario.setConfirmacion("");
			usuario.setPregRecupera("");
			nitAnterior = usuario.getRfc();
			setIdPersona(usuario.getIdPersona());
		}
		return SUCCESS;
	}
	
	public String saveAmmend() throws Exception {
		System.out.println("SaveAmmend");
		String captchaKeyOri = (String) sessionMap.get(Constants.CAPTCHA_KEY);
		// construccion de uri
		HttpServletRequest request = ServletActionContext.getRequest();
		StringBuffer uri = request.getRequestURL();
		String url = uri.toString().replace("usuario/saveAmmend.do", "");
		registroUsuario.setUri(url);

		// validar Captcha
		if (getCaptchaKey().equalsIgnoreCase(captchaKeyOri)) {
			if(upload == null) {
				addActionError(getText("usuario.msg.registro.error.file"));
				return ERROR;
			}

			String mail = usuario.getCveUsuario();
			usuario.setCveUsuario(mail.toLowerCase());
			
			registroUsuario.setTipoOperacion(OPERACION_I);

			boolean error = false;
			// si el rfc es diferente al anterior
			PersonaFisica personaRfc = new PersonaFisica();
			if(!usuario.getRfc().equals(nitAnterior)) {
				personaRfc.setRfc(usuario.getRfc());
				PersonaFisica usuarioRfc = usuarioService.getConsultaRfc(personaRfc);
				if (usuarioRfc.getRfc() != null) {
					addActionError(getText("usuario.msg.registro.rfc.error"));
					error = true;
				}
			}
			
			if(!error) {			
				// validar datos con SAT
				// Generar token
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String formatDateTime = now.format(formatter);
				String strToken = formatDateTime + "#" + mail;
				
				byte[] encodedBytes = Base64.getEncoder().encode(strToken.getBytes());
				String token = new String(encodedBytes);
				usuario.setToken(token);
	            
				// la pregunta es diferente a las predeterminadas
				if(usuario.getPregRecupera().equals("Agregar otra")) {
					usuario.setPregRecupera(otraPregunta);
				}
				// actualizar la informacion del usuario y colocarle estado PA
				usuario.setSitUsuario("PA");
				int resultado = usuariosBusinessService.update(idPersona, usuario);

				if (resultado > 0) {
					// eliminar token de RECHAZO_CUENTA
					RechazoCuenta rechazo = (RechazoCuenta) sessionMap.get("rechazo");
					rechazo.setToken(null);
					rechazoService.update(rechazo.getRechazoCuentaId(), rechazo);
					
					addActionMessage(getText("usuario.msg.registro.success"));
					sessionMap.remove(KEY_CAPTCHA_PARAMS);
					// ENVIAR MAIL
					try {
						// enviar correo de pendiente de aprobacion
						personaFisica = new PersonaFisica();
						personaFisica.setToken(usuario.getToken());
						DatosContacto datosContacto = new DatosContacto();
						datosContacto.setEmailPersonal(usuario.getCveUsuario());
						personaFisica.setDatosContacto(datosContacto);
						personaFisica.setNombre(usuario.getNombre());
		                mailRegistroService.sendMailRegistro(personaFisica, registroUsuario);
					} catch(Exception e) {
						// no pudo enviar correo
						e.printStackTrace();
					}

					// tiene un archivo adjunto
					if(upload != null) {
						Constants c = new Constants();
						String basepath = c.getParamValue(Constants.FS_BASEPATH);
						System.out.println(upload);
						String fileUrl = filesService.saveFile(new FileInputStream(upload), basepath, uploadFileName, usuario.getDocId(), true);
						// eliminar archivos anteriores
						archivoSvc.deleteByObjeto(idPersona);
						
						// insertar en la tabla de archivos
						Archivo archivo = new Archivo();
						archivo.setObjetoId(idPersona);
						archivo.setTipo("DI");
						archivo.setUrl(fileUrl);
						archivo.setEstado("AC");
						Instant instant = Instant.now();
						archivo.setFecha(new Timestamp(instant.toEpochMilli()));
						
						archivoSvc.add(archivo);
					}
					return SUCCESS;
				}
				addActionError(getText("usuario.msg.registro.error"));
			}
		} else {
			setCaptchaKey("");
			addActionError(getText("usuario.msg.registro.error.captcha"));
		}
		return ERROR;
	}

	public String getCausa() {
		return causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
	}

	public String getNitAnterior() {
		return nitAnterior;
	}

	public void setNitAnterior(String nitAnterior) {
		this.nitAnterior = nitAnterior;
	}

	public void setRechazoService(RechazoCuentaService rechazoService) {
		this.rechazoService = rechazoService;
	}
}
