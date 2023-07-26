package gt.gob.rgm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import gt.gob.rgm.model.RugRelGrupoAcreedor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.Path;
import java.util.List;

public interface RugRelGrupoAcreedorRepository extends JpaRepository<RugRelGrupoAcreedor, Long> {

    @Query(value="SELECT r FROM RugRelGrupoAcreedor r WHERE r.idSubUsuario = :idPersona")
    RugRelGrupoAcreedor findByIdPersona(@Param("idPersona") Long idPersona);

    @Query(value = "SELECT ID_GRUPO FROM RUG_REL_GRUPO_ACREEDOR WHERE ID_SUB_USUARIO = :idPersona", nativeQuery = true)
    Long findByAcreedor(@Param("idPersona") Long idPersona);

    @Transactional
    @Modifying
    @Query(value = "UPDATE RugRelGrupoAcreedor r SET r.idGrupo = :idGrupo WHERE r.idSubUsuario = :idPersona")
    Integer updateByAcreedor(@Param("idGrupo") Long idGrupo, @Param("idPersona") Long idPersona);
}
