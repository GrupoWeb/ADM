package gt.gob.rgm.service;

import javax.ws.rs.core.Response;
import gt.gob.rgm.mail.service.MailService;
import org.springframework.context.annotation.Scope;

@Scope("prototype")
public class SendMailServiceImp  implements SendMailService{

    private MailService sendMailService;


    public void setSendMailService(MailService sendMailService) {
        this.sendMailService = sendMailService;
    }

    /**
     * @return
     */
    @Override
    public Response sendMail() {
//        int result = emailServicio.EmailsInQueue(request);
//        Constants c = new Constants();
//        int idAccountSmtp = Integer.valueOf(c.getParamValue(Constants.IDSMPT_REGISTRO_USUARIO));
//        String subject = "prueba de envio";
//        String message = "prueba de correo desde RS";
//        String to = "jjolon@mineco.gob.gt";
//        sendMailService.SendMailResource(16096);
//        int idMail = sendMailService.sendMail(1,idAccountSmtp, to, null, null, subject, message);
//        sendMailService.sendMail();
        return null;
    }
}
