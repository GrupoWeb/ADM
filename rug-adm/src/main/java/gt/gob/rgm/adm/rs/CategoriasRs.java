package gt.gob.rgm.adm.rs;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import gt.gob.rgm.adm.annotation.SecuredResource;
import gt.gob.rgm.adm.model.CategoriaInformacion;
import gt.gob.rgm.adm.service.CategoriaServiceImp;

@Path("/categorias")
@Produces(MediaType.APPLICATION_JSON)
public class CategoriasRs {
	@Inject
	CategoriaServiceImp categoriaService;
	
	@GET
	@SecuredResource
	public List<CategoriaInformacion> getAll() {
		return categoriaService.getCategorias();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@SecuredResource
	public CategoriaInformacion create(CategoriaInformacion categoria) {
		return categoriaService.create(categoria);
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public CategoriaInformacion update(@PathParam(value="id") Integer id, CategoriaInformacion categoria) {
		categoria = categoriaService.update(id, categoria);
		return categoria;
	}
}
