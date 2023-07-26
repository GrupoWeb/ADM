package gt.gob.rgm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Scope;

import gt.gob.rgm.dao.ArchivoRepository;
import gt.gob.rgm.model.Archivo;

@Scope("prototype")
public class ArchivoServiceImp implements ArchivoService {
	
	private ArchivoRepository archivoDao;

	@Override
	public void add(Archivo archivo) {
		archivoDao.save(archivo);
	}

	@Override
	public void delete(long id) {
		Optional<Archivo> archivoEncontrado = archivoDao.findById(id);
		if(archivoEncontrado.isPresent()) {
			Archivo archivoActual = archivoEncontrado.get();
			archivoDao.delete(archivoActual);
		} else {
			// archivo no encontrado
		}
	}

	@Override
	public void deleteByObjeto(long id) {
		List<Archivo> archivos = archivoDao.findByObjetoIdAndEstadoAndTipo(id, "AC", "DI");
		for(Archivo archivo : archivos) {
			archivo.setEstado("IN");
			archivoDao.save(archivo);
		}
	}

	public ArchivoRepository getArchivoDao() {
		return archivoDao;
	}

	public void setArchivoDao(ArchivoRepository archivoDao) {
		this.archivoDao = archivoDao;
	}
}
