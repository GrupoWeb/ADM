package gt.gob.rgm.dao;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import gt.gob.rgm.model.RugSecuUsuarios;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Path;

public interface RugSecuUsuariosRepository extends JpaRepository<RugSecuUsuarios, Long> {
	
	List<RugSecuUsuarios> findByCveUsuarioPadreAndSitUsuario(String cveUsuarioPadre, String sitUsuario);

	List<RugSecuUsuarios> findByCveUsuarioAndSitUsuario(String cveUsuario, String sitUsuario);

	RugSecuUsuarios findByCveUsuario(String cveUsuario);

	@Query(value="SELECT COUNT(r.isAdmin) FROM RugSecuUsuarios r WHERE r.cveUsuarioPadre = :cveUsuario AND r.isAdmin = 1", nativeQuery = false)
	List<RugSecuUsuarios> countIsAdmin(@Param("cveUsuario") String cveUsuario);

	@Query(value="SELECT r.cveUsuario FROM RugSecuUsuarios r WHERE r.idPersona = :idUsuario", nativeQuery = false)
	String cveUsuario(@Param("idUsuario") Long idUsuario);


	@Transactional
	@Modifying
	@Query(value="UPDATE RugSecuUsuarios r SET r.sitUsuario = 'IN' WHERE r.idPersona = :idUsuario")
	void updateStateUser(@Param("idUsuario") Long idUsuario);

	@Query(value="SELECT u FROM RugSecuUsuarios u WHERE u.cveUsuario = :email")
	RugSecuUsuarios getIdUsuarioByEmail(@Param("email") String email);
}
