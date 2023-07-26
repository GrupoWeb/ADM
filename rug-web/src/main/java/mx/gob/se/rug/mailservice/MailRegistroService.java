package mx.gob.se.rug.mailservice;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import gt.gob.rgm.mail.service.MailService;
import gt.gob.rgm.service.JmsMessageSenderService;
import mx.gob.se.rug.administracion.dto.RegistroUsuario;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.dto.PersonaFisica;
import mx.gob.se.rug.garantia.to.BoletaPagoTO;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

public class MailRegistroService{
	
	private MailService mailService;
	
	private JmsMessageSenderService messageSender;



	public void sendMailAll(String url) throws IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url + Constants.SEND_API + Constants.SEND_MAIL);
		HttpResponse response = httpclient.execute(httpGet);
	}

	public void sendEmailRegister(PersonaFisica personaFisica, RegistroUsuario registroUsuario, String url) throws IOException {
		String tipo = "1", idmail = "1";
		String activarUrl = registroUsuario.getUri() + "rs/usuarios/activar?token=" + personaFisica.getToken();
		String subject = Constants.getParamValue(Constants.MAIL_SUBJECT_REGISTRO);
		subject = subject.replace("@nombreCompleto", personaFisica.getNombreCompleto());
		String message = Constants.getParamValue(Constants.TEMPLATE_REGISTER);
		message = message.replace("@nombreCompleto", personaFisica.getNombreCompleto());
		message = message.replace("@cve_usuario", personaFisica.getDatosContacto().getEmailPersonal());
		message = message.replace("@uri", registroUsuario.getUri());
		message = message.replace("@activarUrl", activarUrl);
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url + Constants.SEND_API + Constants.SEND_BITACORA);
		MultipartEntity reqEntity = new MultipartEntity();
		reqEntity.addPart("idTipo", new StringBody(tipo));
		reqEntity.addPart("idMail", new StringBody(idmail));
		reqEntity.addPart("destino", new StringBody(personaFisica.getDatosContacto().getEmailPersonal()));
		reqEntity.addPart("cc", new StringBody(""));
		reqEntity.addPart("cco", new StringBody(""));
		reqEntity.addPart("asunto", new StringBody(subject));
		reqEntity.addPart("mensaje", new StringBody(message));

		httpPost.setEntity(reqEntity);

		HttpResponse response = httpclient.execute(httpPost);
		String data = new BasicResponseHandler().handleResponse(response);
		sendMailAll(url);

	}
	public void sendMailRegistro(PersonaFisica personaFisica, RegistroUsuario registroUsuario) {
		String activarUrl = registroUsuario.getUri() + "rs/usuarios/activar?token=" + personaFisica.getToken();
		Constants c = new Constants();
		String idTipoMensaje = "1";
		String idAccountSmtp = c.getParamValue(Constants.IDSMPT_REGISTRO_USUARIO);
		String to = personaFisica.getDatosContacto().getEmailPersonal();
		String cc = null;
		String cco = null;
		String subject = c.getParamValue(Constants.MAIL_SUBJECT_REGISTRO);
		String message = c.getParamValue(Constants.MAIL_THEME_REGISTRO);
		subject = subject.replace("@nombreCompleto", personaFisica.getNombreCompleto());
		message = message.replace("@nombreCompleto", personaFisica.getNombreCompleto());
		message = message.replace("@cve_usuario", personaFisica.getDatosContacto().getEmailPersonal());
		message = message.replace("@uri", registroUsuario.getUri());
		message = message.replace("@activarUrl", activarUrl);
		try {
			String url = c.getParamValue(Constants.URL_PATH_REPACE_CHANGE);

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url + Constants.SEND_API + Constants.SEND_BITACORA);
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("idTipo", new StringBody(idTipoMensaje));
			reqEntity.addPart("idMail", new StringBody(idAccountSmtp));
			reqEntity.addPart("destino", new StringBody(personaFisica.getDatosContacto().getEmailPersonal()));
			reqEntity.addPart("cc", new StringBody(""));
			reqEntity.addPart("cco", new StringBody(""));
			reqEntity.addPart("asunto", new StringBody(subject));
			reqEntity.addPart("mensaje", new StringBody(message));

			httpPost.setEntity(reqEntity);

			HttpResponse response = httpclient.execute(httpPost);
			String data = new BasicResponseHandler().handleResponse(response);
			sendMailAll(url);
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	}

	public void sendMailRecupera(PersonaFisica personaFisica, RegistroUsuario registroUsuario) {
		Constants c = new Constants();

		String idTipoMensaje = "5";
		String idAccountSmtp = "1";
		String to = personaFisica.getDatosContacto().getEmailPersonal();
		String cc = null;
		String cco = null;
		String subject = c.getParamValue(Constants.MAIL_SUBJECT_RECUPERA);
		String message = c.getParamValue(Constants.MAIL_THEME_RECUPERA);

		subject = subject.replace("@nombreCompleto", personaFisica.getNombreCompleto());
		message = message.replace("@nombreCompleto", personaFisica.getNombreCompleto());
		message = message.replace("@cve_usuario", personaFisica.getDatosContacto().getEmailPersonal());
		message = message.replace("@password", registroUsuario.getPassword());

		try {
			String url = c.getParamValue(Constants.URL_PATH_REPACE_CHANGE);

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url + Constants.SEND_API + Constants.SEND_BITACORA);
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("idTipo", new StringBody(idTipoMensaje));
			reqEntity.addPart("idMail", new StringBody(idAccountSmtp));
			reqEntity.addPart("destino", new StringBody(personaFisica.getDatosContacto().getEmailPersonal()));
			reqEntity.addPart("cc", new StringBody(""));
			reqEntity.addPart("cco", new StringBody(""));
			reqEntity.addPart("asunto", new StringBody(subject));
			reqEntity.addPart("mensaje", new StringBody(message));

			httpPost.setEntity(reqEntity);

			HttpResponse response = httpclient.execute(httpPost);
			String data = new BasicResponseHandler().handleResponse(response);
			sendMailAll(url);
	    } catch(Exception e) {
	    	// si el envio del correo falla, se captura la excepcion pero continua el proceso
	    	e.printStackTrace();
	    }
	}

	public void sendMailRegUsuarioAcreedor(PersonaFisica personaFisica, RegistroUsuario registroUsuario) throws IOException {
		//MailBeanRemote mailBean = getBean();
		//if (mailBean != null) {
			Constants c = new Constants();

			String idTipoMensaje = "7";
			String idAccountSmtp = c.getParamValue(Constants.IDSMPT_ALTA_USU_ACREEDOR);
			String to = personaFisica.getDatosContacto().getEmailPersonal();
			String cc = null;
			String cco = null;
			String subject = c.getParamValue(Constants.MAIL_SUBJECT_REGISTRO);
			String message = c.getParamValue(Constants.MAIL_THEME_REGISTRO_USU_ACREEDOR);

			subject = subject.replace("@nombreCompleto", personaFisica.getNombreCompleto());

			message = message.replace("@nombreCompleto", personaFisica.getNombreCompleto());
			message = message.replace("@cve_usuario", personaFisica.getDatosContacto().getEmailPersonal());
			message = message.replace("@password", registroUsuario.getPassword());
			message = message.replace("@uri", registroUsuario.getUri());
			message = message.replace("@nombreAcreedor", registroUsuario.getNombreAcreedor());

			//mailBean.sendMail(idTipoMensaje, idAccountSmtp, to, cc, cco, subject, message);

			String url = c.getParamValue(Constants.URL_PATH_REPACE_CHANGE);

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url + Constants.SEND_API + Constants.SEND_BITACORA);
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("idTipo", new StringBody(idTipoMensaje));
			reqEntity.addPart("idMail", new StringBody(idAccountSmtp));
			reqEntity.addPart("destino", new StringBody(personaFisica.getDatosContacto().getEmailPersonal()));
			reqEntity.addPart("cc", new StringBody(""));
			reqEntity.addPart("cco", new StringBody(""));
			reqEntity.addPart("asunto", new StringBody(subject));
			reqEntity.addPart("mensaje", new StringBody(message));

			httpPost.setEntity(reqEntity);

			HttpResponse response = httpclient.execute(httpPost);
			String data = new BasicResponseHandler().handleResponse(response);
			sendMailAll(url);
		/*} else {
			MyLogger.Logger.log(Level.INFO, "No inicialzo el EJB");
		}*/
	}

	public void sendMailRegUsuarioAcreedorExiste(PersonaFisica personaFisica, RegistroUsuario registroUsuario) throws IOException {
		//MailBeanRemote mailBean = getBean();
		//if (mailBean != null) {
			Constants c = new Constants();

			String idTipoMensaje = "8";
			String idAccountSmtp = Constants.getParamValue(Constants.IDSMPT_ALTA_USU_NUEVO_ACREEDOR);
			String to = personaFisica.getDatosContacto().getEmailPersonal();
			String cc = null;
			String cco = null;
			String subject = Constants.getParamValue(Constants.MAIL_SUBJECT_REGISTRO);
			String message = Constants.getParamValue(Constants.MAIL_THEME_REGISTRO_USU_ACREEDOR_NO_EXISTE);

			subject = subject.replace("@nombreCompleto", personaFisica.getNombreCompleto());

			message = message.replace("@nombreCompleto", personaFisica.getNombreCompleto());
			message = message.replace("@cve_usuario", personaFisica.getDatosContacto().getEmailPersonal());
			message = message.replace("@nombreAcreedor", registroUsuario.getNombreAcreedor());

			//mailBean.sendMail(idTipoMensaje, idAccountSmtp, to, cc, cco, subject, message);

			String url = c.getParamValue(Constants.URL_PATH_REPACE_CHANGE);

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url + Constants.SEND_API + Constants.SEND_BITACORA);
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("idTipo", new StringBody(idTipoMensaje));
			reqEntity.addPart("idMail", new StringBody(idAccountSmtp));
			reqEntity.addPart("destino", new StringBody(personaFisica.getDatosContacto().getEmailPersonal()));
			reqEntity.addPart("cc", new StringBody(""));
			reqEntity.addPart("cco", new StringBody(""));
			reqEntity.addPart("asunto", new StringBody(subject));
			reqEntity.addPart("mensaje", new StringBody(message));

			httpPost.setEntity(reqEntity);

			HttpResponse response = httpclient.execute(httpPost);
			String data = new BasicResponseHandler().handleResponse(response);
			sendMailAll(url);
		/*} else {
			MyLogger.Logger.log(Level.WARNING, "No inicialzo el EJB");
		}*/
	}
	public void sendMailDesatendidoAviso(UsuarioTO usuarioTO) throws IOException {
		//MailBeanRemote mailBean = getBean();
		
		//if (mailBean != null) {
			
			String idTipoMensaje = "2";
			String idAccountSmtp = Constants.getParamValue(Constants.IDSMPT_ALTA_USU_NUEVO_ACREEDOR);
			String to = usuarioTO.getPersona().getCorreoElectronico();
			String cc = null;
			String cco = null;
			String subject = Constants.getParamValue(Constants.MAIL_SUBJECT_DESATENDIDO);
			String message = Constants.getParamValue(Constants.MAIL_THEME_DESATENDIDO);
			
			//mailBean.sendMail(idTipoMensaje, idAccountSmtp, to, cc, cco,subject, message);

				Constants c = new Constants();
				String url = c.getParamValue(Constants.URL_PATH_REPACE_CHANGE);

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url + Constants.SEND_API + Constants.SEND_BITACORA);
				MultipartEntity reqEntity = new MultipartEntity();
				reqEntity.addPart("idTipo", new StringBody(idTipoMensaje));
				reqEntity.addPart("idMail", new StringBody(idAccountSmtp));
				reqEntity.addPart("destino", new StringBody(to));
				reqEntity.addPart("cc", new StringBody(""));
				reqEntity.addPart("cco", new StringBody(""));
				reqEntity.addPart("asunto", new StringBody(subject));
				reqEntity.addPart("mensaje", new StringBody(message));

				httpPost.setEntity(reqEntity);

				HttpResponse response = httpclient.execute(httpPost);
				String data = new BasicResponseHandler().handleResponse(response);
				sendMailAll(url);

		/*} else {
			MyLogger.Logger.log(Level.WARNING, "No inicialzo el EJB");
		}*/
		
	}
	public void sendMailDesatendidoAvisoFin(UsuarioTO usuarioTO,String idArchivoResultado) throws IOException {
			String idTipoMensaje = "3";
			String idAccountSmtp = Constants.getParamValue(Constants.IDSMPT_ALTA_USU_NUEVO_ACREEDOR);
			String to = usuarioTO.getPersona().getCorreoElectronico();
			String cc = null;
			String cco = null;
			String subject = Constants.getParamValue(Constants.MAIL_SUBJECT_DESATENDIDO_FIN);
			String message = Constants.getParamValue(Constants.MAIL_THEME_DESATENDIDO_FIN);
			
			message= message.replace("@idArchivoResultado", idArchivoResultado);

			Constants c = new Constants();
			String url = c.getParamValue(Constants.URL_PATH_REPACE_CHANGE);

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url + Constants.SEND_API + Constants.SEND_BITACORA);
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("idTipo", new StringBody(idTipoMensaje));
			reqEntity.addPart("idMail", new StringBody(idAccountSmtp));
			reqEntity.addPart("destino", new StringBody(to));
			reqEntity.addPart("cc", new StringBody(""));
			reqEntity.addPart("cco", new StringBody(""));
			reqEntity.addPart("asunto", new StringBody(subject));
			reqEntity.addPart("mensaje", new StringBody(message));

			httpPost.setEntity(reqEntity);

			HttpResponse response = httpclient.execute(httpPost);
			String data = new BasicResponseHandler().handleResponse(response);
			sendMailAll(url);

	}

	public void sendMailRegistroRepAcreedores(PersonaFisica personaFisica, RegistroUsuario registroUsuario) throws IOException {
			Constants c = new Constants();
			String idTipoMensaje = "1";
			String idAccountSmtp = c.getParamValue(Constants.IDSMPT_REGISTRO_USUARIO);
			String to = personaFisica.getDatosContacto().getEmailPersonal();
			String cc = null;
			String cco = null;
			String subject = c.getParamValue(Constants.MAIL_SUBJECT_REGISTRO);
			String message = c.getParamValue(Constants.MAIL_THEME_REGISTRO_REP_ACREEDOR);

			subject = subject.replace("@nombreCompleto",
					personaFisica.getNombreCompleto());

			message = message.replace("@nombreCompleto",
					personaFisica.getNombreCompleto());
			message = message.replace("@cve_usuario", personaFisica
					.getDatosContacto().getEmailPersonal());
			message = message.replace("@password", registroUsuario.getPassword());
			message = message.replace("@uri", registroUsuario.getUri());

			String url = c.getParamValue(Constants.URL_PATH_REPACE_CHANGE);

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url + Constants.SEND_API + Constants.SEND_BITACORA);
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("idTipo", new StringBody(idTipoMensaje));
			reqEntity.addPart("idMail", new StringBody(idAccountSmtp));
			reqEntity.addPart("destino", new StringBody(personaFisica.getDatosContacto().getEmailPersonal()));
			reqEntity.addPart("cc", new StringBody(""));
			reqEntity.addPart("cco", new StringBody(""));
			reqEntity.addPart("asunto", new StringBody(subject));
			reqEntity.addPart("mensaje", new StringBody(message));

			httpPost.setEntity(reqEntity);

			HttpResponse response = httpclient.execute(httpPost);
			String data = new BasicResponseHandler().handleResponse(response);
			sendMailAll(url);
		
	}

	public void sendMailRegistroAprobar(PersonaFisica personaFisica, RegistroUsuario registroUsuario) { 

	    Constants c = new Constants(); 
	    String idTipoMensaje = "1";
	    String idAccountSmtp = c.getParamValue(Constants.IDSMPT_REGISTRO_USUARIO);
	    String to = personaFisica.getDatosContacto().getEmailPersonal(); 
	    String cc = null; 
	    String cco = null; 
	    String subject = c.getParamValue(Constants.MAIL_SUBJECT_APROBACION); 
	    String message = c.getParamValue(Constants.MAIL_THEME_APROBACION); 
	    subject = subject.replace("@nombreCompleto", personaFisica.getNombreCompleto()); 
	    message = message.replace("@nombreCompleto", personaFisica.getNombreCompleto()); 
	    message = message.replace("@cve_usuario", personaFisica.getDatosContacto().getEmailPersonal()); 
	    message = message.replace("@uri", registroUsuario.getUri()); 
	    try {

			String url = c.getParamValue(Constants.URL_PATH_REPACE_CHANGE);

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url + Constants.SEND_API + Constants.SEND_BITACORA);
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("idTipo", new StringBody(idTipoMensaje));
			reqEntity.addPart("idMail", new StringBody(idAccountSmtp));
			reqEntity.addPart("destino", new StringBody(personaFisica.getDatosContacto().getEmailPersonal()));
			reqEntity.addPart("cc", new StringBody(""));
			reqEntity.addPart("cco", new StringBody(""));
			reqEntity.addPart("asunto", new StringBody(subject));
			reqEntity.addPart("mensaje", new StringBody(message));

			httpPost.setEntity(reqEntity);

			HttpResponse response = httpclient.execute(httpPost);
			String data = new BasicResponseHandler().handleResponse(response);
			sendMailAll(url);

	    } catch(Exception e) {
	    	// si el envio del correo falla, se captura la excepcion pero continua el proceso
	    	e.printStackTrace();
	    }
	}
	
	public void sendMailBoletaAprobar(PersonaFisica personaFisica, BoletaPagoTO boleta) { 
	    Constants c = new Constants(); 
	    String idTipoMensaje = "1";
	    String idAccountSmtp = c.getParamValue(Constants.IDSMPT_REGISTRO_USUARIO);
	    String to = personaFisica.getDatosContacto().getEmailPersonal();
	    String cc = null; 
	    String cco = null; 
	    String subject = c.getParamValue(Constants.MAIL_SUBJECT_BOLETA_APROBACION); 
	    String message = c.getParamValue(Constants.MAIL_THEME_BOLETA_APROBACION); 
	    subject = subject.replace("@nombreCompleto", personaFisica.getNombreCompleto()); 
	    message = message.replace("@nombreCompleto", personaFisica.getNombreCompleto()); 
	    message = message.replace("@boleta", boleta.getSerie() + " " + boleta.getNumero()); 
	    message = message.replace("@monto", boleta.getMonto().toString());
	    try {
			String url = c.getParamValue(Constants.URL_PATH_REPACE_CHANGE);

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url + Constants.SEND_API + Constants.SEND_BITACORA);
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("idTipo", new StringBody(idTipoMensaje));
			reqEntity.addPart("idMail", new StringBody(idAccountSmtp));
			reqEntity.addPart("destino", new StringBody(personaFisica.getDatosContacto().getEmailPersonal()));
			reqEntity.addPart("cc", new StringBody(""));
			reqEntity.addPart("cco", new StringBody(""));
			reqEntity.addPart("asunto", new StringBody(subject));
			reqEntity.addPart("mensaje", new StringBody(message));

			httpPost.setEntity(reqEntity);

			HttpResponse response = httpclient.execute(httpPost);
			String data = new BasicResponseHandler().handleResponse(response);
			sendMailAll(url);

	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	public MailService getMailService() {
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public JmsMessageSenderService getMessageSender() {
		return messageSender;
	}

	public void setMessageSender(JmsMessageSenderService messageSender) {
		this.messageSender = messageSender;
	}
}
