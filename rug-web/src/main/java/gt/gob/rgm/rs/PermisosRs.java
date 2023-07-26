package gt.gob.rgm.rs;

import gt.gob.rgm.model.RugGarantiasPermiso;
import gt.gob.rgm.service.RugPermisosService;
import gt.gob.rgm.service.UsuariosService;
import gt.gob.rgm.util.FormPermisos;
import org.directwebremoting.datasync.ExposeToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Controller
@Path("/permisos")
public class PermisosRs {

    private RugPermisosService rugPermisosService;

    private UsuariosService usuariosService;

    public PermisosRs() {
        rugPermisosService = (RugPermisosService) SpringApplicationContext.getBean("rugPermisosService");
        usuariosService = (UsuariosService) SpringApplicationContext.getBean("usuariosService");
    }

    @POST
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getGrupoByUser(FormPermisos permiso){
        return Response.ok(rugPermisosService.findByUser(permiso.getIdPersona())).build();
    }

    @POST
    @Path("/deshabilitar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePermiso(FormPermisos permiso){
        return Response.ok(rugPermisosService.deletePermisoById(permiso.getIdRelacion(), permiso.getSitRelacion())).build();
    }

    @POST
    @Path("grupo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getIdGrupo(FormPermisos permiso){
        return Response.ok(rugPermisosService.getIdRolByUser(permiso.getIdPersona())).build();
    }

    @POST
    @Path("rol-dinamico")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getIdGrupoDinamico(FormPermisos permiso){
        return Response.ok(usuariosService.permisosDinamicos(permiso.getIdPersona(), permiso.getNombre(), permiso.getIsAdmin())).build();
    }

    @GET
    @Path("operaciones-listado")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOperacionesMenu(){
        return Response.ok(rugPermisosService.getPermisosMenu()).build();
    }

    @POST
    @Path("asignar-operacion")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setOperacionPermiso(FormPermisos operacion){
        return Response.ok(rugPermisosService.setOperacionPermiso(operacion.getIdPersona(), operacion.getIdOperacion())).build();
    }

    @GET
    @Path("garantias-asignadas/{usuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGarantiasAsignadas(@PathParam("usuario") String usuario){
        try{
            Object object = new Object() {
                public Iterable<RugGarantiasPermiso> data = rugPermisosService.getGarantiaPermisos(new Integer(usuario));
            };
            return Response.ok(object).build();
        }catch (Exception e){
            e.printStackTrace();
            return Response.ok(false).build();
        }
    }

    @POST
    @Path("asignar-garantia")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setGarantiaPermisoByUsuario(FormPermisos garantia){
        return  Response.ok(rugPermisosService.setGarantiaPermisoByUser(garantia.getIdGarantia(), garantia.getIdUsuario())).build();
    }

    @POST
    @Path("desactivar-garantia")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response desabilitarGarantia(FormPermisos garantia){
        return  Response.ok(rugPermisosService.desabilitarGarantia(garantia.getIdGarantia(), garantia.getIdUsuario())).build();
    }

    @POST
    @Path("validarGarantia")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response validarGarantia(FormPermisos garantia){
        return  Response.ok(rugPermisosService.validarGarantiaPertenencia(garantia.getIdGarantia(), garantia.getIdUsuario())).build();
    }



}
