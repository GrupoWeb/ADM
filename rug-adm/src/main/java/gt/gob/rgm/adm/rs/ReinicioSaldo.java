package gt.gob.rgm.adm.rs;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.service.RugReinicio;

@Path("/reinicio")
@RequestScoped
public class ReinicioSaldo {

    @Inject
    RugReinicio reinicio;
    



    @GET
    @Path("/saldo/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseRs reinicioSaldo(@PathParam(value="id") String codigo){
        ResponseRs resp = new ResponseRs();
        String resultado = reinicio.reinicioSaldoByCode(codigo);
        resp.setData(resultado);
        return resp;
        
    }
}
