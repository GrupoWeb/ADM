package gt.gob.rgm.adm.util;

import gt.gob.rgm.mail.service.MailService;
import gt.gob.rgm.mail.service.MailServiceImp;

public class MailUtils {
	static MailService mailService = null;
	
	public static MailService getMailServiceInstance() {
		if(mailService == null) {
			mailService = new MailServiceImp();
		}
		return mailService;
	}
}
