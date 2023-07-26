    package gt.gob.rgm.adm.rs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import gt.gob.rgm.adm.annotation.SecuredResource;
import gt.gob.rgm.adm.domain.Garantia;
import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.model.VRepListadoGarantias;
import gt.gob.rgm.adm.service.GarantiaService;
import gt.gob.rgm.adm.service.RugParametroConfService;
import gt.gob.rgm.adm.util.Constants;

@Path("/garantia")
@RequestScoped
public class GarantiaRs {
	
	@Inject
	GarantiaService garantiaService;
	
        @Inject
	RugParametroConfService parametroService;	
	
    @GET 
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public ResponseRs getGarantias_(
            @QueryParam(value="fechaInicio") String fechaInicio,@QueryParam(value="fechaFin") String fechaFin,
            @QueryParam(value="idDeudor") Integer idDeudor,@QueryParam(value="idAcreedor") Integer idAcreedor,
            @QueryParam(value="nDeudor") String nDeudor,@QueryParam(value="nAcreedor") String nAcreedor,
            @QueryParam(value="idGarantia") Integer idGarantia,@QueryParam(value="idUsuario") Integer idUsuario,
            @QueryParam(value="acreedor_b") Integer acreedor_b,@QueryParam(value="textobusqueda") String textobusqueda, 
            @QueryParam(value="page") Integer page, @QueryParam(value="size") Integer size) {
    	ResponseRs response = new ResponseRs();
    	List<Garantia> garantia = new ArrayList<>();
    	List<VRepListadoGarantias> garantias;
    	Long garantiasCount = 0L;    	
    	Garantia garantiaFilter = new Garantia();  
    	garantiaFilter.setTextobusqueda(textobusqueda);        
        garantiaFilter.setIdGarantia(idGarantia);
        garantiaFilter.setFechaInicio(fechaInicio);
        garantiaFilter.setFechaFin(fechaFin);        
        garantiaFilter.setIdUsuario(idUsuario);
        garantiaFilter.setnAcreedor(nAcreedor);
        garantiaFilter.setnDeudor(nDeudor);
        garantiaFilter.setIdAcreedor(idAcreedor);
        garantiaFilter.setIdDeudor(idDeudor);
        
    	   /*if(acreedor_b==1){
                garantias = garantiaService.listGarantiasAcr(garantiaFilter, page, size);
                garantiasCount = garantiaService.countGarantiasAcr(garantiaFilter);
           }else{*/
                garantias = garantiaService.listGarantias(garantiaFilter, page, size);
                garantiasCount = garantiaService.countGarantias(garantiaFilter);
           //}
            
        
    	
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String hostUrl = parametroService.getParam(Constants.HOST_URL_ADM).getValorParametro();
        
                
    	for(VRepListadoGarantias garantia_ : garantias) {
    		Garantia garant = new Garantia();
    		garant.setIdGarantia(garantia_.getIdGarantia());
                garant.setMovs(garantia_.getMovs());
                garant.setInscr(garantia_.getInscr() );
                garant.setCert(garantia_.getCert());
                garant.setModif(garantia_.getModif());
                garant.setCancelado(garantia_.getCancelado());
                garant.setEjec(garantia_.getEjec());
                garant.setProrr(garantia_.getProrr());
                garant.setfInscr(garantia_.getfInscr());
                garant.setIdTramite(garantia_.getIdTramite());
                garant.setdTramite(garantia_.getdTramite());
                garant.setVigencia(garantia_.getVigencia());
                garant.setdGarantia(garantia_.getdGarantia());
                garant.setgStatus(garantia_.getgStatus());
                garant.setSolicitante(garantia_.getgStatus());
                garant.setsOriginal(garantia_.getsOriginal());
                garant.setIdDeudor(garantia_.getIdDeudor());
                garant.setnDeudor(garantia_.getnDeudor());
                garant.setIdAcreedor(garantia_.getIdAcreedor());
                garant.setnAcreedor(garantia_.getnAcreedor());
                garant.setIdUsuario(garantia_.getIdUsuario());
    		garant.setnUsuario(garantia_.getnUsuario());
    		
                garantia.add(garant);
    	}
    	response.setTotal(garantiasCount);
    	response.setData(garantia);    	
        return response;
    }
    
    
}
