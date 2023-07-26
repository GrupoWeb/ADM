package gt.gob.rgm.adm.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.model.Usuario;

import java.util.List;

@ApplicationScoped
public class UsuarioRepository {

	@PersistenceContext
    private EntityManager em;

    public Usuario findById(Long id) {
        return em.find(Usuario.class, id);
    }
    
    public void save(Usuario usuario) {
    	em.persist(usuario);
    }

    public List<Usuario> findByStatusOrderedByNombre(String status) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Usuario> criteria = cb.createQuery(Usuario.class);
        Root<Usuario> usuarios = criteria.from(Usuario.class);
        criteria.select(usuarios)
        	.where(cb.equal(usuarios.get("estado"), status))
        	.orderBy(cb.asc(usuarios.get("creado")));
        return em.createQuery(criteria).getResultList();
    }
    
    public Usuario findByEmail(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Usuario> criteria = cb.createQuery(Usuario.class);
        Root<Usuario> usuarios = criteria.from(Usuario.class);
        criteria.select(usuarios)
        	.where(cb.equal(usuarios.get("email"), email));
        Usuario encontrado = null;
		try {
			encontrado = em.createQuery(criteria).getSingleResult();
		} catch (Exception e) {
			//e.printStackTrace();
		}
        return encontrado;
    }
}
