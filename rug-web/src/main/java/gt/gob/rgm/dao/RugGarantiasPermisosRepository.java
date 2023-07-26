package gt.gob.rgm.dao;

import gt.gob.rgm.model.RugGarantiasPermiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface RugGarantiasPermisosRepository extends JpaRepository<RugGarantiasPermiso, Long> {
    @Query(value = "SELECT r FROM RugGarantiasPermiso r WHERE r.idUsuario = :idUsuario AND r.sit = 'AC'")
    Iterable<RugGarantiasPermiso> getGarantias(@Param("idUsuario") Integer idUsuario);

    @Transactional
    @Modifying
    @Query(value = "UPDATE RugGarantiasPermiso r SET r.sit = 'IN', r.actualizadoEn = :actualizadoEn, r.inhabilitadoEn = :eliminadoEn WHERE r.idGarantia = :idGarantia AND r.idUsuario = :idUsuario AND r.sit = 'AC'")
    Integer findByidGarantiaAndSit(@Param("idGarantia") Integer idGarantia, @Param("idUsuario") Integer idUsuario, @Param("actualizadoEn") Date actualizadoEn, @Param("eliminadoEn") Date eliminadoEn);

    @Query(value = "SELECT r FROM RugGarantiasPermiso r WHERE r.idGarantia = :idGarantia AND r.sit = 'AC'")
    RugGarantiasPermiso verificarExistente(@Param("idGarantia") Integer idGarantia);
}
