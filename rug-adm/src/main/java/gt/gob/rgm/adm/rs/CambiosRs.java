package gt.gob.rgm.adm.rs;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import gt.gob.rgm.adm.annotation.SecuredResource;
import gt.gob.rgm.adm.domain.Change;
import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.exception.EntityAlreadyExistsException;
import gt.gob.rgm.adm.service.CambioService;

@Path("/cambios")
@RequestScoped
public class CambiosRs {

	@Inject
	CambioService cambioService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public ResponseRs getCambios(@QueryParam(value="page") Integer page, 
							     @QueryParam(value="size") Integer size, 
							     @QueryParam(value="texto") String texto, 
							     @QueryParam(value="impacto") Long impacto,
							     @QueryParam(value="estado") Long estado,
							     @QueryParam(value="fechaInicio") String fechaInicio, 
							     @QueryParam(value="fechaFin") String fechaFin) {
		
		ResponseRs response = new ResponseRs();
		Long changesCount = 0L;
		List<Change> changes = new ArrayList<Change>();
		Change filter = new Change();
		if(impacto != null) filter.setImpacto(impacto);
		if(estado != null) filter.setEstado(estado);
		if(texto != null) filter.setTexto(texto);
		
		changes = cambioService.findChangesByFilter(filter, page, size, fechaInicio, fechaFin);
		changesCount = cambioService.findChangesByFilterCount(filter, fechaInicio, fechaFin);
		
		
		response.setTotal(changesCount);
    	response.setData(changes);
		return response;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public Change addCambio(Change cambio) {
		try {
			cambio = cambioService.save(cambio);
		} catch(EntityAlreadyExistsException | ParseException e) {
			Response response = Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
			throw new WebApplicationException(response);
		}
		return cambio;
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public Change getCambio(@PathParam(value="id") Long id) {
		return cambioService.getCambio(id);
	}
	
	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Change updateCambio(@PathParam("id") Long id, Change cambio) {
		try {
			cambio = cambioService.update(id, cambio);
		} catch (ParseException e) {
			Response response = Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
			throw new WebApplicationException(response);
		}
		return cambio;
	}
}
