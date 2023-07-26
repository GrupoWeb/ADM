package gt.gob.rgm.adm.service;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Stateless;

import gt.gob.rgm.adm.util.MailUtils;

@Stateless
public class MailCronServiceImp {

	public MailCronServiceImp() {
	}
	
	@Schedule(minute="1", hour="*", dayOfWeek="*")
	public void enviarCorreosPendientes() {
		try {
			MailUtils.getMailServiceInstance().sendMail();
		} catch(Exception e) {
			// si el envio del correo falla, se captura la excepcion pero continua el proceso
	    	e.printStackTrace();
		}
	}
}
