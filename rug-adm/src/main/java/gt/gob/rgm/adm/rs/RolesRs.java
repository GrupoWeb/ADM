    package gt.gob.rgm.adm.rs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import gt.gob.rgm.adm.annotation.SecuredResource;
import gt.gob.rgm.adm.domain.Rol;
import gt.gob.rgm.adm.domain.ExternalUser;
import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.model.Roles;
import gt.gob.rgm.adm.service.RolService;
import gt.gob.rgm.adm.service.RugParametroConfService;
import gt.gob.rgm.adm.util.Constants;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/roles")
@RequestScoped
public class RolesRs {
	
	@Inject
	RolService rolService;
	
        @Inject
	RugParametroConfService parametroService;	
	
    @GET 
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public ResponseRs getMenus() {
    	ResponseRs response = new ResponseRs();
    	List<Rol> lroles = new ArrayList<>();
    	List<Roles> roles;
    	Long rolCount = 0L;    	
    	   System.out.println("ENTRA A BUSCAR ROLES");
    	//if(unico==0)   {
            
            roles = rolService.listRoles();
            rolCount = rolService.countRoles();
        /*}else{
            System.out.println("Unicos");
            deudores = usuarioService.listUsuarios_u(usuarioFilter, page, size);
    	usuariosCount = usuarioService.countUsuarios_u(usuarioFilter);
                }   */
    	
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String hostUrl = parametroService.getParam(Constants.HOST_URL_ADM).getValorParametro();
    	for(Roles rol : roles) {
            System.out.println(rol);
    		Rol role = new Rol();
    		role.setRolId(rol.getRolId());;
                role.setNomRol(rol.getNomRol());;
                role.setTipoRol(rol.getTipoRol());;
    		
                lroles.add(role);
    	}
    	response.setTotal(rolCount);
    	response.setData(lroles);    	
        return response;
    }
    
    @POST 
    @Path("/grabar")
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public ResponseRs setMenus(@FormDataParam(value = "page") Integer page,
            @FormDataParam(value = "size") Integer size, @FormDataParam(value="nombre") String nombre,
            @FormDataParam(value="tipo") String tipo) {
        List<Rol> lmenus = new ArrayList<>();
    	List<Roles> menus;
        Long menuCount = 0L;
        Rol rolFilter = new Rol();
        rolFilter.setNomRol(nombre);
        rolFilter.setTipoRol(tipo);
       

        menus = rolService.crearRoles( page, size,rolFilter);
        menuCount = rolService.countRoles();
      
        
    ResponseRs response = new ResponseRs();
    response.setTotal(50L);
    
    return response;
    }
    @POST 
    @Path("/borrar")
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public ResponseRs delMenus(
            @FormDataParam(value="rolId") Integer rolId, @FormDataParam(value="nomRol") String nomRol) {
        List<Rol> lroles = new ArrayList<>();
    	List<Roles> roles;
        Long rolCount = 0L;
        Rol rolFilter = new Rol();
        
        rolFilter.setRolId(rolId);
        rolFilter.setNomRol(nomRol);
        
        roles = rolService.delRol(rolFilter);
           
        

        

        //menus = menuService.crearMenus( page, size,menuFilter);
        rolCount = rolService.countRoles();
       
    ResponseRs response = new ResponseRs();
    response.setTotal(50L);
    
    return response;
    }
}
