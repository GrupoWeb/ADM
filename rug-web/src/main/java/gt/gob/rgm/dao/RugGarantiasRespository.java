package gt.gob.rgm.dao;

import gt.gob.rgm.model.RugGarantias;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RugGarantiasRespository  extends CrudRepository<RugGarantias, Integer> {

    @Query(value = "SELECT t.idTipoGarantia FROM RugGarantias t WHERE t.id = :idGarantia", nativeQuery = false)
    public List<RugGarantias> findTypeGarantia(@Param("idGarantia") Integer idGarantia);

    @Query(value = "SELECT r FROM RugGarantias r WHERE r.id = :idGarantia")
    RugGarantias getGarantiaById(@Param("idGarantia") Integer idGarantia);
}
