package gt.gob.rgm.dao;

import gt.gob.rgm.model.RugGrupoSubUsuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RugGrupoSubUsuarioRepository extends JpaRepository<RugGrupoSubUsuarios, Integer> {

    @Query(value = "SELECT r FROM RugGrupoSubUsuarios r WHERE r.idUsuario = :idUsuario")
    RugGrupoSubUsuarios findGrupoActual(@Param("idUsuario") long idUsuario);

    @Transactional
    @Modifying
    @Query(value = "UPDATE RugGrupoSubUsuarios s SET s.idGrupo = :idGrupo WHERE s.id = :idUsuario")
    Integer updateGrupoSub(@Param("idUsuario") Integer idUsuario, @Param("idGrupo") Integer idGrupo);


}
