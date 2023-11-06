package gt.gob.mineco.bancosrgm.dao;

import gt.gob.mineco.bancosrgm.models.VCodigoPersonas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VCodigoPersonasRepository extends JpaRepository<VCodigoPersonas, Integer> {

    @Query("SELECT v FROM VCodigoPersonas v WHERE UPPER(v.codigo) = UPPER(:codigoRegistro)")
    VCodigoPersonas findByCodigoRegistro(@Param("codigoRegistro") String pCodigoRegistro);
}
