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

import gt.gob.rgm.adm.domain.Garantia;
import gt.gob.rgm.adm.model.VRepListadoGarantias;
import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
@ApplicationScoped
public class GarantiaRepository {

    @PersistenceContext
    private EntityManager em;

    public VRepListadoGarantias findByidGarantia(Long idGarantia) {
        return em.find(VRepListadoGarantias.class, idGarantia);
    }

    public void save(VRepListadoGarantias garantia) {
        em.persist(garantia);
    }

    public List<VRepListadoGarantias> findWithFilter(Garantia filter, Integer page, Integer size) {
        System.out.println("ENTRA EN FINDWITHFILTER: ");
        
        String select = "  SELECT *\n" +
            "FROM (\n" +
            "    SELECT t.*, ROWNUM AS rn\n" +
            "    FROM (\n" +
            "        SELECT *\n" +
            "        FROM V_REP_GARANTIAS";
        String where = "";
        String pagina = "";
        if (filter.getIdDeudor() != null) {
            if(!"".equals(where)) {
        		where+=" AND ID_DEUDOR = "+filter.getIdDeudor();
        	} else {
        		where+=" ID_DEUDOR = "+filter.getIdDeudor();
        	}              
        }
        if (filter.getnDeudor() != null) {
            if(!"".equals(where)) {
        		where+=" AND NOMBRE_DEUDOR LIKE ('%"+filter.getnDeudor()+"%')";
        	} else {
        		where+=" NOMBRE_DEUDOR LIKE ('%"+filter.getnDeudor()+"%')";
        	}              
        }
        if (filter.getIdGarantia()!= null) {
            if(!"".equals(where)) {
                where+=" AND ID_GARANTIA = "+filter.getIdGarantia();
            } else {
                where+=" ID_GARANTIA = "+filter.getIdGarantia();
            }            
        }
        if (filter.getFechaInicio()!= null) {           
            String fechaInicio =filter.getFechaInicio();  
            String fechaFin =filter.getFechaFin();
             if(!"".equals(where)) {
        		where+=" AND FECHA_INSCR BETWEEN  TO_DATE('"+fechaInicio+"', 'YYYY-MM-DD')  and TO_DATE('"+fechaFin+"', 'YYYY-MM-DD')";
        	} else {
        		where+=" FECHA_INSCR BETWEEN  TO_DATE('"+fechaInicio+"', 'YYYY-MM-DD')  and TO_DATE('"+fechaFin+"', 'YYYY-MM-DD')";
        	}
        }
        if (filter.getIdAcreedor()!= null) {
            if(!"".equals(where)) {
                where+=" AND ID_ACREEDOR = "+filter.getIdAcreedor();
            } else {
                where+=" ID_ACREEDOR = "+filter.getIdAcreedor();
            }
        }
        if (filter.getnAcreedor()!= null) {
            if(!"".equals(where)) {
                where+=" AND NOMBRE_ACREEDOR LIKE('%"+filter.getnAcreedor()+"%')";
            } else {
                where+=" NOMBRE_ACREEDOR LIKE('%"+filter.getnAcreedor()+"%')";
            }
        }
        
        if (filter.getIdUsuario()!= null) {
             if(!"".equals(where)) {
                where+=" AND ID_USUARIO = "+filter.getIdUsuario();
            } else {
                where+=" ID_USUARIO = "+filter.getIdUsuario();
            }
        }
        if (page != null) {
            pagina =" WHERE ROWNUM <="+(page * size)+" ) WHERE rn >"+(page - 1) * size;       
        }else{
            pagina = ")";
        }
        
        String sql = select + " WHERE "+ where +" ) t" +pagina;
        System.out.println("QUERY: "+ sql);
        //String sql = "SELECT * FROM V_REP_GARANTIAS WHERE ID_DEUDOR = "+filter.getIdDeudor()+" ORDER BY ID_GARANTIA ASC";
        List<Object[]> results = em.createNativeQuery(sql)
        .setParameter(1, filter.getIdDeudor())
        .getResultList();
        System.out.println("PASO RESULTS 1");
List<VRepListadoGarantias> garantias = new ArrayList<>();
System.out.println("PASO 2");
for (Object[] row  : results) {
    System.out.println("PASO 3 ");
    VRepListadoGarantias garantia = new VRepListadoGarantias();
   // em.refresh(garantia);
      garantia.setIdGarantia(((BigDecimal) row[0]).intValue());
      garantia.setMovs( ((BigDecimal) row[1]).intValue());
      garantia.setInscr(((BigDecimal) row[2]).intValue());
      garantia.setCert(((BigDecimal) row[3]).intValue());
      garantia.setModif(((BigDecimal) row[4]).intValue());
      garantia.setCancelado(((BigDecimal) row[5]).intValue());
      garantia.setEjec(((BigDecimal) row[6]).intValue());
      garantia.setProrr(((BigDecimal) row[7]).intValue());
      garantia.setfInscr((Date) row[8]);
      garantia.setIdTramite(((BigDecimal) row[9]).intValue());
      garantia.setdTramite((String) row[10]);
      garantia.setVigencia(((BigDecimal) row[11]).intValue());
      garantia.setdGarantia((String) row[12]);
      garantia.setgStatus((String) row[13]);
      garantia.setSolicitante((String) row[14]);
      garantia.setsOriginal((String) row[15]);
      garantia.setIdDeudor(((BigDecimal) row[16]).intValue());
      garantia.setnDeudor((String) row[17]);
      garantia.setIdAcreedor(((BigDecimal) row[18]).intValue());
      garantia.setnAcreedor((String) row[19]);
      garantia.setIdUsuario(((BigDecimal) row[20]).intValue());
      garantia.setnUsuario((String) row[21]);
      garantias.add(garantia);  
   
}
System.out.println("PASO 4");
        return garantias;
     
    }

    public Long countWithFilter(Garantia filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<VRepListadoGarantias> listgarantias = criteria.from(VRepListadoGarantias.class);
        Predicate restrictions = null;
        System.out.println("Select count(*) from Garantias");
        System.out.println(filter.getTextobusqueda());
        System.out.println(filter.getTextobusqueda());
        if (filter.getIdDeudor() != null) {
            System.out.println("Where IdDeudor = "+filter.getIdDeudor());          
            //long numero = Long.parseLong(filter.getIdDeudor());     
            Integer numero =filter.getIdDeudor();
            restrictions = cb.equal(listgarantias.get("idDeudor"), numero);
        }
        if (filter.getnDeudor()!= null) {
            System.out.println("Where nDeudor = "+filter.getnDeudor());          
            //long numero = Long.parseLong(filter.getIdDeudor());     
            
            restrictions = cb.like(listgarantias.get("nDeudor"), "%" +filter.getnDeudor()+ "%");
            //cb.like(listdeudores.get("Rfc"), "%" + filter.getTextobusqueda() + "%")
        }
         if (filter.getnAcreedor()!= null) {
            System.out.println("Where nAcreedor = "+filter.getnAcreedor());          
            //long numero = Long.parseLong(filter.getIdDeudor());     
            
            restrictions = cb.like(listgarantias.get("nAcreedor"), "%" +filter.getnAcreedor()+ "%");
        }
        
        if (filter.getIdGarantia()!= null) {
            System.out.println("Where idGarantia = "+filter.getIdGarantia());          
            long numero =filter.getIdGarantia();  
            if(restrictions != null) {
        		restrictions = cb.and(restrictions,
    				cb.equal(listgarantias.get("idGarantia"), numero)
				);
        	} else {
        		restrictions = cb.equal(listgarantias.get("idGarantia"), numero);
        	}
            
        }
        if (filter.getFechaInicio()!= null) {
            System.out.println("Where fechaInicio = "+filter.getFechaInicio());          
            String fechaInicio =filter.getFechaInicio();  
            String fechaFin =filter.getFechaFin();
            if(restrictions != null) {
        		restrictions = cb.and(restrictions,
    				cb.between(listgarantias.get("fInscr"), fechaInicio,fechaFin)
				);
        	} else {
        		cb.between(listgarantias.get("fInscr"), fechaInicio,fechaFin);
        	}
            
        }
        if (filter.getIdAcreedor()!= null) {
            System.out.println("Where idAcreedor= "+filter.getIdAcreedor());          
            long numero =filter.getIdAcreedor();  
            if(restrictions != null) {
        		restrictions = cb.and(restrictions,
    				cb.equal(listgarantias.get("idAcreedor"), numero)
				);
        	} else {
        		restrictions = cb.equal(listgarantias.get("idAcreedor"), numero);
        	}
            
        }
        if (filter.getIdUsuario()!= null) {
            System.out.println("Where idUsuario= "+filter.getIdUsuario());          
            long numero =filter.getIdUsuario();  
            if(restrictions != null) {
        		restrictions = cb.and(restrictions,
    				cb.equal(listgarantias.get("idUsuario"), numero)
				);
        	} else {
        		restrictions = cb.equal(listgarantias.get("idUsuario"), numero);
        	}
            
        }
        criteria = criteria.select(cb.construct(Long.class,
                cb.count(listgarantias.get("idGarantia"))
        ));
        if (restrictions != null) {
            criteria = criteria.where(restrictions);
        }
        
        

        return em.createQuery(criteria).getSingleResult();
    }
 public List<VRepListadoGarantias> findWithFilterAsc(Garantia filter, Integer page, Integer size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VRepListadoGarantias> criteria = cb.createQuery(VRepListadoGarantias.class);
        Root<VRepListadoGarantias> listdeudores = criteria.from(VRepListadoGarantias.class);
        Predicate restrictions = null;
        System.out.println("Buscar con filtro con ASC");
        System.out.println(filter.getTextobusqueda());
        if (filter.getTextobusqueda() != null) {
            System.out.println("Where");
            int ver = 0;
            try {
                long numero = Long.parseLong(filter.getTextobusqueda());     
                restrictions = cb.equal(listdeudores.get("idAcreedor"), numero);
              /*  restrictions = cb.or(restrictions, cb.like(listdeudores.get("Rfc"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("nombrePersona"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("perJuridica"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("nomNacionalidad"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("email"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("direccion"), "%" + filter.getTextobusqueda() + "%"));*/
            } catch (NumberFormatException e) {                
                ver =1;
            }
            if(ver==1){
              /*  restrictions = cb.like(listdeudores.get("Rfc"), "%" + filter.getTextobusqueda() + "%");
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("nombrePersona"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("perJuridica"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("nomNacionalidad"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("email"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.equal(listdeudores.get("direccion"), "%" + filter.getTextobusqueda() + "%"));*/
            }
        }
        criteria = criteria.select(listdeudores);
        if (restrictions != null) {
            criteria = criteria.where(restrictions);
        }
        criteria = criteria.orderBy(cb.asc(listdeudores.get("idGarantia")));
        TypedQuery<VRepListadoGarantias> query = em.createQuery(criteria);
        if (page != null) {
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
        }
        return query.getResultList();
    }

    public Long countWithFilterAsc(Garantia filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<VRepListadoGarantias> listdeudores = criteria.from(VRepListadoGarantias.class);
        Predicate restrictions = null;
        if (filter.getTextobusqueda() != null) {
            System.out.println("Where");
            int ver = 0;
            try {
                long numero = Long.parseLong(filter.getTextobusqueda());   
                System.out.println(numero);
                restrictions = cb.equal(listdeudores.get("idAcreedor"), numero);
               /* restrictions = cb.or(restrictions, cb.like(listdeudores.get("Rfc"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("nombrePersona"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("perJuridica"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("nomNacionalidad"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("email"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("direccion"), "%" + filter.getTextobusqueda() + "%"));*/
            } catch (NumberFormatException e) {                
                ver =1;
            }
            if(ver==1){
               /* restrictions = cb.like(listdeudores.get("Rfc"), "%" + filter.getTextobusqueda() + "%");
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("nombrePersona"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("perJuridica"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("nomNacionalidad"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.like(listdeudores.get("email"), "%" + filter.getTextobusqueda() + "%"));
                restrictions = cb.or(restrictions, cb.equal(listdeudores.get("direccion"), "%" + filter.getTextobusqueda() + "%"));*/
            }
        }

        criteria = criteria.select(cb.construct(Long.class,
                cb.count(listdeudores.get("idGarantia"))
        ));
        if (restrictions != null) {
            criteria = criteria.where(restrictions);
        }
        
        

        return em.createQuery(criteria).getSingleResult();
    }

}
