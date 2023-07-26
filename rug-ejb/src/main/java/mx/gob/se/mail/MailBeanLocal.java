package mx.gob.se.mail;

import javax.ejb.Local;

@Local
public interface MailBeanLocal {


	public void sendMail();
	public  void sendMail(int idTipoMensaje,int idAccountSmtp,String to,String cc,String cco,String subject,String message );
	
}
