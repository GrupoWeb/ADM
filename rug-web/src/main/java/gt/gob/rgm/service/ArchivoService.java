package gt.gob.rgm.service;

import gt.gob.rgm.model.Archivo;

public interface ArchivoService {
	void add(Archivo archivo);
	
	void delete(long id);
	
	void deleteByObjeto(long id);
}
