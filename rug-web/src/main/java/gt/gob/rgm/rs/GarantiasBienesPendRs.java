package gt.gob.rgm.rs;


import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import gt.gob.rgm.model.RugGarantiasBienesPend;
import gt.gob.rgm.service.RugGarantiasBienesPendService;


@Component
@Path("/garantias-pend")
public class GarantiasBienesPendRs {
    private RugGarantiasBienesPendService rugGarantiasBienesPendService;

    public GarantiasBienesPendRs(){
        rugGarantiasBienesPendService = (RugGarantiasBienesPendService) SpringApplicationContext.getBean("rugGarantiasBienesPendService");
    }


    @GET
	@Path("/verificacion/{id}")
	public Response getValorBienesByTramite(@PathParam(value="id") Integer idTramiteTemp) {
		List<RugGarantiasBienesPend> bienTemporal = rugGarantiasBienesPendService.getValorBienesByTramiteTemp(idTramiteTemp);
		return Response.ok(bienTemporal).build();
	}

    @GET
    @Path("/verificacion-bien/{id}")
    public Response getBienesByGarantia(@PathParam(value="id") Integer idGarantia){
        List<String> listaBienes =  rugGarantiasBienesPendService.getTramitesByGarantia(idGarantia);
        return Response.ok(listaBienes).build();
    }

    @GET
    @Path("/verificacion-bienes/{idTramite}")
    public Response getBienes(@PathParam(value="idTramite") Integer idTramite){
        List<String> listaTipoBien = rugGarantiasBienesPendService.getBienesTramites(idTramite);
        return Response.ok(listaTipoBien).build();
    }
    @GET
    @Path("/cantidad-facturas/{idTramite}")
    public Response getCountFacturas(@PathParam(value="idTramite") Integer idTramite){
        List<String> countFacturas = rugGarantiasBienesPendService.getCountFacturasByTramite(idTramite);
        return Response.ok(countFacturas).build();
    }

    @GET
    @Path("/cantidad-garantia/{idGarantia}")
    public Response getCountFacturaByGarantia(@PathParam("idGarantia") Integer idGarantia){
        List<String> cantidad = rugGarantiasBienesPendService.getCountFacturaByGarantia(idGarantia);
        return Response.ok(cantidad).build();
    }


    
}
