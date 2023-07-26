package gt.gob.rgm.dao;

import gt.gob.rgm.model.RugGrupos;
import gt.gob.rgm.model.RugGruposRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RugGrupoRepository extends JpaRepository<RugGruposRoles, Integer> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO RUG_GRUPOS(ID_GRUPO,ID_ACREEDOR, DESC_GRUPO, ID_PERSONA_CREA, FH_CREACION, SIT_GRUPO) VALUES(5502,:idAcreedor,:DescGrupo,:idPersonaCrea,SYSDATE,'AC')", nativeQuery = true)
    void saveUserRole(@Param("idAcreedor") Long idAcreedor, @Param("DescGrupo") String DesGrupo, @Param("idPersonaCrea") Long idPersonaCrea);

    @Query(value = "SELECT s FROM RugGrupos s WHERE s.idAcreedor = :idAcreedor")
    RugGrupos findByAcreedor(@Param("idAcreedor") BigDecimal idAcreedor);

    @Query(value = "SELECT t.ID_GRUPO FROM (SELECT * FROM RUG_GRUPOS s WHERE s.ID_ACREEDOR = :idAcreedor ORDER BY s.ID_GRUPO DESC) t WHERE rownum = 1", nativeQuery = true)
    String findByUsuario(@Param("idAcreedor") BigDecimal idAcreedor);

}
