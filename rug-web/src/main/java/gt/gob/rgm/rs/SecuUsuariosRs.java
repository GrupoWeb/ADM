package gt.gob.rgm.rs;

import gt.gob.rgm.service.SecuUsuariosService;
import gt.gob.rgm.service.UsuariosService;
import gt.gob.rgm.util.FormUsuarios;
import mx.gob.se.rug.administracion.service.UsuarioService;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/usuarios-admin")
public class SecuUsuariosRs {

    private SecuUsuariosService usuarioService;

    public SecuUsuariosRs(){
        usuarioService = (SecuUsuariosService) SpringApplicationContext.getBean("SecuUsuarioService");
    }

    @POST
    @Path("/verificar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verificarUsuarios(FormUsuarios usuarios){
        return Response.ok(usuarioService.findCveUser(usuarios)).build();
    }
}
