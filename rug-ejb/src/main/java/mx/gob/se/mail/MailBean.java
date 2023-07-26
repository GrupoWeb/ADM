
package mx.gob.se.mail;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import mx.gob.se.exception.NoDestinationAddressException;
import mx.gob.se.mail.to.AccountMailTO;
import mx.gob.se.mail.to.MailTO;
import mx.gob.se.mail.to.MessageTO;

/**
 * Session Bean implementation class MailBean
 */
@Stateless(mappedName = "ejb/MailBeanJNDI")
public class MailBean  implements MailBeanRemote, MailBeanLocal {

public static void main(String a[]){
	String cad=new String( "abcdef");
	String cadpa[]=cad.split(",");
	System.out.println(cadpa[0]);
	
}
	
	

	private  Properties getProperties(MailTO mailTO) {
		Properties p = new Properties();
		p.setProperty("mail.transport.protocol", "smtps");
		p.setProperty("mail.smtp.user", mailTO.getAccountMailTO().getSmtpUserMail());
		p.setProperty("mail.smtp.host", mailTO.getAccountMailTO().getSmtpHost());
		p.setProperty("mail.smtp.port", mailTO.getAccountMailTO().getSmtpPort());
		p.setProperty("mail.smtp.auth",mailTO.getAccountMailTO().getSmtpAuth());
		if(mailTO.getAccountMailTO().getSmtpAuth().equalsIgnoreCase("true")){
			p.setProperty("mail.smtp.ssl.enable", mailTO.getAccountMailTO().getSmtpSslEnable());
			p.setProperty("mail.smtp.starttls.enable","true");
			p.setProperty("mail.smtp.socketFactory.port", mailTO.getAccountMailTO().getSmtpPort());
			p.setProperty("mail.smtp.socketFactory.class",	 "javax.net.ssl.SSLSocketFactory");
			p.setProperty("mail.smtp.socketFactory.fallback", "false");
		}
		return p;
	}

	
	public  void sendMail(int idTipoMensaje,int idAccountSmtp,String to,String cc,String cco,String subject,String message ) {
		MailBitacoraDAO mailBitacoraDAO= new MailBitacoraDAO();
		MailTO mailTO= new MailTO();
		
		mailTO.setIdTipoMensaje(idTipoMensaje);
		
		AccountMailTO accountMailTO= new AccountMailTO();
		accountMailTO.setIdAccountSmtp(idAccountSmtp);
		 mailTO.setAccountMailTO(accountMailTO);
		 
		 
		 MessageTO messageTO= new MessageTO();
		 messageTO.setMailTo(to);
		 messageTO.setMailCc(cc);
		 messageTO.setMailCco(cco);
		 messageTO.setMailSubject(subject);
		 messageTO.setMailText(message);
		 
		 mailTO.setMessageTO(messageTO);
		 
		mailBitacoraDAO.logMail(mailTO);
		
	}
	
	public  void sendMail(){
		System.out.println("Ejb sendmail");
		MailBitacoraDAO mailBitacoraDAO= new MailBitacoraDAO();
		System.out.println("getMails");
		List<MailTO> mailTOs= mailBitacoraDAO.getMailToSend();
		Iterator<MailTO>iterator= mailTOs.iterator();
		while (iterator.hasNext()) {
			System.out.println("Mandando mail");
			MailTO mailTO = (MailTO) iterator.next();
			sendMail(mailTO);
		}
		
	}
	
	public  void sendMail(MailTO  mailTo) {
		MailBitacoraDAO mailBitacoraDAO= new MailBitacoraDAO();
		try {
			// statusProcesando mail
			mailTo.setIdStatus(2);
			mailBitacoraDAO.setMailStatus(mailTo);
			
			Authenticator auth = new SMTPAuthenticator(mailTo.getAccountMailTO().getSmtpUserMail(),mailTo.getAccountMailTO().getSmtpPasswordMail());
			Session sesion = Session.getInstance(getProperties(mailTo),auth);
			sesion.setDebug(true);
			MimeMessage men = new MimeMessage(sesion);
			men.setFrom(new InternetAddress(mailTo.getAccountMailTO().getSmtpUserMail()));
			
			if(	mailTo.getMessageTO().getMailTo()!=null||mailTo.getMessageTO().getMailCc()!=null||mailTo.getMessageTO().getMailCco()!=null){
			if(	mailTo.getMessageTO().getMailTo()!=null){
				String[] dest= mailTo.getMessageTO().getMailTo().split(",");
			for (int i = 0; i < dest.length; i++) {
				men.addRecipient(Message.RecipientType.TO, new InternetAddress(dest[i]));
			}
				
			}
			if(	mailTo.getMessageTO().getMailCc()!=null){
				String[] dest= mailTo.getMessageTO().getMailCc().split(",");
				for (int i = 0; i < dest.length; i++) {
					men.addRecipient(Message.RecipientType.CC, new InternetAddress(dest[i]));
				}
			}
			
			if(	mailTo.getMessageTO().getMailCco()!=null){
				String[] dest= mailTo.getMessageTO().getMailCco().split(",");
				for (int i = 0; i < dest.length; i++) {
					men.addRecipient(Message.RecipientType.BCC, new InternetAddress(dest[i]));
				}
			}
			}else{
				throw new NoDestinationAddressException("Mensaje no tiene Destinatarios");
			}
			men.setSubject(mailTo.getMessageTO().getMailSubject());
			men.setContent(mailTo.getMessageTO().getMailText(),mailTo.getAccountMailTO().getMailContentType());
			Transport t = sesion.getTransport("smtp");
			t.connect(mailTo.getAccountMailTO().getSmtpUserMail(),mailTo.getAccountMailTO().getSmtpPasswordMail());
			t.sendMessage(men, men.getAllRecipients());
			t.close();
			
			//marcamos mail como enviado 
			mailTo.setIdStatus(4);
			mailBitacoraDAO.setMailStatus(mailTo);
			
		} catch (Exception e) {
			System.out.println("No se pudo enviar mail con el id:::"+mailTo.getIdMail());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			
			mailTo.setExceptionMail("ID_MAIL="+mailTo.getIdMail()+ " :::::::::LocalizedMessage:::::::::"+e.getLocalizedMessage()+" :::::::::Message::::::::: "+e.getMessage()+ " :::::::::CAUSE::::::::: " + e.getCause()+"::stack::" + errors.toString() );
			mailTo.setIdStatus(1);
			mailBitacoraDAO.setMailStatus(mailTo);
			e.printStackTrace();
		}

	}
	
	
	

}