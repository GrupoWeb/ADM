package gt.gob.rgm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gt.gob.rgm.model.Archivo;

public interface ArchivoRepository extends JpaRepository<Archivo, Long> {
	List<Archivo> findByObjetoIdAndEstadoAndTipo(long objetoId, String estado, String tipo);
}
