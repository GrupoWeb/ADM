package gt.gob.rgm.rs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import gt.gob.rgm.model.RugCatTipoTramite;
import gt.gob.rgm.service.TiposTramiteService;

@Component
@Path("/tipos-tramite")
public class TiposTramiteRs {
	private TiposTramiteService tiposTramiteService;


	
	public TiposTramiteRs() {
		tiposTramiteService = (TiposTramiteService) SpringApplicationContext.getBean("tiposTramiteService");
	}
	
	@GET
	@Path("/{id}")
	public Response getTipoTramite(@PathParam(value="id") Integer idTipoTramite) {
		RugCatTipoTramite tipoTramite = tiposTramiteService.getTipoTramite(idTipoTramite);
		return Response.ok(tipoTramite).build();
	}


}
