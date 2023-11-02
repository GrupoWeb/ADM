package mx.gob.se.rug.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import mx.gob.se.rug.constants.dao.ConstantsDAO;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.util.MyLogger;

public class Constants {
	
public static Map<String, String> constantValue ;

public static String getParamValue(String cve_constante){
	if(constantValue==null){
		Constants.constantValue= new ConstantsDAO().getConstants();
      }
	String regresa = "Code error: consvalerror";
	try{
		regresa = new String (Constants.constantValue.get(cve_constante));
	}catch(Exception e){
		//MyLogger.Logger.log(Level.WARNING, "No se encontro el parametro con la clave "+cve_constante);
		System.out.println("No se encontro el parametro con la clave "+cve_constante);
		//e.printStackTrace();
	}
	
	return regresa;
}


//Tipos de Datos
public static Map<String, Integer> tipoDatos ;

public static Integer getIdDataType(String tipoDatoString) throws InfrastructureException{
	Integer idTipoDato= null;
	if(tipoDatos==null){
		tipoDatos = new HashMap<String, Integer>();
		tipoDatos.put("String",1);
		tipoDatos.put("Integer",2);
		tipoDatos.put("Double",3);
		tipoDatos.put("Float",4);
		tipoDatos.put("Date",5);
		tipoDatos.put("Timestamp",6);
		tipoDatos.put("CLOB",7);
		tipoDatos.put("Boolean",8);
		tipoDatos.put("XMLGregorianCalendar",9);
		tipoDatos.put("BigInteger",10);
      }
		
	try{
		idTipoDato = new Integer(tipoDatos.get(tipoDatoString));
	}catch(Exception e){
		MyLogger.Logger.log(Level.WARNING, "No se encontro el tipo de dato con la clave "+tipoDatoString);
		//e.printStackTrace();
		throw new InfrastructureException("No se encontro el tipo de Dato a Procesar");
	}
	
	return idTipoDato;
}

//CArgaMAsiva
public static final Integer N_MAX_TRAMITES = 9999;

//ContextPath
public static String contextPath=null;



public static String getContextPath() throws InfrastructureException {
	if(contextPath==null){
		throw new InfrastructureException("No se cargo el contextPath");
	}
	return contextPath;
}

public static void setContextPath(String contextPath) {
	if(Constants.contextPath==null){
		Constants.contextPath = new String(contextPath);
	}
}


public static Integer conexionesAbiertas = 0;
//Correos
public static final String SMTP_MAIL_SENDER_REG="smtpUserMailRegistro";
public static final String SMTP_MAIL_PASSWORD_SENDER_REG="smtpPasswordMailRegistro";
public static final String MAIL_THEME_REGISTRO="mailThemeRegistro";
public static final String MAIL_SUBJECT_REGISTRO="mailSubjectRegistro";
public static final String MAIL_THEME_REGISTRO_USU_ACREEDOR_NO_EXISTE="mailThemeRegistroUsuarioAcreedorExiste";	
public static final String MAIL_THEME_REGISTRO_USU_ACREEDOR="mailThemeRegistroUsuarioAcreedor";
public static final String MAIL_SUBJECT_RECUPERA="mailSubjectRecupera";
public static final String MAIL_THEME_RECUPERA="mailThemeRecupera";
public static final String MAIL_THEME_REGISTRO_REP_ACREEDOR="mailThemeRegistroRepresentanteAcreedores";
public static final String MAIL_SUBJECT_APROBACION="mailSubjectAprobacion";
public static final String MAIL_THEME_APROBACION="mailThemeAprobacion";
public static final String MAIL_SUBJECT_BOLETA_APROBACION="mailSubjectBoletaPendiente";
public static final String MAIL_THEME_BOLETA_APROBACION="mailThemeBoletaPendiente";

public static final String MAIL_SUBJECT_DESATENDIDO="mailSubjectDesatendidoInicio";
public static final String MAIL_THEME_DESATENDIDO="mailThemeDesatendidoInicio";


public static final String MAIL_SUBJECT_DESATENDIDO_FIN="mailSubjectDesatendidoFin";
public static final String MAIL_THEME_DESATENDIDO_FIN="mailThemeDesatendidoFin";

public static final String IDSMPT_REGISTRO_USUARIO="idSmtp-MailRegistroUsuarios";
public static final String IDSMPT_REGISTRO_USUARIO_APROBAR="idSmtp-MailRegistroUsuariosAprobar";
public static final String IDSMPT_RECUPERA_PASSWORD="idSmtp-MailRecuperaPassword";
public static final String IDSMPT_ALTA_USU_ACREEDOR="idSmtp-MailUsusarioAcreedor";
public static final String IDSMPT_ALTA_USU_NUEVO_ACREEDOR="idSmtp-MaillUsusarioNuevoAcreedor";

public static final String CAPTCHA_KEY="captchaKey";
public static final String BOLETA_CERTIFICADO="BoletaCertificado";
public static final String TRAMITES_HISTORICOS="tramiteshistoricos";
public static final String ANOTACION_SIN_GARANTIA="Anotacionsgarantia";
//Fechas
public static final String DATE_FORMAT_DDMMYY = "dd/MM/yyyy";
//Boletas
public static final String BOLETA_HTML="pdfHtmlBoleta";
//public static final String BOLETA_HTML="pdfHtmlBoletaTest";
public static final String MODIFICACION_HTML="pdfHtmlModificacion";
public static final String CANCELACION_HTML="pdfHtmlCancelacion";
public static final String CERTIFICACION_HTML="pdfHtmlCertificacion";
public static final String RENOVACION_HTML="pdfHtmlRenovacion";
public static final String EJECUCION_HTML="pdfHtmlEjecucion";
public static final String BOLETA_DETALLE="boletaDetalle";
public static final String BOLETA_AVISO_PREVENTIVO="boletaAvisoPreventivo";
public static final String BOLETA_ANOTACION_SN_GARANTIA="boletaAnotacionSNGarantia";
public static final String BOLETA_ANOTACION_CN_GARANTIA="boletaAnotacion";
public static final String PDF_FIRMA_CERTIFICACION="pdfFirmaCertificacion";
public static final String BOLETA_FIRMA_MASIVA="pdfHtmlFirmaMasiva";
public static final String BOLETA_PARTE_FIRMA="pdfHtmlParteFirma";
public static final String BOLETA_GMT_EXPLICA="GMTExplica";
public static final String BOLETA_GMT_MEXICO="GMTMexico";
public static final String BOLETA_GMT_ZULU="GMTZULU";
public static final String URL_SERVER="urlServer";

public static final String VAL_MAX_TRAMITES="valMaxTramites";

public static Map<Integer, String> boletaTemplate;
public static Integer MAX_LENGHT_LINE=132;
public static Integer MAX_LENGHT_LINE_SMALL=50;

//Keys Session
public static final String ID_TRAMITE="idTramite";
public static final String ID_GARANTIA="idGarantia";
public static final String ID_TRAMITE_NUEVO="idTramiteNuevo";
public static final String ID_GARANTIA_ARANCEL="idGarantiaArancel";
public static final String ID_TRAMITE_ACTUAL="idTramiteActual";
public static final String USUARIO="usuario";

//ID_GRUPOS
public static final Long SUBADMINISTRADOR = 5502L;
public static final Long SUBUSUARIO = 8L;

public static final String ID_ACREEDOR_REPRESENTADO ="idAcredorRepresentadoIns";
public static final String ID_TIPO_GARANTIA = "idTipoGarantia";

//Keys Properties
public static final String URL_WEB_SERVICE_FRIMA_TRAMITE ="urlWebserviceFirmaTramites";

//Struts
public static final String FAILED="failed";
public static final String SUCCESS="success";
//carga masiva
public static final String TRAMITES="idTramitesCorrectos";
public static final String TRAMITES_ERRONEOS="idTramitesErrores";
public static final String ID_ARCHIVO="idArchivoCM";
public static final String ID_FIRMA_RES = "firmaMasivaRes";
//numero de acessos al login
public static int PETICIONES_lOGIN = 0;
public static int PETICIONES_FALLIDAS_BD = 0;

//Cache
public static final String CACHE_STATUS = "cache";

//Firma
public static final String URL_SERVICE_ADVANTAGE = "urlServiceAdvantage";
public static final String ENTIDAD_SERVICE_ADVANTAGE = "entidadServiceAdvantage";
public static final String OPERACION_SERVICE_ADVANTAGE = "operacionServiceAdvantage";
public static final String LOGIN_SERVICE_ADVANTAGE = "loginServiceAdvantage";
public static final String PASSWORD_SERVICE_ADVANTAGE = "passwordServiceAdvantage";
public static final String URL_SERVICE_FIRMA = "urlServiceFirma";
public static final String FIRMA_XML = "XMLFirma";
public static final String FIRMA_DEBUG = "firmaDebug";
//Seguridad
public static final String SEGURIDAD_APP_CONTEXT ="seguridad-context.xml";

//log
public static final String LOG_LEVEL = "logLevel";
public static final String PATH = "path";
public static final String LOG_NAME= "logName";
//log CM
public static final String LOG_NAME_CM= "logNameCM";
public static final String LOG_NAME_EF= "logNameEF";


//tipo de tramites
public static final Integer ID_TIPO_TRAMITE_AVISO_PREVENTIVO= 3;
public static final Integer ID_TIPO_TRAMITE_ANOTACION = 2;
public static final Integer ID_TIPO_TRAMITE_ANOTACION_SIN_GARANTIA = 10;

//EXcepciones Carga MAsiva
public static final Integer CODE_EXCEPTION_GARANTIA_REPETIDA= 10;
public static final String MESASAGE_EXCEPTION_GARANTIA_REPETIDA= "Error, m�s de un tr�mite a una misma garant�a  en el mismo archivo(XML). ";

public static final Integer CODE_EXCEPTION_NO_DATA_FOUND= 11;
public static final String MESASAGE_EXCEPTION_NO_DATA_FOUND= "El campo x es obligatorio";


//URL Otras
public static final String URL_RENAPO_CURP= "urlRenapoCurp";
public static final String ACTIVAR_RENAPO= "activarRenapo";

//formatoFecha
public static final String FORMATO_FECHA= "formatoFecha";

public static final String FS_BASEPATH = "fsBasePath";

// firma digital de PDF
 public static final String SIGN_TEXT = "SIGN_TEXT";
 public static final String SIGN_IMAGE = "SIGN_IMAGE";
 public static final String SIGN_FILE = "SIGN_FILE";
 public static final String SIGN_PASSWORD = "SIGN_PASSWORD";
 public static final String SIGN_LOCATION = "SIGN_LOCATION";
 public static final String SIGN_LLX = "SIGN_LLX";
 public static final String SIGN_LLY = "SIGN_LLY";
 public static final String SIGN_URX = "SIGN_URX";
 public static final String SIGN_URY = "SIGN_URY";
 public static final String SIGN_PAGE = "SIGN_PAGE";
 public static final String SIGN_FIELDNAME = "SIGN_FIELDNAME";
 public static final String SIGN_ENABLED = "SIGN_ENABLED";
 public static final String SIGN_IMAGE_LOCAL = "SIGN_IMAGE_LOCAL";
 public static final String SIGN_FILE_LOCAL = "SIGN_FILE_LOCAL";
 public static final String SIGN_LOCAL = "SIGN_LOCAL";
 public static final String SIGN_ZIP_URL = "SIGN_ZIP_URL";
 public static final String SIGN_PDF_URL = "SIGN_PDF_URL";

//Privielgios VS tipoTramite
public static Map<Integer, Integer> mapTipoTramite;

public static Integer getIdTipoTramite(Integer idPrivilegio){
	Integer idTipoTramite= null;
	if(mapTipoTramite==null){
		mapTipoTramite = new HashMap<Integer, Integer>();
		mapTipoTramite.put(24,1);
		mapTipoTramite.put(26,4);
		mapTipoTramite.put(28,7);
		mapTipoTramite.put(30,9);
		mapTipoTramite.put(29,8);
		mapTipoTramite.put(27,6);
		//mapTipoTramite.put("CLOB",2);
		//mapTipoTramite.put("Boolean",8);
		mapTipoTramite.put(32,3);
		//mapTipoTramite.put("BigInteger",12);
		mapTipoTramite.put(56,31);
		mapTipoTramite.put(60,30); //ejecucion
      }
		
	try{
		idTipoTramite = new Integer(mapTipoTramite.get(idPrivilegio));
	}catch(Exception e){
		MyLogger.Logger.log(Level.WARNING, "No se encontro el tipo de dato con la clave "+idPrivilegio);
		//e.printStackTrace();
	}
	
	return idTipoTramite;
}


//Anotaci�n SN Garant�a
public static final String ID_TRAMITE_ANOTACION_SG_PADRE= "idTramiteAnotacionSGPadre";
public static final String ID_TRAMITE_ANOTACION_SG_NUEVO= "idTramiteAnotacionSGNuevo";

//Mensaje Cargas Masivas
public static final String MSG_CM= "msgCargasMasivas";


//API SEND MAIL

	public static final String SEND_API = "send/";
	public static final String SEND_BITACORA = "bitacora";
	public static final String SEND_MAIL = "sendMail";
	public static final String URL_PATH_REPACE = "Rug/usuario/save.do";
	public static final String URL_PATH_REPACE_CHANGE = "urlPathReplaceChange";
	public static final	String TEMPLATE_REGISTER = "mailRegistroRug";


	/**
	 * URL para firma electronica desde el mismo servidor
	 */


	public static final String SIGN_BASE_REPLACE = 	 "rug_web/signature.pdo";

	public static final String SIGN_BASE_PROD = 	 "https://operaciones.rgm.gob.gt/";
	public static final String ENV = 	 "prod"; // Enum: "dev, prod"

	public static final String SIGN_URL = 	 "api/signature";
	public static final String SIGN_BYTES =  "api/toBytes";
	public static final String SIGN_VERIFY = "api/verifyFile";

}
