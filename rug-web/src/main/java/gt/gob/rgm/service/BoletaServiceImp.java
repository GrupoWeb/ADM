package gt.gob.rgm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Scope;

import gt.gob.rgm.dao.BoletaRepository;
import gt.gob.rgm.model.Boleta;

@Scope("prototype")
public class BoletaServiceImp implements BoletaService {
	
	private BoletaRepository boletaDao;

	@Override
	public void add(Boleta boleta) {
		boletaDao.save(boleta);
	}

	@Override
	public void update(Long id, Integer estado) {
		Optional<Boleta> boletaEncontrada = boletaDao.findById(id);
		if(boletaEncontrada.isPresent()) {
			Boleta boletaActual = boletaEncontrada.get();
			boletaActual.setUsada(estado);
			boletaDao.save(boletaActual);
		} else {
			// boleta no encontrada
		}
	}

	@Override
	public List<Boleta> listBoletas() {
		return boletaDao.findAll();
	}

	public void setBoletaDao(BoletaRepository boletaDao) {
		this.boletaDao = boletaDao;
	}

	public BoletaRepository getBoletaDao() {
		return boletaDao;
	}
}
