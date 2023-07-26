package gt.gob.rgm.rs;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import mx.gob.se.rug.boleta.dao.BoletaDAO;
import mx.gob.se.rug.boleta.to.BoletaCertificacionTO;

@Component
@Path("/home")
public class QrRs {
	
	@GET
	@Path("/validar")
	public Response validarBoletar(@QueryParam(value="token") String token) throws URISyntaxException {
		
		try {
			BoletaDAO boletaDao = new BoletaDAO();
			BoletaCertificacionTO resultado = boletaDao.findBoletaByToken(token);
			if(resultado!=null) {
				return Response.temporaryRedirect(new URI("/Rug/home/validoQr.do?idTramite="+resultado.getIdTramite())).build();
			} else {
				return Response.temporaryRedirect(new URI("/Rug/home/invalidoQr.do")).build();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return Response.temporaryRedirect(new URI("/Rug/usuario/activatedError.do")).build();
	}

}
