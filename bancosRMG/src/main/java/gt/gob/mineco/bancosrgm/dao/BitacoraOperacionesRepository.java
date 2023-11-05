package gt.gob.mineco.bancosrgm.dao;

import gt.gob.mineco.bancosrgm.models.BitacoraOperaciones;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Reference
@Transactional
public interface BitacoraOperacionesRepository extends JpaRepository<BitacoraOperaciones, Integer> {
}
