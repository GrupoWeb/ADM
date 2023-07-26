package gt.gob.rgm.rs;

import org.springframework.stereotype.Component;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import gt.gob.rgm.service.SendMailService;

@Component
@Path("/mail")
public class SendMailRs {

    private SendMailService mailSendService;



    public SendMailRs(){
        mailSendService = (SendMailService) SpringApplicationContext.getBean("mailSendService");
    }

    @POST
    @Path("/send")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendMailTest(){
//        return Response.ok(mailSendService.sendMail(request)).build();
        return Response.ok("").build();
    }
}
