package gt.gob.rgm.adm.rs;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.domain.Tramites;
import gt.gob.rgm.adm.model.RugPersonasFisicas;
import gt.gob.rgm.adm.model.VTramitesPagados;
import gt.gob.rgm.adm.service.TramitesService;

import java.util.List;

@Path("/reporte-tramites")
@RequestScoped
public class ReporteRs {

    @Inject
    TramitesService tramiteService;

    @POST
    @Path("/tramites")
    @Produces(MediaType.APPLICATION_JSON)
    public List<VTramitesPagados> tramites(Tramites tramite){
        return tramiteService.reporteTramite(tramite);
    }


    @GET
    @Path("/usuarios")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RugPersonasFisicas> getUserAll(){
        return tramiteService.ListUserAll();
    }

}
