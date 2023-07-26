package gt.gob.rgm.adm.service;

import java.text.ParseException;
import java.util.List;

import gt.gob.rgm.adm.domain.Incident;
import gt.gob.rgm.adm.exception.EntityAlreadyExistsException;

public interface IncidenteService {

	Incident getIncidente(Long id);
	
	List<Incident> getAll();
	
	Incident save(Incident incidente) throws EntityAlreadyExistsException, ParseException;
	
	Incident update(Long id, Incident incidente) throws ParseException;
	
	List<Incident> findIncidentsByFilter(Incident filtro, Integer page, Integer size, String fechaInicio, String fechaFin);
	
	Long findIncidentsByFilterCount(Incident filtro, String fechaInicio, String fechaFin);
}
