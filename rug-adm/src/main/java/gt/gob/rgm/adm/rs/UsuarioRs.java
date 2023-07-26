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
import gt.gob.rgm.adm.domain.Usuario;
import gt.gob.rgm.adm.domain.ExternalUser;
import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.model.VRepListadoUsuarios;
import gt.gob.rgm.adm.service.Usuario_Service;
import gt.gob.rgm.adm.service.RugParametroConfService;
import gt.gob.rgm.adm.util.Constants;

@Path("/usuario")
@RequestScoped
public class UsuarioRs {
	
	@Inject
	Usuario_Service usuarioService;
	
        @Inject
	RugParametroConfService parametroService;	
	
    @GET 
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public ResponseRs getUsuarios_(
            @QueryParam(value="idUsuario_b") String idUsuario_b,
            @QueryParam(value="textobusqueda") String textobusqueda, 
            @QueryParam(value="page") Integer page, @QueryParam(value="size") Integer size) {
    	ResponseRs response = new ResponseRs();
    	List<Usuario> users = new ArrayList<>();
    	List<VRepListadoUsuarios> usuarios;
    	Long usuariosCount = 0L;    	
    	Usuario usuarioFilter = new Usuario();  
    	usuarioFilter.setTextobusqueda(textobusqueda);
        usuarioFilter.setIdUsuario_b(idUsuario_b);
    	//if(unico==0)   {
            
            usuarios = usuarioService.listUsuarios(usuarioFilter, page, size);
            usuariosCount = usuarioService.countUsuarios(usuarioFilter);
        /*}else{
            System.out.println("Unicos");
            deudores = usuarioService.listUsuarios_u(usuarioFilter, page, size);
    	usuariosCount = usuarioService.countUsuarios_u(usuarioFilter);
                }   */
    	
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String hostUrl = parametroService.getParam(Constants.HOST_URL_ADM).getValorParametro();
    	for(VRepListadoUsuarios usuario : usuarios) {
            System.out.println(usuario);
    		Usuario usua = new Usuario();
    		usua.setIdPersona(usuario.getIdPersona());
    		usua.setRfc(usuario.getRfc());
    		usua.setNombrePersona(usuario.getNombrePersona());
    		usua.setPerJuridica(usuario.getPerJuridica());
    		usua.setIdNacionalidad(usuario.getIdNacionalidad());
    		usua.setNomNacionalidad(usuario.getNomNacionalidad());
    		usua.setEmail(usuario.getEmail());
    		usua.setIdDomicilio(usuario.getIdDomicilio());
    		usua.setDireccion(usuario.getDireccion());
    		usua.setcGarantias(usuario.getcGarantias());
    		usua.setcVigentes(usuario.getcVigentes());
                usua.setCnVigentes(usuario.getCnVigentes());
                usua.setcCanceladas(usuario.getcCanceladas());
                users.add(usua);
    	}
    	response.setTotal(usuariosCount);
    	response.setData(users);    	
        return response;
    }
    
    
}
