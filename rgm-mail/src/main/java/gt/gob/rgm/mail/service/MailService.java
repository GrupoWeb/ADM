package gt.gob.rgm.mail.service;

import gt.gob.rgm.mail.domain.MailTO;

public interface MailService {
	public void sendMail();
	
	public int sendMail(int idTipoMensaje, int idAccountSmtp, String to, String cc, String cco, String subject, String message);
	
	public void sendSingleMail(int id);

}
