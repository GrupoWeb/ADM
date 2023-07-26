package gt.gob.rgm.adm.rs;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.service.RugSignatureFile;
import gt.gob.rgm.adm.util.FormSignature;

@Path("/firma")
@RequestScoped
public class FirmaBoleta {

    @Inject
    private RugSignatureFile signature;

    @GET
//    @Path("/signature")
    @Path("/signature/{tramite}/{garantia}/{email}")
//    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signatureFile(@PathParam("tramite") Integer tramite, @PathParam("garantia") Integer garantia, @PathParam("email") String email){
        return Response.ok(signature.signatureFiles(tramite, garantia, email)).build();
    }
    
}
