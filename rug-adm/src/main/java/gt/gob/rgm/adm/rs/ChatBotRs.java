package gt.gob.rgm.adm.rs;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import gt.gob.rgm.adm.model.RugEncuestaToken;
import gt.gob.rgm.adm.model.RugPregunta;
import gt.gob.rgm.adm.model.RugSecuUsuario;
import gt.gob.rgm.adm.service.RugEncuestaTokenService;
import gt.gob.rgm.adm.service.RugParametroConfService;
import gt.gob.rgm.adm.service.RugPreguntaService;
import gt.gob.rgm.adm.util.Constants;
import gt.gob.rgm.adm.util.MailUtils;

@Path("/chats")
@RequestScoped
public class ChatBotRs {

	@Inject
	RugPreguntaService preguntaSvc;
	
	@Inject
	RugParametroConfService parametroService;
	
	@Inject
	RugEncuestaTokenService encuestaSvc;
	
	@GET
    public String setQuestion(@QueryParam(value="question") String question,
    		                  @QueryParam(value="type") Integer type,
    		                  @QueryParam(value="name") String name,
    		                  @QueryParam(value="email") String email,
    		                  @HeaderParam("authorization") String authString) {
        
		if("19a2f516e790bb9a8724cd8b73f35ce614fc311a".equalsIgnoreCase(authString)) {
			System.out.println("Se autentico");
			
			RugPregunta pregunta = new RugPregunta();
			pregunta.setDescPregunta(question);
			pregunta.setTipoPregunta(new Long(type));
			pregunta.setFecha(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
			
			preguntaSvc.savePregunta(pregunta);
			
			/** encuesta de satisfaccion **/
			if(type > 0) {
				String url = parametroService.getParam(Constants.URL_ENCUESTA).getValorParametro();
				UUID token = UUID.randomUUID();
				
				RugEncuestaToken encuesta = new RugEncuestaToken();
				encuesta.setToken(token.toString());
				encuesta.setEstado(0);
				encuestaSvc.save(encuesta);
				
				url = url + token.toString();
				
				// enviar correo con resultado exitoso	    			    			    		
				int idTipoMensaje = 1;
				int idAccountSmtp = Integer.valueOf(parametroService.getParam(Constants.ID_SMTP_MAIL_REGISTRO_USUARIOS).getValorParametro());
				String to = email;
				String cc = null;
				String cco = null;
				String subject = "";
				String message = "";
				
    			subject = parametroService.getParam(Constants.MAIL_SUBJECT_ENCUESTA).getValorParametro();
    			message = parametroService.getParam(Constants.MAIL_THEME_ENCUESTA).getValorParametro();
	    			
    			subject = subject.replace("@nombreCompleto", name);
    			message = message.replace("@nombreCompleto", name);
    			message = message.replace("@url", url);
    			
    			try {
    				MailUtils.getMailServiceInstance().sendMail(idTipoMensaje, idAccountSmtp, to, cc, cco, subject, message);
    			} catch(Exception e) {
    				// si el envio del correo falla, se captura la excepcion pero continua el proceso
    		    	e.printStackTrace();
    			}	    		
			} else { //envio de correo a los administadores
				// enviar correo con resultado exitoso	    			    		
	    		String emailAdmin = parametroService.getParam(Constants.MAIL_ADMIN).getValorParametro();
	    			    		
				int idTipoMensaje = 1;
				int idAccountSmtp = Integer.valueOf(parametroService.getParam(Constants.ID_SMTP_MAIL_REGISTRO_USUARIOS).getValorParametro());
				String to = emailAdmin;
				String cc = null;
				String cco = null;
				String subject = "";
				String message = "";
				
    			subject = parametroService.getParam(Constants.MAIL_SUBJECT_PREGUNTA).getValorParametro();
    			message = parametroService.getParam(Constants.MAIL_THEME_PREGUNTA).getValorParametro();
	    			    			
    			message = message.replace("@nombreCompleto", name);    			
    			message = message.replace("@correoElectronico", email);
    			message = message.replace("@pregunta", question);
    			
    			try {
    				MailUtils.getMailServiceInstance().sendMail(idTipoMensaje, idAccountSmtp, to, cc, cco, subject, message);
    			} catch(Exception e) {
    				// si el envio del correo falla, se captura la excepcion pero continua el proceso
    		    	e.printStackTrace();
    			}	   
				
			}
		}
				
		return "OK";
    }
	
	/*private Boolean isUserAuthenticated(String authString){
        		
        String decodedAuth = "";
        
        String[] authParts = authString.split("\\s+");
        String authInfo = authParts[1];
        
        byte[] bytes = null;
        try {
            bytes = new BASE64Decoder().decodeBuffer(authInfo);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        decodedAuth = new String(bytes);
        //System.out.println(decodedAuth);
         
        String[] values = decodedAuth.split(":");        
                
        if("Ch4tB0t$2020".equalsIgnoreCase(hash(values[1], "SHA-1"))) {
        	return true;
        } 
        
        return false;
    }

	public static String hash(String str, String algorithm) {
		StringBuffer sb = new StringBuffer();
		try {
			MessageDigest mdigest = MessageDigest.getInstance(algorithm);
			try {
				mdigest.update(str.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				mdigest.update(str.getBytes());
			}
			byte[] digest = mdigest.digest();
			for(int i = 0; i < digest.length; i++) {
				sb.append(String.format("%02x", digest[i]));
			}
		} catch (NoSuchAlgorithmException e) {
			sb.append(str);
			sb = sb.reverse();
		}
		
		return sb.toString();
	}*/
}
