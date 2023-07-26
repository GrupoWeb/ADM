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
import gt.gob.rgm.adm.domain.Incident;
import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.exception.EntityAlreadyExistsException;
import gt.gob.rgm.adm.service.IncidenteService;

@Path("/incidentes")
@RequestScoped
public class IncidenteRs {

	@Inject
	IncidenteService incidenteService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public ResponseRs getIncidentes(@QueryParam(value="page") Integer page, 
								    @QueryParam(value="size") Integer size, 
								    @QueryParam(value="texto") String texto, 
								    @QueryParam(value="tipoSolicitud") Long tipoSolicitud,
								    @QueryParam(value="categoria") Long categoria,
								    @QueryParam(value="modoIngreso") Long modoIngreso,
								    @QueryParam(value="prioridad") Long prioridad,
								    @QueryParam(value="impacto") Long impacto,
								    @QueryParam(value="estado") Long estado,
								    @QueryParam(value="fechaInicio") String fechaInicio, 
								    @QueryParam(value="fechaFin") String fechaFin) {
		
		ResponseRs response = new ResponseRs();
		Long incidentsCount = 0L;
		List<Incident> incidents = new ArrayList<Incident>();
		Incident filter = new Incident();
		if(impacto != null) filter.setImpacto(impacto);
		if(estado != null) filter.setEstado(estado);		
		if(tipoSolicitud != null) filter.setTipoSolicitud(tipoSolicitud);
		if(categoria != null) filter.setCategoria(categoria);
		if(modoIngreso != null) filter.setModoIngreso(modoIngreso);
		if(prioridad != null) filter.setPrioridad(prioridad);		
		if(texto != null) filter.setTexto(texto);
		
		incidents = incidenteService.findIncidentsByFilter(filter, page, size, fechaInicio, fechaFin);
		incidentsCount = incidenteService.findIncidentsByFilterCount(filter, fechaInicio, fechaFin);
		
		
		response.setTotal(incidentsCount);
    	response.setData(incidents);
		return response;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public Incident addIncidente(Incident incidente) {
		try {
			incidente = incidenteService.save(incidente);
		} catch(EntityAlreadyExistsException | ParseException e) {
			Response response = Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
			throw new WebApplicationException(response);
		}
		return incidente;
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public Incident getIncidente(@PathParam(value="id") Long id) {
		return incidenteService.getIncidente(id);
	}
	
	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Incident updateCambio(@PathParam("id") Long id, Incident incidente) {
		try {
			incidente = incidenteService.update(id, incidente);
		} catch (ParseException e) {
			Response response = Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
			throw new WebApplicationException(response);
		}
		return incidente;
	}
}
