package gt.gob.rgm.adm.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import gt.gob.rgm.adm.model.CategoriaInformacion;

public class CategoriaInformacionRepository {
	@PersistenceContext
	EntityManager em;
	
	public List<CategoriaInformacion> findAll() {
		TypedQuery<CategoriaInformacion> query = em.createNamedQuery("CategoriaInformacion.findAll", CategoriaInformacion.class);
		return query.getResultList();
	}
	
	public CategoriaInformacion save(CategoriaInformacion categoria) {
		em.persist(categoria);
		return categoria;
	}
	
	public CategoriaInformacion findById(Integer categoriaInformacionId) {
		return em.find(CategoriaInformacion.class, categoriaInformacionId);
	}
}
