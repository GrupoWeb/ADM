package gt.gob.rgm.dao;

import gt.gob.rgm.model.RugPrivilegio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RugPrivilegioRepository extends JpaRepository<RugPrivilegio, Long> {
    @Query(value = "SELECT r.* FROM RUG_PRIVILEGIOS r WHERE r.ID_PRIVILEGIO in (74,75,76,77,78)", nativeQuery = true)
    List<RugPrivilegio> getPermisosMenu();
}
