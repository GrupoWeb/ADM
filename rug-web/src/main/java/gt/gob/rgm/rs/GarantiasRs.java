package gt.gob.rgm.rs;


import gt.gob.rgm.model.RugGarantias;
import gt.gob.rgm.service.RugGarantiasService;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Component
@Path("/garantias")
public class GarantiasRs {

    private RugGarantiasService rugGarantiasService;

    public GarantiasRs(){
        rugGarantiasService = (RugGarantiasService) SpringApplicationContext.getBean("rugGarantiasService");
    }

    @GET
    @Path("/verificar-tipo/{idGarantia}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTipoGarantia(@PathParam("idGarantia") Integer idGarantia){
        List<RugGarantias> garantia = rugGarantiasService.findTypeGarantia(idGarantia);
        return Response.ok(garantia).build();
    }

}
