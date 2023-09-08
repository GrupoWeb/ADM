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
import gt.gob.rgm.adm.domain.Menu;
import gt.gob.rgm.adm.domain.ExternalUser;
import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.model.Menus;
import gt.gob.rgm.adm.service.MenuService;
import gt.gob.rgm.adm.service.RugParametroConfService;
import gt.gob.rgm.adm.util.Constants;
import javax.ws.rs.PathParam;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/menus")
@RequestScoped
public class MenusRs {
	
	@Inject
	MenuService menuService;
	
        @Inject
	RugParametroConfService parametroService;	
	
    @GET 
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public ResponseRs getMenus(@QueryParam(value = "page") Integer page,
            @QueryParam(value = "size") Integer size,@QueryParam(value = "menuId") Integer menuId,
            @QueryParam(value = "rol") String rol) {
    	ResponseRs response = new ResponseRs();
    	List<Menu> lmenus = new ArrayList<>();
    	List<Menus> menus;
    	Long menuCount = 0L;
        if(menuId != null)   {
            Menu menuFilter = new Menu();
            menuFilter.setMenuId(menuId);
            menuFilter.setRol(rol);
            menus = menuService.updateMenus( page, size,menuFilter);
            menuCount = menuService.countMenus();
            
        }else{
            menus = menuService.listMenus( page, size);
            menuCount = menuService.countMenus();
                }  
    	
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String hostUrl = parametroService.getParam(Constants.HOST_URL_ADM).getValorParametro();
    	for(Menus men : menus) {
            System.out.println(men);
    		Menu menu = new Menu();
    		menu.setMenuId(men.getMenuId());
                menu.setNomMenu(men.getNomMenu());
    		menu.setNomSubMenu(men.getNomSubMenu());
    		menu.setRol(men.getRol());
    		menu.setLink(men.getLink());
    		
                lmenus.add(menu);
    	}
    	response.setTotal(menuCount);
    	response.setData(lmenus);    	
        return response;
    }
    @POST 
    @Path("/grabar")
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public ResponseRs setMenus(@FormDataParam(value = "page") Integer page,
            @FormDataParam(value = "size") Integer size, @FormDataParam(value="nombreMenu") String nombreMenu,
            @FormDataParam(value="nombreSubMenu") String nombreSubMenu,@FormDataParam(value="rol") String rol,
            @FormDataParam(value="link") String link) {
        List<Menu> lmenus = new ArrayList<>();
    	List<Menus> menus;
        Long menuCount = 0L;
        Menu menuFilter = new Menu();
        menuFilter.setNomMenu(nombreMenu);
        menuFilter.setNomSubMenu(nombreSubMenu);
        menuFilter.setRol(rol);
        menuFilter.setLink(link);

        menus = menuService.crearMenus( page, size,menuFilter);
        menuCount = menuService.countMenus();
        System.out.println("nombreMenu:"+nombreMenu);
        System.out.println("nombreSubMenu:"+nombreSubMenu);
        System.out.println("rol:"+rol);
        System.out.println("link:"+link);
        
    ResponseRs response = new ResponseRs();
    response.setTotal(50L);
    
    return response;
    }
    @POST 
    @Path("/borrar")
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public ResponseRs delMenus(@FormDataParam(value="escabecera") String escabecera,
            @FormDataParam(value="nomMenu") String nomMenu,@FormDataParam(value="menuId") Integer menuId,
            @FormDataParam(value="nomSubMenu") String nomSubMenu,@FormDataParam(value="link") String link) {
        List<Menu> lmenus = new ArrayList<>();
    	List<Menus> menus;
        Long menuCount = 0L;
        Menu menuFilter = new Menu();
        if("1".equals(escabecera)){
            System.out.println("Entra a eliminar completo: "+nomMenu);
            menuFilter.setNomMenu(nomMenu);
            menus = menuService.delCMenus( menuFilter);
        }else{
            menuFilter.setNomSubMenu(nomSubMenu);
            menuFilter.setMenuId(menuId);
            menuFilter.setLink(link);
            
            menus = menuService.delMenus( menuFilter);
        }    
        

        

        //menus = menuService.crearMenus( page, size,menuFilter);
        menuCount = menuService.countMenus();
       
    ResponseRs response = new ResponseRs();
    response.setTotal(50L);
    
    return response;
    }
    
}
