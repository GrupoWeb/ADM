package gt.gob.rgm.service;

import gt.gob.rgm.dao.RugGrupoRepository;
import gt.gob.rgm.model.RugGruposRoles;
import gt.gob.rgm.util.FormGrupos;
import org.springframework.context.annotation.Scope;

import java.sql.Date;
import java.time.LocalDate;

@Scope("prototype")
public class RugGrupoPrivilegioImp implements RugGrupoPrivilegio{

    private RugGrupoRepository grupoDao;

    public void setGrupoDao(RugGrupoRepository grupoDao) {
        this.grupoDao = grupoDao;
    }

    @Override
    public RugGruposRoles setGrupoPrivilegio(FormGrupos grupo) {
        LocalDate now = LocalDate.now();
        RugGruposRoles rPrivilegio = new RugGruposRoles();
        rPrivilegio.setDescGrupo("true");
        rPrivilegio.setDescGrupo(grupo.getDescGrupo());
        rPrivilegio.setFhCreacion(Date.valueOf(now));
        rPrivilegio.setIdAcreedor(grupo.getIdAcreedor());
        rPrivilegio.setIdPersonaCrea(grupo.getIdPersonaCrea());
        rPrivilegio.setSitGrupo("AC");
        grupoDao.save(rPrivilegio);
//        grupoDao.saveUserRole(grupo.getIdAcreedor(), grupo.getDescGrupo(), grupo.getIdPersonaCrea());
        return rPrivilegio;
    }
}
