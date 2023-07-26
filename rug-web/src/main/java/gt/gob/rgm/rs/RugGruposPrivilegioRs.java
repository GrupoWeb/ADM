package gt.gob.rgm.rs;


import gt.gob.rgm.model.RugGruposRoles;
import gt.gob.rgm.service.RugGrupoPrivilegio;
import gt.gob.rgm.util.FormGrupos;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/grupos")
public class RugGruposPrivilegioRs {

    private RugGrupoPrivilegio rugGrupoPrivilegio;

    public RugGruposPrivilegioRs() {
        rugGrupoPrivilegio = (RugGrupoPrivilegio) SpringApplicationContext.getBean("rugGrupoPrivilegio");
    }

    @POST
    @Path("/obtener")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response grupoPrivilegio(FormGrupos grupo){
        RugGruposRoles grupos = rugGrupoPrivilegio.setGrupoPrivilegio(grupo);
        return Response.ok(grupos).build();
    }
}
