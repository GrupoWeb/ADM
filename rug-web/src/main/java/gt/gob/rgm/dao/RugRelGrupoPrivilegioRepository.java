package gt.gob.rgm.dao;

import gt.gob.rgm.model.RugPrivilegio;
import gt.gob.rgm.model.RugRelGrupoPrivilegio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RugRelGrupoPrivilegioRepository extends JpaRepository<RugRelGrupoPrivilegio, Long> {

    @Query(value = "SELECT r FROM RugRelGrupoPrivilegio r INNER JOIN RugPrivilegio p ON p.idPrivilegio = r.idPrivilegio WHERE r.idGrupo = :idGrupo")
    List<RugRelGrupoPrivilegio> findByGrupo(@Param("idGrupo") Long idGrupo);

    @Transactional
    @Modifying
    @Query(value = "UPDATE RugRelGrupoPrivilegio r SET r.sitRelacion = :sitRelacion WHERE r.idRelacion = :idPrivilegio")
    Integer inactivarRelacion(@Param("idPrivilegio") Long idPrivilegio, @Param("sitRelacion") String sitRelacion);

    @Query(value = "SELECT r FROM RugRelGrupoPrivilegio r WHERE r.idGrupo = :idGrupo AND r.idPrivilegio = :idPrivilegio")
    RugRelGrupoPrivilegio buscarPrivilegio(@Param("idGrupo") Long idGrupo, @Param("idPrivilegio") Long idPrivilegio);


}
