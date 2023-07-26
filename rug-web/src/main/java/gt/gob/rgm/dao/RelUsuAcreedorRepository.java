package gt.gob.rgm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import gt.gob.rgm.model.RelUsuAcreedor;
import gt.gob.rgm.model.RelUsuAcreedorPK;

public interface RelUsuAcreedorRepository extends JpaRepository<RelUsuAcreedor, RelUsuAcreedorPK> {
}
