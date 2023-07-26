package gt.gob.rgm.adm.util;

public class Constants {
	public static final String FS_BASE_PATH = "fsBasePath";
	public static final String ID_SMTP_MAIL_REGISTRO_USUARIOS = "idSmtp-MailRegistroUsuarios";
	public static final String MAIL_SUBJECT_APROBACION = "mailSubjectAprobacion";
	public static final String MAIL_THEME_APROBACION = "mailThemeAprobacion";
	public static final String MAIL_SUBJECT_RECHAZO = "mailSubjectRechazo";
	public static final String MAIL_THEME_RECHAZO = "mailThemeRechazo";
	public static final String HOST_URL = "hostUrl";
	public static final String MAIL_ADMIN = "mailAdmin";
	public static final String URL_ENCUESTA = "urlEncuesta";
	public static final String HOST_URL_ADM = "hostUrlAdm";
	public static final String MAIL_SUBJECT_BOLETA_APROBADA = "mailSubjectBoletaAprobada";
	public static final String MAIL_THEME_BOLETA_APROBADA = "mailThemeBoletaAprobada";
	public static final String MAIL_SUBJECT_ENCUESTA = "mailSubjectEncuesta";
	public static final String MAIL_THEME_ENCUESTA = "mailThemeEncuesta";
	public static final String MAIL_SUBJECT_PREGUNTA = "mailSubjectPregunta";
	public static final String MAIL_THEME_PREGUNTA = "mailThemePregunta";
	public static final String MAIL_SUBJECT_BOLETA_RECHAZADA = "mailSubjectBoletaRechazada";
	public static final String MAIL_THEME_BOLETA_RECHAZADA = "mailThemeBoletaRechazada";
	public static final String SECRET = "ABCDEF123456#ZYXWVU987654";
	public static final Long SESSION_DURATION = 4 * 60 * 60 * 1000L;
	public static final String MAIL_SUBJECT_CUENTA_ADM = "mailSubjectCuentaAdm";
	public static final String MAIL_THEME_CUENTA_ADM = "mailThemeCuentaAdm";
	public static final String MAIL_THEME_VENCIMIENTO = "mailVigenciaGarantia";	
	public static final Integer MAX_LENGHT_LINE=132;
	public static final Integer MAX_LENGHT_LINE_SMALL=50;
	public static final String URL_SERVER="urlServer";
	
	public static final String HTML_INSCRIPCION = "pdfHtmlBoleta";
	public static final String HTML_MODIFICACION = "pdfHtmlModificacion";
	public static final String HTML_RENOVACION = "pdfHtmlRenovacion";
	public static final String HTML_CANCELACION = "pdfHtmlCancelacion";
	public static final String HTML_EJECUCION = "pdfHtmlEjecucion";

	public static final String PERSONA_INDIVIDUAL = "PF";
	public static final String PERSONA_JURIDICA = "PM";
	public static final String PERSONA_PROCEDENCIA = "NAL";
	public static final String ESTADO_ACTIVO = "AC";
	public static final String ESTADO_INACTIVO = "IN";
	public static final String CONFIRMACION = "Y";
	public static final String CONFIRMACION_ES = "S";
	public static final String FALSE = "F";
	public static final String PERFIL = "CIUDADANO";
	public static final String APLICACION = "RugPortal";
	public static final String INSTITUCION = "SE";
	public static final String DEFAULT_PASSWORD = "P@ssw0rd";
	public static final String DEFAULT_PREGUNTA = "Cual es el nombre de su primera mascota";
	public static final String DEFAULT_RESPUESTA = "fido";
	public static final Integer GRUPO_ACREEDOR = 1;
	public static final Integer GRUPO_CIUDADANO = 2;
	public static final Integer GRUPO_DELEGADO = 6;
	public static final Integer GRUPO_SUBDELEGADO = 8;
	public static final Integer GRUPO_CORREDOR = 13;
	public static final Integer PAIS_NACIONAL = 1;
	public static final String INSCRITO_NACIONAL = "N";
	public static final String INSCRITO_EXTRANJERO = "E";
	public static final Integer PARTE_SOLICITANTE = 4;
	public static final Integer PARTE_DEUDOR = 2;
	public static final Integer PARTE_ACREEDOR = 3;
	public static final Integer TRAMITE_INSCRIPCION = 1;
	public static final Integer STATUS_TRAMITE = 3;
	public static final Integer PASO_TRAMITE = 0;
	public static final Integer CREACION_SISTEMA = 0;
	public static final Integer TIPO_GARANTIA = 1;
	public static final Integer MESES_GARANTIA = 60;
	public static final Integer VIGENCIA = 5;
	public static final Integer MONEDA = 1;
	public static final Long GARANTIA_MODIFICAR = 0L;
	public static final String FECHA_INICIO_SISTEMA = "2018-04-24";
	public static final Integer RANDOM_LIMIT = 100000;
	
	public static final Integer RESULTADO_EXITOSO = 2000;
	public static final Integer ERROR_GARANTIA_EXISTENTE = 5001;
	public static final Integer ERROR_TRAMITE_EXISTENTE = 5002;
        
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

	//API SEND MAIL

	public static final String SEND_API = "send/";
	public static final String SEND_BITACORA = "bitacora";
	public static final String SEND_MAIL = "sendMail";
	public static final	String TEMPLATE_APPROVE = "mailAprobacionRug";
	public static final	String TEMPLATE_REBOUND = "mailRechazoRug";
	public static final	String TEMPLATE_BOLETA_APPROVE = "mailAprobacionBoletaRug";
	public static final	String TEMPLATE_BOLETA_REBOUND = "mailRechazoBoletaRug";
	public static final	String SEND_PATH = "hostMail";
}
