package gt.gob.rgm.adm.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.domain.Deudo;
import gt.gob.rgm.adm.model.VRepListadoDeudores;
import gt.gob.rgm.adm.model.VRepListadoDeudorUsuario;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.persistence.criteria.Expression;

@ApplicationScoped
public class DeudorRepository {

    @PersistenceContext
    private EntityManager em;

    public VRepListadoDeudores findByidPersona(Long idPersona) {
        return em.find(VRepListadoDeudores.class, idPersona);
    }

    public void save(VRepListadoDeudores boleta) {
        em.persist(boleta);
    }

    public List<VRepListadoDeudores> findWithFilter(Deudo filter, Integer page, Integer size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VRepListadoDeudores> criteria = cb.createQuery(VRepListadoDeudores.class);
        Root<VRepListadoDeudores> listdeudores = criteria.from(VRepListadoDeudores.class);
        Predicate restrictions = null;
        System.out.println("Select * from deudor");        
        if (filter.getTextobusqueda() != null) {
            System.out.println("Where Textobusqueda= "+filter.getTextobusqueda());
            int ver = 0;
            try {
                long numero = Long.parseLong(filter.getTextobusqueda());     
                restrictions = cb.equal(listdeudores.get("idPersona"), numero);
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("Rfc"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("nombrePersona"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("perJuridica"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("nomNacionalidad"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("email"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("direccion"), "%" + filter.getTextobusqueda() + "%"));
            } catch (NumberFormatException e) {                
                ver =1;
            }
            if(ver==1){
                restrictions = cb.like(listdeudores.get("Rfc"), "%" + filter.getTextobusqueda() + "%");
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("nombrePersona"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("perJuridica"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("nomNacionalidad"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("email"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.equal(listdeudores.get("direccion"), "%" + filter.getTextobusqueda() + "%"));
            }
        }
        if (filter.getIdDeudor_b()!= null) {
            System.out.println("Where idAcreedor = "+filter.getIdDeudor_b());  
            String lista = filter.getIdDeudor_b();
            List<Integer> ids = new ArrayList<>();
          if (lista.contains(",")) {
                ids = Arrays.stream(lista.split(","))
                           .map(Integer::parseInt)
                           .collect(Collectors.toList());
            } else {
                ids.add(Integer.parseInt(lista));
            }
            Expression<Integer> idExpression = listdeudores.get("idPersona");
            if(restrictions != null) {
        		restrictions = cb.and(restrictions,
                                idExpression.in(ids)
				);
        	} else {
        		restrictions = idExpression.in(ids);
        	}
            
        }
        criteria = criteria.select(listdeudores);
        if (restrictions != null) {
            criteria = criteria.where(restrictions);
        }
        criteria = criteria.orderBy(cb.asc(listdeudores.get("idPersona")));
        TypedQuery<VRepListadoDeudores> query = em.createQuery(criteria);
        if (page != null) {
            System.out.println(" Con page = "+page+" y con size = "+size); 
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
        }
        return query.getResultList();
    }

    public Long countWithFilter(Deudo filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<VRepListadoDeudores> listdeudores = criteria.from(VRepListadoDeudores.class);
        Predicate restrictions = null;
        if (filter.getTextobusqueda() != null) {
            System.out.println("Where");
            int ver = 0;
            try {
                long numero = Long.parseLong(filter.getTextobusqueda());     
                restrictions = cb.equal(listdeudores.get("idPersona"), numero);
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("Rfc"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("nombrePersona"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("perJuridica"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("nomNacionalidad"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("email"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("direccion"), "%" + filter.getTextobusqueda() + "%"));
            } catch (NumberFormatException e) {                
                ver =1;
            }
            if(ver==1){
                restrictions = cb.like(listdeudores.get("Rfc"), "%" + filter.getTextobusqueda() + "%");
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("nombrePersona"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("perJuridica"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("nomNacionalidad"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("email"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.equal(listdeudores.get("direccion"), "%" + filter.getTextobusqueda() + "%"));
            }
        }
        if (filter.getIdDeudor_b()!= null) {
            System.out.println("Where idAcreedor = "+filter.getIdDeudor_b());  
            String lista = filter.getIdDeudor_b();
            List<Integer> ids = new ArrayList<>();
          if (lista.contains(",")) {
                ids = Arrays.stream(lista.split(","))
                           .map(Integer::parseInt)
                           .collect(Collectors.toList());
            } else {
                ids.add(Integer.parseInt(lista));
            }
            Expression<Integer> idExpression = listdeudores.get("idPersona");
            if(restrictions != null) {
        		restrictions = cb.and(restrictions,
                                idExpression.in(ids)
				);
        	} else {
        		restrictions = idExpression.in(ids);
        	}
            
        }
        criteria = criteria.select(cb.construct(Long.class,
                cb.count(listdeudores.get("idPersona"))
        ));
        if (restrictions != null) {
            criteria = criteria.where(restrictions);
        }
        return em.createQuery(criteria).getSingleResult();
    }

       public List<VRepListadoDeudorUsuario> findWithFilter_u(Deudo filter, Integer page, Integer size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VRepListadoDeudorUsuario> criteria = cb.createQuery(VRepListadoDeudorUsuario.class);
        Root<VRepListadoDeudorUsuario> listdeudores = criteria.from(VRepListadoDeudorUsuario.class);
        Predicate restrictions = null;
        System.out.println("Buscar con filtro en U");
        
        if (filter.getDusuarios()!= null) {
            System.out.println("Where");
            int ver = 0;
            try {
                
                restrictions = cb.equal(listdeudores.get("idUsuario"), filter.getDusuarios());
               
            } catch (NumberFormatException e) {                
                ver =1;
            }
          
        }
        criteria = criteria.select(listdeudores);
        if (restrictions != null) {
            criteria = criteria.where(restrictions);
        }
        criteria = criteria.orderBy(cb.asc(listdeudores.get("idPersona")));
        TypedQuery<VRepListadoDeudorUsuario> query = em.createQuery(criteria);
        if (page != null) {
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
        }
        return query.getResultList();
    }

    public Long countWithFilter_u(Deudo filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<VRepListadoDeudorUsuario> listdeudores = criteria.from(VRepListadoDeudorUsuario.class);
        Predicate restrictions = null;
        if (filter.getDusuarios()!= null) {
            System.out.println("Where");
            int ver = 0;
            try {
                
                restrictions = cb.equal(listdeudores.get("idUsuario"), filter.getDusuarios());
               
            } catch (NumberFormatException e) {                
                ver =1;
            }
          
        }

        criteria = criteria.select(cb.construct(Long.class,
                cb.count(listdeudores.get("idPersona"))
        ));
        if (restrictions != null) {
            criteria = criteria.where(restrictions);
        }
        return em.createQuery(criteria).getSingleResult();
    }

}
