package mx.gob.se.mail;
import javax.ejb.Remote;

@Remote
public interface MailBeanRemote {

	public void sendMail();
	public  void sendMail(int idTipoMensaje,int idAccountSmtp,String to,String cc,String cco,String subject,String message );
	
}
