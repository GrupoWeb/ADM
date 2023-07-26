package gt.gob.rgm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import gt.gob.rgm.model.RugSecuUsuariosExternos;

public interface RugSecuUsuariosExternosRepository extends JpaRepository<RugSecuUsuariosExternos, Long>{

	RugSecuUsuariosExternos findByCveUsuario(String cveUsuario);
}
