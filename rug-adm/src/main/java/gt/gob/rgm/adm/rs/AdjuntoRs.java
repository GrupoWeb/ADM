package gt.gob.rgm.adm.rs;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import gt.gob.rgm.adm.annotation.SecuredResource;
import gt.gob.rgm.adm.domain.Attachment;
import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.service.AdjuntoService;

@Path("/adjuntos")
@RequestScoped
public class AdjuntoRs {

	@Inject
	AdjuntoService adjuntoService;
	
	@GET 
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public ResponseRs getAdjuntos(@QueryParam(value="incidenteId") Long incidenteId) {
		ResponseRs response = new ResponseRs();
		
		List<Attachment> adjuntos = adjuntoService.findByIncidente(incidenteId);
		
		response.setData(adjuntos);
		response.setTotal(new Long(adjuntos.size()));
		
		return response;
	}
	
	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public Response addAdjunto(@FormDataParam("adjunto") InputStream inputStream,
							   @FormDataParam("adjunto") FormDataContentDisposition formDataContentDisposition,
							   @FormDataParam("idIncidente") Long idIncidente,
							   @Context HttpServletRequest request) {
		Attachment adjunto = new Attachment();
		
		try {
			String path = request.getServletContext().getRealPath("") + formDataContentDisposition.getFileName();
			System.out.println("URL:" + path);
						
			adjunto.setAdjunto(inputStream);
			adjunto.setDescripcion(formDataContentDisposition.getFileName());
			adjunto.setIncidenteId(idIncidente);
			adjunto.setTipo(formDataContentDisposition.getType());
			
			adjunto = adjuntoService.save(adjunto);
			
			return Response.ok(adjunto)
					.header("Access-Control-Allow-Orign", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD")
					.build();
		} catch(Exception e) {
			Response response = Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage())
					.header("Access-Control-Allow-Orign", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD")
					.build();
			throw new WebApplicationException(response);
		}		
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public Attachment getAdjunto(@PathParam(value="id") Long id) {
		return adjuntoService.getAdjunto(id);
	}
}
