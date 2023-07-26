package gt.gob.rgm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import gt.gob.rgm.model.RechazoCuenta;

public interface RechazoCuentaRepository extends JpaRepository<RechazoCuenta, Long> {
	RechazoCuenta findByToken(String token);
}
