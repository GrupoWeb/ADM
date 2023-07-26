package gt.gob.rgm.rs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import gt.gob.rgm.service.RugSignature;

@Component
@Path("/signature")
public class FirmaElectronica {

    private RugSignature signatureFile;


    public FirmaElectronica() {
		signatureFile = (RugSignature) SpringApplicationContext.getBean("signatureFile");
	}

    @GET
    @Path("/certificacion/{idGarantia}/{idTramite}/{idUsuario}/{idTipoTramiteRest}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response signatureTest(@PathParam("idGarantia") Integer idGarantia, @PathParam("idTramite") Integer idTramite, @PathParam("idUsuario") Integer idUsuario,@PathParam("idTipoTramiteRest") Integer idTipoTramiteRest){
        return signatureFile.signatureFiles(idGarantia, idTramite, idUsuario, idTipoTramiteRest);
    }
    
}
