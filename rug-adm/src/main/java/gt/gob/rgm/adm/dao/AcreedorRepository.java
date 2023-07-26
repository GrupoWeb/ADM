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

import gt.gob.rgm.adm.domain.Acreedor;
import gt.gob.rgm.adm.model.VRepListadoAcreedores;
import gt.gob.rgm.adm.model.VRepListadoAcreedoresUsuario;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.persistence.criteria.Expression;

@ApplicationScoped
public class AcreedorRepository {

    @PersistenceContext
    private EntityManager em;

    public VRepListadoAcreedores findByidPersona(Long idPersona) {
        return em.find(VRepListadoAcreedores.class, idPersona);
    }

    public void save(VRepListadoAcreedores boleta) {
        em.persist(boleta);
    }

    public List<VRepListadoAcreedores> findWithFilter(Acreedor filter, Integer page, Integer size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VRepListadoAcreedores> criteria = cb.createQuery(VRepListadoAcreedores.class);
        Root<VRepListadoAcreedores> listacreedores= criteria.from(VRepListadoAcreedores.class);
        Predicate restrictions = null;
        System.out.println("Buscar acreedores con filtro");
        System.out.println(filter.getTextobusqueda());
        if (filter.getTextobusqueda() != null) {            
            System.out.println("Where");
            int ver = 0;
            try {
                long numero = Long.parseLong(filter.getTextobusqueda());     
                restrictions = cb.equal(listacreedores.get("idPersona"), numero);
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("Rfc"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("nombrePersona"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("perJuridica"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("nomNacionalidad"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("email"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("direccion"), "%" + filter.getTextobusqueda() + "%"));
            } catch (NumberFormatException e) {                
                ver =1;
            }
            if(ver==1){
                restrictions = cb.like(listacreedores.get("Rfc"), "%" + filter.getTextobusqueda() + "%");
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("nombrePersona"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("perJuridica"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("nomNacionalidad"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("email"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.equal(listacreedores.get("direccion"), "%" + filter.getTextobusqueda() + "%"));
            }
        }
        if (filter.getIdAcreedor_b()!= null) {
            System.out.println("Where idAcreedor = "+filter.getIdAcreedor_b());  
            String lista = filter.getIdAcreedor_b();
            List<Integer> ids = new ArrayList<>();
          if (lista.contains(",")) {
                ids = Arrays.stream(lista.split(","))
                           .map(Integer::parseInt)
                           .collect(Collectors.toList());
            } else {
                ids.add(Integer.parseInt(lista));
            }
            Expression<Integer> idExpression = listacreedores.get("idPersona");
            if(restrictions != null) {
        		restrictions = cb.and(restrictions,
                                idExpression.in(ids)
				);
        	} else {
        		restrictions = idExpression.in(ids);
        	}
            
        }
        
        criteria = criteria.select(listacreedores);
        if (restrictions != null) {
            criteria = criteria.where(restrictions);
        }
        criteria = criteria.orderBy(cb.asc(listacreedores.get("idPersona")));
        TypedQuery<VRepListadoAcreedores> query = em.createQuery(criteria);
        if (page != null) {
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
        }
        return query.getResultList();
    }

    public Long countWithFilter(Acreedor filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<VRepListadoAcreedores> listacreedores = criteria.from(VRepListadoAcreedores.class);
        Predicate restrictions = null;
        if (filter.getTextobusqueda() != null) {            
            System.out.println("Where");
            int ver = 0;
            try {
                long numero = Long.parseLong(filter.getTextobusqueda());     
                restrictions = cb.equal(listacreedores.get("idPersona"), numero);
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("Rfc"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("nombrePersona"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("perJuridica"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("nomNacionalidad"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("email"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("direccion"), "%" + filter.getTextobusqueda() + "%"));
            } catch (NumberFormatException e) {                
                ver =1;
            }
            if(ver==1){
                restrictions = cb.like(listacreedores.get("Rfc"), "%" + filter.getTextobusqueda() + "%");
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("nombrePersona"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("perJuridica"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("nomNacionalidad"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listacreedores.get("email"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.equal(listacreedores.get("direccion"), "%" + filter.getTextobusqueda() + "%"));
            }
        }
        if (filter.getIdAcreedor_b()!= null) {
            System.out.println("Where idGarantia = "+filter.getIdAcreedor_b());  
            String lista = filter.getIdAcreedor_b();
            List<Integer> ids = new ArrayList<>();
          if (lista.contains(",")) {
                ids = Arrays.stream(lista.split(","))
                           .map(Integer::parseInt)
                           .collect(Collectors.toList());
            } else {
                ids.add(Integer.parseInt(lista));
            }
            Expression<Integer> idExpression = listacreedores.get("idPersona");
            if(restrictions != null) {
        		restrictions = cb.and(restrictions,
                                idExpression.in(ids)
				);
        	} else {
        		restrictions = idExpression.in(ids);
        	}
            
        }

        criteria = criteria.select(cb.construct(Long.class,
                cb.count(listacreedores.get("idPersona"))
        ));
        if (restrictions != null) {
            criteria = criteria.where(restrictions);
        }
        return em.createQuery(criteria).getSingleResult();
    }

    public List<VRepListadoAcreedoresUsuario> findWithFilter_u(Acreedor filter, Integer page, Integer size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VRepListadoAcreedoresUsuario> criteria = cb.createQuery(VRepListadoAcreedoresUsuario.class);
        Root<VRepListadoAcreedoresUsuario> listdeudores = criteria.from(VRepListadoAcreedoresUsuario.class);
        Predicate restrictions = null;
        System.out.println("Buscar acreedores con filtro con U");
        System.out.println(filter.getTextobusqueda());
         if (filter.getAusuarios()!= null) {
            System.out.println("Where");
            int ver = 0;
            try {
                
                restrictions = cb.equal(listdeudores.get("idUsuario"), filter.getAusuarios());
               
            } catch (NumberFormatException e) {                
                ver =1;
            }
          
        }
        criteria = criteria.select(listdeudores);
        if (restrictions != null) {
            criteria = criteria.where(restrictions);
        }
        criteria = criteria.orderBy(cb.asc(listdeudores.get("idPersona")));
        TypedQuery<VRepListadoAcreedoresUsuario> query = em.createQuery(criteria);
        if (page != null) {
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
        }
        return query.getResultList();
    }

    public Long countWithFilter_u(Acreedor filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<VRepListadoAcreedoresUsuario> listdeudores = criteria.from(VRepListadoAcreedoresUsuario.class);
        Predicate restrictions = null;
         if (filter.getAusuarios()!= null) {
            System.out.println("Where");
            int ver = 0;
            try {
                
                restrictions = cb.equal(listdeudores.get("idUsuario"), filter.getAusuarios());
               
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
