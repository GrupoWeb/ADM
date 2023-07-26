package gt.gob.rgm.adm.rs;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import gt.gob.rgm.adm.annotation.SecuredResource;
import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.model.RugCatNacionalidades;
import gt.gob.rgm.adm.service.NacionalidadesServiceImp;

@Path("/nacionalidades")
@Produces(MediaType.APPLICATION_JSON)
public class NacionalidadesRs {
	@Inject
	NacionalidadesServiceImp nacionalidadesService;
	
	@GET
	@SecuredResource
	public ResponseRs getAll(@QueryParam(value="status") String status) {
		ResponseRs response = new ResponseRs();
		List<RugCatNacionalidades> nacionalidades = nacionalidadesService.getNacionalidadesByStatus(status);
		response.setData(nacionalidades);
		return response;
	}
}
