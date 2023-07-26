package gt.gob.rgm.adm.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.IncidenteRepository;
import gt.gob.rgm.adm.domain.Incident;
import gt.gob.rgm.adm.exception.EntityAlreadyExistsException;
import gt.gob.rgm.adm.model.Incidente;

@Stateless
public class IncidenteServiceImp implements IncidenteService {
	
	@Inject
	private IncidenteRepository incidenteDao;
	
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	public Incident getIncidente(Long id) {
		Incident incidente = new Incident();
		Incidente incidenteBD = incidenteDao.findById(id); 
		
		incidente.setIncidenteId(incidenteBD.getIncidenteId());
		incidente.setAsunto(incidenteBD.getAsunto());
		incidente.setCategoria(incidenteBD.getCategoria()!=null?incidenteBD.getCategoria().longValue():null);
		incidente.setDescripcion(incidenteBD.getDescripcion());
		incidente.setDiasPausa(incidenteBD.getDiasPausa()!=null?incidenteBD.getDiasPausa().longValue():null);
		incidente.setEstado(incidenteBD.getEstado()!=null?incidenteBD.getEstado().longValue():null);
		incidente.setFechaCreacion(dateFormat.format(incidenteBD.getFechaCreacion()));
		incidente.setFechaFin(incidenteBD.getFechaFin()!=null?dateFormat.format(incidenteBD.getFechaFin()):null);
		incidente.setImpacto(incidenteBD.getImpacto()!=null?incidenteBD.getImpacto().longValue():null);
		incidente.setModoIngreso(incidenteBD.getModoIngreso()!=null?incidenteBD.getModoIngreso().longValue():null);
		incidente.setPrioridad(incidenteBD.getPrioridad()!=null?incidenteBD.getPrioridad().longValue():null);
		incidente.setResolucion(incidenteBD.getResolucion());
		incidente.setTipoSolicitud(incidenteBD.getTipoSolicitud()!=null?incidenteBD.getTipoSolicitud().longValue():null);
		incidente.setUrgencia(incidenteBD.getUrgencia()!=null?incidenteBD.getUrgencia().longValue():null);
		incidente.setUsuarioSolicitante(incidenteBD.getUsuarioSolicitante());
		
		return incidente;
	}
	
	@Override
	public List<Incident> getAll(){
		List<Incident> incidentes = new ArrayList<Incident>();
		List<Incidente> incidentesBD = incidenteDao.findAll(); 
		
		for(Incidente incidenteBD: incidentesBD) {
			Incident incidente = new Incident();
			incidente.setIncidenteId(incidenteBD.getIncidenteId());
			incidente.setAsunto(incidenteBD.getAsunto());
			incidente.setCategoria(incidenteBD.getCategoria()!=null?incidenteBD.getCategoria().longValue():null);
			incidente.setDescripcion(incidenteBD.getDescripcion());
			incidente.setDiasPausa(incidenteBD.getDiasPausa()!=null?incidenteBD.getDiasPausa().longValue():null);
			incidente.setEstado(incidenteBD.getEstado()!=null?incidenteBD.getEstado().longValue():null);
			incidente.setFechaCreacion(dateFormat.format(incidenteBD.getFechaCreacion()));
			incidente.setFechaFin(incidenteBD.getFechaFin()!=null?dateFormat.format(incidenteBD.getFechaFin()):null);
			incidente.setImpacto(incidenteBD.getImpacto()!=null?incidenteBD.getImpacto().longValue():null);
			incidente.setModoIngreso(incidenteBD.getModoIngreso()!=null?incidenteBD.getModoIngreso().longValue():null);
			incidente.setPrioridad(incidenteBD.getPrioridad()!=null?incidenteBD.getPrioridad().longValue():null);
			incidente.setResolucion(incidenteBD.getResolucion());
			incidente.setTipoSolicitud(incidenteBD.getTipoSolicitud()!=null?incidenteBD.getTipoSolicitud().longValue():null);
			incidente.setUrgencia(incidenteBD.getUrgencia()!=null?incidenteBD.getUrgencia().longValue():null);
			incidente.setUsuarioSolicitante(incidenteBD.getUsuarioSolicitante());
			
			incidentes.add(incidente);
		}
		
		return incidentes;
	}

	@Override
	public Incident save(Incident incidente) throws EntityAlreadyExistsException, ParseException {
		
		Incidente incidenteBD = new Incidente();
		//incidenteBD.setIncidenteId(incidente.getIncidenteId());
		incidenteBD.setAsunto(incidente.getAsunto());
		incidenteBD.setCategoria(incidente.getCategoria()!=null?BigDecimal.valueOf(incidente.getCategoria()):null);
		incidenteBD.setDescripcion(incidente.getDescripcion());
		incidenteBD.setDiasPausa(incidente.getDiasPausa()!=null?BigDecimal.valueOf(incidente.getDiasPausa()):null);
		incidenteBD.setEstado(incidente.getEstado()!=null?BigDecimal.valueOf(incidente.getEstado()):null);
		incidenteBD.setFechaCreacion(dateFormat.parse(incidente.getFechaCreacion()));
		incidenteBD.setFechaFin(incidente.getFechaFin()!=null?dateFormat.parse(incidente.getFechaFin()):null);
		incidenteBD.setImpacto(incidente.getImpacto()!=null?BigDecimal.valueOf(incidente.getImpacto()):null);
		incidenteBD.setModoIngreso(incidente.getModoIngreso()!=null?BigDecimal.valueOf(incidente.getModoIngreso()):null);
		incidenteBD.setPrioridad(incidente.getPrioridad()!=null?BigDecimal.valueOf(incidente.getPrioridad()):null);
		incidenteBD.setResolucion(incidente.getResolucion());
		incidenteBD.setTipoSolicitud(incidente.getTipoSolicitud()!=null?BigDecimal.valueOf(incidente.getTipoSolicitud().longValue()):null);
		incidenteBD.setUrgencia(incidente.getUrgencia()!=null?BigDecimal.valueOf(incidente.getUrgencia()):null);
		incidenteBD.setUsuarioSolicitante(incidente.getUsuarioSolicitante());
		incidenteBD.setUsuarioId(BigDecimal.valueOf(incidente.getUsuarioId()));
		
		incidenteDao.save(incidenteBD);
		incidente.setIncidenteId(incidenteBD.getIncidenteId());
		return incidente;
	}

	@Override
	public List<Incident> findIncidentsByFilter(Incident filtro, Integer page, Integer size, String fechaInicio,
			String fechaFin) {
		List<Incident> incidentes = new ArrayList<Incident>();
		List<Incidente> incidentesBD = incidenteDao.findWithFilter(filtro, page, size, fechaInicio, fechaFin); 
		
		for(Incidente incidenteBD: incidentesBD) {
			Incident incidente = new Incident();
			incidente.setIncidenteId(incidenteBD.getIncidenteId());
			incidente.setAsunto(incidenteBD.getAsunto());
			incidente.setCategoria(incidenteBD.getCategoria()!=null?incidenteBD.getCategoria().longValue():null);
			incidente.setDescripcion(incidenteBD.getDescripcion());
			incidente.setDiasPausa(incidenteBD.getDiasPausa()!=null?incidenteBD.getDiasPausa().longValue():null);
			incidente.setEstado(incidenteBD.getEstado()!=null?incidenteBD.getEstado().longValue():null);
			incidente.setFechaCreacion(dateFormat.format(incidenteBD.getFechaCreacion()));
			incidente.setFechaFin(incidenteBD.getFechaFin()!=null?dateFormat.format(incidenteBD.getFechaFin()):null);
			incidente.setImpacto(incidenteBD.getImpacto()!=null?incidenteBD.getImpacto().longValue():null);
			incidente.setModoIngreso(incidenteBD.getModoIngreso()!=null?incidenteBD.getModoIngreso().longValue():null);
			incidente.setPrioridad(incidenteBD.getPrioridad()!=null?incidenteBD.getPrioridad().longValue():null);
			incidente.setResolucion(incidenteBD.getResolucion());
			incidente.setTipoSolicitud(incidenteBD.getTipoSolicitud()!=null?incidenteBD.getTipoSolicitud().longValue():null);
			incidente.setUrgencia(incidenteBD.getUrgencia()!=null?incidenteBD.getUrgencia().longValue():null);
			incidente.setUsuarioSolicitante(incidenteBD.getUsuarioSolicitante());
			
			incidentes.add(incidente);
		}
		
		return incidentes;
	}

	@Override
	public Long findIncidentsByFilterCount(Incident filtro, String fechaInicio, String fechaFin) {
		return incidenteDao.countWithFilter(filtro, fechaInicio, fechaFin);
	}

	@Override
	public Incident update(Long id, Incident incidente) throws ParseException {
		
		Incidente vIncidente = incidenteDao.findById(id);
		
		Incidente incidenteBD = new Incidente();
		incidenteBD.setIncidenteId(vIncidente.getIncidenteId());
		incidenteBD.setAsunto(incidente.getAsunto());
		incidenteBD.setCategoria(incidente.getCategoria()!=null?BigDecimal.valueOf(incidente.getCategoria()):null);
		incidenteBD.setDescripcion(incidente.getDescripcion());
		incidenteBD.setDiasPausa(incidente.getDiasPausa()!=null?BigDecimal.valueOf(incidente.getDiasPausa()):null);
		incidenteBD.setEstado(incidente.getEstado()!=null?BigDecimal.valueOf(incidente.getEstado()):null);
		incidenteBD.setFechaCreacion(dateFormat.parse(incidente.getFechaCreacion()));
		incidenteBD.setFechaFin(incidente.getFechaFin()!=null?dateFormat.parse(incidente.getFechaFin()):null);
		incidenteBD.setImpacto(incidente.getImpacto()!=null?BigDecimal.valueOf(incidente.getImpacto()):null);
		incidenteBD.setModoIngreso(incidente.getModoIngreso()!=null?BigDecimal.valueOf(incidente.getModoIngreso()):null);
		incidenteBD.setPrioridad(incidente.getPrioridad()!=null?BigDecimal.valueOf(incidente.getPrioridad()):null);
		incidenteBD.setResolucion(incidente.getResolucion());
		incidenteBD.setTipoSolicitud(incidente.getTipoSolicitud()!=null?BigDecimal.valueOf(incidente.getTipoSolicitud().longValue()):null);
		incidenteBD.setUrgencia(incidente.getUrgencia()!=null?BigDecimal.valueOf(incidente.getUrgencia()):null);
		incidenteBD.setUsuarioSolicitante(incidente.getUsuarioSolicitante());
		incidenteBD.setUsuarioId(BigDecimal.valueOf(incidente.getUsuarioId()));
		
		incidenteDao.update(incidenteBD);
		incidente.setIncidenteId(vIncidente.getIncidenteId());
		return incidente;
	}

}
