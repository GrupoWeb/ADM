package gt.gob.rgm.adm.service;

import java.text.ParseException;
import java.util.List;

import gt.gob.rgm.adm.domain.Change;
import gt.gob.rgm.adm.exception.EntityAlreadyExistsException;

public interface CambioService {

	Change getCambio(Long id);
	
	List<Change> getAll();
	
	Change save(Change cambio) throws EntityAlreadyExistsException, ParseException;
	
	Change update(Long id, Change cambio) throws ParseException;
	
	List<Change> findChangesByFilter(Change filtro, Integer page, Integer size, String fechaInicio, String fechaFin);
	
	Long findChangesByFilterCount(Change filtro, String fechaInicio, String fechaFin);
}
