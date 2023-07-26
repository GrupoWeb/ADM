package gt.gob.rgm.adm.rs;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import gt.gob.rgm.adm.annotation.SecuredResource;
import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.model.ClasificacionTermino;
import gt.gob.rgm.adm.service.TerminoServiceImp;

@Path("/terminos")
@Produces(MediaType.APPLICATION_JSON)
public class TerminosRs {
	@Inject
	TerminoServiceImp terminoService;
	
	@GET
	@SecuredResource
	public ResponseRs getAll(@QueryParam(value="page") Integer page, @QueryParam(value="size") Integer size, @QueryParam(value="sin-clasificar") Boolean sinClasificar) {
		ResponseRs response = new ResponseRs();
		List<ClasificacionTermino> terminos = terminoService.getTerminos(sinClasificar, page, size);
		Long terminosCount = terminoService.countTerminos(sinClasificar);
		response.setTotal(terminosCount);
    	response.setData(terminos);
		return response;
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public ClasificacionTermino update(@PathParam(value="id") Long id, ClasificacionTermino termino) {
		return terminoService.update(id, termino);
	}
}
