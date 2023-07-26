package gt.gob.rgm.adm.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.CambioRepository;
import gt.gob.rgm.adm.domain.Change;
import gt.gob.rgm.adm.exception.EntityAlreadyExistsException;
import gt.gob.rgm.adm.model.Cambio;

@Stateless
public class CambioServiceImp implements CambioService {

	@Inject
	private CambioRepository cambioDao;
	
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	@Override
	public Change getCambio(Long id) {
		Change cambio = new Change();
		Cambio cambioBD = cambioDao.findById(id); 
		
		cambio.setCambioId(cambioBD.getCambioId());
		cambio.setUsuarioSolicitante(cambioBD.getUsuarioSolicitante());
		cambio.setSistema(cambioBD.getSistema());
		cambio.setVersion(cambioBD.getVersion());
		cambio.setDescripcion(cambioBD.getDescripcion());		
		cambio.setEstado(cambioBD.getEstado()!=null?cambioBD.getEstado().longValue():null);
		cambio.setFechaRegistro(dateFormat.format(cambioBD.getFechaRegistro()));
		cambio.setFechaInicio(cambioBD.getFechaInicio()!=null?dateFormat.format(cambioBD.getFechaInicio()):null);
		cambio.setFechaFin(cambioBD.getFechaFin()!=null?dateFormat.format(cambioBD.getFechaFin()):null);
		cambio.setImpacto(cambioBD.getImpacto()!=null?cambioBD.getImpacto().longValue():null);
		cambio.setObservaciones(cambioBD.getObservaciones());
		
		return cambio;
	}

	@Override
	public List<Change> getAll() {
		List<Change> cambios = new ArrayList<Change>();
		List<Cambio> cambiosBD = cambioDao.findAll(); 
		
		for(Cambio cambioBD: cambiosBD) {
			Change cambio = new Change();
			cambio.setCambioId(cambioBD.getCambioId());
			cambio.setUsuarioSolicitante(cambioBD.getUsuarioSolicitante());
			cambio.setSistema(cambioBD.getSistema());
			cambio.setVersion(cambioBD.getVersion());
			cambio.setDescripcion(cambioBD.getDescripcion());		
			cambio.setEstado(cambioBD.getEstado()!=null?cambioBD.getEstado().longValue():null);
			cambio.setFechaRegistro(dateFormat.format(cambioBD.getFechaRegistro()));
			cambio.setFechaInicio(cambioBD.getFechaInicio()!=null?dateFormat.format(cambioBD.getFechaInicio()):null);
			cambio.setFechaFin(cambioBD.getFechaFin()!=null?dateFormat.format(cambioBD.getFechaFin()):null);
			cambio.setImpacto(cambioBD.getImpacto()!=null?cambioBD.getImpacto().longValue():null);
			cambio.setObservaciones(cambioBD.getObservaciones());
			
			cambios.add(cambio);
		}
		
		return cambios;
	}

	@Override
	public Change save(Change cambio) throws EntityAlreadyExistsException, ParseException {
		Cambio cambioBD = new Cambio();
		//cambioBD.setCambioId(cambio.getCambioId());
		cambioBD.setUsuarioSolicitante(cambio.getUsuarioSolicitante());
		cambioBD.setSistema(cambio.getSistema());
		cambioBD.setVersion(cambio.getVersion());
		cambioBD.setDescripcion(cambio.getDescripcion());		
		cambioBD.setEstado(cambio.getEstado()!=null?BigDecimal.valueOf(cambio.getEstado()):null);
		cambioBD.setFechaRegistro(dateFormat.parse(cambio.getFechaRegistro()));
		cambioBD.setFechaInicio(cambio.getFechaInicio()!=null?dateFormat.parse(cambio.getFechaInicio()):null);
		cambioBD.setFechaFin(cambio.getFechaFin()!=null?dateFormat.parse(cambio.getFechaFin()):null);
		cambioBD.setImpacto(cambio.getImpacto()!=null?BigDecimal.valueOf(cambio.getImpacto()):null);
		cambioBD.setObservaciones(cambio.getObservaciones());
		cambioBD.setUsuarioId(BigDecimal.valueOf(cambio.getUsuarioId()));
		
		cambioDao.save(cambioBD);
		cambio.setCambioId(cambioBD.getCambioId());
		return cambio;
	}

	@Override
	public List<Change> findChangesByFilter(Change filtro, Integer page, Integer size, String fechaInicio,
			String fechaFin) {
		List<Change> cambios = new ArrayList<Change>();
		List<Cambio> cambiosBD = cambioDao.findWithFilter(filtro, page, size, fechaInicio, fechaFin); 
		
		for(Cambio cambioBD: cambiosBD) {
			Change cambio = new Change();
			cambio.setCambioId(cambioBD.getCambioId());
			cambio.setUsuarioSolicitante(cambioBD.getUsuarioSolicitante());
			cambio.setSistema(cambioBD.getSistema());
			cambio.setVersion(cambioBD.getVersion());
			cambio.setDescripcion(cambioBD.getDescripcion());		
			cambio.setEstado(cambioBD.getEstado()!=null?cambioBD.getEstado().longValue():null);
			cambio.setFechaRegistro(dateFormat.format(cambioBD.getFechaRegistro()));
			cambio.setFechaInicio(cambioBD.getFechaInicio()!=null?dateFormat.format(cambioBD.getFechaInicio()):null);
			cambio.setFechaFin(cambioBD.getFechaFin()!=null?dateFormat.format(cambioBD.getFechaFin()):null);
			cambio.setImpacto(cambioBD.getImpacto()!=null?cambioBD.getImpacto().longValue():null);
			cambio.setObservaciones(cambioBD.getObservaciones());
			
			cambios.add(cambio);
		}
		
		return cambios;
	}

	@Override
	public Long findChangesByFilterCount(Change filtro, String fechaInicio, String fechaFin) {
		return cambioDao.countWithFilter(filtro, fechaInicio, fechaFin);
	}

	@Override
	public Change update(Long id, Change cambio) throws ParseException {
		
		Cambio vCambio = cambioDao.findById(id);
		
		Cambio cambioBD = new Cambio();
		cambioBD.setCambioId(vCambio.getCambioId());
		cambioBD.setUsuarioSolicitante(cambio.getUsuarioSolicitante());
		cambioBD.setSistema(cambio.getSistema());
		cambioBD.setVersion(cambio.getVersion());
		cambioBD.setDescripcion(cambio.getDescripcion());		
		cambioBD.setEstado(cambio.getEstado()!=null?BigDecimal.valueOf(cambio.getEstado()):null);
		cambioBD.setFechaRegistro(vCambio.getFechaRegistro());
		cambioBD.setFechaInicio(cambio.getFechaInicio()!=null?dateFormat.parse(cambio.getFechaInicio()):null);
		cambioBD.setFechaFin(cambio.getFechaFin()!=null?dateFormat.parse(cambio.getFechaFin()):null);
		cambioBD.setImpacto(cambio.getImpacto()!=null?BigDecimal.valueOf(cambio.getImpacto()):null);
		cambioBD.setObservaciones(cambio.getObservaciones());
		cambioBD.setUsuarioId(BigDecimal.valueOf(cambio.getUsuarioId()));
		
		cambioDao.update(cambioBD);		
		cambio.setCambioId(vCambio.getCambioId());
		
		return cambio;
	}

}
