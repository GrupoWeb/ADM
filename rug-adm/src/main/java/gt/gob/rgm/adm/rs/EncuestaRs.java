package gt.gob.rgm.adm.rs;

import java.text.ParseException;
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
import gt.gob.rgm.adm.domain.SurveyStats;
import gt.gob.rgm.adm.domain.SurveyStatsMulti;
import gt.gob.rgm.adm.service.RugRespuestaService;

@Path("/encuesta")
@RequestScoped
public class EncuestaRs {

	@Inject
	RugRespuestaService respuestaService;
	
	@GET
    @Path("/stats/single")
    @Produces(MediaType.APPLICATION_JSON)
	//@SecuredResource
    public List<SurveyStats> getStatsSingle(@QueryParam(value="fechaInicio") String fechaInicio,
    		@QueryParam(value="fechaFin") String fechaFin) {
    	
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		List<SurveyStats> stats = new ArrayList<SurveyStats>();
		
		SurveyStats stat2 = new SurveyStats();
		stat2.setLabel1("Meses");
		try {
			stat2.setData1si(respuestaService.findTotalByParams(1L, "1", formatter.parse(fechaInicio), formatter.parse(fechaFin)));
			stat2.setData1no(respuestaService.findTotalByParams(1L, "0", formatter.parse(fechaInicio), formatter.parse(fechaFin)));
			
			stat2.setData5si(respuestaService.findTotalByParams(5L, "1", formatter.parse(fechaInicio), formatter.parse(fechaFin)));
			stat2.setData5no(respuestaService.findTotalByParams(5L, "0", formatter.parse(fechaInicio), formatter.parse(fechaFin)));
			
			stat2.setData6si(respuestaService.findTotalByParams(6L, "1", formatter.parse(fechaInicio), formatter.parse(fechaFin)));
			stat2.setData6no(respuestaService.findTotalByParams(6L, "0", formatter.parse(fechaInicio), formatter.parse(fechaFin)));
			
			stat2.setData3uno(respuestaService.findTotalByParams(3L, "1", formatter.parse(fechaInicio), formatter.parse(fechaFin)));
			stat2.setData3dos(respuestaService.findTotalByParams(3L, "2", formatter.parse(fechaInicio), formatter.parse(fechaFin)));
			stat2.setData3tres(respuestaService.findTotalByParams(3L, "3", formatter.parse(fechaInicio), formatter.parse(fechaFin)));
			stat2.setData3cuatro(respuestaService.findTotalByParams(3L, "4", formatter.parse(fechaInicio), formatter.parse(fechaFin)));
			stat2.setData3cinco(respuestaService.findTotalByParams(3L, "5", formatter.parse(fechaInicio), formatter.parse(fechaFin)));
			
			stat2.setData4uno(respuestaService.findTotalByParams(4L, "1", formatter.parse(fechaInicio), formatter.parse(fechaFin)));
			stat2.setData4dos(respuestaService.findTotalByParams(4L, "2", formatter.parse(fechaInicio), formatter.parse(fechaFin)));
			stat2.setData4tres(respuestaService.findTotalByParams(4L, "3", formatter.parse(fechaInicio), formatter.parse(fechaFin)));
			stat2.setData4cuatro(respuestaService.findTotalByParams(4L, "4", formatter.parse(fechaInicio), formatter.parse(fechaFin)));
			stat2.setData4cinco(respuestaService.findTotalByParams(4L, "5", formatter.parse(fechaInicio), formatter.parse(fechaFin)));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		stats.add(stat2);
		
		return stats;
		
    }
	
	@GET
    @Path("/stats/multiple")
    @Produces(MediaType.APPLICATION_JSON)
	//@SecuredResource
    public List<SurveyStatsMulti> getStatsMultiple(@QueryParam(value="fechaInicio") String fechaInicio,
    		@QueryParam(value="fechaFin") String fechaFin,
    		@QueryParam(value="idPregunta") String idPregunta) {
    	
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		List<SurveyStatsMulti> stats = new ArrayList<SurveyStatsMulti>();
					
		try {
			stats = respuestaService.findGroupByParam(new Long(idPregunta), formatter.parse(fechaInicio), formatter.parse(fechaFin));			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		
		return stats;
		
    }
	
	@GET
    @Path("/stats/text")
    @Produces(MediaType.APPLICATION_JSON)
	//@SecuredResource
    public List<String> getStatsText(@QueryParam(value="fechaInicio") String fechaInicio,
    		@QueryParam(value="fechaFin") String fechaFin,
    		@QueryParam(value="idPregunta") String idPregunta) {
    	
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		List<String> stats = new ArrayList<String>();
					
		try {
			stats = respuestaService.findRespuestaByParam(new Long(idPregunta), formatter.parse(fechaInicio), formatter.parse(fechaFin));			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		
		return stats;
		
    }
}
