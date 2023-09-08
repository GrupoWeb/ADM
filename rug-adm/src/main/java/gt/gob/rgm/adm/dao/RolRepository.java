package gt.gob.rgm.adm.dao;

import gt.gob.rgm.adm.domain.Rol;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.domain.Usuario;
import gt.gob.rgm.adm.model.Roles;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.persistence.criteria.Expression;
@ApplicationScoped
public class RolRepository {

    @PersistenceContext
    private EntityManager em;

  

    public List<Roles> findWithFilter() {
        //String select = "  SELECT * FROM V_REP_LISTADO_USUARIOS ";
          String select ="SELECT * FROM ROLES";
         String sql = "";
         System.out.println("QUERY: "+ select);
         List<Object[]> results = em.createNativeQuery(select).getResultList();
         List<Roles> roles = new ArrayList<>();
         for (Object[] row  : results) 
         {
              Roles rol = new Roles();
              rol.setRolId(((BigDecimal) row[0]).intValue());
              rol.setNomRol((String) row[1]);
              rol.setTipoRol((String) row[2]);
             
              
                //garantia.setIdGarantia(((BigDecimal) row[0]).intValue());
              roles.add(rol); 
         }
         return roles;
    }
    
    public List<Roles> crearWithFilter(Integer page, Integer size, Rol filtro) {
    
        
        Roles nuevoRol = new Roles();
        nuevoRol.setNomRol(filtro.getNomRol());
        nuevoRol.setTipoRol(filtro.getTipoRol());
        

        try {
            em.persist(nuevoRol); // Insertar la nueva entidad en la base de datos
            
        } catch (Exception e) {
            System.out.println("ERROR");
            // Manejar excepciones
        }
     
         return findWithFilter();
    }
    
    public List<Roles> delWithFilter(Rol filtro) {
        Roles rol = em.find(Roles.class, filtro.getRolId());

        if (rol != null) {
            System.out.println("Encuentra el rol");
          /*  menu.setRol(filtro.getRol());
            em.merge(menu);*/
            em.remove(rol);
        } else {
            // Manejar el caso en el que el menú no se encontró por el ID
                        System.out.println("No Encuentra el menu");
        }
     
         return findWithFilter();
    }

    public Long countWithFilter() {
       CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Roles> listusuarios = criteria.from(Roles.class);
        Predicate restrictions = null;
     
      
        criteria = criteria.select(cb.construct(Long.class,
                cb.count(listusuarios.get("rolId"))
        ));
        if (restrictions != null) {
            criteria = criteria.where(restrictions);
        }
        return em.createQuery(criteria).getSingleResult();
    }

    
}
