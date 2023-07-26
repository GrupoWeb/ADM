package gt.gob.rgm.adm.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.CategoriaInformacionRepository;
import gt.gob.rgm.adm.model.CategoriaInformacion;

@Stateless
public class CategoriaServiceImp {
	@Inject
	CategoriaInformacionRepository categoriaDao;
	
	public List<CategoriaInformacion> getCategorias() {
		return categoriaDao.findAll();
	}
	
	public CategoriaInformacion create(CategoriaInformacion categoria) {
		return categoriaDao.save(categoria);
	}
	
	public CategoriaInformacion update(Integer categoriaInformacionId, CategoriaInformacion categoria) {
		CategoriaInformacion categoriaAnterior = categoriaDao.findById(categoriaInformacionId);
		if(categoriaAnterior != null) {
			categoriaAnterior.setNombre(categoria.getNombre());
			categoriaAnterior.setExcluir(categoria.getExcluir());
		}
		return categoriaAnterior;
	}
	
	public CategoriaInformacion getById(Integer categoriaInformacionId) {
		return categoriaDao.findById(categoriaInformacionId);
	}
}
