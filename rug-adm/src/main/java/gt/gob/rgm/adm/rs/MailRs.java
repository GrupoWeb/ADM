
package gt.gob.rgm.adm.rs;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import gt.gob.rgm.adm.service.RugParametroConfService;
import gt.gob.rgm.adm.model.RugMailPool;
import gt.gob.rgm.adm.service.MailSendServiceImp;
import gt.gob.rgm.adm.service.RugMailPoolService;

@Path("/mail")
@RequestScoped
public class MailRs {
	
	@Inject
	RugParametroConfService parametroService;
	
	@Inject
	MailSendServiceImp mailSendService;
	
	@Inject
	RugMailPoolService mailPoolService;
	
    @POST
    @Path("/{id}/send")
    @Produces(MediaType.APPLICATION_JSON)
    public RugMailPool sendPendingMails(@PathParam(value="id") Long id) {
    	mailSendService.sendMessage("ENVIAR:" + id);
    	
    	RugMailPool mail = mailPoolService.find(id);
    	
        return mail;
    }
    
    @PUT
    @Path("/{id}/state")
    @Produces(MediaType.APPLICATION_JSON)
    public RugMailPool reprocess(@PathParam(value="id") Long id, Long status) {
    	RugMailPool mail = mailPoolService.updateEstado(id, status);
    	return mail;
    }
}
