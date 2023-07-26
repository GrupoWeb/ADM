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

import gt.gob.rgm.adm.domain.Usuario;
import gt.gob.rgm.adm.model.VRepListadoUsuarios;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.persistence.criteria.Expression;
@ApplicationScoped
public class Usuario_Repository {

    @PersistenceContext
    private EntityManager em;

    public VRepListadoUsuarios findByidPersona(Long idPersona) {
        return em.find(VRepListadoUsuarios.class, idPersona);
    }

    public void save(VRepListadoUsuarios boleta) {
        em.persist(boleta);
    }

    public List<VRepListadoUsuarios> findWithFilter(Usuario filter, Integer page, Integer size) {
        //String select = "  SELECT * FROM V_REP_LISTADO_USUARIOS ";
          String select = "  SELECT *\n" +
            "FROM (\n" +
            "    SELECT t.*, ROWNUM AS rn\n" +
            "    FROM (\n" +
            "        SELECT *\n" +
            "        FROM V_REP_LISTADO_USUARIOS";
        String where = "";
        String pagina = "";
        String sql = "";
        if (filter.getTextobusqueda() != null) {
            System.out.println("Where");
            int ver = 0;
            try {
                long numero = Long.parseLong(filter.getTextobusqueda());     
                where += " ID_PERSONA ="+numero;
                where += " or RFC like('%"+filter.getTextobusqueda()+"%')";
                where += " or NOMBRE_PERSONA like('%"+filter.getTextobusqueda()+"%')";
                where += " or PER_JURIDICA like('%"+filter.getTextobusqueda()+"%')";
                where += " or NOM_NACIONALIDAD like('%"+filter.getTextobusqueda()+"%')";
                where += " or E_MAIL like('%"+filter.getTextobusqueda()+"%')";
                where += " or DIRECCION like('%"+filter.getTextobusqueda()+"%')";               
                
                
            } catch (NumberFormatException e) {                
                ver =1;
            }
            if(ver==1){
                where += " RFC like('%"+filter.getTextobusqueda()+"%')";
                where += " or NOMBRE_PERSONA like('%"+filter.getTextobusqueda()+"%')";
                where += " or PER_JURIDICA like('%"+filter.getTextobusqueda()+"%')";
                where += " or NOM_NACIONALIDAD like('%"+filter.getTextobusqueda()+"%')";
                where += " or E_MAIL like('%"+filter.getTextobusqueda()+"%')";
                where += " or DIRECCION like('%"+filter.getTextobusqueda()+"%')";    
            }
        }
        if (filter.getIdUsuario_b()!= null) {
            System.out.println("Where idUsuario = "+filter.getIdUsuario_b());  
            String lista = filter.getIdUsuario_b();
          /*  List<Integer> ids = new ArrayList<>();
          if (lista.contains(",")) {
                ids = Arrays.stream(lista.split(","))
                           .map(Integer::parseInt)
                           .collect(Collectors.toList());
            } else {
                ids.add(Integer.parseInt(lista));
            }            */
            //Expression<Integer> idExpression = listusuarios.get("idPersona");
            if(where != "") {
        		where += " and ID_PERSONA IN ("+lista+")"; 
        	} else {
        		where += " ID_PERSONA IN ("+lista+")"; 
        	}
            
           
        }
          if (page != null) {
            pagina =" WHERE ROWNUM <="+(page * size)+" ) WHERE rn >"+(page - 1) * size;       
        }else{
            pagina = ")";
        }
         if(where!=""){
             
              sql = select + " WHERE "+ where+" ) t"  +pagina;
         }else{
              sql = select +" ) t"  +pagina;
         }
            
         System.out.println("QUERY: "+ sql);
         List<Object[]> results = em.createNativeQuery(sql).getResultList();
         List<VRepListadoUsuarios> usuarios = new ArrayList<>();
         for (Object[] row  : results) 
         {
              VRepListadoUsuarios usuario = new VRepListadoUsuarios();
              usuario.setIdPersona(((BigDecimal) row[0]).intValue());
              usuario.setRfc((String) row[1]);
              usuario.setNombrePersona((String) row[2]);
              usuario.setPerJuridica((String) row[3]);
              if(row[4]!=null){
                  usuario.setIdNacionalidad(((BigDecimal) row[4]).intValue());
              }else{
                   usuario.setIdNacionalidad(0);
              }              
              usuario.setNomNacionalidad((String) row[5]);
              usuario.setEmail((String) row[6]);
              usuario.setIdDomicilio((String) row[7]);
              usuario.setDireccion((String) row[8]);
              if(row[9]!=null){
                  usuario.setcGarantias(((BigDecimal) row[9]).intValue());
              }else{
                   usuario.setcGarantias(0);
              }  
              if(row[10]!=null){
                  usuario.setcVigentes(((BigDecimal) row[10]).intValue());
              }else{
                   usuario.setcVigentes(0);
              }  
              if(row[11]!=null){
                  usuario.setCnVigentes(((BigDecimal) row[11]).intValue());
              }else{
                   usuario.setCnVigentes(0);
              }  
              if(row[12]!=null){
                  usuario.setcCanceladas(((BigDecimal) row[12]).intValue());
              }else{
                   usuario.setcCanceladas(0);
              }  
              
              
            
              
                //garantia.setIdGarantia(((BigDecimal) row[0]).intValue());
              usuarios.add(usuario); 
         }
         return usuarios;
    }

    public Long countWithFilter(Usuario filter) {
       CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<VRepListadoUsuarios> listusuarios = criteria.from(VRepListadoUsuarios.class);
        Predicate restrictions = null;
         if (filter.getTextobusqueda() != null) {            
            System.out.println("Where");
            int ver = 0;
            try {
                long numero = Long.parseLong(filter.getTextobusqueda());     
                restrictions = cb.equal(listusuarios.get("idPersona"), numero);
                restrictions = cb.or(restrictions, cb.like(listusuarios.get("Rfc"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listusuarios.get("nombrePersona"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listusuarios.get("perJuridica"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listusuarios.get("nomNacionalidad"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listusuarios.get("email"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listusuarios.get("direccion"), "%" + filter.getTextobusqueda() + "%"));
            } catch (NumberFormatException e) {                
                ver =1;
            }
            if(ver==1){
                restrictions = cb.like(listusuarios.get("Rfc"), "%" + filter.getTextobusqueda() + "%");
                restrictions = cb.or(restrictions, cb.like(listusuarios.get("nombrePersona"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listusuarios.get("perJuridica"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listusuarios.get("nomNacionalidad"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listusuarios.get("email"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.equal(listusuarios.get("direccion"), "%" + filter.getTextobusqueda() + "%"));
            }
        }
        if (filter.getIdUsuario_b()!= null) {
            System.out.println("Where idGarantia = "+filter.getIdUsuario_b());  
            String lista = filter.getIdUsuario_b();
            List<Integer> ids = new ArrayList<>();
          if (lista.contains(",")) {
                ids = Arrays.stream(lista.split(","))
                           .map(Integer::parseInt)
                           .collect(Collectors.toList());
            } else {
                ids.add(Integer.parseInt(lista));
            }
            Expression<Integer> idExpression = listusuarios.get("idPersona");
            if(restrictions != null) {
        		restrictions = cb.and(restrictions,
                                idExpression.in(ids)
				);
        	} else {
        		restrictions = idExpression.in(ids);
        	}
            
        }

        criteria = criteria.select(cb.construct(Long.class,
                cb.count(listusuarios.get("idPersona"))
        ));
        if (restrictions != null) {
            criteria = criteria.where(restrictions);
        }
        return em.createQuery(criteria).getSingleResult();
    }

       public List<VRepListadoUsuarios> findWithFilter_u(Usuario filter, Integer page, Integer size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VRepListadoUsuarios> criteria = cb.createQuery(VRepListadoUsuarios.class);
        Root<VRepListadoUsuarios> listdeudores = criteria.from(VRepListadoUsuarios.class);
        Predicate restrictions = null;
        System.out.println("Buscar con filtro en U");
        System.out.println(filter.getTextobusqueda());
        if (filter.getTextobusqueda() != null) {
            System.out.println("Where");
            int ver = 0;
            try {
                long numero = Long.parseLong(filter.getTextobusqueda());     
                restrictions = cb.equal(listdeudores.get("idPersona"), numero);
               
            } catch (NumberFormatException e) {                
                ver =1;
                restrictions = cb.equal(listdeudores.get("idPersona"), 0);
            }
          
        }
        criteria = criteria.select(listdeudores);
        if (restrictions != null) {
            criteria = criteria.where(restrictions);
        }
        criteria = criteria.orderBy(cb.asc(listdeudores.get("idPersona")));
        TypedQuery<VRepListadoUsuarios> query = em.createQuery(criteria);
        if (page != null) {
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
        }
        return query.getResultList();
    }

    public Long countWithFilter_u(Usuario filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<VRepListadoUsuarios> listdeudores = criteria.from(VRepListadoUsuarios.class);
        Predicate restrictions = null;
        if (filter.getTextobusqueda() != null) {
            System.out.println("Where");
            int ver = 0;
            try {
                long numero = Long.parseLong(filter.getTextobusqueda());     
                restrictions = cb.equal(listdeudores.get("idPersona"), numero);
               
            } catch (NumberFormatException e) {                
                ver =1;
                restrictions = cb.equal(listdeudores.get("idPersona"), 0);
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
